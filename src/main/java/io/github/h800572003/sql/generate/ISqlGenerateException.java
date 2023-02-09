package io.github.h800572003.sql.generate;

public class ISqlGenerateException extends RuntimeException{

    public ISqlGenerateException() {
        super();
    }

    public ISqlGenerateException(String message) {
        super(message);
    }

    public ISqlGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ISqlGenerateException(Throwable cause) {
        super(cause);
    }

    protected ISqlGenerateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
