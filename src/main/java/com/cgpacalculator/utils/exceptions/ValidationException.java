package com.cgpacalculator.utils.exceptions;

// Base exception class for validation errors with field and value tracking
public class ValidationException extends RuntimeException {
    
    private final String fieldName;
    private final Object invalidValue;
    
    // Constructs ValidationException with detailed error message
    public ValidationException(String message) {
        super(message);
        this.fieldName = null;
        this.invalidValue = null;
    }
    
    // Constructs ValidationException with message and field name
    public ValidationException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = null;
    }
    
    // Constructs ValidationException with message, field name, and invalid value
    public ValidationException(String message, String fieldName, Object invalidValue) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
    
    // Constructs ValidationException with message and underlying cause
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.fieldName = null;
        this.invalidValue = null;
    }
    
    // Gets the name of the field that failed validation
    public String getFieldName() {
        return fieldName;
    }
    
    // Gets the invalid value that caused the validation failure
    public Object getInvalidValue() {
        return invalidValue;
    }
    
    // Returns detailed string representation with field name, invalid value, and message
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append(": ").append(getMessage());
        
        if (fieldName != null) {
            result.append(" [Field: ").append(fieldName).append("]");
        }
        
        if (invalidValue != null) {
            result.append(" [Invalid Value: ").append(invalidValue).append("]");
        }
        
        return result.toString();
    }
}