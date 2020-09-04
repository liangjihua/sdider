package sdider.impl.exception;

/**
 *
 * @author yujiaxin
 */
public class SdiderExecuteException extends RuntimeException{

    public SdiderExecuteException(String message) {
        super(message);
    }

    public SdiderExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
