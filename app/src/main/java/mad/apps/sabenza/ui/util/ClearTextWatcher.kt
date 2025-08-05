package mad.apps.sabenza.ui.util

import android.text.Editable
import android.text.TextWatcher

abstract class ClearTextWatcher : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        if (p0.isNullOrEmpty()) {
            emptyValue()
        } else {
            newString(p0.toString())
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    abstract fun emptyValue()

    abstract fun newString(newString: String)
}

abstract class SimpleTextWatcher : ClearTextWatcher() {
    override fun emptyValue() {
    }
}