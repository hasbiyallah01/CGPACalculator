package com.cgpacalculator.utils;

import java.util.Map;
import java.util.HashMap;

// Constants class containing static final variables and grade mappings
public final class Constants {
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
    
    // Grade Point Mappings
    public static final Map<String, Double> GRADE_POINTS_MAP = Map.of(
        "A", 5.0,
        "B", 4.0,
        "C", 3.0,
        "D", 2.0,
        "E", 1.0,
        "F", 0.0
    );
    
    // Valid Grades Array
    public static final String[] VALID_GRADES = {"A", "B", "C", "D", "E", "F"};
    
    // Academic Constraints
    public static final int MIN_COURSE_UNITS = 1;
    public static final int MAX_COURSE_UNITS = 6;
    public static final int MIN_SEMESTER_UNITS = 18;
    public static final int MAX_SEMESTER_UNITS = 24;
    public static final int MIN_CUMULATIVE_UNITS_FOR_CGPA = 24;
    
    // CGPA Ranges
    public static final double MIN_CGPA = 0.00;
    public static final double MAX_CGPA = 5.00;
    
    // Course Name Constraints
    public static final int MIN_COURSE_NAME_LENGTH = 1;
    public static final int MAX_COURSE_NAME_LENGTH = 50;
    
    // Degree Classification Ranges
    public static final double FIRST_CLASS_MIN = 4.50;
    public static final double SECOND_CLASS_UPPER_MIN = 3.50;
    public static final double SECOND_CLASS_LOWER_MIN = 2.50;
    public static final double THIRD_CLASS_MIN = 1.50;
    
    // Classification Labels
    public static final String FIRST_CLASS = "First Class";
    public static final String SECOND_CLASS_UPPER = "Second Class Upper";
    public static final String SECOND_CLASS_LOWER = "Second Class Lower";
    public static final String THIRD_CLASS = "Third Class";
    public static final String FAIL_CLASS = "Fail";
    
    // Motivational Messages by CGPA Range
    public static final Map<String, String[]> MOTIVATIONAL_MESSAGES = new HashMap<String, String[]>() {{
        put("EXCELLENT", new String[]{
            "NAH THIS IS CRAZY — you’re actually HIM/HER",
            "You’re cooking like a 5-star chef, zero missed shots",
            "Top-tier performance — give this person a trophy already",
            "You’re on demon time fr",
            "This is elite behaviour — everyone else should be taking notes",
            "You’re not studying… you’re speed-running academics",
            "Bruh, leave some success for others, you’re hogging all the Ws"
        });

        
        put("GOOD", new String[]{
            "W performance! But don’t get too comfy",
            "You’re above average, but relax — you’re not a superstar yet",
            "Good stuff! Now push before it turns mid",
            "Clean work, but aim higher — we need greatness not goodness",
            "You’re doing good… but good is the enemy of GOATED",
            "Nice run, but don’t start celebrating like you won Champions League"
        });

        
        put("AVERAGE", new String[]{
            "You’re not bad, but you’re definitely not him/herrrr",
            "This is giving ‘almost there but not quite’ energy",
            "Mid performance tbh — lock in before it gets embarrassing",
            "You’re skating by… time to actually cook fr",
            "You’re cruising like it’s Sunday afternoon — PRESS THE GAS",
            "You’re one distraction away from joining the Needs Improvement gang",
            "Average isn’t a personality, level up bro"
        });

        
        put("NEEDS_IMPROVEMENT", new String[]{
            "Nahh bro, be for real… did you even read the questions?",
            "At this point, just press Ctrl+Alt+Del on your brain",
            "You better lock in BEFORE I tell you to drop out fr",
            "The way this result is looking… school might not be your calling",
            "No pressure, but if you continue like this, start practicing ‘Welcome to KFC, how may I help you?’",
            "You’re one score away from transferring to Night School",
            "This performance is giving ‘Try Again Next Semester’"
        });

    }};
    
    // Validation Error Messages
    public static final String ERROR_INVALID_COURSE_NAME = "Course name must be 1-50 characters";
    public static final String ERROR_INVALID_UNITS = "Units must be between 1-6";
    public static final String ERROR_INVALID_GRADE = "Grade must be A, B, C, D, E, or F";
    public static final String ERROR_UNITS_BELOW_MINIMUM = "Minimum required units per semester not met";
    public static final String ERROR_UNITS_ABOVE_MAXIMUM = "Maximum allowed units per semester exceeded";
    public static final String ERROR_DUPLICATE_COURSE = "Duplicate course detected";
    public static final String ERROR_INVALID_CGPA = "CGPA must be between 0.00-5.00";
    public static final String ERROR_INSUFFICIENT_CUMULATIVE_UNITS = "Insufficient cumulative units for CGPA calculation";
    public static final String ERROR_INCONSISTENT_ACADEMIC_DATA = "Current CGPA provided but cumulative units missing";
    
    // Warning Messages
    public static final String WARNING_REGISTRATION_BLOCKED = "Maximum total units exceeded - registration may be blocked";
    public static final String WARNING_BELOW_MINIMUM_UNITS = "Total semester units below minimum requirement - add more courses";
    
    // UI Constants
    public static final String APPLICATION_TITLE = "CGPA Calculator";
    public static final String VERSION = "1.0.0";
    
    // File Constants
    public static final String DEFAULT_SAVE_FILENAME = "cgpa_data.json";
    public static final String FILE_EXTENSION = ".json";
    
    // Calculation Precision
    public static final int DECIMAL_PLACES = 2;
    
    // Color Coding for Classifications (RGB values)
    public static final String FIRST_CLASS_COLOR = "#4CAF50";      // Green
    public static final String SECOND_CLASS_UPPER_COLOR = "#8BC34A"; // Light Green
    public static final String SECOND_CLASS_LOWER_COLOR = "#FFC107"; // Amber
    public static final String THIRD_CLASS_COLOR = "#FF9800";       // Orange
    public static final String FAIL_CLASS_COLOR = "#F44336";        // Red
    
    // Gets the grade point value for a given letter grade
    public static double getGradePoints(String grade) {
        return GRADE_POINTS_MAP.getOrDefault(grade.toUpperCase(), 0.0);
    }
    
    // Checks if a grade is valid
    public static boolean isValidGrade(String grade) {
        return grade != null && GRADE_POINTS_MAP.containsKey(grade.toUpperCase());
    }
    
    // Gets the classification for a given CGPA
    public static String getClassification(double cgpa) {
        if (cgpa >= FIRST_CLASS_MIN) {
            return FIRST_CLASS;
        } else if (cgpa >= SECOND_CLASS_UPPER_MIN) {
            return SECOND_CLASS_UPPER;
        } else if (cgpa >= SECOND_CLASS_LOWER_MIN) {
            return SECOND_CLASS_LOWER;
        } else if (cgpa >= THIRD_CLASS_MIN) {
            return THIRD_CLASS;
        } else {
            return FAIL_CLASS;
        }
    }
    
    // Gets the color code for a given classification
    public static String getClassificationColor(String classification) {
        switch (classification) {
            case FIRST_CLASS:
                return FIRST_CLASS_COLOR;
            case SECOND_CLASS_UPPER:
                return SECOND_CLASS_UPPER_COLOR;
            case SECOND_CLASS_LOWER:
                return SECOND_CLASS_LOWER_COLOR;
            case THIRD_CLASS:
                return THIRD_CLASS_COLOR;
            case FAIL_CLASS:
                return FAIL_CLASS_COLOR;
            default:
                return "#000000"; // Black for unknown
        }
    }
}