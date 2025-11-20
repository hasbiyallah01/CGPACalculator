package com.cgpacalculator.controller;

import com.cgpacalculator.model.*;
import com.cgpacalculator.view.*;
import com.cgpacalculator.utils.*;
import com.cgpacalculator.utils.exceptions.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

// Controller responsible for handling all calculation-related operations
// Coordinates between the model and view for GPA/CGPA calculations
public class CalculationController {
    
    // Model components
    private Student studentModel;
    private List<CalculationObserver> calculationObservers;
    
    // View components
    private MainFrame mainView;
    private CoursePanel coursePanel;
    private ResultsPanel resultsPanel;
    
    // Validation components
    private CourseValidator courseValidator;
    private StudentValidator studentValidator;
    
    // State management
    private boolean isCalculationInProgress;
    private boolean isRealTimeUpdatesEnabled;
    
    // Constructor initializes the calculation controller with view and model references
    public CalculationController(MainFrame mainView, Student studentModel) {
        this.mainView = mainView;
        this.studentModel = studentModel;
        this.calculationObservers = new ArrayList<>();
        this.isCalculationInProgress = false;
        this.isRealTimeUpdatesEnabled = true;
        
        // Initialize view components
        initializeViewComponents();
        
        // Initialize validation components
        initializeValidationComponents();
        
        // Set up event listeners
        setupEventListeners();
        
        // Register for real-time updates
        setupRealTimeCalculationUpdates();
    }
    
    // Initializes view component references for clean interaction.
    private void initializeViewComponents() {
        this.coursePanel = mainView.getCoursePanel();
        // Initialize ResultsPanel if available in MainFrame
        // For now, ResultsPanel is not integrated into MainFrame, so we keep it null
        // and rely on MainFrame's result display methods
        this.resultsPanel = null; // Will be initialized when MainFrame provides getResultsPanel() method
    }
    
    // Initializes validation components for comprehensive input validation.
    private void initializeValidationComponents() {
        // ValidationUtils is a utility class with static methods
        this.courseValidator = new CourseValidator();
        this.studentValidator = new StudentValidator();
    }
    
    // Sets up event listeners for UI components
    private void setupEventListeners() {
        // Calculate button event handler
        mainView.addCalculateButtonListener(new CalculateGPAActionHandler(this));
        
        // Course panel event handlers for real-time updates
        coursePanel.addCourseChangeListener(new CourseDataChangeHandler(this));
        coursePanel.addAddCourseListener(new CourseAdditionHandler(this));
        coursePanel.addRemoveCourseListener(new CourseRemovalHandler(this));
    }
    
    // Sets up real-time calculation updates
    private void setupRealTimeCalculationUpdates() {
        // Register this controller as an observer for course changes
        addCalculationObserver(new RealTimeCalculationObserver(this));
        
        // Enable automatic updates
        enableRealTimeUpdates();
    }
    
    // Performs comprehensive GPA and CGPA calculations with validation
    public void performCalculations() {
        if (isCalculationInProgress) {
            return; // Prevent concurrent calculations
        }
        
        try {
            isCalculationInProgress = true;
            
            // Step 1: Validate current academic data
            ValidationResult academicDataValidation = validateCurrentAcademicData();
            if (!academicDataValidation.isSuccessful()) {
                handleValidationError("Academic Data Validation", academicDataValidation.getAllErrorMessagesAsString());
                return;
            }
            
            // Step 2: Validate course data
            ValidationResult courseDataValidation = validateCourseData();
            if (!courseDataValidation.isSuccessful()) {
                handleValidationError("Course Data Validation", courseDataValidation.getAllErrorMessagesAsString());
                return;
            }
            
            // Step 3: Validate academic constraints using new validator
            ValidationResult constraintValidation = validateAcademicConstraintsWithNewValidator();
            if (!constraintValidation.isSuccessful()) {
                handleConstraintWarning(constraintValidation.getAllErrorMessagesAsString());
                // Continue with calculation despite warnings
            }
            
            // Step 4: Perform calculations
            CalculationResult calculationResult = executeCalculations();
            
            // Step 5: Update UI with results
            updateCalculationResults(calculationResult);
            
            // Step 6: Notify observers of successful calculation
            notifyCalculationObservers(calculationResult);
            
        } catch (CalculationException e) {
            handleCalculationError("Calculation Error", e.getMessage());
        } catch (Exception e) {
            handleUnexpectedError("Unexpected Error", e.getMessage());
        } finally {
            isCalculationInProgress = false;
        }
    }
    
    // Validates current academic data (CGPA and cumulative units)
    private ValidationResult validateCurrentAcademicData() {
        ValidationResult result = new ValidationResult();
        String cgpaInput = mainView.getCurrentCGPAInput();
        String unitsInput = mainView.getCumulativeUnitsInput();
        
        // Validate CGPA input if provided
        if (!cgpaInput.isEmpty()) {
            try {
                double cgpaValue = Double.parseDouble(cgpaInput);
                if (!ValidationUtils.isValidCGPAValue(cgpaValue)) {
                    result.addGeneralErrorMessage("Current CGPA must be between 0.00 and 5.00");
                    return result;
                }
                studentModel.setCurrentCGPA(cgpaValue);
            } catch (NumberFormatException e) {
                result.addGeneralErrorMessage("Current CGPA must be a valid decimal number");
                return result;
            }
        } else {
            studentModel.setCurrentCGPA(0.0);
        }
        
        // Validate cumulative units input if provided
        if (!unitsInput.isEmpty()) {
            try {
                int unitsValue = Integer.parseInt(unitsInput);
                if (!ValidationUtils.isValidCumulativeUnits(unitsValue)) {
                    result.addGeneralErrorMessage("Cumulative units must be a positive number");
                    return result;
                }
                studentModel.setCumulativeUnits(unitsValue);
            } catch (NumberFormatException e) {
                result.addGeneralErrorMessage("Cumulative units must be a valid whole number");
                return result;
            }
        } else {
            studentModel.setCumulativeUnits(0);
        }
        
        // Validate consistency between CGPA and units
        if (!ValidationUtils.isConsistentAcademicData(studentModel.getCurrentCGPA(), studentModel.getCumulativeUnits())) {
            result.addGeneralErrorMessage("If current CGPA is provided, cumulative units must also be provided");
            return result;
        }
        
        return result;
    }
    
    // Validates course data and updates student model
    private ValidationResult validateCourseData() {
        ValidationResult result = new ValidationResult();
        List<Course> courses = coursePanel.getAllCourses();
        
        if (courses.isEmpty()) {
            result.addGeneralErrorMessage("Please add at least one course before calculating");
            return result;
        }
        
        // Validate each course individually
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            try {
                CourseValidator.validateCompleteCourse(course);
            } catch (Exception e) {
                result.addGeneralErrorMessage(String.format("Course %d: %s", i + 1, e.getMessage()));
                return result;
            }
        }
        
        // Update student model with validated courses
        studentModel.clearAllCourses();
        for (Course course : courses) {
            try {
                studentModel.addCourse(course);
            } catch (IllegalArgumentException e) {
                result.addGeneralErrorMessage("Duplicate course detected: " + course.getCourseName());
                return result;
            }
        }
        
        return result;
    }
    
    // Validates academic constraints using the new comprehensive validator
    private ValidationResult validateAcademicConstraintsWithNewValidator() {
        // Use the comprehensive academic constraint validator
        ValidationResult result = AcademicConstraintValidator.validateAllAcademicConstraints(studentModel);
        
        // Display validation results in the course panel
        coursePanel.getValidationFeedbackPanel().displayValidationResults(result);
        
        return result;
    }
    
    // Legacy method for validating academic constraints
    private ValidationResult validateAcademicConstraints() {
        ValidationResult result = new ValidationResult();
        
        // Check semester unit constraints
        int totalSemesterUnits = studentModel.calculateTotalSemesterUnits();
        
        if (studentModel.isBelowMinimumUnits()) {
            result.addGeneralErrorMessage("Semester units below minimum requirement (18 units)");
        }
        
        if (studentModel.isAboveMaximumUnits()) {
            result.addGeneralErrorMessage("Semester units exceed maximum allowed (24 units)");
        }
        
        // Check cumulative unit sufficiency for CGPA calculation
        if (studentModel.getCurrentCGPA() > 0.0 && 
            !ValidationUtils.hasSufficientCumulativeUnitsForCGPA(studentModel.getCumulativeUnits())) {
            result.addGeneralErrorMessage("Cumulative units below 24 may affect CGPA calculation accuracy");
        }
        
        // Check individual course unit constraints
        for (Course course : studentModel.getCourses()) {
            if (course.getUnits() < 1 || course.getUnits() > 6) {
                result.addGeneralErrorMessage(String.format("Course '%s' has invalid units (%d). Units should be 1-6", 
                    course.getCourseName(), course.getUnits()));
            }
        }
        
        return result;
    }
    
    // Executes the actual GPA and CGPA calculations
    private CalculationResult executeCalculations() throws CalculationException {
        try {
            List<Course> courses = studentModel.getCourses();
            
            // Calculate current semester GPA
            double currentSemesterGPA = GPACalculator.calculateCurrentSemesterGPA(courses);
            
            // Calculate total semester units
            int totalSemesterUnits = GPACalculator.calculateTotalCreditUnits(courses);
            
            // Calculate updated CGPA
            double updatedCGPA;
            if (studentModel.getCumulativeUnits() > 0) {
                updatedCGPA = GPACalculator.calculateUpdatedCGPA(
                    studentModel.getCurrentCGPA(), 
                    studentModel.getCumulativeUnits(), 
                    courses
                );
            } else {
                updatedCGPA = GPACalculator.calculateInitialCGPA(courses);
            }
            
            // Determine classification
            String classification = ClassificationManager.getClassificationName(updatedCGPA);
            
            // Generate motivational message
            String motivationalMessage = ClassificationManager.generateMotivationalMessage(updatedCGPA);
            
            // Calculate total credit points for breakdown
            double totalCreditPoints = GPACalculator.calculateTotalCreditPoints(courses);
            
            return new CalculationResult(
                currentSemesterGPA,
                updatedCGPA,
                totalSemesterUnits,
                totalCreditPoints,
                classification,
                motivationalMessage,
                studentModel.getCurrentCGPA(),
                studentModel.getCumulativeUnits()
            );
            
        } catch (Exception e) {
            throw new CalculationException("Failed to perform calculations: " + e.getMessage(), e);
        }
    }
    
    // Updates the UI with calculation results
    private void updateCalculationResults(CalculationResult result) {
        // Update main display values
        mainView.updateCurrentGPADisplay(result.getCurrentSemesterGPA());
        mainView.updateCGPADisplay(result.getUpdatedCGPA());
        mainView.updateClassificationDisplay(result.getClassification());
        mainView.updateTotalUnitsDisplay(result.getTotalSemesterUnits());
        mainView.updateMotivationalMessage(result.getMotivationalMessage());
        
        // If ResultsPanel is available, update it as well
        if (resultsPanel != null) {
            resultsPanel.updateResults(
                studentModel.getCourses(),
                result.getPreviousCGPA(),
                result.getPreviousUnits()
            );
        }
    }
    
    // Handles validation errors by displaying error messages
    private void handleValidationError(String title, String message) {
        mainView.showErrorMessage(title, message);
        clearCalculationResults();
    }
    
    // Handles constraint warnings
    private void handleConstraintWarning(String message) {
        mainView.showWarningMessage("Academic Constraint Warning", message);
    }
    
    // Method comment
    private void handleCalculationError(String title, String message) {
        mainView.showErrorMessage(title, "Calculation failed: " + message);
        clearCalculationResults();
    }
    
    // Method comment
    private void handleUnexpectedError(String title, String message) {
        mainView.showErrorMessage(title, "An unexpected error occurred: " + message);
        clearCalculationResults();
    }
    
    // Clears calculation results from the UI.
    private void clearCalculationResults() {
        mainView.clearResultDisplays();
        if (resultsPanel != null) {
            resultsPanel.clearResults();
        }
    }
    
    // Enables real-time calculation updates.
    public void enableRealTimeUpdates() {
        this.isRealTimeUpdatesEnabled = true;
    }
    
    // Disables real-time calculation updates.
    public void disableRealTimeUpdates() {
        this.isRealTimeUpdatesEnabled = false;
    }
    
    // Method comment
    public boolean isRealTimeUpdatesEnabled() {
        return isRealTimeUpdatesEnabled;
    }
    
    // Method comment
    public void addCalculationObserver(CalculationObserver observer) {
        if (observer != null && !calculationObservers.contains(observer)) {
            calculationObservers.add(observer);
        }
    }
    
    // Method comment
    public void removeCalculationObserver(CalculationObserver observer) {
        calculationObservers.remove(observer);
    }
    
    // Method comment
    private void notifyCalculationObservers(CalculationResult result) {
        for (CalculationObserver observer : calculationObservers) {
            try {
                observer.onCalculationUpdated(result);
            } catch (Exception e) {
                // Log error but don't let observer errors break the calculation
                System.err.println("Error notifying calculation observer: " + e.getMessage());
            }
        }
    }
    
    // Triggers real-time calculation update if enabled.
    private void triggerRealTimeCalculationUpdate() {
        if (isRealTimeUpdatesEnabled && !isCalculationInProgress) {
            SwingUtilities.invokeLater(() -> {
                try {
                    performCalculations();
                } catch (Exception e) {
                    // Silently handle real-time calculation errors
                    // to avoid disrupting user input
                }
            });
        }
    }
    
    // Event Handler Classes
    static class CalculateGPAActionHandler implements ActionListener {
        private final CalculationController controller;
        
        public CalculateGPAActionHandler(CalculationController controller) {
            this.controller = controller;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.performCalculations();
        }
    }
    
    // Handler for course data changes that trigger real-time updates.
    static class CourseDataChangeHandler implements ActionListener {
        private final CalculationController controller;
        
        public CourseDataChangeHandler(CalculationController controller) {
            this.controller = controller;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.triggerRealTimeCalculationUpdate();
        }
    }
    
    // Handler for course addition events with academic constraint validation.
    static class CourseAdditionHandler implements ActionListener {
        private final CalculationController controller;
        
        public CourseAdditionHandler(CalculationController controller) {
            this.controller = controller;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            // Validate the course being added
            Course currentCourse = controller.coursePanel.getCurrentCourseInput();
            if (currentCourse != null) {
                ValidationResult additionValidation = AcademicConstraintValidator.validateCourseAddition(
                    controller.studentModel, currentCourse
                );
                
                // Display validation results
                controller.coursePanel.getValidationFeedbackPanel().displayValidationResults(additionValidation);
                
                // Check for critical errors that should prevent addition
                if (additionValidation.hasFieldError("Duplicate Course")) {
                    controller.mainView.showWarningMessage("Duplicate Course", 
                        "This course name already exists. Please use a unique name.");
                    return;
                }
                
                if (additionValidation.hasFieldError("Course Load Warning")) {
                    // Show warning but allow addition
                    controller.mainView.showWarningMessage("Course Load Warning", 
                        additionValidation.getFieldErrorMessage("Course Load Warning"));
                }
            }
            
            controller.triggerRealTimeCalculationUpdate();
        }
    }
    
    // Handler for course removal events with constraint re-validation.
    static class CourseRemovalHandler implements ActionListener {
        private final CalculationController controller;
        
        public CourseRemovalHandler(CalculationController controller) {
            this.controller = controller;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            // After removal, re-validate academic constraints
            SwingUtilities.invokeLater(() -> {
                ValidationResult constraintValidation = AcademicConstraintValidator.validateAllAcademicConstraints(controller.studentModel);
                controller.coursePanel.getValidationFeedbackPanel().displayValidationResults(constraintValidation);
            });
            
            controller.triggerRealTimeCalculationUpdate();
        }
    }
    
    // Observer implementation for real-time calculation updates.
    static class RealTimeCalculationObserver implements CalculationObserver {
        private final CalculationController controller;
        
        public RealTimeCalculationObserver(CalculationController controller) {
            this.controller = controller;
        }
        
        @Override
        public void onCalculationUpdated(CalculationResult result) {
            // This observer can be used for additional real-time update logic
            // such as updating external components or logging calculation events
        }
    }
}

