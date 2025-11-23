package com.cgpacalculator.utils;

/**
 * Utility class for converting between letter grades and grade points
 */
public class GradeConverter {
    
    /**
     * Converts letter grade to grade points
     */
    public static double convertLetterGradeToPoints(String letterGrade) {
        return Constants.getGradePoints(letterGrade);
    }
    
    /**
     * Checks if a letter grade is valid
     */
    public static boolean isValidLetterGrade(String letterGrade) {
        return Constants.isValidGrade(letterGrade);
    }
}