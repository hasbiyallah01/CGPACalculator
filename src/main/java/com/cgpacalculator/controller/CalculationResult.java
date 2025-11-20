package com.cgpacalculator.controller;

/**
 * Data class containing calculation results.
 * Immutable class that holds all calculation outputs including GPA, CGPA,
 * units, classification, and motivational messages.
 */
public class CalculationResult {
    private final double currentSemesterGPA;
    private final double updatedCGPA;
    private final int totalSemesterUnits;
    private final double totalCreditPoints;
    private final String classification;
    private final String motivationalMessage;
    private final double previousCGPA;
    private final int previousUnits;
    
    /**
     * Constructs a new CalculationResult with all calculation data.
     * 
     * @param currentSemesterGPA The GPA for the current semester
     * @param updatedCGPA The updated cumulative GPA
     * @param totalSemesterUnits Total units for the current semester
     * @param totalCreditPoints Total credit points earned
     * @param classification Academic classification based on CGPA
     * @param motivationalMessage Motivational message for the student
     * @param previousCGPA Previous CGPA before current semester
     * @param previousUnits Previous cumulative units before current semester
     */
    public CalculationResult(double currentSemesterGPA, double updatedCGPA, 
                           int totalSemesterUnits, double totalCreditPoints,
                           String classification, String motivationalMessage,
                           double previousCGPA, int previousUnits) {
        this.currentSemesterGPA = currentSemesterGPA;
        this.updatedCGPA = updatedCGPA;
        this.totalSemesterUnits = totalSemesterUnits;
        this.totalCreditPoints = totalCreditPoints;
        this.classification = classification;
        this.motivationalMessage = motivationalMessage;
        this.previousCGPA = previousCGPA;
        this.previousUnits = previousUnits;
    }
    
    /**
     * Gets the current semester GPA.
     * @return The GPA for the current semester
     */
    public double getCurrentSemesterGPA() { 
        return currentSemesterGPA; 
    }
    
    /**
     * Gets the updated cumulative GPA.
     * @return The updated CGPA including current semester
     */
    public double getUpdatedCGPA() { 
        return updatedCGPA; 
    }
    
    /**
     * Gets the total semester units.
     * @return Total units for the current semester
     */
    public int getTotalSemesterUnits() { 
        return totalSemesterUnits; 
    }
    
    /**
     * Gets the total credit points.
     * @return Total credit points earned in current semester
     */
    public double getTotalCreditPoints() { 
        return totalCreditPoints; 
    }
    
    /**
     * Gets the academic classification.
     * @return Classification based on the updated CGPA
     */
    public String getClassification() { 
        return classification; 
    }
    
    /**
     * Gets the motivational message.
     * @return Motivational message for the student
     */
    public String getMotivationalMessage() { 
        return motivationalMessage; 
    }
    
    /**
     * Gets the previous CGPA.
     * @return CGPA before the current semester calculations
     */
    public double getPreviousCGPA() { 
        return previousCGPA; 
    }
    
    /**
     * Gets the previous cumulative units.
     * @return Cumulative units before the current semester
     */
    public int getPreviousUnits() { 
        return previousUnits; 
    }
    
    /**
     * Returns a string representation of the calculation result.
     * @return Formatted string with key calculation values
     */
    @Override
    public String toString() {
        return String.format("CalculationResult{GPA=%.2f, CGPA=%.2f, Units=%d, Classification='%s'}", 
            currentSemesterGPA, updatedCGPA, totalSemesterUnits, classification);
    }
}