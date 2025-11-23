package com.cgpacalculator.utils;

import com.cgpacalculator.model.Course;
import java.util.List;

/**
 * Validator specifically for CGPA calculation requirements
 * Ensures all necessary data is present before allowing calculation
 */
public class CGPACalculationValidator {
    
    /**
     * Validates if all required data is present for CGPA calculation
     * @param courses List of courses entered
     * @param currentCGPA Current CGPA input (can be empty for new students)
     * @param cumulativeUnits Cumulative units input (can be empty for new students)
     * @return ValidationResult with detailed feedback
     */
    public static ValidationResult validateCalculationReadiness(List<Course> courses, String currentCGPA, String cumulativeUnits) {
        ValidationResult result = new ValidationResult();
        
        // Check if courses are present
        if (courses == null || courses.isEmpty()) {
            result.addErrorMessage("Courses", "Please add at least one course before calculating CGPA");
            return result;
        }
        
        // Check if total units are within required range
        int totalUnits = courses.stream().mapToInt(Course::getUnits).sum();
        if (totalUnits < Constants.MIN_SEMESTER_UNITS) {
            result.addErrorMessage("Total Units", 
                String.format("You need at least %d units. Currently you have %d units. Please add more courses.", 
                    Constants.MIN_SEMESTER_UNITS, totalUnits));
            return result;
        }
        
        if (totalUnits > Constants.MAX_SEMESTER_UNITS) {
            result.addErrorMessage("Total Units", 
                String.format("Maximum %d units allowed. You have %d units. Please remove some courses.", 
                    Constants.MAX_SEMESTER_UNITS, totalUnits));
            return result;
        }
        
        // Check if all courses have valid data
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                result.addErrorMessage("Course " + (i + 1), "Course name is required");
            }
            if (course.getUnits() < Constants.MIN_COURSE_UNITS || course.getUnits() > Constants.MAX_COURSE_UNITS) {
                result.addErrorMessage("Course " + (i + 1), "Invalid units for course: " + course.getCourseName());
            }
            if (course.getLetterGrade() == null || !Constants.isValidGrade(course.getLetterGrade())) {
                result.addErrorMessage("Course " + (i + 1), "Invalid grade for course: " + course.getCourseName());
            }
        }
        
        // Check consistency of current CGPA and cumulative units
        boolean hasCGPA = currentCGPA != null && !currentCGPA.trim().isEmpty();
        boolean hasUnits = cumulativeUnits != null && !cumulativeUnits.trim().isEmpty();
        
        if (hasCGPA && !hasUnits) {
            result.addErrorMessage("Academic Data", "If current CGPA is provided, cumulative units must also be provided");
        }
        
        if (hasUnits && !hasCGPA) {
            result.addErrorMessage("Academic Data", "If cumulative units are provided, current CGPA must also be provided");
        }
        
        // If we have errors, return them
        if (result.hasErrors()) {
            return result;
        }
        
        // All validations passed
        result.addSuccessMessage("All required data is complete. Ready to calculate CGPA!");
        return result;
    }
    
    /**
     * Quick check if calculation can proceed
     */
    public static boolean canCalculate(List<Course> courses, String currentCGPA, String cumulativeUnits) {
        ValidationResult result = validateCalculationReadiness(courses, currentCGPA, cumulativeUnits);
        return !result.hasErrors();
    }
    
    /**
     * Gets a summary message for calculation readiness
     */
    public static String getCalculationReadinessSummary(List<Course> courses, String currentCGPA, String cumulativeUnits) {
        ValidationResult result = validateCalculationReadiness(courses, currentCGPA, cumulativeUnits);
        
        if (result.hasErrors()) {
            return "Cannot calculate: " + result.getErrorMessages().get(0).getMessage();
        } else {
            int totalUnits = courses.stream().mapToInt(Course::getUnits).sum();
            return String.format("Ready to calculate! %d courses, %d total units", courses.size(), totalUnits);
        }
    }
}