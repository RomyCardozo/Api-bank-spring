package com.apibank.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("Saldo insuficiente");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
