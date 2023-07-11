package com.zippy.api.exception;

public class BillingInformationNotFoundException extends RuntimeException {
    public BillingInformationNotFoundException(String message) {
        super(message);
    }
}
