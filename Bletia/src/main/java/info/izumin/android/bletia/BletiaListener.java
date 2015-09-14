package info.izumin.android.bletia;

/**
 * Created by izumin on 9/10/15.
 */
public interface BletiaListener {
    void onConnect();
    void onDisconnect();
    void onError(BletiaException exception);
}
