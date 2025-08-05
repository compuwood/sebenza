package mad.apps.sabenza.service.image

import android.net.Uri
import io.reactivex.Observable

interface ImageCapturer {

    fun takePhotoAndSaveToFirebase() : Observable<Uri>

    fun grabImageFromGalleryAndSaveToFirebase() : Observable<Uri>

}