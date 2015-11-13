package info.izumin.android.bletia;

import org.jdeferred.impl.DeferredObject;

import info.izumin.android.bletia.core.ActionResolver;

/**
 * Created by izumin on 11/11/15.
 */
public class DeferredResolver<T, E extends Throwable> implements ActionResolver<T, E> {
    public static final String TAG = DeferredResolver.class.getSimpleName();

    private final DeferredObject<T, E, Void> mDeferred;

    public DeferredResolver() {
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
