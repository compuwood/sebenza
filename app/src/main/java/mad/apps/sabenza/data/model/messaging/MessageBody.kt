package mad.apps.sabenza.data.model.messaging

import com.google.gson.annotations.SerializedName

data class MessageBody(
        @SerializedName("body") val body : String
)