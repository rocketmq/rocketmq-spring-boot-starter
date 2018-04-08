package org.rocketmq.starter.exception;

/**
 * @author He Jialin
 */
public class MethodNotSupportException extends RuntimeException{


    private static final long serialVersionUID = 5136128643850367154L;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public MethodNotSupportException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     * later retrieval by the {@link #getMessage()} method.
     */
    public MethodNotSupportException(String message) {
        super(message);
    }
}
