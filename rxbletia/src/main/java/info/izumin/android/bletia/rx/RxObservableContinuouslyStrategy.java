package info.izumin.android.bletia.rx;

/**
 * Created by izumin on 11/15/15.
 */
public class RxObservableContinuouslyStrategy<T, E extends Throwable> extends RxObservableStrategy<T, E> {
    public static final String TAG = RxObservableContinuouslyStrategy.class.getSimpleName();

    @Override
    public void resolve(T value) {
        getSubject().onNext(value);
    }

    public void complete() {
        getSubject().onCompleted();
    }
}
