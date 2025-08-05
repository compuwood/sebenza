package mad.apps.sabenza.data.rpc.calls

class BaseRPCResponse<T> : ArrayList<T>() {

    fun hasResponse() : Boolean {
        return size >= 1
    }

    fun getResponse() : T {
        return first()
    }

}