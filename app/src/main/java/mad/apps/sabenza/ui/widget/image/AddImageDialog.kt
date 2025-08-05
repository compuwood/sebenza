package mad.apps.sabenza.ui.widget.image

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Window
import android.widget.Button
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.service.image.ImageCapturer
import mad.apps.sabenza.ui.widget.LoadingScreen
import javax.inject.Inject

class AddImageDialog(context: Context?, val subscriber : SingleEmitter<Uri>) : Dialog(context) {

    @Inject
    lateinit var imageCapturer : ImageCapturer

    val cameraButton by bindView<Button>(R.id.add_image_dialog_camera_button)
    val galleryButton by bindView<Button>(R.id.add_image_dialog_gallery_button)
    val busyView by bindView<LoadingScreen>(R.id.add_image_busy_state)

    init {
        DaggerService.getDaggerComponent(context).inject(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        setContentView(R.layout.add_image_dialog)

        cameraButton.setOnClickListener{
            takePhoto()
        }

        galleryButton.setOnClickListener{
            fetchImageFromGallery()
        }
    }

    private fun fetchImageFromGallery() {
        busyView.show()
        imageCapturer.grabImageFromGalleryAndSaveToFirebase()
                .singleOrError()
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<Uri> () {
                    override fun onSuccess(t: Uri) {
                        subscriber.onSuccess(t)
                        busyView.hide()
                        dismiss()
                    }

                    override fun onError(e: Throwable) {
                        subscriber.onError(e)
                        busyView.hide()
                        dismiss()
                    }
                })
    }

    private fun takePhoto() {
        busyView.show()
        imageCapturer.takePhotoAndSaveToFirebase()
                .singleOrError()
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<Uri> () {
                    override fun onSuccess(t: Uri) {
                        subscriber.onSuccess(t)
                        busyView.hide()
                        dismiss()
                    }

                    override fun onError(e: Throwable) {
                        subscriber.onError(e)
                        busyView.hide()
                        dismiss()
                    }
                })
    }

}

object AddImageDialogBuilder {

    fun build(context: Context) : Single<Uri> {
        return Single.create<Uri> { subscriber ->
            val dialog = AddImageDialog(context, subscriber)
            dialog.show()
        }
        .subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
    }

}

