package com.cgpacalculator.utils.exceptions;public class AcademicConstraintException extends ValidationException {
    
    private final String constraintType;
    private final double currentValue;
    private final double limitValue;public AcademicConstraintException(String message) {
        super(message);
        this.constraintType = null;
        this.currentValue = 0.0;
        this.limitValue = 0.0;
    }
    
    // Method comment
    public AcademicConstraintException(String message, String constraintType) {
        super(message);
        this.constraintType = constraintType;
        this.currentValue = 0.0;
        this.limitValue = 0.0;
    }
    
    // Method comment
    public AcademicConstraintException(String message, String constraintType, 
                                     double currentValue, double limitValue) {
        super(message, constraintType, currentValue);
        this.constraintType = constraintType;
        this.currentValue = currentValue;
        this.limitValue = limitValue;
    }
    
    // Method comment
    public String getConstraintType() {
        return constraintType;
    }
    
    // Method comment
    public double getCurrentValue() {
        return currentValue;
    }
    
    // Method comment
    public double getLimitValue() {
        return limitValue;
    }
    
    // Method comment
    public boolean isMinimumViolation() {
        return currentValue < limitValue;
    }
    
    // Method comment
    public boolean isMaximumViolation() {
        return currentValue > limitValue;
    }@Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append(": ").append(getMessage());
        
        if (constraintType != null) {
            result.append(" [Constraint: ").append(constraintType).append("]");
        }
        
        if (currentValue != 0.0 || limitValue != 0.0) {
            result.append(" [Current: ").append(currentValue)
                  .append(", Limit: ").append(limitValue).append("]");
        }
        
        return result.toString();
    }
}