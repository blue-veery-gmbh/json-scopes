package com.blueveery.bluehr.services;

/**
 * Created by tomek on 12.09.16.
 */
public class OperationNotAllowedException extends BaseException {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
