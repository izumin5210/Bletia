package info.izumin.android.bletia.action;

import info.izumin.android.bletia.DeferredResolver;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadRemoteRssiAction;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadRemoteRssiAction extends AbstractReadRemoteRssiAction {

    public ReadRemoteRssiAction() {
        super(new DeferredResolver<Integer, BletiaException>());
    }
}
