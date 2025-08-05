package mad.apps.sabenza.framework.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UIObservableTransformer<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}