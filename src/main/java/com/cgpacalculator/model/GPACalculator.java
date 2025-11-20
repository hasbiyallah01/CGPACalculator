package com.cgpacalculator.model;

import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;public class GPACalculator {private static final int DECIMAL_PRECISION = 2;
    
    // Minimum valid GPA/CGPA value.
    private static final double MINIMUM_GPA = 0.00;
    
    // Maximum valid GPA/CGPA value.
    private static final double MAXIMUM_GPA = 5.00;
    
    // Private constructor to prevent instantiation of utility class.
    private GPACalculator() {
        throw new UnsupportedOperationException("GPACalculator is a utility class and cannot be instantiated");
    }
    
    // Calculates the Grade Point Average (GPA) for a list of courses.
    // 
    // Formula: GPA = Sum of (Course Units × Grade Points) / Sum of Course Units
    // 
    // @param courseList the list of courses to calculate GPA for
    // @return the calculated GPA rounded to 2 decimal places
    // @throws IllegalArgumentException if course list is null or empty, or contains invalid courses
    public static double calculateCurrentSemesterGPA(List<Course> courseList) {
        // Validate input parameters
        if (courseList == null) {
            throw new IllegalArgumentException("Course list cannot be null");
        }
        
        if (courseList.isEmpty()) {
            throw new IllegalArgumentException("Course list cannot be empty for GPA calculation");
        }
        
        double totalCreditPoints = 0.0;
        int totalCreditUnits = 0;
        
        // Calculate total credit points and units
        for (Course course : courseList) {
            // Validate each course
            if (course == null) {
                throw new IllegalArgumentException("Course list contains null course");
            }
            
            if (!course.isValidCourse()) {
                throw new IllegalArgumentException("Course list contains invalid course: " + course.getCourseName());
            }
            
            // Accumulate credit points and units
            totalCreditPoints += course.calculateCreditPoints();
            totalCreditUnits += course.getCourseUnits();
        }
        
        // Prevent division by zero (should not happen with validation above)
        if (totalCreditUnits == 0) {
            throw new IllegalArgumentException("Total credit units cannot be zero");
        }
        
        // Calculate GPA and round to specified precision
        double calculatedGPA = totalCreditPoints / totalCreditUnits;
        return roundToDecimalPlaces(calculatedGPA, DECIMAL_PRECISION);
    }
    
    // Calculates the updated Cumulative Grade Point Average (CGPA) incorporating
    // previous academic records with new semester courses.
    // 
    // Formula: CGPA = (Previous Total Credit Points + New Credit Points) / 
    //                 (Previous Total Units + New Units)
    // 
    // @param previousCGPA the student's current CGPA before new courses
    // @param previousCumulativeUnits the total units completed before new courses
    // @param newSemesterCourses the list of new courses to incorporate
    // @return the updated CGPA rounded to 2 decimal places
    // @throws IllegalArgumentException if parameters are invalid
    public static double calculateUpdatedCGPA(double previousCGPA, int previousCumulativeUnits, 
                                            List<Course> newSemesterCourses) {
        // Validate previous CGPA
        if (previousCGPA < MINIMUM_GPA || previousCGPA > MAXIMUM_GPA) {
            throw new IllegalArgumentException("Previous CGPA must be between " + MINIMUM_GPA + 
                " and " + MAXIMUM_GPA + ". Provided: " + previousCGPA);
        }
        
        // Validate previous cumulative units
        if (previousCumulativeUnits < 0) {
            throw new IllegalArgumentException("Previous cumulative units cannot be negative. Provided: " + 
                previousCumulativeUnits);
        }
        
        // Validate new courses
        if (newSemesterCourses == null || newSemesterCourses.isEmpty()) {
            throw new IllegalArgumentException("New semester courses cannot be null or empty");
        }
        
        // Calculate previous total credit points
        double previousTotalCreditPoints = previousCGPA * previousCumulativeUnits;
        
        // Calculate new semester credit points and units
        double newSemesterCreditPoints = 0.0;
        int newSemesterUnits = 0;
        
        for (Course course : newSemesterCourses) {
            if (course == null || !course.isValidCourse()) {
                throw new IllegalArgumentException("New semester contains invalid course");
            }
            
            newSemesterCreditPoints += course.calculateCreditPoints();
            newSemesterUnits += course.getCourseUnits();
        }
        
        // Calculate updated totals
        double updatedTotalCreditPoints = previousTotalCreditPoints + newSemesterCreditPoints;
        int updatedTotalUnits = previousCumulativeUnits + newSemesterUnits;
        
        // Prevent division by zero
        if (updatedTotalUnits == 0) {
            throw new IllegalArgumentException("Updated total units cannot be zero");
        }
        
        // Calculate and return updated CGPA
        double updatedCGPA = updatedTotalCreditPoints / updatedTotalUnits;
        return roundToDecimalPlaces(updatedCGPA, DECIMAL_PRECISION);
    }
    
    // Method comment
    public static double calculateInitialCGPA(List<Course> initialCourses) {
        // For initial CGPA calculation, it's the same as GPA calculation
        return calculateCurrentSemesterGPA(initialCourses);
    }
    
    // Method comment
    public static double calculateTotalCreditPoints(List<Course> courseList) {
        if (courseList == null) {
            throw new IllegalArgumentException("Course list cannot be null");
        }
        
        double totalCreditPoints = 0.0;
        
        for (Course course : courseList) {
            if (course == null || !course.isValidCourse()) {
                throw new IllegalArgumentException("Course list contains invalid course");
            }
            
            totalCreditPoints += course.calculateCreditPoints();
        }
        
        return roundToDecimalPlaces(totalCreditPoints, DECIMAL_PRECISION);
    }
    
    // Method comment
    public static int calculateTotalCreditUnits(List<Course> courseList) {
        if (courseList == null) {
            throw new IllegalArgumentException("Course list cannot be null");
        }
        
        int totalUnits = 0;
        
        for (Course course : courseList) {
            if (course == null || !course.isValidCourse()) {
                throw new IllegalArgumentException("Course list contains invalid course");
            }
            
            totalUnits += course.getCourseUnits();
        }
        
        return totalUnits;
    }
    
    // Validates if a GPA or CGPA value is within acceptable range.
    // 
    // @param gpaValue the GPA/CGPA value to validate
    // @return true if the value is valid, false otherwise
    public static boolean isValidGPAValue(double gpaValue) {
        return gpaValue >= MINIMUM_GPA && gpaValue <= MAXIMUM_GPA;
    }
    
    // Method comment
    private static double roundToDecimalPlaces(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places cannot be negative");
        }
        
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    // Method comment
    public static String generateCalculationBreakdown(List<Course> courseList) {
        if (courseList == null || courseList.isEmpty()) {
            return "No courses available for calculation breakdown";
        }
        
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("Calculation Breakdown:\n");
        breakdown.append("====================\n");
        
        double totalCreditPoints = 0.0;
        int totalUnits = 0;
        
        for (Course course : courseList) {
            if (course != null && course.isValidCourse()) {
                double creditPoints = course.calculateCreditPoints();
                breakdown.append(String.format("%-20s: %d units × %.1f points = %.2f credit points\n",
                    course.getCourseName(), 
                    course.getCourseUnits(),
                    course.getGradePoints(),
                    creditPoints));
                
                totalCreditPoints += creditPoints;
                totalUnits += course.getCourseUnits();
            }
        }
        
        breakdown.append("--------------------\n");
        breakdown.append(String.format("Total Credit Points: %.2f\n", totalCreditPoints));
        breakdown.append(String.format("Total Units: %d\n", totalUnits));
        breakdown.append(String.format("GPA: %.2f ÷ %d = %.2f\n", 
            totalCreditPoints, totalUnits, 
            totalUnits > 0 ? totalCreditPoints / totalUnits : 0.0));
        
        return breakdown.toString();
    }
}