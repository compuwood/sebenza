package mad.apps.sabenza.ui.widget.image

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.picture.SebenzaPicture
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.rx.NetworkTransformer
import mad.apps.sabenza.framework.rx.observer.EnhancedSingleObserver
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.service.image.SebenzaImageService
import mad.apps.sabenza.ui.widget.LoadingScreen
import javax.inject.Inject

class AddSebenzaImageDialog(context: Context?, val subscriber : SingleEmitter<SebenzaPicture>) : Dialog(context) {

    @Inject
    lateinit var sebenzaImageService : SebenzaImageService

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
        sebenzaImageService.sebenzaImageFromGallery()
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<SebenzaPicture>() {
                    override fun onSuccess(t: SebenzaPicture) {
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
        sebenzaImageService.sebenzaImageFromCamera()
                .compose(NetworkTransformer())
                .subscribe(object : EnhancedSingleObserver<SebenzaPicture>() {
                    override fun onSuccess(t: SebenzaPicture) {
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

object AddSebenzaImageDialogBuilder {

    fun build(context: Context) : Single<SebenzaPicture> {
        return Single.create<SebenzaPicture> { subscriber ->
            val dialog = AddSebenzaImageDialog(context, subscriber)
            dialog.show()
        }
        .subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
    }

}