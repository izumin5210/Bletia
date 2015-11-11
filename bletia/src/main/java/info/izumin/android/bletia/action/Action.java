package info.izumin.android.bletia.action;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;

import info.izumin.android.bletia.core.BletiaException;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class Action<T, I> extends info.izumin.android.bletia.core.action.Action<T, I> {

    private final Deferred<T, BletiaException, Void> mDeferred;

    public Action(I identity) {
        super(identity);
        mDeferred = new DeferredObject<>();
    }

    public Deferred<T, BletiaException, Void> getDeferred() {
        return mDeferred;
    }

    @Override
    public void resolve(T value) {
        mDeferred.resolve(value);
    }

    @Override
    public void reject(BletiaException throwable) {
        mDeferred.reject(throwable);
    }
}
