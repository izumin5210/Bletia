package info.izumin.android.bletia.rx.action;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.action.AbstractDiscoverServicesAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxDiscoverServicesAction extends AbstractDiscoverServicesAction<Observable<Void>> {
    public static final String TAG = RxDiscoverServicesAction.class.getSimpleName();

    public RxDiscoverServicesAction(StateContainer container) {
        super(new RxObservableStrategy<Void, BletiaException>(), container);
    }
}
