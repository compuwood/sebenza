package mad.apps.sabenza.service.image

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable

interface ImageServiceAPI : ImageCapturer {

    fun getImageDownloadUri(firebaseUrl: String): Observable<Uri>

    fun uploadImage(localImageUri: Uri): Observable<Uri>

    fun takePhoto(): Observable<Uri>

    fun openGallery(): Observable<Uri>

    fun imageResult(intent: Intent)

    fun imageResult(imageUri: Uri)

    fun init(activity: AppCompatActivity)

    fun imageCaptureCancelled()
}
