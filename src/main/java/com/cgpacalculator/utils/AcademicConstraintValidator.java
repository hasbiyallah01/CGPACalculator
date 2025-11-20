package com.cgpacalculator.utils;

import com.cgpacalculator.model.Course;
import com.cgpacalculator.model.Student;
import com.cgpacalculator.utils.exceptions.AcademicConstraintException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;public class AcademicConstraintValidator {public static ValidationResult validateSemesterUnitConstraints(int totalSemesterUnits) {
        ValidationResult validationResult = new ValidationResult();
        
        // Check minimum units requirement
        if (totalSemesterUnits < Constants.MIN_SEMESTER_UNITS) {
            validationResult.addErrorMessage(
                "Total Semester Units", 
                String.format("You need at least %d total units from all courses combined. Currently you have %d units. Please add more courses to reach the minimum requirement.", 
                            Constants.MIN_SEMESTER_UNITS,
                            totalSemesterUnits)
            );
        }
        
        // Check maximum units constraint
        if (totalSemesterUnits > Constants.MAX_SEMESTER_UNITS) {
            validationResult.addErrorMessage(
                "Total Semester Units", 
                String.format("You have %d total units from all courses, which exceeds the maximum of %d units per semester. Registration may be blocked or require special approval.", 
                            totalSemesterUnits, 
                            Constants.MAX_SEMESTER_UNITS)
            );
        }
        
        // Add success message if within acceptable range
        if (ValidationUtils.isValidSemesterUnitsRange(totalSemesterUnits)) {
            validationResult.addSuccessMessage(
                String.format("Total semester units (%d) are within the acceptable range of %d-%d units", 
                            totalSemesterUnits, Constants.MIN_SEMESTER_UNITS, Constants.MAX_SEMESTER_UNITS)
            );
        }
        
        return validationResult;
    }
    
    // Method comment
    public static ValidationResult validateCourseUnitConstraints(String courseName, int courseUnits) {
        ValidationResult validationResult = new ValidationResult();
        
        if (!ValidationUtils.isValidCourseUnits(courseUnits)) {
            validationResult.addErrorMessage(
                "Course Units",
                String.format("Course '%s' has invalid units (%d). Valid range is %d-%d units.",
                            courseName, courseUnits, 
                            Constants.MIN_COURSE_UNITS, Constants.MAX_COURSE_UNITS)
            );
        } else {
            validationResult.addSuccessMessage(
                String.format("Course '%s' units are valid (%d units)", courseName, courseUnits)
            );
        }
        
        return validationResult;
    }
    
    // Method comment
    public static ValidationResult validateDuplicateCourseNames(String newCourseName, List<Course> existingCourses) {
        ValidationResult validationResult = new ValidationResult();
        
        if (newCourseName == null || newCourseName.trim().isEmpty()) {
            validationResult.addErrorMessage("Course Name", "Course name cannot be empty");
            return validationResult;
        }
        
        String normalizedNewName = newCourseName.trim().toLowerCase();
        
        // Check for duplicates
        for (Course existingCourse : existingCourses) {
            if (existingCourse.getCourseName() != null && 
                existingCourse.getCourseName().trim().toLowerCase().equals(normalizedNewName)) {
                
                validationResult.addErrorMessage(
                    "Duplicate Course",
                    String.format("Course name '%s' already exists. Please use a unique course name or modify the existing entry.",
                                newCourseName.trim())
                );
                return validationResult;
            }
        }
        
        // No duplicates found
        validationResult.addSuccessMessage(
            String.format("Course name '%s' is unique", newCourseName.trim())
        );
        
        return validationResult;
    }
    
    // Method comment
    public static ValidationResult validateCourseLoadViolations(int currentSemesterUnits, int additionalCourseUnits) {
        ValidationResult validationResult = new ValidationResult();
        int projectedTotal = currentSemesterUnits + additionalCourseUnits;
        
        // Check if adding course would exceed maximum
        if (projectedTotal > Constants.MAX_SEMESTER_UNITS) {
            validationResult.addErrorMessage(
                "Course Load Warning",
                String.format("Adding this %d-unit course would give you %d total units for the semester, " +
                            "which exceeds the %d-unit maximum. You may need special approval to register.",
                            additionalCourseUnits, projectedTotal, Constants.MAX_SEMESTER_UNITS)
            );
        }
        
        // Check if total would still be below minimum
        if (projectedTotal < Constants.MIN_SEMESTER_UNITS) {
            validationResult.addGeneralErrorMessage(
                String.format("After adding this course, you'll have %d total units. You still need %d more units to reach the minimum %d units required for full-time status.",
                            projectedTotal, (Constants.MIN_SEMESTER_UNITS - projectedTotal), Constants.MIN_SEMESTER_UNITS)
            );
        }
        
        // Add success message if within acceptable range
        if (ValidationUtils.isValidSemesterUnitsRange(projectedTotal)) {
            validationResult.addSuccessMessage(
                String.format("Adding this course would give you %d total units (within the acceptable %d-%d range)", 
                            projectedTotal, Constants.MIN_SEMESTER_UNITS, Constants.MAX_SEMESTER_UNITS)
            );
        }
        
        return validationResult;
    }
    
    // Method comment
    public static ValidationResult validateCumulativeUnitWarnings(int cumulativeUnits, double currentCGPA) {
        ValidationResult validationResult = new ValidationResult();
        
        if (currentCGPA > 0.0 && cumulativeUnits < Constants.MIN_CUMULATIVE_UNITS_FOR_CGPA) {
            validationResult.addErrorMessage(
                "CGPA Calculation Warning",
                String.format("Cumulative units (%d) are below the recommended minimum (%d) for accurate CGPA calculation. " +
                            "Results may not reflect true academic standing.",
                            cumulativeUnits, Constants.MIN_CUMULATIVE_UNITS_FOR_CGPA)
            );
        } else if (currentCGPA > 0.0) {
            validationResult.addSuccessMessage(
                String.format("Cumulative units (%d) are sufficient for CGPA calculation", cumulativeUnits)
            );
        }
        
        return validationResult;
    }
    
    // Method comment
    public static ValidationResult validateAllAcademicConstraints(Student student) {
        ValidationResult consolidatedResult = new ValidationResult();
        
        if (student == null) {
            consolidatedResult.addGeneralErrorMessage("Student object cannot be null for validation");
            return consolidatedResult;
        }
        
        // Validate semester unit constraints
        int totalSemesterUnits = student.calculateTotalSemesterUnits();
        ValidationResult semesterValidation = validateSemesterUnitConstraints(totalSemesterUnits);
        consolidatedResult.mergeValidationResult(semesterValidation);
        
        // Validate individual course constraints
        List<Course> courses = student.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            
            // Validate course units
            ValidationResult courseUnitsValidation = validateCourseUnitConstraints(
                course.getCourseName(), course.getUnits()
            );
            consolidatedResult.mergeValidationResult(courseUnitsValidation);
        }
        
        // Validate duplicate course names
        ValidationResult duplicateValidation = validateAllCoursesForDuplicates(courses);
        consolidatedResult.mergeValidationResult(duplicateValidation);
        
        // Validate cumulative unit warnings
        ValidationResult cumulativeValidation = validateCumulativeUnitWarnings(
            student.getCumulativeUnits(), student.getCurrentCGPA()
        );
        consolidatedResult.mergeValidationResult(cumulativeValidation);
        
        return consolidatedResult;
    }
    
    // Method comment
    public static ValidationResult validateAllCoursesForDuplicates(List<Course> courses) {
        ValidationResult validationResult = new ValidationResult();
        
        if (courses == null || courses.isEmpty()) {
            validationResult.addSuccessMessage("No courses to validate for duplicates");
            return validationResult;
        }
        
        Set<String> seenCourseNames = new HashSet<>();
        List<String> duplicateNames = new ArrayList<>();
        
        for (Course course : courses) {
            if (course.getCourseName() != null) {
                String normalizedName = course.getCourseName().trim().toLowerCase();
                
                if (seenCourseNames.contains(normalizedName)) {
                    if (!duplicateNames.contains(course.getCourseName().trim())) {
                        duplicateNames.add(course.getCourseName().trim());
                    }
                } else {
                    seenCourseNames.add(normalizedName);
                }
            }
        }
        
        // Report duplicates
        if (!duplicateNames.isEmpty()) {
            for (String duplicateName : duplicateNames) {
                validationResult.addErrorMessage(
                    "Duplicate Course",
                    String.format("Course name '%s' appears multiple times. Each course should have a unique name.",
                                duplicateName)
                );
            }
        } else {
            validationResult.addSuccessMessage("All course names are unique");
        }
        
        return validationResult;
    }
    
    // Method comment
    public static ValidationResult validateCourseAddition(Student student, Course newCourse) {
        ValidationResult validationResult = new ValidationResult();
        
        if (student == null) {
            validationResult.addGeneralErrorMessage("Student object cannot be null");
            return validationResult;
        }
        
        if (newCourse == null) {
            validationResult.addGeneralErrorMessage("Course to add cannot be null");
            return validationResult;
        }
        
        // Validate the course itself
        ValidationResult courseValidation = validateCourseUnitConstraints(
            newCourse.getCourseName(), newCourse.getUnits()
        );
        validationResult.mergeValidationResult(courseValidation);
        
        // Check for duplicate course names
        ValidationResult duplicateValidation = validateDuplicateCourseNames(
            newCourse.getCourseName(), student.getCourses()
        );
        validationResult.mergeValidationResult(duplicateValidation);
        
        // Check course load violations
        int currentSemesterUnits = student.calculateTotalSemesterUnits();
        ValidationResult loadValidation = validateCourseLoadViolations(
            currentSemesterUnits, newCourse.getUnits()
        );
        validationResult.mergeValidationResult(loadValidation);
        
        return validationResult;
    }
    
    // Method comment
    public static ValidationResult validateAcademicConstraintsWithDetails(
            int totalSemesterUnits, List<Course> courses, int cumulativeUnits, double currentCGPA) {
        
        ValidationResult consolidatedResult = new ValidationResult();
        
        // Validate semester units
        ValidationResult semesterValidation = validateSemesterUnitConstraints(totalSemesterUnits);
        consolidatedResult.mergeValidationResult(semesterValidation);
        
        // Validate individual courses
        if (courses != null) {
            for (Course course : courses) {
                ValidationResult courseValidation = validateCourseUnitConstraints(
                    course.getCourseName(), course.getUnits()
                );
                consolidatedResult.mergeValidationResult(courseValidation);
            }
            
            // Validate duplicates
            ValidationResult duplicateValidation = validateAllCoursesForDuplicates(courses);
            consolidatedResult.mergeValidationResult(duplicateValidation);
        }
        
        // Validate cumulative units
        ValidationResult cumulativeValidation = validateCumulativeUnitWarnings(cumulativeUnits, currentCGPA);
        consolidatedResult.mergeValidationResult(cumulativeValidation);
        
        return consolidatedResult;
    }
    
    // Method comment
    public static boolean isGPACalculationAllowed(Student student) {
        if (student == null || !student.hasCourses()) {
            return false;
        }
        
        ValidationResult validation = validateAllAcademicConstraints(student);
        
        // Allow calculation if there are no critical errors
        // Warnings are acceptable for calculation
        return !validation.hasFieldError("Course Units") && 
               !validation.hasFieldError("Duplicate Course");
    }
    
    // Method comment
    public static String getAcademicConstraintSummary(Student student) {
        if (student == null) {
            return "No student data available for constraint summary";
        }
        
        ValidationResult validation = validateAllAcademicConstraints(student);
        
        if (validation.isSuccessful()) {
            return "All academic constraints are satisfied";
        } else {
            return String.format("Found %d constraint violations that need attention", 
                               validation.getTotalErrorCount());
        }
    }
}