package info.izumin.android.bletia.action;

import org.jdeferred.impl.DeferredObject;

import info.izumin.android.bletia.core.ResolveStrategy;

/**
 * Created by izumin on 11/11/15.
 */
public class DeferredStrategy<T, E extends Throwable> implements ResolveStrategy<T, E> {
    public static final String TAG = DeferredStrategy.class.getSimpleName();

    private final DeferredObject<T, E, Void> mDeferred;

    public DeferredStrategy() {
        mDeferred = new DeferredObject<>();
    }

    public DeferredObject<T, E, Void> getDeferred() {
        return mDeferred;
    }

    @Override
    public void resolve(T value) {
        mDeferred.resolve(value);
    }

    @Override
    public void reject(E throwable) {
        mDeferred.reject(throwable);
    }
}
