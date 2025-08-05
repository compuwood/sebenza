package mad.apps.sabenza.data.model.messaging

import java.text.SimpleDateFormat
import java.util.*

class MessagePreviewModel(
        val messageId: String,
        val relatedJobId: String,
        val relatedJobName: String,
        val sendDate: Calendar,
        val senderName: String,
        val messageBody: String
) {
    fun getSendDateAsString() : String{
        return buildStringFromDate(sendDate)
    }

    private fun buildStringFromDate(calendar: Calendar) : String {
        val sdf = SimpleDateFormat("MMM DD", Locale.US)
        return sdf.format(calendar.time)
    }
}