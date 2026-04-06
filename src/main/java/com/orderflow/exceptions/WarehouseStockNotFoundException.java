package com.orderflow.exceptions;

public class WarehouseStockNotFoundException extends RuntimeException{
    public WarehouseStockNotFoundException(String message){
        super(message);
    }
}
