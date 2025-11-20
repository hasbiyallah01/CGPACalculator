package com.cgpacalculator.utils;

import java.util.HashMap;
import java.util.Map;

// Utility class for converting letter grades to grade points using 5-point scale
public class GradeConverter {
    
    // Immutable mapping of letter grades to grade points
    private static final Map<String, Double> LETTER_GRADE_TO_POINTS;
    
    // Static initializer block to populate the grade mapping
    static {
        LETTER_GRADE_TO_POINTS = new HashMap<>();
        LETTER_GRADE_TO_POINTS.put("A", 5.0);
        LETTER_GRADE_TO_POINTS.put("B", 4.0);
        LETTER_GRADE_TO_POINTS.put("C", 3.0);
        LETTER_GRADE_TO_POINTS.put("D", 2.0);
        LETTER_GRADE_TO_POINTS.put("E", 1.0);
        LETTER_GRADE_TO_POINTS.put("F", 0.0);
    }
    
    // Array of valid letter grades for validation
    private static final String[] VALID_LETTER_GRADES = {"A", "B", "C", "D", "E", "F"};
    
    // Private constructor to prevent instantiation
    private GradeConverter() {
        throw new UnsupportedOperationException("GradeConverter is a utility class and cannot be instantiated");
    }
    
    // Converts letter grade to grade points
    public static double convertLetterGradeToPoints(String letterGrade) {
        try {
            // Validate input parameter
            if (letterGrade == null || letterGrade.trim().isEmpty()) {
                throw new IllegalArgumentException("Letter grade cannot be null or empty");
            }
            
            // Normalize the grade to uppercase for consistent lookup
            String normalizedGrade = letterGrade.trim().toUpperCase();
            
            // Check if the grade exists in our mapping
            if (!LETTER_GRADE_TO_POINTS.containsKey(normalizedGrade)) {
                throw new IllegalArgumentException("Invalid letter grade: " + letterGrade + 
                    ". Valid grades are: A, B, C, D, E, F");
            }
            
            return LETTER_GRADE_TO_POINTS.get(normalizedGrade);
            
        } catch (IllegalArgumentException e) {
            // Re-throw with context
            throw e;
        } catch (Exception e) {
            // Handle unexpected errors
            throw new IllegalArgumentException("Unexpected error converting grade '" + letterGrade + "': " + e.getMessage(), e);
        }
    }
    
    // Validates if string is a valid letter grade
    public static boolean isValidLetterGrade(String letterGrade) {
        // Handle null or empty input
        if (letterGrade == null || letterGrade.trim().isEmpty()) {
            return false;
        }
        
        // Normalize and check against valid grades
        String normalizedGrade = letterGrade.trim().toUpperCase();
        return LETTER_GRADE_TO_POINTS.containsKey(normalizedGrade);
    }
    
    // Returns array of all valid letter grades
    public static String[] getValidLetterGrades() {
        // Return a copy to prevent external modification
        return VALID_LETTER_GRADES.clone();
    }
    
    // Gets grade points safely, returns -1.0 if invalid
    public static double getGradePointsSafely(String letterGrade) {
        if (!isValidLetterGrade(letterGrade)) {
            return -1.0;
        }
        
        String normalizedGrade = letterGrade.trim().toUpperCase();
        return LETTER_GRADE_TO_POINTS.get(normalizedGrade);
    }
    
    // Returns formatted string of grading scale for display
    public static String getGradingScaleDisplay() {
        StringBuilder gradingScale = new StringBuilder();
        gradingScale.append("Grading Scale: ");
        
        for (String grade : VALID_LETTER_GRADES) {
            gradingScale.append(grade)
                       .append("=")
                       .append(LETTER_GRADE_TO_POINTS.get(grade))
                       .append(", ");
        }
        
        // Remove the trailing comma and space
        if (gradingScale.length() > 2) {
            gradingScale.setLength(gradingScale.length() - 2);
        }
        
        return gradingScale.toString();
    }
}