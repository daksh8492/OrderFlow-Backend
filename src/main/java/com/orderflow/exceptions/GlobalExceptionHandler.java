package com.orderflow.exceptions;

import com.orderflow.dto.WarehouseLocationDto;
import org.aspectj.weaver.ast.Var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundException> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VendorNotFoundException.class)
    public ResponseEntity<VendorNotFoundException> handleVendorNotFoundException(VendorNotFoundException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<CustomerNotFoundException> handleCustomerNotFoundException(CustomerNotFoundException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity<WarehouseNotFoundException> handleWarehouseNotFoundException(WarehouseNotFoundException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WarehouseLocationNotFoundException.class)
    public ResponseEntity<WarehouseLocationNotFoundException> handleWarehouseLocationNotFoundException(WarehouseLocationNotFoundException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WarehouseLocationTypeInvalidException.class)
    public ResponseEntity<WarehouseLocationTypeInvalidException> handleWarehouseLocationTypeInvalidException(WarehouseLocationTypeInvalidException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ItemNotFoundException> handleItemNotFoundException(ItemNotFoundException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VariantNotFoundException.class)
    public ResponseEntity<VariantNotFoundException> handleVariantNotFoundException(VariantNotFoundException ex){
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }
}
