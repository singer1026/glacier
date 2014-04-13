/**
 * @author zhaolie
 * @create-time 2010-12-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.exception;

public class HackerException extends RuntimeException {
    public HackerException() {
        super("黑客行为");
    }

    public HackerException(String message) {
        super(message);
    }

    public HackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HackerException(Throwable cause) {
        super(cause);
    }
}
