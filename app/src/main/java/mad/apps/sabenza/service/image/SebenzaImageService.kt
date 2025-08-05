package mad.apps.sabenza.service.image

import io.reactivex.Single
import mad.apps.sabenza.data.model.picture.SebenzaPicture

interface SebenzaImageService {

    fun getImageUrlForId(id : String) : Single<String>

    fun uploadImageIdToSebenza(url : String, caption : String) : Single<SebenzaPicture>

    fun sebenzaImageFromGallery() : Single<SebenzaPicture>

    fun sebenzaImageFromCamera() : Single<SebenzaPicture>

}