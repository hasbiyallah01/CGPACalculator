package com.cgpacalculator.view;

import com.cgpacalculator.utils.ValidationResult;
import com.cgpacalculator.utils.Constants;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

// Panel for displaying validation feedback and academic constraint warnings
public class ValidationFeedbackPanel extends JPanel {
    
    // Validation message display components
    private final JTextArea validationMessageArea;
    private final JScrollPane messageScrollPane;
    
    // Warning indicator components
    private final JLabel warningIconLabel;
    private final JLabel statusLabel;
    
    // Color constants for different message types
    private static final Color ERROR_COLOR = new Color(220, 53, 69);      // Bootstrap danger red
    private static final Color WARNING_COLOR = new Color(255, 193, 7);    // Bootstrap warning yellow
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);    // Bootstrap success green
    private static final Color INFO_COLOR = new Color(23, 162, 184);      // Bootstrap info blue
    
    // Constructor initializes the validation feedback panel with all components
    public ValidationFeedbackPanel() {
        // Initialize components
        this.validationMessageArea = createValidationMessageArea();
        this.messageScrollPane = createMessageScrollPane();
        this.warningIconLabel = createWarningIconLabel();
        this.statusLabel = createStatusLabel();
        
        // Set up the layout
        setupPanelLayout();
        
        // Configure panel properties
        configurePanelProperties();
        
        // Initialize with default state
        clearAllValidationMessages();
    }
    
    // Creates the text area for displaying validation messages
    private JTextArea createValidationMessageArea() {
        JTextArea area = new JTextArea(4, 50);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        area.setBackground(getBackground());
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return area;
    }
    
    // Creates the scroll pane for the validation message area
    private JScrollPane createMessageScrollPane() {
        JScrollPane scrollPane = new JScrollPane(validationMessageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setPreferredSize(new Dimension(0, 100));
        return scrollPane;
    }
    
    // Creates the warning icon label for visual indicators
    private JLabel createWarningIconLabel() {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(24, 24));
        return label;
    }
    
    // Creates the status label for displaying validation status
    private JLabel createStatusLabel() {
        JLabel label = new JLabel("All constraints satisfied");
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        label.setForeground(SUCCESS_COLOR);
        return label;
    }
    
    // Sets up the main layout of the validation feedback panel
    private void setupPanelLayout() {
        setLayout(new BorderLayout(5, 5));
        
        // Create header panel with icon and status
        JPanel headerPanel = createHeaderPanel();
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(messageScrollPane, BorderLayout.CENTER);
    }
    
    // Creates the header panel with warning icon and status
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(warningIconLabel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.CENTER);
        return panel;
    }
    
    // Configures general panel properties
    private void configurePanelProperties() {
        setBorder(new TitledBorder("Academic Constraint Validation"));
        setPreferredSize(new Dimension(0, 140));
    }
    
    // Displays semester unit validation warnings based on total units
    public void displaySemesterUnitValidation(int totalSemesterUnits) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean hasWarnings = false;
        
        // Check minimum units requirement
        if (totalSemesterUnits < Constants.MIN_SEMESTER_UNITS) {
            messageBuilder.append("📚 SEMESTER UNITS INFO: ");
            messageBuilder.append("You need at least ").append(Constants.MIN_SEMESTER_UNITS);
            messageBuilder.append(" total units from ALL courses combined for full-time status.\n");
            messageBuilder.append("Current total: ").append(totalSemesterUnits).append(" units");
            messageBuilder.append(" (Need ").append(Constants.MIN_SEMESTER_UNITS - totalSemesterUnits).append(" more)\n");
            messageBuilder.append("💡 TIP: Add more courses to reach the minimum requirement.");
            hasWarnings = true;
        }
        
        // Check maximum units constraint
        if (totalSemesterUnits > Constants.MAX_SEMESTER_UNITS) {
            messageBuilder.append("⚠ MAXIMUM UNITS EXCEEDED: ");
            messageBuilder.append("Your total of ").append(totalSemesterUnits);
            messageBuilder.append(" units exceeds the ").append(Constants.MAX_SEMESTER_UNITS);
            messageBuilder.append("-unit maximum per semester.\n");
            messageBuilder.append("Registration may require special approval.");
            hasWarnings = true;
        }
        
        // Display appropriate feedback
        if (hasWarnings) {
            displayWarningMessages(messageBuilder.toString());
        } else {
            // Units are within acceptable range
            String successMessage = String.format("✓ Total semester units (%d) are within the acceptable range of %d-%d units", 
                                                totalSemesterUnits, Constants.MIN_SEMESTER_UNITS, Constants.MAX_SEMESTER_UNITS);
            displaySuccessMessage(successMessage);
        }
    }
    
    // Displays course unit validation for individual courses
    public void displayCourseUnitValidation(String courseName, int courseUnits) {
        if (courseUnits < Constants.MIN_COURSE_UNITS || courseUnits > Constants.MAX_COURSE_UNITS) {
            String errorMessage = String.format("❌ INVALID COURSE UNITS: Course '%s' has %d units. " +
                                               "Valid range is %d-%d units.",
                                               courseName, courseUnits, 
                                               Constants.MIN_COURSE_UNITS, Constants.MAX_COURSE_UNITS);
            displayErrorMessage(errorMessage);
        }
    }
    
    // Displays duplicate course name detection warnings
    public void displayDuplicateCourseWarning(String duplicateCourseName) {
        String warningMessage = String.format("⚠ DUPLICATE COURSE DETECTED: Course name '%s' already exists. " +
                                            "Please use a unique course name or modify the existing entry.",
                                            duplicateCourseName);
        displayWarningMessage(warningMessage);
    }
    
    // Displays comprehensive validation results from ValidationResult object
    public void displayValidationResults(ValidationResult validationResult) {
        if (validationResult == null) {
            clearAllValidationMessages();
            return;
        }
        
        StringBuilder messageBuilder = new StringBuilder();
        
        // Add field-specific error messages
        Map<String, String> fieldErrors = validationResult.getFieldErrorMessages();
        for (Map.Entry<String, String> fieldError : fieldErrors.entrySet()) {
            messageBuilder.append("❌ ").append(fieldError.getKey()).append(": ");
            messageBuilder.append(fieldError.getValue()).append("\n");
        }
        
        // Add general error messages
        List<String> generalErrors = validationResult.getGeneralErrorMessages();
        for (String error : generalErrors) {
            messageBuilder.append("❌ ").append(error).append("\n");
        }
        
        // Display messages based on validation result
        if (validationResult.hasErrors()) {
            displayErrorMessages(messageBuilder.toString());
        } else {
            // Show success messages if available
            List<String> successMessages = validationResult.getSuccessMessages();
            if (!successMessages.isEmpty()) {
                StringBuilder successBuilder = new StringBuilder();
                for (String success : successMessages) {
                    successBuilder.append("✓ ").append(success).append("\n");
                }
                displaySuccessMessage(successBuilder.toString());
            } else {
                displaySuccessMessage("✓ All validation checks passed successfully");
            }
        }
    }
    
    // Displays academic constraint warnings for course load violations
    public void displayCourseLoadViolations(int currentUnits, int additionalUnits) {
        int projectedTotal = currentUnits + additionalUnits;
        
        StringBuilder messageBuilder = new StringBuilder();
        
        if (projectedTotal > Constants.MAX_SEMESTER_UNITS) {
            messageBuilder.append("⚠ COURSE LOAD WARNING: Adding this course (").append(additionalUnits);
            messageBuilder.append(" units) would result in ").append(projectedTotal);
            messageBuilder.append(" total units, exceeding the maximum limit of ");
            messageBuilder.append(Constants.MAX_SEMESTER_UNITS).append(" units.\n");
            messageBuilder.append("Registration may be blocked or require special approval.");
        } else if (projectedTotal < Constants.MIN_SEMESTER_UNITS) {
            messageBuilder.append("ℹ COURSE LOAD INFO: Current total of ").append(projectedTotal);
            messageBuilder.append(" units is below the minimum requirement of ");
            messageBuilder.append(Constants.MIN_SEMESTER_UNITS).append(" units.\n");
            messageBuilder.append("Additional courses may be required for full-time status.");
        }
        
        if (messageBuilder.length() > 0) {
            displayWarningMessage(messageBuilder.toString());
        }
    }
    
    // Displays cumulative unit warnings for CGPA calculation
    public void displayCumulativeUnitWarnings(int cumulativeUnits, double currentCGPA) {
        if (currentCGPA > 0.0 && cumulativeUnits < Constants.MIN_CUMULATIVE_UNITS_FOR_CGPA) {
            String warningMessage = String.format("⚠ CGPA CALCULATION WARNING: Cumulative units (%d) are below " +
                                                "the recommended minimum (%d) for accurate CGPA calculation. " +
                                                "Results may not reflect true academic standing.",
                                                cumulativeUnits, Constants.MIN_CUMULATIVE_UNITS_FOR_CGPA);
            displayWarningMessage(warningMessage);
        }
    }
    
    // Displays error messages with appropriate styling
    public void displayErrorMessage(String message) {
        displayErrorMessages(message);
    }
    
    // Displays multiple error messages with appropriate styling
    public void displayErrorMessages(String messages) {
        validationMessageArea.setText(messages.trim());
        validationMessageArea.setForeground(ERROR_COLOR);
        warningIconLabel.setText("❌");
        statusLabel.setText("Validation errors found");
        statusLabel.setForeground(ERROR_COLOR);
        setVisible(true);
    }
    
    // Displays warning message with appropriate styling
    public void displayWarningMessage(String message) {
        displayWarningMessages(message);
    }
    
    // Displays multiple warning messages with appropriate styling
    public void displayWarningMessages(String messages) {
        validationMessageArea.setText(messages.trim());
        validationMessageArea.setForeground(WARNING_COLOR.darker());
        warningIconLabel.setText("⚠");
        statusLabel.setText("Academic constraints need attention");
        statusLabel.setForeground(WARNING_COLOR.darker());
        setVisible(true);
    }
    
    // Displays success message with appropriate styling
    public void displaySuccessMessage(String message) {
        validationMessageArea.setText(message.trim());
        validationMessageArea.setForeground(SUCCESS_COLOR);
        warningIconLabel.setText("✓");
        statusLabel.setText("All constraints satisfied");
        statusLabel.setForeground(SUCCESS_COLOR);
        setVisible(true);
    }
    
    // Displays informational message with appropriate styling
    public void displayInfoMessage(String message) {
        validationMessageArea.setText(message.trim());
        validationMessageArea.setForeground(INFO_COLOR);
        warningIconLabel.setText("ℹ");
        statusLabel.setText("Information");
        statusLabel.setForeground(INFO_COLOR);
        setVisible(true);
    }
    
    // Clears all validation messages and resets to default state
    public void clearAllValidationMessages() {
        validationMessageArea.setText("No validation issues detected. All academic constraints are satisfied.");
        validationMessageArea.setForeground(SUCCESS_COLOR);
        warningIconLabel.setText("✓");
        statusLabel.setText("All constraints satisfied");
        statusLabel.setForeground(SUCCESS_COLOR);
    }
    
    // Hides the validation feedback panel
    public void hideValidationFeedback() {
        setVisible(false);
    }
    
    // Shows the validation feedback panel
    public void showValidationFeedback() {
        setVisible(true);
    }
    
    // Checks if the panel is currently showing error messages
    public boolean isShowingErrors() {
        return validationMessageArea.getForeground().equals(ERROR_COLOR);
    }
    
    // Checks if the panel is currently showing warning messages
    public boolean isShowingWarnings() {
        return validationMessageArea.getForeground().equals(WARNING_COLOR.darker());
    }
    
    // Checks if the panel is currently showing success messages
    public boolean isShowingSuccess() {
        return validationMessageArea.getForeground().equals(SUCCESS_COLOR);
    }
    
    // Gets the current validation message text
    public String getCurrentValidationMessage() {
        return validationMessageArea.getText();
    }
    
    // Sets a custom validation message with specified color
    public void setCustomValidationMessage(String message, Color textColor, String icon, String status) {
        validationMessageArea.setText(message);
        validationMessageArea.setForeground(textColor);
        warningIconLabel.setText(icon);
        statusLabel.setText(status);
        statusLabel.setForeground(textColor);
        setVisible(true);
    }
    
    // Shows error feedback with the specified message
    public void showErrorFeedback(String message) {
        displayErrorMessage(message);
    }
    
    // Shows warning feedback with the specified message
    public void showWarningFeedback(String message) {
        displayWarningMessage(message);
    }
    
    // Shows success feedback with the specified message
    public void showSuccessFeedback(String message) {
        displaySuccessMessage(message);
    }
    
    // Gets a course name validation label (creates a new one for compatibility)
    public JLabel getCourseNameValidationLabel() {
        // Return a label that updates the main validation area
        JLabel label = new JLabel();
        return label;
    }
    
    // Gets a units validation label (creates a new one for compatibility)
    public JLabel getUnitsValidationLabel() {
        // Return a label that updates the main validation area
        JLabel label = new JLabel();
        return label;
    }
}