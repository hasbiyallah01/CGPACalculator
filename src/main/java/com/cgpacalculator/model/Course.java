package com.cgpacalculator.model;

import com.cgpacalculator.utils.GradeConverter;

// Represents a course with name, units, grade, and validation methods
public class Course {
    private String courseName;
    private int units;
    private String letterGrade;
    private double gradePoints;
    
    // Default constructor
    public Course() {
        this.courseName = "";
        this.units = 0;
        this.letterGrade = "";
        this.gradePoints = 0.0;
    }
    
    // Parameterized constructor
    public Course(String courseName, int units, String letterGrade) {
        this.courseName = courseName;
        this.units = units;
        this.letterGrade = letterGrade;
        this.gradePoints = GradeConverter.convertLetterGradeToPoints(letterGrade);
    }
    
    // Course Name - Getter and Setter
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        try {
            if (isValidCourseName(courseName)) {
                this.courseName = courseName;
            } else {
                String errorMsg = courseName == null ? "Course name cannot be null" :
                                courseName.trim().isEmpty() ? "Course name cannot be empty" :
                                "Course name must be 1-50 characters (current length: " + courseName.trim().length() + ")";
                throw new IllegalArgumentException(errorMsg);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error setting course name: " + e.getMessage(), e);
        }
    }
    
    // Units - Getter and Setter
    public int getUnits() {
        return units;
    }
    
    // Alternative getter name for consistency with GPACalculator
    public int getCourseUnits() {
        return units;
    }
    
    public void setUnits(int units) {
        try {
            if (isValidUnits(units)) {
                this.units = units;
            } else {
                throw new IllegalArgumentException("Units must be between 1-6 (provided: " + units + ")");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error setting course units: " + e.getMessage(), e);
        }
    }
    
    // Letter Grade - Getter and Setter
    public String getLetterGrade() {
        return letterGrade;
    }
    
    public void setLetterGrade(String letterGrade) {
        try {
            if (GradeConverter.isValidLetterGrade(letterGrade)) {
                this.letterGrade = letterGrade;
                this.gradePoints = GradeConverter.convertLetterGradeToPoints(letterGrade);
            } else {
                String errorMsg = letterGrade == null ? "Grade cannot be null" :
                                letterGrade.trim().isEmpty() ? "Grade cannot be empty" :
                                "Grade must be A, B, C, D, E, or F (provided: " + letterGrade + ")";
                throw new IllegalArgumentException(errorMsg);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error setting letter grade: " + e.getMessage(), e);
        }
    }
    
    // Grade Points - Getter only (calculated field)
    public double getGradePoints() {
        return gradePoints;
    }
    
    // Validates if the course data is complete and valid
    public boolean isValid() {
        return isValidCourseName(courseName) && 
               isValidUnits(units) && 
               isValidGrade(letterGrade);
    }
    
    // Alternative method name for consistency with GPACalculator
    public boolean isValidCourse() {
        return isValid();
    }
    
    // Calculates credit points for this course (units × grade points)
    public double calculateCreditPoints() {
        return units * gradePoints;
    }
    
    // Private validation methods
    private boolean isValidCourseName(String name) {
        return name != null && name.trim().length() >= 1 && name.trim().length() <= 50;
    }
    
    private boolean isValidUnits(int units) {
        return units >= 1 && units <= 6;
    }
    
    private boolean isValidGrade(String grade) {
        return GradeConverter.isValidLetterGrade(grade);
    }
    
    @Override
    public String toString() {
        return String.format("Course{name='%s', units=%d, grade='%s', points=%.1f}", 
                           courseName, units, letterGrade, gradePoints);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseName.equals(course.courseName);
    }
    
    @Override
    public int hashCode() {
        return courseName.hashCode();
    }
}