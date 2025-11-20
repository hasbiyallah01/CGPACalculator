package com.cgpacalculator.utils.exceptions;public class StudentValidationException extends ValidationException {
    
    private final String studentId;
    private final String violationType;public StudentValidationException(String message) {
        super(message);
        this.studentId = null;
        this.violationType = null;
    }
    
    // Method comment
    public StudentValidationException(String message, String violationType) {
        super(message);
        this.studentId = null;
        this.violationType = violationType;
    }
    
    // Method comment
    public StudentValidationException(String message, String fieldName, Object invalidValue) {
        super(message, fieldName, invalidValue);
        this.studentId = null;
        this.violationType = null;
    }
    
    // Method comment
    public StudentValidationException(String message, String studentId, String violationType, 
                                    String fieldName, Object invalidValue) {
        super(message, fieldName, invalidValue);
        this.studentId = studentId;
        this.violationType = violationType;
    }
    
    // Method comment
    public String getStudentId() {
        return studentId;
    }
    
    // Method comment
    public String getViolationType() {
        return violationType;
    }@Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append(": ").append(getMessage());
        
        if (studentId != null) {
            result.append(" [Student: ").append(studentId).append("]");
        }
        
        if (violationType != null) {
            result.append(" [Violation: ").append(violationType).append("]");
        }
        
        if (getFieldName() != null) {
            result.append(" [Field: ").append(getFieldName()).append("]");
        }
        
        if (getInvalidValue() != null) {
            result.append(" [Invalid Value: ").append(getInvalidValue()).append("]");
        }
        
        return result.toString();
    }
}