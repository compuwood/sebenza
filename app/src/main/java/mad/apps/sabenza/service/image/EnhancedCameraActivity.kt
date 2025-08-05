package mad.apps.sabenza.service.image

import android.content.Intent
import android.os.Bundle
import com.desmond.squarecamera.CameraActivity

class EnhancedCameraActivity : CameraActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setupFailedReturn() {
        val data = Intent()

        if (getParent() == null) {
            setResult(RESULT_CANCELED, data)
        } else {
            getParent().setResult(RESULT_CANCELED, data)
        }
    }

    override fun onBackPressed() {
        setupFailedReturn()
        finish()
    }
}
