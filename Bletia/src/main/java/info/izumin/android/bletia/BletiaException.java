package info.izumin.android.bletia;

/**
 * Created by izumin on 9/14/15.
 */
public class BletiaException extends Exception {

    private final BletiaErrorType mType;

    public BletiaException(BletiaErrorType type) {
        this(type.getName(), type);
    }

    public BletiaException(String detailMessage, BletiaErrorType type) {
        super(detailMessage);
        this.mType = type;
    }

    public BletiaErrorType getType() {
        return mType;
    }
}
