package mad.apps.sabenza.framework.rx

import io.reactivex.functions.Function

class NullOrEmptyErrorMapper<T> : Function<T, T> {
    override fun apply(t: T): T {
        if (t == null) {
            throw NullResponseError()
        }
        return t
    }
}

class NullResponseError : Exception("Null Response From Retrofit")