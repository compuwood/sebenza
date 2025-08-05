package mad.apps.sabenza.data.api

class EqualsQueryString(val string : String) {
    override fun toString(): String {
        return "eq." + string
    }
}

fun String.toEqualQueryString() : EqualsQueryString {
    return EqualsQueryString(this)
}

fun Int.toEqualQueryString() : EqualsQueryString {
    return EqualsQueryString(this.toString())
}

fun Long.toEqualQueryString() : EqualsQueryString {
    return EqualsQueryString(this.toString())
}