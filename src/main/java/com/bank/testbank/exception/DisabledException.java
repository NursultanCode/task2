package com.bank.testbank.exception;

public class DisabledException extends RuntimeException{
    public DisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
