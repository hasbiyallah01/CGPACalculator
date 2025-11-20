package com.cgpacalculator.view;

import com.cgpacalculator.utils.ValidationUtils;
import com.cgpacalculator.utils.Constants;
import com.cgpacalculator.utils.ValidationResult;
import com.cgpacalculator.utils.AcademicConstraintValidator;
import com.cgpacalculator.model.Course;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;public class InputValidationManager {
    
    // Visual feedback colors
    private static final Color ERROR_COLOR = new Color(220, 53, 69);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color FOCUS_COLOR = new Color(255, 255, 240);
    
    // Border styles for validation feedback
    private static final Border ERROR_BORDER = BorderFactory.createLineBorder(ERROR_COLOR, 2);
    private static final Border SUCCESS_BORDER = BorderFactory.createLineBorder(SUCCESS_COLOR, 1);
    private static final Border WARNING_BORDER = BorderFactory.createLineBorder(WARNING_COLOR, 1);
    private static final Border DEFAULT_BORDER = UIManager.getBorder("TextField.border");
    
    // Validation delay for real-time feedback (milliseconds)
    private static final int VALIDATION_DELAY = 300;public static void setupTextFieldValidation(JTextField textField, JLabel validationLabel, ValidationType validationType) {
        // Store original background color
        Color originalBackground = textField.getBackground();
        
        // Set up document listener for real-time validation
        textField.getDocument().addDocumentListener(new DelayedValidationListener(textField, validationLabel, validationType));
        
        // Set up focus listener for visual feedback
        textField.addFocusListener(new ValidationFocusListener(textField, originalBackground));
        
        // Set up accessibility features
        KeyboardShortcutManager.setupAccessibilityFeatures(textField);
        
        // Initialize validation state
        validateField(textField, validationLabel, validationType);
    }
    
    // Method comment
    public static void setupSpinnerValidation(JSpinner spinner, JLabel validationLabel) {
        // Add change listener for real-time validation
        spinner.addChangeListener(e -> {
            int value = (Integer) spinner.getValue();
            ValidationResult result = AcademicConstraintValidator.validateCourseUnitConstraints("Current Course", value);
            updateValidationFeedback(validationLabel, result, null);
        });
        
        // Set up accessibility features
        KeyboardShortcutManager.setupAccessibilityFeatures(spinner);
    }
    
    // Method comment
    public static void setupComboBoxValidation(JComboBox<String> comboBox) {
        // Set up accessibility features
        KeyboardShortcutManager.setupAccessibilityFeatures(comboBox);
        
        // Combo box validation is typically not needed as it has predefined valid values
        // But we can add change listeners for dependent validations
        comboBox.addActionListener(e -> {
            // Trigger validation of dependent fields if needed
            // This could be used to validate course combinations or trigger other validations
        });
    }
    
    // Method comment
    private static void validateField(JTextField textField, JLabel validationLabel, ValidationType validationType) {
        String text = textField.getText().trim();
        ValidationResult result = new ValidationResult();
        
        switch (validationType) {
            case COURSE_NAME:
                result = validateCourseName(text);
                break;
            case CGPA:
                result = validateCGPA(text);
                break;
            case CUMULATIVE_UNITS:
                result = validateCumulativeUnits(text);
                break;
        }
        
        updateValidationFeedback(validationLabel, result, textField);
        updateTooltipFeedback(textField, result);
    }
    
    // Method comment
    private static ValidationResult validateCourseName(String courseName) {
        ValidationResult result = new ValidationResult();
        
        if (courseName.isEmpty()) {
            result.addErrorMessage("Course Name", "Course name is required");
        } else if (courseName.length() < Constants.MIN_COURSE_NAME_LENGTH) {
            result.addErrorMessage("Course Name", 
                String.format("Course name must be at least %d characters", Constants.MIN_COURSE_NAME_LENGTH));
        } else if (courseName.length() > Constants.MAX_COURSE_NAME_LENGTH) {
            result.addErrorMessage("Course Name", 
                String.format("Course name must not exceed %d characters", Constants.MAX_COURSE_NAME_LENGTH));
        } else {
            result.addSuccessMessage("Valid course name");
        }
        
        return result;
    }
    
    // Method comment
    private static ValidationResult validateCGPA(String cgpaText) {
        ValidationResult result = new ValidationResult();
        
        if (cgpaText.isEmpty()) {
            result.addSuccessMessage("CGPA is optional");
            return result;
        }
        
        try {
            double cgpa = Double.parseDouble(cgpaText);
            if (!ValidationUtils.isValidCGPAValue(cgpa)) {
                result.addErrorMessage("CGPA", "CGPA must be between 0.00 and 5.00");
            } else {
                result.addSuccessMessage("Valid CGPA");
            }
        } catch (NumberFormatException e) {
            result.addErrorMessage("CGPA", "Please enter a valid decimal number");
        }
        
        return result;
    }
    
    // Method comment
    private static ValidationResult validateCumulativeUnits(String unitsText) {
        ValidationResult result = new ValidationResult();
        
        if (unitsText.isEmpty()) {
            result.addSuccessMessage("Cumulative units is optional");
            return result;
        }
        
        try {
            int units = Integer.parseInt(unitsText);
            if (!ValidationUtils.isValidCumulativeUnits(units)) {
                result.addErrorMessage("Cumulative Units", "Cumulative units must be a positive number");
            } else {
                result.addSuccessMessage("Valid cumulative units");
            }
        } catch (NumberFormatException e) {
            result.addErrorMessage("Cumulative Units", "Please enter a valid whole number");
        }
        
        return result;
    }
    
    // Method comment
    private static void updateValidationFeedback(JLabel validationLabel, ValidationResult result, JTextField textField) {
        if (result.hasErrors()) {
            // Show error feedback
            String errorMessage = result.getAllErrorMessagesAsString();
            if (errorMessage.contains(":")) {
                errorMessage = errorMessage.substring(errorMessage.indexOf(":") + 1).trim();
            }
            validationLabel.setText("✗ " + errorMessage);
            validationLabel.setForeground(ERROR_COLOR);
            
            if (textField != null) {
                textField.setBorder(ERROR_BORDER);
            }
        } else if (result.getSuccessMessageCount() > 0) {
            // Show success feedback
            String successMessage = result.getAllSuccessMessagesAsString();
            validationLabel.setText("✓ " + successMessage);
            validationLabel.setForeground(SUCCESS_COLOR);
            
            if (textField != null) {
                textField.setBorder(SUCCESS_BORDER);
            }
        } else {
            // Clear feedback
            validationLabel.setText(" ");
            if (textField != null) {
                textField.setBorder(DEFAULT_BORDER);
            }
        }
    }
    
    // Method comment
    private static void updateTooltipFeedback(JComponent component, ValidationResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrorMessagesAsString();
            if (errorMessage.contains(":")) {
                errorMessage = errorMessage.substring(errorMessage.indexOf(":") + 1).trim();
            }
            TooltipManager.updateValidationTooltip(component, true, errorMessage);
        } else {
            TooltipManager.updateValidationTooltip(component, false, null);
        }
    }
    
    // Method comment
    public static ValidationResult validateCourseAddition(String courseName, int units, String grade, List<Course> existingCourses) {
        ValidationResult result = new ValidationResult();
        
        // Validate course name
        ValidationResult nameValidation = validateCourseName(courseName);
        result.mergeValidationResult(nameValidation);
        
        // Validate units
        ValidationResult unitsValidation = AcademicConstraintValidator.validateCourseUnitConstraints(courseName, units);
        result.mergeValidationResult(unitsValidation);
        
        // Validate grade
        if (!ValidationUtils.isValidLetterGrade(grade)) {
            result.addErrorMessage("Grade", "Please select a valid grade");
        }
        
        // Check for duplicates
        if (!courseName.trim().isEmpty()) {
            ValidationResult duplicateValidation = AcademicConstraintValidator.validateDuplicateCourseNames(courseName, existingCourses);
            result.mergeValidationResult(duplicateValidation);
        }
        
        return result;
    }
    
    // Method comment
    public static ValidationResult validateSemesterConstraints(List<Course> courses, int additionalUnits) {
        int currentUnits = courses.stream().mapToInt(Course::getUnits).sum();
        int totalUnits = currentUnits + additionalUnits;
        
        return AcademicConstraintValidator.validateSemesterUnitConstraints(totalUnits);
    }
    
    // Method comment
    public static void clearValidationFeedback(JTextField textField, JLabel validationLabel) {
        validationLabel.setText(" ");
        validationLabel.setForeground(Color.BLACK);
        textField.setBorder(DEFAULT_BORDER);
    }
    
    // Enumeration for validation types
    public enum ValidationType {
        COURSE_NAME,
        CGPA,
        CUMULATIVE_UNITS
    }
    
    // Document listener that provides delayed validation to avoid excessive validation calls
    private static class DelayedValidationListener implements DocumentListener {
        private final JTextField textField;
        private final JLabel validationLabel;
        private final ValidationType validationType;
        private Timer validationTimer;
        
        public DelayedValidationListener(JTextField textField, JLabel validationLabel, ValidationType validationType) {
            this.textField = textField;
            this.validationLabel = validationLabel;
            this.validationType = validationType;
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            scheduleValidation();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            scheduleValidation();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            scheduleValidation();
        }
        
        private void scheduleValidation() {
            if (validationTimer != null) {
                validationTimer.cancel();
            }
            
            validationTimer = new Timer();
            validationTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> {
                        validateField(textField, validationLabel, validationType);
                    });
                }
            }, VALIDATION_DELAY);
        }
    }
    
    // Focus listener that provides visual feedback when fields gain/lose focus
    private static class ValidationFocusListener implements FocusListener {
        private final JTextField textField;
        private final Color originalBackground;
        
        public ValidationFocusListener(JTextField textField, Color originalBackground) {
            this.textField = textField;
            this.originalBackground = originalBackground;
        }
        
        @Override
        public void focusGained(FocusEvent e) {
            // Highlight field when focused (unless it has an error)
            if (!textField.getBorder().equals(ERROR_BORDER)) {
                textField.setBackground(FOCUS_COLOR);
            }
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            // Restore original background
            textField.setBackground(originalBackground);
        }
    }
}