package mad.apps.sabenza.framework

import java.util.*

object TestStringHelper {
    fun nextPrintableString(length: Int): String {
        return (0..length - 1).map { Random().nextPrintableChar() }.joinToString("")
    }

    private fun Random.nextPrintableChar(): Char {
        val low = 65
        val high = 90
        return (nextInt(high - low) + low).toChar()
    }

    fun nextPrintableNumber(length: Int): String {
        return (0..length - 1).map { Random().nextPrintableNumber() }.joinToString("")
    }

    private fun Random.nextPrintableNumber(): Char {
        val low = 48
        val high = 57
        return (nextInt(high - low) + low).toChar()
    }
}