package com.cgpacalculator.model;

import java.util.ArrayList;
import java.util.List;

// Represents a student with course management capabilities
public class Student {
    private double currentCGPA;
    private int cumulativeUnits;
    private List<Course> courses;
    
    // Default constructor
    public Student() {
        this.currentCGPA = 0.0;
        this.cumulativeUnits = 0;
        this.courses = new ArrayList<>();
    }
    
    // Constructor with current academic standing
    public Student(double currentCGPA, int cumulativeUnits) {
        this.currentCGPA = currentCGPA;
        this.cumulativeUnits = cumulativeUnits;
        this.courses = new ArrayList<>();
    }
    
    // Current CGPA - Getter and Setter
    public double getCurrentCGPA() {
        return currentCGPA;
    }
    
    public void setCurrentCGPA(double currentCGPA) {
        if (isValidCGPA(currentCGPA)) {
            this.currentCGPA = currentCGPA;
        } else {
            throw new IllegalArgumentException("CGPA must be between 0.00-5.00");
        }
    }
    
    // Cumulative Units - Getter and Setter
    public int getCumulativeUnits() {
        return cumulativeUnits;
    }
    
    public void setCumulativeUnits(int cumulativeUnits) {
        if (cumulativeUnits >= 0) {
            this.cumulativeUnits = cumulativeUnits;
        } else {
            throw new IllegalArgumentException("Cumulative units must be non-negative");
        }
    }
    
    // Courses - Getters only (managed through specific methods)
    public List<Course> getCourses() {
        return new ArrayList<>(courses); // Return defensive copy
    }
    
    public int getNumberOfCourses() {
        return courses.size();
    }
    
    // Method comment
    public boolean addCourse(Course course) {
        if (course == null || !course.isValid()) {
            return false;
        }
        
        // Check for duplicate course names
        if (hasDuplicateCourseName(course.getCourseName())) {
            throw new IllegalArgumentException("Duplicate course detected: " + course.getCourseName());
        }
        
        courses.add(course);
        return true;
    }
    
    // Method comment
    public boolean removeCourse(int index) {
        if (index >= 0 && index < courses.size()) {
            courses.remove(index);
            return true;
        }
        return false;
    }
    
    // Method comment
    public boolean removeCourse(Course course) {
        return courses.remove(course);
    }
    
    // Clears all courses from the student's list
    public void clearAllCourses() {
        courses.clear();
    }
    
    // Method comment
    public Course getCourse(int index) {
        if (index >= 0 && index < courses.size()) {
            return courses.get(index);
        }
        return null;
    }
    
    // Method comment
    public int calculateTotalSemesterUnits() {
        return courses.stream()
                     .mapToInt(Course::getUnits)
                     .sum();
    }
    
    // Method comment
    public double calculateTotalCreditPoints() {
        return courses.stream()
                     .mapToDouble(Course::calculateCreditPoints)
                     .sum();
    }
    
    // Method comment
    public boolean validateCourseLoad() {
        int totalUnits = calculateTotalSemesterUnits();
        return totalUnits >= 18 && totalUnits <= 24;
    }
    
    // Method comment
    public boolean isBelowMinimumUnits() {
        return calculateTotalSemesterUnits() < 18;
    }
    
    // Method comment
    public boolean isAboveMaximumUnits() {
        return calculateTotalSemesterUnits() > 24;
    }
    
    // Method comment
    public boolean hasCourses() {
        return !courses.isEmpty();
    }
    
    // Method comment
    public boolean isAcademicDataConsistent() {
        // If CGPA is provided, cumulative units should also be provided
        if (currentCGPA > 0.0 && cumulativeUnits == 0) {
            return false;
        }
        return true;
    }
    
    // Private helper methods
    private boolean isValidCGPA(double cgpa) {
        return cgpa >= 0.0 && cgpa <= 5.0;
    }
    
    private boolean hasDuplicateCourseName(String courseName) {
        return courses.stream()
                     .anyMatch(course -> course.getCourseName().equalsIgnoreCase(courseName));
    }
    
    @Override
    public String toString() {
        return String.format("Student{CGPA=%.2f, Units=%d, Courses=%d}", 
                           currentCGPA, cumulativeUnits, courses.size());
    }
}