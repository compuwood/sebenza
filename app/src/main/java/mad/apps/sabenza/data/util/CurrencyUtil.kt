package mad.apps.sabenza.data.util

object CurrencyUtil {

    fun getCurrencySymbol() : String {
        return "$"
    }

    fun convertCurrencyStringToDouble(currencyString : String) : Double {
        return currencyString.replace(getCurrencySymbol(), "").toDouble()
    }

}