package mad.apps.sabenza.ui.util

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import java.util.regex.Pattern


fun checkIfEmpty(editText: EditText, errMsg: String) : Boolean{
    if (editText.text.toString().isNullOrEmpty()) {
        editText.error = errMsg
        return true
    }
    return false
}

fun isValidEmail(target: CharSequence?): Boolean {
    if (target == null) {
        return false
    } else {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}

fun isPhoneNumber(phone: String): Boolean{
    val phoneRegex = Regex("^[+]?[0-9 ]{10,13}$")
    if (phone.matches(phoneRegex)) {
        return true
    }
    return false
}

class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {

    internal var mPattern: Pattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {

        val matcher = mPattern.matcher(dest)
        if (!matcher.matches())
            return ""
        return null
    }

}