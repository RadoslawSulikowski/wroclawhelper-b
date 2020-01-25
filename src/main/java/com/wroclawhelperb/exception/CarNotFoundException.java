package com.wroclawhelperb.exception;

public class CarNotFoundException extends Exception {
    public CarNotFoundException() {
        super();
    }

    public CarNotFoundException(String message) {
        super(message);
    }
}
