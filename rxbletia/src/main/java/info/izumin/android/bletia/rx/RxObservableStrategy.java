package info.izumin.android.bletia.rx;

import info.izumin.android.bletia.core.ResolveStrategy;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by izumin on 11/15/15.
 */
public class RxObservableStrategy<T, E extends Throwable> implements ResolveStrategy<T, E, Observable<T>> {
    public static final String TAG = RxObservableStrategy.class.getSimpleName();

    private final PublishSubject<T> mSubject;

    public RxObservableStrategy() {
        mSubject = PublishSubject.create();
    }

    protected PublishSubject<T> getSubject() {
        return mSubject;
    }

    @Override
    public void resolve(T value) {
        mSubject.onNext(value);
        mSubject.onCompleted();
    }

    @Override
    public void reject(E throwable) {
        mSubject.onError(throwable);
    }

    @Override
    public Observable<T> getResolver() {
        return mSubject;
    }
}
