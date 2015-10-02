package info.izumin.android.bletia;

import info.izumin.android.bletia.action.Action;

/**
 * Created by izumin on 9/14/15.
 */
public class BletiaException extends Exception {

    private final String mTag;
    private final BletiaErrorType mType;
    private Action mAction = null;

    public BletiaException(Action action, BletiaErrorType type) {
        this(action, type.getName(), type);
    }

    public BletiaException(Action action, String detailMessage, BletiaErrorType type) {
        this(action.getClass().getSimpleName(), detailMessage, type);
        mAction = action;
    }

    public BletiaException(BleErrorType type) {
        super();
        mType = type;
        mTag = getStackTrace()[0].getMethodName();
    }

    public BletiaException(String tag, BletiaErrorType type) {
        this(tag, type.getName(), type);
    }

    public BletiaException(String tag, String detailMessage, BletiaErrorType type) {
        super(detailMessage);
        mType = type;
        mTag = tag;
    }

    @Override
    public String getMessage() {
        return "[" + mTag + "] " + super.getMessage();
    }

    public String getTag() {
        return mTag;
    }

    public BletiaErrorType getType() {
        return mType;
    }

    public Action getAction() {
        return mAction;
    }

    public boolean doCauseByAction() {
        return mAction != null;
    }
}
