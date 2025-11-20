package com.cgpacalculator.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Manager class for enhanced tooltips with validation feedback and help information.
// Provides dynamic tooltips that change based on validation state and user context.
public class TooltipManager {
    
    // Tooltip delay constants
    private static final int INITIAL_DELAY = 500;  // 0.5 seconds
    private static final int DISMISS_DELAY = 10000; // 10 seconds
    private static final int RESHOW_DELAY = 100;   // 0.1 seconds
    
    // Sets up enhanced tooltips for the main frame components
    // 
    // @param mainFrame the main application frame
    public static void setupMainFrameTooltips(MainFrame mainFrame) {
        // Configure global tooltip settings
        ToolTipManager.sharedInstance().setInitialDelay(INITIAL_DELAY);
        ToolTipManager.sharedInstance().setDismissDelay(DISMISS_DELAY);
        ToolTipManager.sharedInstance().setReshowDelay(RESHOW_DELAY);
        
        // Set up CGPA field tooltip
        setupCGPAFieldTooltip(mainFrame.getCurrentCGPAField());
        
        // Set up cumulative units field tooltip
        setupCumulativeUnitsFieldTooltip(mainFrame.getCumulativeUnitsField());
        
        // Set up button tooltips with shortcuts
        setupButtonTooltips(mainFrame);
    }
    
    // Method comment
    public static void setupCoursePanelTooltips(CoursePanel coursePanel) {
        // Set up course name field tooltip
        setupCourseNameFieldTooltip(coursePanel.getCourseNameField());
        
        // Set up units spinner tooltip
        setupUnitsSpinnerTooltip(coursePanel.getUnitsSpinner());
        
        // Set up grade combo box tooltip
        setupGradeComboBoxTooltip(coursePanel.getGradeComboBox());
        
        // Set up course panel button tooltips
        setupCoursePanelButtonTooltips(coursePanel);
    }
    
    // Method comment
    private static void setupCGPAFieldTooltip(JTextField cgpaField) {
        cgpaField.setToolTipText(createCGPATooltip(false, null));
        
        // Add mouse listener for dynamic tooltip updates
        cgpaField.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                String currentText = cgpaField.getText().trim();
                boolean hasError = cgpaField.getBorder() instanceof javax.swing.border.LineBorder &&
                                 ((javax.swing.border.LineBorder) cgpaField.getBorder()).getLineColor().equals(Color.RED);
                
                cgpaField.setToolTipText(createCGPATooltip(hasError, currentText));
            }
        });
    }
    
    // Method comment
    private static void setupCumulativeUnitsFieldTooltip(JTextField unitsField) {
        unitsField.setToolTipText(createCumulativeUnitsTooltip(false, null));
        
        // Add mouse listener for dynamic tooltip updates
        unitsField.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                String currentText = unitsField.getText().trim();
                boolean hasError = unitsField.getBorder() instanceof javax.swing.border.LineBorder &&
                                 ((javax.swing.border.LineBorder) unitsField.getBorder()).getLineColor().equals(Color.RED);
                
                unitsField.setToolTipText(createCumulativeUnitsTooltip(hasError, currentText));
            }
        });
    }
    
    // Method comment
    private static void setupCourseNameFieldTooltip(JTextField courseNameField) {
        courseNameField.setToolTipText(createCourseNameTooltip(false, null));
        
        // Add mouse listener for dynamic tooltip updates
        courseNameField.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                String currentText = courseNameField.getText().trim();
                boolean hasError = courseNameField.getBorder() instanceof javax.swing.border.LineBorder &&
                                 ((javax.swing.border.LineBorder) courseNameField.getBorder()).getLineColor().equals(Color.RED);
                
                courseNameField.setToolTipText(createCourseNameTooltip(hasError, currentText));
            }
        });
    }
    
    // Method comment
    private static void setupUnitsSpinnerTooltip(JSpinner unitsSpinner) {
        String tooltip = "<html><b>Course Units</b><br>" +
                        "Select the number of credit units for this course.<br><br>" +
                        "<b>Valid Range:</b> 1-6 units<br>" +
                        "<b>Typical Values:</b><br>" +
                        "• 1-2 units: Lab courses, seminars<br>" +
                        "• 3 units: Standard lecture courses<br>" +
                        "• 4-6 units: Advanced or intensive courses<br><br>" +
                        "<i>Use arrow keys or click buttons to change value</i></html>";
        
        unitsSpinner.setToolTipText(tooltip);
        
        // Also set tooltip for the spinner's editor
        JComponent editor = unitsSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setToolTipText(tooltip);
        }
    }
    
    // Method comment
    private static void setupGradeComboBoxTooltip(JComboBox<String> gradeComboBox) {
        String tooltip = "<html><b>Letter Grade</b><br>" +
                        "Select the letter grade received for this course.<br><br>" +
                        "<b>Grade Point Values:</b><br>" +
                        "• A = 5.0 points (Excellent)<br>" +
                        "• B = 4.0 points (Very Good)<br>" +
                        "• C = 3.0 points (Good)<br>" +
                        "• D = 2.0 points (Satisfactory)<br>" +
                        "• E = 1.0 point (Pass)<br>" +
                        "• F = 0.0 points (Fail)<br><br>" +
                        "<i>Use arrow keys or click to select grade</i></html>";
        
        gradeComboBox.setToolTipText(tooltip);
    }
    
    // Method comment
    private static void setupButtonTooltips(MainFrame mainFrame) {
        // These methods would need to be added to MainFrame to access buttons
        // For now, we'll set up tooltips through the existing public methods
        
        // Note: This would require MainFrame to expose button getters or 
        // we could modify the existing button creation methods to include enhanced tooltips
    }
    
    // Method comment
    private static void setupCoursePanelButtonTooltips(CoursePanel coursePanel) {
        // Add Course button tooltip
        String addCourseTooltip = "<html><b>Add Course</b><br>" +
                                 "Add the entered course information to your course list.<br><br>" +
                                 "<b>Requirements:</b><br>" +
                                 "• Course name must be 1-50 characters<br>" +
                                 "• Units must be between 1-6<br>" +
                                 "• Grade must be selected<br><br>" +
                                 "<b>Shortcuts:</b> Ctrl+A or Insert</html>";
        
        // Remove Course button tooltip
        String removeCourseTooltip = "<html><b>Remove Selected Course</b><br>" +
                                   "Remove the currently selected course from the list.<br><br>" +
                                   "<b>Note:</b> Select a course in the table first.<br>" +
                                   "This action will show a confirmation dialog.<br><br>" +
                                   "<b>Shortcuts:</b> Delete or Ctrl+R</html>";
        
        // Clear All button tooltip
        String clearAllTooltip = "<html><b>Clear All Courses</b><br>" +
                               "Remove all courses from the course list.<br><br>" +
                               "<b>Warning:</b> This action cannot be undone.<br>" +
                               "A confirmation dialog will be shown.<br><br>" +
                               "<b>Shortcut:</b> Ctrl+Shift+Delete</html>";
        
        // Note: These would need to be applied to the actual buttons
        // This requires either exposing button getters or modifying button creation
    }
    
    // Method comment
    private static String createCGPATooltip(boolean hasError, String currentValue) {
        StringBuilder tooltip = new StringBuilder("<html><b>Current CGPA</b><br>");
        
        if (hasError) {
            tooltip.append("<font color='red'><b>⚠ Validation Error</b></font><br>");
            if (currentValue != null && !currentValue.isEmpty()) {
                try {
                    double value = Double.parseDouble(currentValue);
                    if (value < 0.0 || value > 5.0) {
                        tooltip.append("Value must be between 0.00 and 5.00<br>");
                    }
                } catch (NumberFormatException e) {
                    tooltip.append("Please enter a valid decimal number<br>");
                }
            }
            tooltip.append("<br>");
        }
        
        tooltip.append("Enter your current cumulative GPA (optional).<br><br>");
        tooltip.append("<b>Valid Range:</b> 0.00 - 5.00<br>");
        tooltip.append("<b>Format:</b> Decimal number (e.g., 3.75)<br>");
        tooltip.append("<b>Note:</b> If provided, cumulative units must also be entered<br><br>");
        
        tooltip.append("<b>Classification Ranges:</b><br>");
        tooltip.append("• 4.50-5.00: First Class<br>");
        tooltip.append("• 3.50-4.49: Second Class Upper<br>");
        tooltip.append("• 2.50-3.49: Second Class Lower<br>");
        tooltip.append("• 1.50-2.49: Third Class<br>");
        tooltip.append("• Below 1.50: Fail<br><br>");
        
        tooltip.append("<i>Shortcut: F2 to focus this field</i></html>");
        
        return tooltip.toString();
    }
    
    // Method comment
    private static String createCumulativeUnitsTooltip(boolean hasError, String currentValue) {
        StringBuilder tooltip = new StringBuilder("<html><b>Cumulative Units</b><br>");
        
        if (hasError) {
            tooltip.append("<font color='red'><b>⚠ Validation Error</b></font><br>");
            if (currentValue != null && !currentValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(currentValue);
                    if (value < 0) {
                        tooltip.append("Value must be a positive number<br>");
                    }
                } catch (NumberFormatException e) {
                    tooltip.append("Please enter a valid whole number<br>");
                }
            }
            tooltip.append("<br>");
        }
        
        tooltip.append("Enter your total cumulative units completed (optional).<br><br>");
        tooltip.append("<b>Format:</b> Positive whole number<br>");
        tooltip.append("<b>Note:</b> Required if current CGPA is provided<br>");
        tooltip.append("<b>Recommendation:</b> At least 24 units for accurate CGPA calculation<br><br>");
        
        tooltip.append("<b>Typical Values:</b><br>");
        tooltip.append("• 1st Year: 24-48 units<br>");
        tooltip.append("• 2nd Year: 48-96 units<br>");
        tooltip.append("• 3rd Year: 96-144 units<br>");
        tooltip.append("• 4th Year: 144+ units<br><br>");
        
        tooltip.append("<i>Shortcut: F3 to focus this field</i></html>");
        
        return tooltip.toString();
    }
    
    // Method comment
    private static String createCourseNameTooltip(boolean hasError, String currentValue) {
        StringBuilder tooltip = new StringBuilder("<html><b>Course Name</b><br>");
        
        if (hasError) {
            tooltip.append("<font color='red'><b>⚠ Validation Error</b></font><br>");
            if (currentValue != null) {
                if (currentValue.isEmpty()) {
                    tooltip.append("Course name is required<br>");
                } else if (currentValue.length() > 50) {
                    tooltip.append("Course name too long (max 50 characters)<br>");
                } else {
                    tooltip.append("Course name already exists<br>");
                }
            }
            tooltip.append("<br>");
        }
        
        tooltip.append("Enter the name of the course.<br><br>");
        tooltip.append("<b>Requirements:</b><br>");
        tooltip.append("• 1-50 characters long<br>");
        tooltip.append("• Must be unique (no duplicates)<br>");
        tooltip.append("• Cannot be empty<br><br>");
        
        tooltip.append("<b>Examples:</b><br>");
        tooltip.append("• Data Structures<br>");
        tooltip.append("• Calculus I<br>");
        tooltip.append("• Introduction to Psychology<br>");
        tooltip.append("• Advanced Java Programming<br><br>");
        
        if (currentValue != null && !currentValue.isEmpty()) {
            tooltip.append(String.format("<b>Current Length:</b> %d/50 characters<br><br>", currentValue.length()));
        }
        
        tooltip.append("<i>Shortcut: F1 to focus this field</i></html>");
        
        return tooltip.toString();
    }
    
    // Method comment
    public static void updateValidationTooltip(JComponent component, boolean hasError, String errorMessage) {
        String currentTooltip = component.getToolTipText();
        
        if (hasError && errorMessage != null) {
            // Add error information to existing tooltip
            String enhancedTooltip = "<html><font color='red'><b>⚠ " + errorMessage + "</b></font><br><br>" +
                                   (currentTooltip != null ? currentTooltip.replaceAll("</?html>", "") : "") +
                                   "</html>";
            component.setToolTipText(enhancedTooltip);
        } else {
            // Restore original tooltip (remove error information)
            if (currentTooltip != null && currentTooltip.contains("⚠")) {
                // Extract original tooltip without error message
                String originalTooltip = currentTooltip.replaceAll("<font color='red'>.*?</font><br><br>", "");
                component.setToolTipText(originalTooltip);
            }
        }
    }
    
    // Method comment
    public static String createButtonTooltip(String buttonText, String description, String shortcut) {
        StringBuilder tooltip = new StringBuilder("<html><b>").append(buttonText).append("</b><br>");
        tooltip.append(description).append("<br><br>");
        
        if (shortcut != null && !shortcut.isEmpty()) {
            tooltip.append("<b>Keyboard Shortcut:</b> ").append(shortcut).append("<br><br>");
        }
        
        tooltip.append("<i>Click to activate or use keyboard shortcut</i></html>");
        
        return tooltip.toString();
    }
}