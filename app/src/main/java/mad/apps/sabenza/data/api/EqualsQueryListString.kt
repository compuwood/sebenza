package mad.apps.sabenza.data.api

class EqualsQueryList(val values: List<String>) {
    override fun toString(): String {
        var string = ""
        values.asSequence().forEach { string = string + "," + it }
        return "in." + string.removeRange(0, 1)
    }
}

fun List<*>.toEqualQueryList(): EqualsQueryList {
    return EqualsQueryList(this.map { it.toString() })
}

