package info.izumin.android.bletia.rx.action;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadRemoteRssiAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxReadRemoteRssiAction extends AbstractReadRemoteRssiAction<Observable<Integer>> {
    public static final String TAG = RxReadRemoteRssiAction.class.getSimpleName();

    public RxReadRemoteRssiAction() {
        super(new RxObservableStrategy<Integer, BletiaException>());
    }
}
