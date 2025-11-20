package com.cgpacalculator.utils;

import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.exceptions.CourseValidationException;public class CourseValidator {public static void validateCompleteCourse(Course courseToValidate) {
        if (courseToValidate == null) {
            throw new CourseValidationException("Course object cannot be null");
        }
        
        validateCourseName(courseToValidate.getCourseName());
        validateCourseUnits(courseToValidate.getUnits());
        validateCourseLetterGrade(courseToValidate.getLetterGrade());
    }
    
    // Method comment
    public static void validateCourseName(String courseNameToValidate) {
        if (!ValidationUtils.isNotNullOrEmpty(courseNameToValidate)) {
            throw new CourseValidationException(
                Constants.ERROR_INVALID_COURSE_NAME, 
                "courseName", 
                courseNameToValidate
            );
        }
        
        if (!ValidationUtils.isValidCourseName(courseNameToValidate)) {
            throw new CourseValidationException(
                Constants.ERROR_INVALID_COURSE_NAME,
                "courseName",
                courseNameToValidate
            );
        }
    }
    
    // Method comment
    public static void validateCourseUnits(int courseUnitsToValidate) {
        if (!ValidationUtils.isValidCourseUnits(courseUnitsToValidate)) {
            throw new CourseValidationException(
                Constants.ERROR_INVALID_UNITS,
                "units",
                courseUnitsToValidate
            );
        }
    }
    
    // Method comment
    public static void validateCourseLetterGrade(String letterGradeToValidate) {
        if (!ValidationUtils.isNotNullOrEmpty(letterGradeToValidate)) {
            throw new CourseValidationException(
                Constants.ERROR_INVALID_GRADE,
                "letterGrade",
                letterGradeToValidate
            );
        }
        
        if (!ValidationUtils.isValidLetterGrade(letterGradeToValidate)) {
            throw new CourseValidationException(
                Constants.ERROR_INVALID_GRADE,
                "letterGrade",
                letterGradeToValidate
            );
        }
    }
    
    // Method comment
    public static void validateUniqueCourseName(String newCourseName, String[] existingCourseNames) {
        if (existingCourseNames == null || newCourseName == null) {
            return;
        }
        
        String normalizedNewName = newCourseName.trim().toLowerCase();
        
        for (String existingCourseName : existingCourseNames) {
            if (existingCourseName != null && 
                existingCourseName.trim().toLowerCase().equals(normalizedNewName)) {
                throw new CourseValidationException(
                    Constants.ERROR_DUPLICATE_COURSE,
                    newCourseName,
                    "courseName",
                    newCourseName
                );
            }
        }
    }
    
    // Method comment
    public static ValidationResult validateCourseCreationData(String courseName, int courseUnits, String letterGrade) {
        ValidationResult validationResult = new ValidationResult();
        
        try {
            validateCourseName(courseName);
            validationResult.addSuccessMessage("Course name is valid");
        } catch (CourseValidationException exception) {
            validationResult.addErrorMessage("Course Name", exception.getMessage());
        }
        
        try {
            validateCourseUnits(courseUnits);
            validationResult.addSuccessMessage("Course units are valid");
        } catch (CourseValidationException exception) {
            validationResult.addErrorMessage("Course Units", exception.getMessage());
        }
        
        try {
            validateCourseLetterGrade(letterGrade);
            validationResult.addSuccessMessage("Letter grade is valid");
        } catch (CourseValidationException exception) {
            validationResult.addErrorMessage("Letter Grade", exception.getMessage());
        }
        
        return validationResult;
    }
    
    // Method comment
    public static boolean hasCompleteRequiredFields(Course courseToCheck) {
        if (courseToCheck == null) {
            return false;
        }
        
        return ValidationUtils.isNotNullOrEmpty(courseToCheck.getCourseName()) &&
               courseToCheck.getUnits() > 0 &&
               ValidationUtils.isNotNullOrEmpty(courseToCheck.getLetterGrade());
    }
    
    // Method comment
    public static void validateCourseForAddition(Course courseToAdd, String[] existingCourseNames) {
        if (courseToAdd == null) {
            throw new CourseValidationException("Cannot add null course to student record");
        }
        
        if (!hasCompleteRequiredFields(courseToAdd)) {
            throw new CourseValidationException("Course must have all required fields completed before addition");
        }
        
        validateCompleteCourse(courseToAdd);
        validateUniqueCourseName(courseToAdd.getCourseName(), existingCourseNames);
    }
}