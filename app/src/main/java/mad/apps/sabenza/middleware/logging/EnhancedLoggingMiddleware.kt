package mad.apps.sabenza.middleware.logging

import com.google.gson.Gson
import zendesk.suas.LoggerMiddleware
import zendesk.suas.Middleware
import zendesk.suas.State

object EnhancedLoggingMiddlewareBuilder {

    fun buildEnhancedLoggerMiddleware(loggingTransformer : StateLoggingTransformer) : Middleware {
        return LoggerMiddleware.Builder()
                .withSerialization(LoggerMiddleware.Serialization.GSON)
                .withLineLength(-1)
                .withStateTransformer(loggingTransformer)
                .build()
    }

}

class StateLoggingTransformer : LoggerMiddleware.Transformer<State> {
    var gson : Gson

    var includedClasses : List<Class<*>> = emptyList()

    init {
        gson = Gson()
    }

    override fun transform(state: State): String {
        if (includedClasses.isEmpty()) {
            return gson.toJson(state)
        } else {
            var stateString = ""
            for (includedClass in includedClasses) {
                if (state.getState(includedClass) != null) {
                    stateString += gson.toJson(state.getState(includedClass))
                }
            }
            return stateString
        }
    }

    fun filterAllExcept(includedClasses : List<Class<*>>) {
        this.includedClasses = includedClasses
    }
}

