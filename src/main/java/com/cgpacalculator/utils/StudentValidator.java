package com.cgpacalculator.utils;

import com.cgpacalculator.model.Student;
import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.exceptions.StudentValidationException;
import com.cgpacalculator.utils.exceptions.AcademicConstraintException;
import java.util.List;public class StudentValidator {public static void validateCompleteStudent(Student studentToValidate) {
        if (studentToValidate == null) {
            throw new StudentValidationException("Student object cannot be null");
        }
        
        validateStudentCGPA(studentToValidate.getCurrentCGPA());
        validateStudentCumulativeUnits(studentToValidate.getCumulativeUnits());
        validateAcademicDataConsistency(studentToValidate.getCurrentCGPA(), studentToValidate.getCumulativeUnits());
    }
    
    // Method comment
    public static void validateStudentCGPA(double cgpaToValidate) {
        if (!ValidationUtils.isValidCGPAValue(cgpaToValidate)) {
            throw new StudentValidationException(
                Constants.ERROR_INVALID_CGPA,
                "currentCGPA",
                cgpaToValidate
            );
        }
    }
    
    // Method comment
    public static void validateStudentCumulativeUnits(int cumulativeUnitsToValidate) {
        if (!ValidationUtils.isValidCumulativeUnits(cumulativeUnitsToValidate)) {
            throw new StudentValidationException(
                "Cumulative units must be non-negative",
                "cumulativeUnits",
                cumulativeUnitsToValidate
            );
        }
    }
    
    // Method comment
    public static void validateAcademicDataConsistency(double currentCGPA, int cumulativeUnits) {
        if (!ValidationUtils.isConsistentAcademicData(currentCGPA, cumulativeUnits)) {
            throw new StudentValidationException(
                Constants.ERROR_INCONSISTENT_ACADEMIC_DATA,
                "ACADEMIC_CONSISTENCY"
            );
        }
    }
    
    // Method comment
    public static void validateSemesterUnitConstraints(Student studentToValidate) {
        if (studentToValidate == null) {
            throw new StudentValidationException("Student object cannot be null for semester validation");
        }
        
        int totalSemesterUnits = studentToValidate.calculateTotalSemesterUnits();
        
        if (!ValidationUtils.meetsSemesterMinimumUnits(totalSemesterUnits)) {
            throw new AcademicConstraintException(
                Constants.ERROR_UNITS_BELOW_MINIMUM,
                "MINIMUM_SEMESTER_UNITS",
                totalSemesterUnits,
                Constants.MIN_SEMESTER_UNITS
            );
        }
        
        if (!ValidationUtils.isWithinSemesterMaximumUnits(totalSemesterUnits)) {
            throw new AcademicConstraintException(
                Constants.ERROR_UNITS_ABOVE_MAXIMUM,
                "MAXIMUM_SEMESTER_UNITS",
                totalSemesterUnits,
                Constants.MAX_SEMESTER_UNITS
            );
        }
    }
    
    // Method comment
    public static void validateSufficientCumulativeUnitsForCGPA(Student studentToValidate) {
        if (studentToValidate == null) {
            throw new StudentValidationException("Student object cannot be null for CGPA validation");
        }
        
        int cumulativeUnits = studentToValidate.getCumulativeUnits();
        
        if (!ValidationUtils.hasSufficientCumulativeUnitsForCGPA(cumulativeUnits)) {
            throw new AcademicConstraintException(
                Constants.ERROR_INSUFFICIENT_CUMULATIVE_UNITS,
                "INSUFFICIENT_CUMULATIVE_UNITS",
                cumulativeUnits,
                Constants.MIN_CUMULATIVE_UNITS_FOR_CGPA
            );
        }
    }
    
    // Method comment
    public static void validateCourseAddition(Student studentToValidate, Course newCourseToAdd) {
        if (studentToValidate == null) {
            throw new StudentValidationException("Student object cannot be null for course addition");
        }
        
        if (newCourseToAdd == null) {
            throw new StudentValidationException("Course to add cannot be null");
        }
        
        // Validate the course itself
        CourseValidator.validateCompleteCourse(newCourseToAdd);
        
        // Check for duplicate course names
        List<Course> existingCourses = studentToValidate.getCourses();
        String[] existingCourseNames = existingCourses.stream()
                                                     .map(Course::getCourseName)
                                                     .toArray(String[]::new);
        
        CourseValidator.validateUniqueCourseName(newCourseToAdd.getCourseName(), existingCourseNames);
        
        // Check if adding this course would violate unit constraints
        int currentSemesterUnits = studentToValidate.calculateTotalSemesterUnits();
        int newTotalUnits = currentSemesterUnits + newCourseToAdd.getUnits();
        
        if (!ValidationUtils.isWithinSemesterMaximumUnits(newTotalUnits)) {
            throw new AcademicConstraintException(
                "Adding this course would exceed maximum semester units",
                "MAXIMUM_SEMESTER_UNITS",
                newTotalUnits,
                Constants.MAX_SEMESTER_UNITS
            );
        }
    }
    
    // Method comment
    public static ValidationResult validateStudentForAcademicWarnings(Student studentToValidate) {
        ValidationResult validationResult = new ValidationResult();
        
        if (studentToValidate == null) {
            validationResult.addGeneralErrorMessage("Student object cannot be null");
            return validationResult;
        }
        
        int totalSemesterUnits = studentToValidate.calculateTotalSemesterUnits();
        
        // Check minimum units warning
        if (!ValidationUtils.meetsSemesterMinimumUnits(totalSemesterUnits)) {
            validationResult.addErrorMessage(
                "Semester Units", 
                Constants.WARNING_BELOW_MINIMUM_UNITS + " (Current: " + totalSemesterUnits + ")"
            );
        }
        
        // Check maximum units warning
        if (!ValidationUtils.isWithinSemesterMaximumUnits(totalSemesterUnits)) {
            validationResult.addErrorMessage(
                "Semester Units", 
                Constants.WARNING_REGISTRATION_BLOCKED + " (Current: " + totalSemesterUnits + ")"
            );
        }
        
        // Check CGPA calculation eligibility
        if (studentToValidate.getCurrentCGPA() > 0.0 && 
            !ValidationUtils.hasSufficientCumulativeUnitsForCGPA(studentToValidate.getCumulativeUnits())) {
            validationResult.addErrorMessage(
                "Cumulative Units", 
                Constants.ERROR_INSUFFICIENT_CUMULATIVE_UNITS
            );
        }
        
        // Check academic data consistency
        if (!ValidationUtils.isConsistentAcademicData(
                studentToValidate.getCurrentCGPA(), 
                studentToValidate.getCumulativeUnits())) {
            validationResult.addErrorMessage(
                "Academic Data", 
                Constants.ERROR_INCONSISTENT_ACADEMIC_DATA
            );
        }
        
        // Add success messages if no issues found
        if (validationResult.isSuccessful()) {
            validationResult.addSuccessMessage("All academic constraints are satisfied");
            validationResult.addSuccessMessage("Student data is consistent and valid");
        }
        
        return validationResult;
    }
    
    // Method comment
    public static boolean hasRegisteredCourses(Student studentToCheck) {
        return studentToCheck != null && studentToCheck.hasCourses();
    }
    
    // Method comment
    public static void validateEligibilityForGPACalculation(Student studentToValidate) {
        if (!hasRegisteredCourses(studentToValidate)) {
            throw new StudentValidationException(
                "Student must have at least one course registered for GPA calculation",
                "NO_COURSES_REGISTERED"
            );
        }
        
        // Validate all courses are complete and valid
        List<Course> studentCourses = studentToValidate.getCourses();
        for (int courseIndex = 0; courseIndex < studentCourses.size(); courseIndex++) {
            Course currentCourse = studentCourses.get(courseIndex);
            try {
                CourseValidator.validateCompleteCourse(currentCourse);
            } catch (Exception validationException) {
                throw new StudentValidationException(
                    "Course at index " + courseIndex + " is invalid: " + validationException.getMessage(),
                    "INVALID_COURSE_DATA"
                );
            }
        }
    }
    
    // Method comment
    public static void validateEligibilityForCGPACalculation(Student studentToValidate) {
        // First validate GPA calculation eligibility
        validateEligibilityForGPACalculation(studentToValidate);
        
        // Then validate CGPA-specific requirements
        if (studentToValidate.getCurrentCGPA() > 0.0) {
            validateSufficientCumulativeUnitsForCGPA(studentToValidate);
            validateAcademicDataConsistency(
                studentToValidate.getCurrentCGPA(), 
                studentToValidate.getCumulativeUnits()
            );
        }
    }
}