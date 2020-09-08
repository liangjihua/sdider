package com.sdider.impl.exception;

/**
 *
 * @author yujiaxin
 */
public class SdiderExecuteException extends RuntimeException{

    public SdiderExecuteException(String message) {
        super(message);
    }

    public SdiderExecuteException(Throwable cause) {
        super(cause);
    }
}
