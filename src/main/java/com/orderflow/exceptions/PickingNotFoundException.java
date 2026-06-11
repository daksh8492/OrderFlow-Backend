package com.orderflow.exceptions;

public class PickingNotFoundException extends RuntimeException {
    public PickingNotFoundException(String s) {
        super(s);
    }
}
