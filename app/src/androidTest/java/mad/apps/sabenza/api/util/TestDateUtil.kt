package mad.apps.sabenza.api.util

import java.text.SimpleDateFormat
import java.util.*

object TestDateUtil {

    fun generateFutureDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis() + (86400000 + Math.random() * 2000 * 86400000).toInt()
        return calendar
    }

    fun addDayToDate(inputCalendar: Calendar): Calendar {
        val timeInMillis = inputCalendar.timeInMillis
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis + 86400000
        return calendar
    }

    val formatter =  SimpleDateFormat("yyyy-mm-dd hh:mm:ss")

    fun getISOTimeFormattedString(calendar: Calendar) : String {
        val todayDate = calendar.time
        return formatter.format(todayDate)
    }

}

fun Calendar.getISOTimeFormattedString() : String {
    return TestDateUtil.getISOTimeFormattedString(this)
}