/**
 * @author zhaolie
 * @create-time 2010-12-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.exception;

public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException() {
        super("数据已经被其他用户更新,请刷新");
    }

    public OptimisticLockException(String message) {
        super(message);
    }

    public OptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisticLockException(Throwable cause) {
        super(cause);
    }
}
