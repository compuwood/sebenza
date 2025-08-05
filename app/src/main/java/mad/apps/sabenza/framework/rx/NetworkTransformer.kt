package mad.apps.sabenza.framework.rx

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NetworkTransformer<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class CompletableNetworkTransformer : CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

class ObservableNetworkTransformer<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}