package com.orderflow.exceptions;

import com.orderflow.entity.warehouse.WarehouseLocation;

public class WarehouseLocationNotFoundException extends RuntimeException {

    public WarehouseLocationNotFoundException(String message) {
        super(message);
    }
}
