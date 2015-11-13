package info.izumin.android.bletia.core;

/**
 * Created by izumin on 11/11/15.
 */
public interface ResolveStrategy<T, E extends Throwable, R> {
    void resolve(T value);
    void reject(E throwable);
    R getResolver();
}
