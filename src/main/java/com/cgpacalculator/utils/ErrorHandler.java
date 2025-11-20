package com.cgpacalculator.utils;

import com.cgpacalculator.utils.exceptions.ValidationException;
import com.cgpacalculator.utils.exceptions.CalculationException;
import java.util.logging.Logger;
import java.util.logging.Level;

// Centralized error handling utility for comprehensive error management
public final class ErrorHandler {
    
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    
    // Private constructor to prevent instantiation
    private ErrorHandler() {
        throw new UnsupportedOperationException("ErrorHandler is a utility class and cannot be instantiated");
    }
    
    // Handles validation errors with user-friendly messages
    public static String handleValidationError(ValidationException e) {
        String userMessage = createUserFriendlyMessage(e);
        logError("Validation Error", e);
        return userMessage;
    }
    
    // Handles calculation errors with recovery suggestions
    public static String handleCalculationError(CalculationException e) {
        String userMessage = "Calculation failed: " + e.getMessage();
        logError("Calculation Error", e);
        return userMessage;
    }
    
    // Handles general exceptions with fallback error messages
    public static String handleGeneralError(Exception e) {
        String userMessage = "An unexpected error occurred. Please check your input and try again.";
        logError("General Error", e);
        return userMessage;
    }
    
    // Handles number format exceptions with specific guidance
    public static String handleNumberFormatError(NumberFormatException e, String fieldName) {
        String userMessage = String.format("Invalid number format in %s. Please enter a valid number.", fieldName);
        logError("Number Format Error in " + fieldName, e);
        return userMessage;
    }
    
    // Handles file operation errors with recovery suggestions
    public static String handleFileError(Exception e, String operation) {
        String userMessage = String.format("File %s failed: %s", operation, e.getMessage());
        logError("File Operation Error", e);
        return userMessage;
    }
    
    // Creates user-friendly error messages from validation exceptions
    private static String createUserFriendlyMessage(ValidationException e) {
        StringBuilder message = new StringBuilder();
        
        if (e.getFieldName() != null) {
            message.append("Error in ").append(e.getFieldName()).append(": ");
        }
        
        message.append(e.getMessage());
        
        if (e.getInvalidValue() != null) {
            message.append(" (Invalid value: ").append(e.getInvalidValue()).append(")");
        }
        
        return message.toString();
    }
    
    // Logs errors with appropriate level and context
    private static void logError(String context, Exception e) {
        logger.log(Level.WARNING, context + ": " + e.getMessage(), e);
    }
    
    // Validates input and provides specific error messages for common validation failures
    public static void validateCourseInput(String courseName, String unitsText, String grade) {
        try {
            // Validate course name
            if (!ValidationUtils.isNotNullOrEmpty(courseName)) {
                throw new ValidationException("Course name is required", "Course Name", courseName);
            }
            
            if (!ValidationUtils.isValidCourseName(courseName)) {
                throw new ValidationException("Course name must be 1-50 characters", "Course Name", courseName);
            }
            
            // Validate units
            if (!ValidationUtils.isNotNullOrEmpty(unitsText)) {
                throw new ValidationException("Course units are required", "Units", unitsText);
            }
            
            int units;
            try {
                units = Integer.parseInt(unitsText.trim());
            } catch (NumberFormatException e) {
                throw new ValidationException("Units must be a valid number", "Units", unitsText);
            }
            
            if (!ValidationUtils.isValidCourseUnits(units)) {
                throw new ValidationException("Units must be between 1-6", "Units", units);
            }
            
            // Validate grade
            if (!ValidationUtils.isNotNullOrEmpty(grade)) {
                throw new ValidationException("Grade is required", "Grade", grade);
            }
            
            if (!ValidationUtils.isValidLetterGrade(grade)) {
                throw new ValidationException("Grade must be A, B, C, D, E, or F", "Grade", grade);
            }
            
        } catch (ValidationException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new ValidationException("Unexpected error during course validation: " + e.getMessage(), e);
        }
    }
    
    // Validates CGPA input with detailed error messages
    public static void validateCGPAInput(String cgpaText) {
        try {
            if (!ValidationUtils.isNotNullOrEmpty(cgpaText)) {
                throw new ValidationException("CGPA is required", "CGPA", cgpaText);
            }
            
            double cgpa;
            try {
                cgpa = Double.parseDouble(cgpaText.trim());
            } catch (NumberFormatException e) {
                throw new ValidationException("CGPA must be a valid decimal number", "CGPA", cgpaText);
            }
            
            if (!ValidationUtils.isValidCGPAValue(cgpa)) {
                throw new ValidationException("CGPA must be between 0.00-5.00", "CGPA", cgpa);
            }
            
        } catch (ValidationException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new ValidationException("Unexpected error during CGPA validation: " + e.getMessage(), e);
        }
    }
    
    // Validates cumulative units input with detailed error messages
    public static void validateCumulativeUnitsInput(String unitsText) {
        try {
            if (!ValidationUtils.isNotNullOrEmpty(unitsText)) {
                throw new ValidationException("Cumulative units are required", "Cumulative Units", unitsText);
            }
            
            int units;
            try {
                units = Integer.parseInt(unitsText.trim());
            } catch (NumberFormatException e) {
                throw new ValidationException("Cumulative units must be a valid whole number", "Cumulative Units", unitsText);
            }
            
            if (!ValidationUtils.isValidCumulativeUnits(units)) {
                throw new ValidationException("Cumulative units must be non-negative", "Cumulative Units", units);
            }
            
        } catch (ValidationException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new ValidationException("Unexpected error during cumulative units validation: " + e.getMessage(), e);
        }
    }
    
    // Provides recovery suggestions based on error type
    public static String getRecoverySuggestion(Exception e) {
        if (e instanceof ValidationException) {
            ValidationException ve = (ValidationException) e;
            if (ve.getFieldName() != null) {
                switch (ve.getFieldName().toLowerCase()) {
                    case "course name":
                        return "Try entering a course name between 1-50 characters.";
                    case "units":
                        return "Enter a number between 1-6 for course units.";
                    case "grade":
                        return "Select a valid grade: A, B, C, D, E, or F.";
                    case "cgpa":
                        return "Enter a CGPA value between 0.00 and 5.00.";
                    case "cumulative units":
                        return "Enter a positive number for cumulative units.";
                }
            }
            return "Please correct the highlighted field and try again.";
        } else if (e instanceof CalculationException) {
            return "Check that all course data is valid and try recalculating.";
        } else if (e instanceof NumberFormatException) {
            return "Make sure all numeric fields contain valid numbers.";
        }
        return "Please review your input and try again.";
    }
}