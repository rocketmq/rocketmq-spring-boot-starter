package org.rocketmq.starter.exception;

/**
 * @author He Jialin
 */
public class NoListenerFoundException extends RuntimeException{

    private static final long serialVersionUID = -7909279829141688730L;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public NoListenerFoundException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     * later retrieval by the {@link #getMessage()} method.
     */
    public NoListenerFoundException(String message) {
        super(message);
    }
}
