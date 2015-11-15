package info.izumin.android.bletia.rx.action;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.action.AbstractDisconnectAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxDisconnectAction extends AbstractDisconnectAction<Observable<Void>> {
    public static final String TAG = RxDisconnectAction.class.getSimpleName();

    public RxDisconnectAction(StateContainer container) {
        super(new RxObservableStrategy<Void, BletiaException>(), container);
    }
}
