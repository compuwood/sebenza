package mad.apps.sabenza.data.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private val formatter =  SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
    //Sort a list of strings into a list of calendars, earliest date first
    fun sortListOfDateStrings(dateStrings : List<String>) : List<Calendar> {
        val calendars = dateStrings.map {
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(it)
            calendar
        }.filterNotNull()

        return calendars.sortedWith(object : Comparator<Calendar> {
            override fun compare(p0: Calendar, p1: Calendar): Int {
                return ((p0.timeInMillis- p1.timeInMillis)/3600).toInt()
            }
        })
    }

    fun dateStringToCalendar(dateString : String) : Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(dateString)

        return calendar
    }

    fun getISOTimeFormattedString(calendar: Calendar) : String {
        val todayDate = calendar.time
        return formatter.format(todayDate)
    }

}

fun Calendar.getISOTimeFormattedString() : String {
    return DateUtil.getISOTimeFormattedString(this)
}
