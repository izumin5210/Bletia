package info.izumin.android.bletia.action;

import org.jdeferred.Promise;

import info.izumin.android.bletia.DeferredStrategy;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.action.AbstractDiscoverServicesAction;

/**
 * Created by izumin on 11/14/15.
 */
public class DiscoverServicesAction extends AbstractDiscoverServicesAction<Promise<Void, BletiaException, Void>> {
    public static final String TAG = DiscoverServicesAction.class.getSimpleName();

    public DiscoverServicesAction(StateContainer container) {
        super(new DeferredStrategy<Void, BletiaException>(), container);
    }
}
