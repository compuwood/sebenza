package mad.apps.sabenza.service.image

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.noveogroup.android.log.LoggerManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mad.apps.sabenza.R
import mad.apps.sabenza.framework.rx.ObservableNetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedObserver
import mad.apps.sabenza.framework.util.ExceptionTracker
import mad.apps.sabenza.service.permission.PermissionService
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*

class ImageService(context: Context, val permissionService: PermissionService) : ImageServiceAPI {

    var firebaseStorage: FirebaseStorage
    var firebaseReference: StorageReference

    init {
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseReference = firebaseStorage.getReferenceFromUrl(context.resources.getString(R.string.firebase_storage_bucket_url))
    }

    private var mainActivity: AppCompatActivity? = null
    var isGalleryImage : Boolean = false

    override fun init(activity: AppCompatActivity) {
        mainActivity = activity
    }

    override fun getImageDownloadUri(firebaseUrl: String): Observable<Uri> {
        return Observable.create<Uri> {
            val storageReference: StorageReference = firebaseStorage.getReferenceFromUrl(firebaseUrl)
            storageReference.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
                override fun onSuccess(p0: Uri?) {
                    it.onNext(p0!!)
                    it.onComplete()
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    it.onError(p0)
                }
            })
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
    }

    override fun uploadImage(localImageUri: Uri): Observable<Uri> {
        if (localImageUri == null) {
            return Observable.error(Throwable("There was an error uploading the image"))
        }
        return Observable.create {
            var id: String = UUID.randomUUID().toString() + ".jpg"
            var imageRef: StorageReference = firebaseReference.child(id)

            val uploadTask = imageRef.putBytes(compressImage(localImageUri!!))
            uploadTask.addOnFailureListener(object : OnFailureListener {
                override fun onFailure(@NonNull exception: Exception) {
                    // Handle unsuccessful uploads
                    it.onError(exception)
                }
            }).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    val downloadUrl = taskSnapshot.getDownloadUrl()
                    it.onNext(downloadUrl!!)
                    it.onComplete()
                }
            })
        }
    }

    private fun compressImage(imageUri: Uri): ByteArray {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4 // shrink it down otherwise we will use stupid amounts of memory
        val bitmap = BitmapFactory.decodeFile(imageUri.path, options)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, baos)
        return baos.toByteArray()
    }


    override fun takePhoto(): Observable<Uri> {
        return Observable.create {
            isGalleryImage = false
            startCamera(it)
        }
    }


    override fun openGallery(): Observable<Uri> {
        return Observable.create {
            isGalleryImage = true
            openGalleryForResult(it)
        }

    }

    private fun startCamera(subscriber: ObservableEmitter<Uri>) {
        if (!permissionService.allowedToUseCamera()) {
            permissionService.requestCameraPermission().subscribe(object : EnhancedObserver<Boolean>() {
                override fun onComplete() {
                    subscriber.onComplete()
                }

                override fun error(error: Throwable) {
                    subscriber.onError(error)
                }

                override fun onNext(aBoolean: Boolean) {
                    if (aBoolean) {
                        startCameraForResult(subscriber)
                    }
                }
            })
        } else {
            startCameraForResult(subscriber)
        }
    }

    var cameraSubscriber: ObservableEmitter<Uri>? = null

    private fun startCameraForResult(subscriber: ObservableEmitter<Uri>) {
        var successful: Boolean = false

        try {
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, cameraInfo)
            successful = true
        } catch (e: RuntimeException) {
            ExceptionTracker.trackException(e)
            subscriber.onError(Throwable("There was an error with the camera"))
            successful = false
        }

        if (successful) {
            // Start CameraActivity
            cameraSubscriber = subscriber
            val startCustomCameraIntent = Intent(mainActivity, EnhancedCameraActivity::class.java)
            mainActivity?.startActivityForResult(startCustomCameraIntent, ImageUtil.REQUEST_CAMERA)
        }
    }

    override fun imageResult(intent: Intent) {
        imageResult(intent.data)
    }

    override fun imageResult(imageUri: Uri) {
        if (cameraSubscriber != null) {
            if (isGalleryImage) {
                isGalleryImage = false
                launchCroppingActivity(imageUri!!)
            } else {
                cameraSubscriber!!.onNext(imageUri!!)
                cameraSubscriber!!.onComplete()
            }
        }
    }

    private fun launchCroppingActivity(input: Uri) {
        galleryUri = getTempFile()
        CropImage.activity(input)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(mainActivity as Activity)
    }

    override fun imageCaptureCancelled() {
        if (cameraSubscriber != null) {
            cameraSubscriber!!.onComplete()
        }
    }

    private var galleryUri: Uri? = null

    private fun openGalleryForResult(it: ObservableEmitter<Uri>) {
        cameraSubscriber = it
        val intent: Intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        mainActivity?.startActivityForResult(intent, ImageUtil.REQUEST_CAMERA)
    }

    fun getTempFile() : Uri? {
        var file : File? = null

        try {
            var fileName : String = "taptuckImage" + (Math.random()*100).toInt().toString() + ".jpg"
            file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName)
        } catch (e: IOException) {
            // Error while creating file
            if (cameraSubscriber != null) {
                cameraSubscriber?.onError(Throwable("Unable to load iamge from gallery"))
            }
        }

        return Uri.fromFile(file)
    }

    override fun takePhotoAndSaveToFirebase(): Observable<Uri> {
        return takePhoto().flatMap { uploadImage(it) }
    }

    override fun grabImageFromGalleryAndSaveToFirebase(): Observable<Uri> {
        return openGallery().flatMap { uploadImage(it) }
    }
}