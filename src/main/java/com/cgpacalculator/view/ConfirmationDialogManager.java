package com.cgpacalculator.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;public class ConfirmationDialogManager {
    
    // Dialog icons for different types of confirmations
    private static final Icon WARNING_ICON = UIManager.getIcon("OptionPane.warningIcon");
    private static final Icon QUESTION_ICON = UIManager.getIcon("OptionPane.questionIcon");
    private static final Icon INFO_ICON = UIManager.getIcon("OptionPane.informationIcon");public static boolean confirmClearAllCourses(Component parent, int courseCount) {
        String title = "Confirm Clear All Courses";
        String message;
        
        if (courseCount == 0) {
            message = "There are no courses to clear.";
            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE, INFO_ICON);
            return false;
        } else if (courseCount == 1) {
            message = "Are you sure you want to clear the 1 course?\n\n" +
                     "This action cannot be undone. All course data will be permanently removed.";
        } else {
            message = String.format("Are you sure you want to clear all %d courses?\n\n" +
                                  "This action cannot be undone. All course data will be permanently removed.", 
                                  courseCount);
        }
        
        // Create custom option pane with keyboard shortcuts
        Object[] options = {"Clear All", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON,
            options,
            options[1] // Default to Cancel
        );
        
        return result == 0; // Return true if "Clear All" was selected
    }
    
    // Method comment
    public static boolean confirmRemoveCourse(Component parent, String courseName) {
        String title = "Confirm Remove Course";
        String message = String.format("Are you sure you want to remove the course:\n\n" +
                                      "\"%s\"\n\n" +
                                      "This action cannot be undone.", 
                                      courseName != null ? courseName : "Selected Course");
        
        Object[] options = {"Remove Course", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON,
            options,
            options[1] // Default to Cancel
        );
        
        return result == 0; // Return true if "Remove Course" was selected
    }
    
    // Method comment
    public static boolean confirmDataLoad(Component parent, boolean hasUnsavedChanges) {
        String title = "Confirm Load Data";
        String message;
        
        if (hasUnsavedChanges) {
            message = "Loading new data will replace all current course information.\n\n" +
                     "You have unsaved changes that will be lost.\n\n" +
                     "Do you want to continue?";
        } else {
            message = "Loading new data will replace all current course information.\n\n" +
                     "Do you want to continue?";
        }
        
        Object[] options = {"Load Data", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            QUESTION_ICON,
            options,
            options[1] // Default to Cancel
        );
        
        return result == 0; // Return true if "Load Data" was selected
    }
    
    // Method comment
    public static int confirmExitWithUnsavedChanges(Component parent) {
        String title = "Unsaved Changes";
        String message = "You have unsaved changes.\n\n" +
                        "Do you want to save your changes before exiting?";
        
        Object[] options = {"Save and Exit", "Exit without Saving", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON,
            options,
            options[0] // Default to Save and Exit
        );
        
        return result;
    }
    
    // Method comment
    public static boolean confirmNewCalculation(Component parent, boolean hasData) {
        if (!hasData) {
            return true; // No confirmation needed if no data exists
        }
        
        String title = "Confirm New Calculation";
        String message = "Creating a new calculation will clear all current course data and results.\n\n" +
                        "Do you want to continue?";
        
        Object[] options = {"New Calculation", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            QUESTION_ICON,
            options,
            options[1] // Default to Cancel
        );
        
        return result == 0; // Return true if "New Calculation" was selected
    }
    
    // Method comment
    public static void showSuccessMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE,
            INFO_ICON
        );
    }
    
    // Method comment
    public static void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    // Method comment
    public static void showWarningMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON
        );
    }
    
    // Method comment
    public static boolean confirmFileOverwrite(Component parent, String fileName) {
        String title = "Confirm File Overwrite";
        String message = String.format("The file \"%s\" already exists.\n\n" +
                                      "Do you want to overwrite it?", fileName);
        
        Object[] options = {"Overwrite", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            WARNING_ICON,
            options,
            options[1] // Default to Cancel
        );
        
        return result == 0; // Return true if "Overwrite" was selected
    }
}