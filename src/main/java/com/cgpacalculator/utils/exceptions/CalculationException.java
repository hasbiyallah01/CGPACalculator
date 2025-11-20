package com.cgpacalculator.utils.exceptions;

// Exception thrown when calculation operations fail
public class CalculationException extends RuntimeException {
    
    // Constructs CalculationException with detail message
    public CalculationException(String message) {
        super(message);
    }
    
    // Constructs CalculationException with message and cause
    public CalculationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Constructs CalculationException with cause
    public CalculationException(Throwable cause) {
        super(cause);
    }
}