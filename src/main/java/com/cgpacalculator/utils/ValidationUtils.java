package com.cgpacalculator.utils;

import com.cgpacalculator.utils.exceptions.ValidationException;
import com.cgpacalculator.utils.exceptions.CourseValidationException;
import com.cgpacalculator.utils.exceptions.StudentValidationException;
import com.cgpacalculator.utils.exceptions.AcademicConstraintException;

// Utility class providing static validation methods with descriptive names
public final class ValidationUtils {
    
    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new UnsupportedOperationException("ValidationUtils is a utility class and cannot be instantiated");
    }
    
    // Validates if course name is 1-50 characters and not null or empty
    public static boolean isValidCourseName(String courseNameToValidate) {
        if (courseNameToValidate == null) {
            return false;
        }
        
        String trimmedCourseName = courseNameToValidate.trim();
        return trimmedCourseName.length() >= Constants.MIN_COURSE_NAME_LENGTH && 
               trimmedCourseName.length() <= Constants.MAX_COURSE_NAME_LENGTH;
    }
    
    // Validates if course units are between 1-6 inclusive
    public static boolean isValidCourseUnits(int courseUnitsToValidate) {
        return courseUnitsToValidate >= Constants.MIN_COURSE_UNITS && 
               courseUnitsToValidate <= Constants.MAX_COURSE_UNITS;
    }
    
    // Validates if letter grade is A, B, C, D, E, or F (case-insensitive)
    public static boolean isValidLetterGrade(String letterGradeToValidate) {
        if (letterGradeToValidate == null) {
            return false;
        }
        
        String normalizedGrade = letterGradeToValidate.trim().toUpperCase();
        return Constants.GRADE_POINTS_MAP.containsKey(normalizedGrade);
    }
    
    // Validates if CGPA value is between 0.00-5.00 inclusive
    public static boolean isValidCGPAValue(double cgpaValueToValidate) {
        return cgpaValueToValidate >= Constants.MIN_CGPA && 
               cgpaValueToValidate <= Constants.MAX_CGPA;
    }
    
    // Validates if cumulative units are non-negative
    public static boolean isValidCumulativeUnits(int cumulativeUnitsToValidate) {
        return cumulativeUnitsToValidate >= 0;
    }
    
    // Validates if semester units meet minimum requirement of 18
    public static boolean meetsSemesterMinimumUnits(int semesterUnitsToCheck) {
        return semesterUnitsToCheck >= Constants.MIN_SEMESTER_UNITS;
    }
    
    // Validates if semester units are within maximum limit of 24
    public static boolean isWithinSemesterMaximumUnits(int semesterUnitsToCheck) {
        return semesterUnitsToCheck <= Constants.MAX_SEMESTER_UNITS;
    }
    
    // Validates if semester units are within acceptable range (18-24)
    public static boolean isValidSemesterUnitsRange(int semesterUnitsToValidate) {
        return meetsSemesterMinimumUnits(semesterUnitsToValidate) && 
               isWithinSemesterMaximumUnits(semesterUnitsToValidate);
    }
    
    // Validates if cumulative units are sufficient for CGPA calculation (minimum 24)
    public static boolean hasSufficientCumulativeUnitsForCGPA(int cumulativeUnitsToCheck) {
        return cumulativeUnitsToCheck >= Constants.MIN_CUMULATIVE_UNITS_FOR_CGPA;
    }
    
    // Validates if academic data is consistent (CGPA > 0 requires cumulative units > 0)
    public static boolean isConsistentAcademicData(double currentCGPA, int cumulativeUnits) {
        // If CGPA is provided, cumulative units should also be provided
        if (currentCGPA > 0.0 && cumulativeUnits == 0) {
            return false;
        }
        return true;
    }
    
    // Validates if string is not null and not empty after trimming
    public static boolean isNotNullOrEmpty(String stringToValidate) {
        return stringToValidate != null && !stringToValidate.trim().isEmpty();
    }
    
    // Validates if numeric value is within specified range (inclusive)
    public static boolean isWithinRange(double valueToCheck, double minimumValue, double maximumValue) {
        return valueToCheck >= minimumValue && valueToCheck <= maximumValue;
    }
    
    // Validates if integer value is within specified range (inclusive)
    public static boolean isWithinRange(int valueToCheck, int minimumValue, int maximumValue) {
        return valueToCheck >= minimumValue && valueToCheck <= maximumValue;
    }
    
    // Throws ValidationException with descriptive message if condition is false
    public static void requireValidCondition(boolean conditionToCheck, String errorMessage) {
        if (!conditionToCheck) {
            throw new ValidationException(errorMessage);
        }
    }
    
    // Throws ValidationException with field information if condition is false
    public static void requireValidCondition(boolean conditionToCheck, String errorMessage, 
                                           String fieldName, Object invalidValue) {
        if (!conditionToCheck) {
            throw new ValidationException(errorMessage, fieldName, invalidValue);
        }
    }
}
