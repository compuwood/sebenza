package mad.apps.sabenza.data.rpc.calls

import com.google.gson.Gson
import com.noveogroup.android.log.LoggerManager
import retrofit2.HttpException

class RPCEndpointError(val exception : HttpException) {

    var errorResponse: ErrorResponse? = null
        private set

    init {
        val json = exception.response().errorBody().toString()
        try {
            errorResponse = Gson().fromJson(json, ErrorResponse::class.java)
        } catch (e: Exception){
            LoggerManager.getLogger().e(e)
        }
    }

    fun valid() : Boolean {
        return errorResponse != null
    }

}