package mad.apps.sabenza.framework.ui

object ViewArgs {

    fun buildArg(arg: Any) : Pair<String, Any> {
        return Pair(arg::javaClass.name, arg)
    }

    fun getArgCode(klazz: Class<*>) : String {
        return klazz.name
    }

}