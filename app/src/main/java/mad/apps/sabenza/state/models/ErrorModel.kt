package mad.apps.sabenza.state.models

import mad.apps.sabenza.framework.redux.RxModel

data class ErrorModel(
        val apiErrors: List<Throwable> = emptyList(),
        val mostRecentError : Throwable? = null
) : RxModel()