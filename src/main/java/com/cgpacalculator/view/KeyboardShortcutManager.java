package com.cgpacalculator.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Component;public class KeyboardShortcutManager {
    
    // Action command constants for keyboard shortcuts
    public static final String ACTION_CALCULATE = "calculate_gpa";
    public static final String ACTION_ADD_COURSE = "add_course";
    public static final String ACTION_REMOVE_COURSE = "remove_course";
    public static final String ACTION_CLEAR_ALL = "clear_all_courses";
    public static final String ACTION_SAVE_DATA = "save_data";
    public static final String ACTION_LOAD_DATA = "load_data";
    public static final String ACTION_NEW_CALCULATION = "new_calculation";
    public static final String ACTION_FOCUS_COURSE_NAME = "focus_course_name";
    public static final String ACTION_FOCUS_CGPA = "focus_cgpa";
    public static final String ACTION_FOCUS_UNITS = "focus_units";
    public static final String ACTION_SHOW_HELP = "show_help";
    public static final String ACTION_EXIT_APPLICATION = "exit_application";public static void setupMainFrameShortcuts(MainFrame mainFrame) {
        JRootPane rootPane = mainFrame.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();
        
        // Calculate GPA - Enter or F5
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ACTION_CALCULATE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), ACTION_CALCULATE);
        
        // Save Data - Ctrl+S
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), ACTION_SAVE_DATA);
        
        // Load Data - Ctrl+O
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), ACTION_LOAD_DATA);
        
        // New Calculation - Ctrl+N
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), ACTION_NEW_CALCULATION);
        
        // Enhanced focus navigation shortcuts
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), ACTION_FOCUS_COURSE_NAME);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), ACTION_FOCUS_CGPA);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), ACTION_FOCUS_UNITS);
        
        // Advanced navigation - Tab cycling through sections
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK), "cycle_sections");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "cycle_sections_reverse");
        
        // Quick access to specific sections
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK), "focus_cgpa_section");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK), "focus_course_section");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK), "focus_results_section");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK), "focus_reference_section");
        
        // Help - F1 with Ctrl
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK), ACTION_SHOW_HELP);
        
        // Exit - Alt+F4 or Ctrl+Q
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK), ACTION_EXIT_APPLICATION);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), ACTION_EXIT_APPLICATION);
        
        // Set up action listeners (these will be connected by the controller)
        actionMap.put(ACTION_CALCULATE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Trigger calculate button click
                mainFrame.getCalculateButton().doClick();
            }
        });
        
        actionMap.put(ACTION_SAVE_DATA, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Trigger save button click
                mainFrame.getSaveDataButton().doClick();
            }
        });
        
        actionMap.put(ACTION_LOAD_DATA, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Trigger load button click
                mainFrame.getLoadDataButton().doClick();
            }
        });
        
        actionMap.put(ACTION_NEW_CALCULATION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all fields for new calculation
                mainFrame.getCurrentCGPAField().setText("");
                mainFrame.getCumulativeUnitsField().setText("");
                mainFrame.getCoursePanel().clearAllCourses();
                mainFrame.clearResultDisplays();
            }
        });
        
        actionMap.put(ACTION_FOCUS_COURSE_NAME, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getCoursePanel().getCourseNameField().requestFocusInWindow();
            }
        });
        
        actionMap.put(ACTION_FOCUS_CGPA, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getCurrentCGPAField().requestFocusInWindow();
            }
        });
        
        actionMap.put(ACTION_FOCUS_UNITS, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getCumulativeUnitsField().requestFocusInWindow();
            }
        });
        
        actionMap.put(ACTION_SHOW_HELP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showKeyboardShortcutsDialog(mainFrame);
            }
        });
        
        actionMap.put(ACTION_EXIT_APPLICATION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Trigger window closing event
                mainFrame.dispatchEvent(new java.awt.event.WindowEvent(mainFrame, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    
    // Method comment
    public static void setupCoursePanelShortcuts(CoursePanel coursePanel) {
        InputMap inputMap = coursePanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = coursePanel.getActionMap();
        
        // Add Course - Ctrl+A or Insert
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), ACTION_ADD_COURSE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), ACTION_ADD_COURSE);
        
        // Remove Course - Delete or Ctrl+R
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), ACTION_REMOVE_COURSE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), ACTION_REMOVE_COURSE);
        
        // Clear All - Ctrl+Shift+Delete
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 
                    InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), ACTION_CLEAR_ALL);
        
        // Set up action listeners
        actionMap.put(ACTION_ADD_COURSE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coursePanel.getAddCourseButton().doClick();
            }
        });
        
        actionMap.put(ACTION_REMOVE_COURSE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coursePanel.getRemoveCourseButton().doClick();
            }
        });
        
        actionMap.put(ACTION_CLEAR_ALL, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coursePanel.getClearAllButton().doClick();
            }
        });
    }
    
    // Method comment
    public static void setupEnhancedMnemonics(JComponent component) {
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            String text = button.getText();
            
            // Set mnemonics based on button text
            if (text != null) {
                switch (text.toLowerCase()) {
                    case "calculate gpa & cgpa":
                    case "calculate":
                        button.setMnemonic(KeyEvent.VK_C);
                        button.setDisplayedMnemonicIndex(0);
                        break;
                    case "add course":
                        button.setMnemonic(KeyEvent.VK_A);
                        button.setDisplayedMnemonicIndex(0);
                        break;
                    case "remove selected":
                    case "remove course":
                        button.setMnemonic(KeyEvent.VK_R);
                        button.setDisplayedMnemonicIndex(0);
                        break;
                    case "clear all":
                        button.setMnemonic(KeyEvent.VK_L);
                        button.setDisplayedMnemonicIndex(5); // 'L' in "Clear aLL"
                        break;
                    case "save data":
                        button.setMnemonic(KeyEvent.VK_S);
                        button.setDisplayedMnemonicIndex(0);
                        break;
                    case "load data":
                        button.setMnemonic(KeyEvent.VK_O);
                        button.setDisplayedMnemonicIndex(1); // 'O' in "lOad"
                        break;
                }
            }
        }
    }
    
    // Method comment
    public static String createTooltipWithShortcut(String baseTooltip, String shortcut) {
        return String.format("<html>%s<br><small><i>Shortcut: %s</i></small></html>", 
                           baseTooltip, shortcut);
    }
    
    // Method comment
    public static void setupAccessibilityFeatures(JComponent component) {
        // Enable focus traversal
        component.setFocusTraversalKeysEnabled(true);
        
        // Set accessible names and descriptions for screen readers
        if (component instanceof JTextField) {
            JTextField field = (JTextField) component;
            String name = field.getName();
            
            if (name != null) {
                switch (name) {
                    case "courseNameField":
                        field.getAccessibleContext().setAccessibleName("Course Name Input");
                        field.getAccessibleContext().setAccessibleDescription(
                            "Enter the name of the course (1-50 characters)");
                        break;
                    case "currentCGPAField":
                        field.getAccessibleContext().setAccessibleName("Current CGPA Input");
                        field.getAccessibleContext().setAccessibleDescription(
                            "Enter your current CGPA (0.00 to 5.00)");
                        break;
                    case "cumulativeUnitsField":
                        field.getAccessibleContext().setAccessibleName("Cumulative Units Input");
                        field.getAccessibleContext().setAccessibleDescription(
                            "Enter your current cumulative units (positive integer)");
                        break;
                }
            }
        } else if (component instanceof JSpinner) {
            JSpinner spinner = (JSpinner) component;
            spinner.getAccessibleContext().setAccessibleName("Course Units Selector");
            spinner.getAccessibleContext().setAccessibleDescription(
                "Select the number of units for this course (1-6)");
        } else if (component instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) component;
            comboBox.getAccessibleContext().setAccessibleName("Grade Selector");
            comboBox.getAccessibleContext().setAccessibleDescription(
                "Select the letter grade for this course (A, B, C, D, E, or F)");
        }
    }
    
    // Method comment
    private static void showKeyboardShortcutsDialog(Component parent) {
        String helpText = getKeyboardShortcutsHelp();
        JOptionPane.showMessageDialog(parent, helpText, "Keyboard Shortcuts Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Method comment
    public static String getKeyboardShortcutsHelp() {
        return "<html><h3>Keyboard Shortcuts</h3>" +
               "<table border='0' cellpadding='3'>" +
               "<tr><td><b>General Actions:</b></td><td></td></tr>" +
               "<tr><td>Calculate GPA</td><td>Enter or F5</td></tr>" +
               "<tr><td>Save Data</td><td>Ctrl+S</td></tr>" +
               "<tr><td>Load Data</td><td>Ctrl+O</td></tr>" +
               "<tr><td>New Calculation</td><td>Ctrl+N</td></tr>" +
               "<tr><td>Exit Application</td><td>Alt+F4 or Ctrl+Q</td></tr>" +
               "<tr><td></td><td></td></tr>" +
               "<tr><td><b>Course Management:</b></td><td></td></tr>" +
               "<tr><td>Add Course</td><td>Ctrl+A or Insert</td></tr>" +
               "<tr><td>Remove Course</td><td>Delete or Ctrl+R</td></tr>" +
               "<tr><td>Clear All Courses</td><td>Ctrl+Shift+Delete</td></tr>" +
               "<tr><td></td><td></td></tr>" +
               "<tr><td><b>Navigation:</b></td><td></td></tr>" +
               "<tr><td>Focus Course Name</td><td>F1</td></tr>" +
               "<tr><td>Focus Current CGPA</td><td>F2</td></tr>" +
               "<tr><td>Focus Cumulative Units</td><td>F3</td></tr>" +
               "<tr><td>Show Help</td><td>Ctrl+F1</td></tr>" +
               "<tr><td></td><td></td></tr>" +
               "<tr><td><b>Accessibility:</b></td><td></td></tr>" +
               "<tr><td>Tab Navigation</td><td>Tab / Shift+Tab</td></tr>" +
               "<tr><td>Button Mnemonics</td><td>Alt+Letter</td></tr>" +
               "<tr><td>Menu Access</td><td>Alt+Letter</td></tr>" +
               "</table></html>";
    }
}