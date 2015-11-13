package info.izumin.android.bletia.action;

import org.jdeferred.Promise;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadRemoteRssiAction;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadRemoteRssiAction extends AbstractReadRemoteRssiAction<Promise<Integer, BletiaException, Void>> {

    public ReadRemoteRssiAction() {
        super(new DeferredStrategy<Integer, BletiaException>());
    }
}
