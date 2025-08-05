package mad.apps.sabenza.service.image

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mad.apps.sabenza.data.api.PicturesAPI
import mad.apps.sabenza.data.api.toEqualQueryString
import mad.apps.sabenza.data.model.picture.SebenzaPicture

class SebenzaImageServiceImpl(val picturesAPI: PicturesAPI, val imageCapturer: ImageCapturer) : SebenzaImageService {

    override fun getImageUrlForId(id: String): Single<String> {
        return picturesAPI.getPictureForId(id.toEqualQueryString()).map { it.uri }
    }

    override fun uploadImageIdToSebenza(url: String, caption: String): Single<SebenzaPicture> {
        return picturesAPI.createPicture(caption = caption, uri = url)
    }

    override fun sebenzaImageFromGallery(): Single<SebenzaPicture> {
        return imageCapturer.grabImageFromGalleryAndSaveToFirebase()
                .singleOrError()
                .flatMap {
                    picturesAPI.createPicture(caption = it.toString(), uri = it.toString())
                }
    }

    override fun sebenzaImageFromCamera(): Single<SebenzaPicture> {
        return imageCapturer.takePhotoAndSaveToFirebase()
                .singleOrError()
                .flatMap {
                    picturesAPI.createPicture(caption = it.toString(), uri = it.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
    }
}