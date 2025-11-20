package com.cgpacalculator.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;public class ClassificationManager {public enum DegreeClassification {
        FIRST_CLASS("First Class", 4.50, 5.00, "#2E7D32"),        // Green
        SECOND_CLASS_UPPER("Second Class Upper", 3.50, 4.49, "#1976D2"), // Blue  
        SECOND_CLASS_LOWER("Second Class Lower", 2.50, 3.49, "#F57C00"), // Orange
        THIRD_CLASS("Third Class", 1.50, 2.49, "#FF5722"),        // Deep Orange
        FAIL("Fail", 0.00, 1.49, "#D32F2F");                     // Red
        
        private final String displayName;
        private final double minimumCGPA;
        private final double maximumCGPA;
        private final String colorCode;DegreeClassification(String displayName, double minimumCGPA, double maximumCGPA, String colorCode) {
            this.displayName = displayName;
            this.minimumCGPA = minimumCGPA;
            this.maximumCGPA = maximumCGPA;
            this.colorCode = colorCode;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMinimumCGPA() { return minimumCGPA; }
        public double getMaximumCGPA() { return maximumCGPA; }
        public String getColorCode() { return colorCode; }
    
    // Method comment
    public boolean containsCGPA(double cgpa) {
            return cgpa >= minimumCGPA && cgpa <= maximumCGPA;
        }
    }
    
    // Motivational message categories based on performance levels.
    private static final List<String> EXCELLENT_PERFORMANCE_MESSAGES = Arrays.asList(
        "Excellent work! Keep it up! 🌟",
        "Outstanding performance! You're crushing it! 💪",
        "Phenomenal grades! You're on fire! 🔥",
        "Incredible achievement! Stay focused! 🎯",
        "Amazing work! You're setting the bar high! 📈",
        "Stellar performance! Keep pushing boundaries! 🚀"
    );
    
    private static final List<String> GOOD_PERFORMANCE_MESSAGES = Arrays.asList(
        "Great job! You're doing well! 👍",
        "Solid performance! Keep up the momentum! ⚡",
        "Nice work! You're on the right track! 🛤️",
        "Good progress! Stay consistent! 📊",
        "Well done! Keep building on this success! 🏗️",
        "Strong performance! You've got this! 💯"
    );
    
    private static final List<String> AVERAGE_PERFORMANCE_MESSAGES = Arrays.asList(
        "Above average, you can do better! Time to lock in! 🔒",
        "Good foundation! Let's push for excellence! 📚",
        "Decent work! Ready to level up? 📈",
        "You're getting there! Time to step it up! ⬆️",
        "Solid base! Let's aim higher next semester! 🎯",
        "Not bad! But you've got more potential! 💎"
    );
    
    private static final List<String> NEEDS_IMPROVEMENT_MESSAGES = Arrays.asList(
        "You've got this! Every expert was once a beginner! 🌱",
        "Tough semester? Fresh start ahead! 🌅",
        "Challenges build character! Keep pushing! 💪",
        "Every setback is a setup for a comeback! 🔄",
        "Growth happens outside comfort zones! 📈",
        "Your potential is unlimited! Time to unlock it! 🔓"
    );
    
    private static final List<String> CRITICAL_IMPROVEMENT_MESSAGES = Arrays.asList(
        "New semester, new opportunities! You can turn this around! 🔄",
        "Every journey starts with a single step! 👣",
        "Believe in yourself! Improvement is always possible! 🌟",
        "Focus on progress, not perfection! 📈",
        "Your comeback story starts now! 📖",
        "Small steps lead to big changes! 🚶‍♂️➡️🏃‍♂️"
    );
    
    // Random number generator for selecting motivational messages.
    private static final Random messageRandomizer = new Random();
    
    // Private constructor to prevent instantiation of utility class.
    private ClassificationManager() {
        throw new UnsupportedOperationException("ClassificationManager is a utility class and cannot be instantiated");
    }
    
    // Method comment
    public static DegreeClassification determineDegreeClassification(double cgpaValue) {
        // Validate CGPA range
        if (cgpaValue < 0.00 || cgpaValue > 5.00) {
            throw new IllegalArgumentException("CGPA must be between 0.00 and 5.00. Provided: " + cgpaValue);
        }
        
        // Find the appropriate classification
        for (DegreeClassification classification : DegreeClassification.values()) {
            if (classification.containsCGPA(cgpaValue)) {
                return classification;
            }
        }
        
        // This should never happen with proper enum setup, but included for safety
        throw new IllegalStateException("No classification found for CGPA: " + cgpaValue);
    }
    
    // Method comment
    public static String getClassificationName(double cgpaValue) {
        return determineDegreeClassification(cgpaValue).getDisplayName();
    }
    
    // Method comment
    public static String getClassificationColor(double cgpaValue) {
        return determineDegreeClassification(cgpaValue).getColorCode();
    }
    
    // Method comment
    public static String generateMotivationalMessage(double cgpaValue) {
        // Validate CGPA range
        if (cgpaValue < 0.00 || cgpaValue > 5.00) {
            throw new IllegalArgumentException("CGPA must be between 0.00 and 5.00. Provided: " + cgpaValue);
        }
        
        List<String> messageCategory;
        
        // Select message category based on CGPA performance
        if (cgpaValue >= 4.50) {
            // First Class - Excellent performance
            messageCategory = EXCELLENT_PERFORMANCE_MESSAGES;
        } else if (cgpaValue >= 4.00) {
            // High Second Class Upper - Good performance  
            messageCategory = GOOD_PERFORMANCE_MESSAGES;
        } else if (cgpaValue >= 2.50) {
            // Second Class Upper/Lower - Average performance
            messageCategory = AVERAGE_PERFORMANCE_MESSAGES;
        } else if (cgpaValue >= 1.50) {
            // Third Class - Needs improvement
            messageCategory = NEEDS_IMPROVEMENT_MESSAGES;
        } else {
            // Fail - Critical improvement needed
            messageCategory = CRITICAL_IMPROVEMENT_MESSAGES;
        }
        
        // Return a random message from the selected category
        int randomIndex = messageRandomizer.nextInt(messageCategory.size());
        return messageCategory.get(randomIndex);
    }
    
    // Method comment
    public static String generatePerformanceSummary(double cgpaValue) {
        DegreeClassification classification = determineDegreeClassification(cgpaValue);
        String motivationalMessage = generateMotivationalMessage(cgpaValue);
        
        StringBuilder summary = new StringBuilder();
        summary.append("CGPA: ").append(String.format("%.2f", cgpaValue)).append("\n");
        summary.append("Classification: ").append(classification.getDisplayName()).append("\n");
        summary.append("Range: ").append(String.format("%.2f - %.2f", 
            classification.getMinimumCGPA(), classification.getMaximumCGPA())).append("\n");
        summary.append("Message: ").append(motivationalMessage);
        
        return summary.toString();
    }
    
    // Method comment
    public static String getClassificationRangesDisplay() {
        StringBuilder ranges = new StringBuilder();
        ranges.append("Degree Classification Ranges:\n");
        ranges.append("============================\n");
        
        for (DegreeClassification classification : DegreeClassification.values()) {
            ranges.append(String.format("%-20s: %.2f - %.2f\n",
                classification.getDisplayName(),
                classification.getMinimumCGPA(),
                classification.getMaximumCGPA()));
        }
        
        return ranges.toString();
    }
    
    // Method comment
    public static boolean isPassingGrade(double cgpaValue) {
        if (cgpaValue < 0.00 || cgpaValue > 5.00) {
            throw new IllegalArgumentException("CGPA must be between 0.00 and 5.00. Provided: " + cgpaValue);
        }
        
        return cgpaValue >= DegreeClassification.THIRD_CLASS.getMinimumCGPA();
    }
    
    // Method comment
    public static double getPointsToNextLevel(double currentCGPA) {
        if (currentCGPA < 0.00 || currentCGPA > 5.00) {
            throw new IllegalArgumentException("CGPA must be between 0.00 and 5.00. Provided: " + currentCGPA);
        }
        
        // If already at First Class level, no improvement needed
        if (currentCGPA >= DegreeClassification.FIRST_CLASS.getMinimumCGPA()) {
            return 0.0;
        }
        
        // Find the next classification level
        for (DegreeClassification classification : DegreeClassification.values()) {
            if (currentCGPA < classification.getMinimumCGPA()) {
                return classification.getMinimumCGPA() - currentCGPA;
            }
        }
        
        return 0.0;
    }
}