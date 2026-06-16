package com.orderflow.exceptions;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(String s) {
        super(s);
    }
}
