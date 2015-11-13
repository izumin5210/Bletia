package info.izumin.android.bletia.core;

/**
 * Created by izumin on 11/11/15.
 */
public interface ActionResolver<T, E extends Throwable> {
    void resolve(T value);
    void reject(E throwable);
}
