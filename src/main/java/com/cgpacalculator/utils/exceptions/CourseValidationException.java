package com.cgpacalculator.utils.exceptions;public class CourseValidationException extends ValidationException {
    
    private final String courseName;public CourseValidationException(String message) {
        super(message);
        this.courseName = null;
    }
    
    // Method comment
    public CourseValidationException(String message, String courseName) {
        super(message, "course", courseName);
        this.courseName = courseName;
    }
    
    // Method comment
    public CourseValidationException(String message, String fieldName, Object invalidValue) {
        super(message, fieldName, invalidValue);
        this.courseName = null;
    }
    
    // Method comment
    public CourseValidationException(String message, String courseName, String fieldName, Object invalidValue) {
        super(message, fieldName, invalidValue);
        this.courseName = courseName;
    }
    
    // Method comment
    public String getCourseName() {
        return courseName;
    }@Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append(": ").append(getMessage());
        
        if (courseName != null) {
            result.append(" [Course: ").append(courseName).append("]");
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