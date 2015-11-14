package info.izumin.android.bletia.action;

import org.jdeferred.Promise;

import info.izumin.android.bletia.DeferredStrategy;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.action.AbstractDisconnectAction;

/**
 * Created by izumin on 11/14/15.
 */
public class DisconnectAction extends AbstractDisconnectAction<Promise<Void, BletiaException, Void>> {
    public static final String TAG = DisconnectAction.class.getSimpleName();

    public DisconnectAction(StateContainer container) {
        super(new DeferredStrategy<Void, BletiaException>(), container);
    }
}
