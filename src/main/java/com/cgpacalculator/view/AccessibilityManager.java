package com.cgpacalculator.view;

import javax.swing.*;
import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.HashSet;public class AccessibilityManager {public static void setupMainFrameAccessibility(MainFrame mainFrame) {
        // Set up frame accessibility properties
        AccessibleContext frameContext = mainFrame.getAccessibleContext();
        frameContext.setAccessibleName("CGPA Calculator Application");
        frameContext.setAccessibleDescription("Academic performance calculator for computing GPA and CGPA");
        
        // Set up keyboard navigation
        setupKeyboardNavigation(mainFrame);
        
        // Set up screen reader support
        setupScreenReaderSupport(mainFrame);
        
        // Set up high contrast support
        setupHighContrastSupport(mainFrame);
        
        // Set up focus management
        setupFocusManagement(mainFrame);
    }
    
    // Method comment
    public static void setupCoursePanelAccessibility(CoursePanel coursePanel) {
        // Set up panel accessibility properties
        AccessibleContext panelContext = coursePanel.getAccessibleContext();
        panelContext.setAccessibleName("Course Management Panel");
        panelContext.setAccessibleDescription("Panel for adding, removing, and managing course information");
        
        // Set up input field accessibility
        setupInputFieldAccessibility(coursePanel);
        
        // Set up table accessibility
        setupTableAccessibility(coursePanel);
        
        // Set up button accessibility
        setupButtonAccessibility(coursePanel);
    }
    
    // Method comment
    private static void setupKeyboardNavigation(MainFrame mainFrame) {
        // Enable focus traversal policy
        mainFrame.setFocusTraversalPolicyProvider(true);
        
        // Set up custom focus traversal order
        setupFocusTraversalOrder(mainFrame);
        
        // Set up keyboard shortcuts
        KeyboardShortcutManager.setupMainFrameShortcuts(mainFrame);
        
        // Set up mnemonics for all buttons
        setupMnemonics(mainFrame);
    }
    
    // Method comment
    private static void setupScreenReaderSupport(MainFrame mainFrame) {
        // Set up accessible names and descriptions for all components
        setupAccessibleNames(mainFrame);
        
        // Set up accessible relationships
        setupAccessibleRelationships(mainFrame);
        
        // Set up live regions for dynamic content
        setupLiveRegions(mainFrame);
    }
    
    // Method comment
    private static void setupHighContrastSupport(MainFrame mainFrame) {
        // Check if high contrast mode is enabled
        if (isHighContrastMode()) {
            applyHighContrastColors(mainFrame);
        }
        
        // Set up system property listeners for theme changes
        setupThemeChangeListeners(mainFrame);
    }
    
    // Method comment
    private static void setupFocusManagement(MainFrame mainFrame) {
        // Set initial focus
        SwingUtilities.invokeLater(() -> {
            mainFrame.getCoursePanel().getCourseNameField().requestFocusInWindow();
        });
        
        // Set up focus indicators
        setupFocusIndicators(mainFrame);
        
        // Set up focus cycling
        setupFocusCycling(mainFrame);
    }
    
    // Method comment
    private static void setupInputFieldAccessibility(CoursePanel coursePanel) {
        // Course name field
        JTextField courseNameField = coursePanel.getCourseNameField();
        AccessibleContext nameContext = courseNameField.getAccessibleContext();
        nameContext.setAccessibleName("Course Name");
        nameContext.setAccessibleDescription("Enter the name of the course (1 to 50 characters)");
        
        // Units spinner
        JSpinner unitsSpinner = coursePanel.getUnitsSpinner();
        AccessibleContext unitsContext = unitsSpinner.getAccessibleContext();
        unitsContext.setAccessibleName("Course Units");
        unitsContext.setAccessibleDescription("Select the number of credit units for this course (1 to 6)");
        
        // Grade combo box
        JComboBox<String> gradeComboBox = coursePanel.getGradeComboBox();
        AccessibleContext gradeContext = gradeComboBox.getAccessibleContext();
        gradeContext.setAccessibleName("Letter Grade");
        gradeContext.setAccessibleDescription("Select the letter grade received for this course");
        
        // Set up field relationships
        setupFieldRelationships(coursePanel);
    }
    
    // Method comment
    private static void setupTableAccessibility(CoursePanel coursePanel) {
        // Get table from course panel (would need to be exposed)
        // JTable courseTable = coursePanel.getCourseTable();
        
        // Set up table accessibility properties
        // AccessibleContext tableContext = courseTable.getAccessibleContext();
        // tableContext.setAccessibleName("Course List Table");
        // tableContext.setAccessibleDescription("Table showing all entered courses with their units, grades, and calculated points");
        
        // Set up column headers accessibility
        // setupTableHeaderAccessibility(courseTable);
        
        // Set up row selection accessibility
        // setupTableSelectionAccessibility(courseTable);
    }
    
    // Method comment
    private static void setupButtonAccessibility(CoursePanel coursePanel) {
        // Add Course button
        JButton addButton = coursePanel.getAddCourseButton();
        AccessibleContext addContext = addButton.getAccessibleContext();
        addContext.setAccessibleName("Add Course Button");
        addContext.setAccessibleDescription("Add the entered course information to the course list. Keyboard shortcut: Ctrl+A");
        
        // Remove Course button
        JButton removeButton = coursePanel.getRemoveCourseButton();
        AccessibleContext removeContext = removeButton.getAccessibleContext();
        removeContext.setAccessibleName("Remove Course Button");
        removeContext.setAccessibleDescription("Remove the selected course from the course list. Keyboard shortcut: Delete");
        
        // Clear All button
        JButton clearButton = coursePanel.getClearAllButton();
        AccessibleContext clearContext = clearButton.getAccessibleContext();
        clearContext.setAccessibleName("Clear All Courses Button");
        clearContext.setAccessibleDescription("Remove all courses from the course list. Keyboard shortcut: Ctrl+Shift+Delete");
    }
    
    // Method comment
    private static void setupFocusTraversalOrder(MainFrame mainFrame) {
        // Create custom focus traversal policy
        FocusTraversalPolicy customPolicy = new DefaultFocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                // Custom logic for focus order
                return super.getComponentAfter(aContainer, aComponent);
            }
            
            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                // Custom logic for reverse focus order
                return super.getComponentBefore(aContainer, aComponent);
            }
        };
        
        mainFrame.setFocusTraversalPolicy(customPolicy);
    }
    
    // Method comment
    private static void setupMnemonics(MainFrame mainFrame) {
        // Set up button mnemonics
        JButton calculateButton = mainFrame.getCalculateButton();
        calculateButton.setMnemonic(KeyEvent.VK_C);
        calculateButton.setDisplayedMnemonicIndex(0);
        
        JButton saveButton = mainFrame.getSaveDataButton();
        saveButton.setMnemonic(KeyEvent.VK_S);
        saveButton.setDisplayedMnemonicIndex(0);
        
        JButton loadButton = mainFrame.getLoadDataButton();
        loadButton.setMnemonic(KeyEvent.VK_L);
        loadButton.setDisplayedMnemonicIndex(0);
        
        // Set up course panel mnemonics
        setupCoursePanelMnemonics(mainFrame.getCoursePanel());
    }
    
    // Method comment
    private static void setupCoursePanelMnemonics(CoursePanel coursePanel) {
        JButton addButton = coursePanel.getAddCourseButton();
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.setDisplayedMnemonicIndex(0);
        
        JButton removeButton = coursePanel.getRemoveCourseButton();
        removeButton.setMnemonic(KeyEvent.VK_R);
        removeButton.setDisplayedMnemonicIndex(0);
        
        JButton clearButton = coursePanel.getClearAllButton();
        clearButton.setMnemonic(KeyEvent.VK_E);
        clearButton.setDisplayedMnemonicIndex(5); // 'E' in "Clear All"
    }
    
    // Method comment
    private static void setupAccessibleNames(MainFrame mainFrame) {
        // CGPA input field
        JTextField cgpaField = mainFrame.getCurrentCGPAField();
        AccessibleContext cgpaContext = cgpaField.getAccessibleContext();
        cgpaContext.setAccessibleName("Current CGPA Input Field");
        cgpaContext.setAccessibleDescription("Enter your current cumulative GPA (0.00 to 5.00). This field is optional.");
        
        // Cumulative units field
        JTextField unitsField = mainFrame.getCumulativeUnitsField();
        AccessibleContext unitsContext = unitsField.getAccessibleContext();
        unitsContext.setAccessibleName("Cumulative Units Input Field");
        unitsContext.setAccessibleDescription("Enter your total cumulative units completed. Required if current CGPA is provided.");
    }
    
    // Method comment
    private static void setupAccessibleRelationships(MainFrame mainFrame) {
        // Set up label-for relationships
        // This would require access to labels and their associated fields
        
        // Set up described-by relationships for validation messages
        // This would require access to validation labels
    }
    
    // Method comment
    private static void setupLiveRegions(MainFrame mainFrame) {
        // Set up live regions for results that change dynamically
        // This would require access to result display components
        
        // Set up live regions for validation messages
        // This would require access to validation feedback components
    }
    
    // Method comment
    private static void setupFieldRelationships(CoursePanel coursePanel) {
        // Set up relationships between input fields and their validation labels
        // This would require access to validation labels
        
        // Set up group relationships for related fields
        // This would require creating accessible groups
    }
    
    // Method comment
    private static boolean isHighContrastMode() {
        // Check system properties for high contrast mode
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Boolean highContrast = (Boolean) toolkit.getDesktopProperty("win.highContrast.on");
            return highContrast != null && highContrast;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Method comment
    private static void applyHighContrastColors(MainFrame mainFrame) {
        // Apply high contrast color scheme
        Color highContrastBackground = Color.BLACK;
        Color highContrastForeground = Color.WHITE;
        Color highContrastSelection = Color.YELLOW;
        
        // Apply colors to main frame
        mainFrame.setBackground(highContrastBackground);
        mainFrame.setForeground(highContrastForeground);
        
        // Apply colors to all child components recursively
        applyHighContrastToComponents(mainFrame, highContrastBackground, highContrastForeground);
    }
    
    // Method comment
    private static void applyHighContrastToComponents(Container container, Color background, Color foreground) {
        for (Component component : container.getComponents()) {
            component.setBackground(background);
            component.setForeground(foreground);
            
            if (component instanceof Container) {
                applyHighContrastToComponents((Container) component, background, foreground);
            }
        }
    }
    
    // Method comment
    private static void setupThemeChangeListeners(MainFrame mainFrame) {
        // Set up property change listeners for system theme changes
        Toolkit.getDefaultToolkit().addPropertyChangeListener("win.highContrast.on", evt -> {
            SwingUtilities.invokeLater(() -> {
                if (isHighContrastMode()) {
                    applyHighContrastColors(mainFrame);
                } else {
                    // Restore normal colors
                    restoreNormalColors(mainFrame);
                }
            });
        });
    }
    
    // Method comment
    private static void restoreNormalColors(MainFrame mainFrame) {
        // Restore default look and feel colors
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(mainFrame);
        } catch (Exception e) {
            // Handle exception silently
        }
    }
    
    // Method comment
    private static void setupFocusIndicators(MainFrame mainFrame) {
        // Set up enhanced focus indicators
        UIManager.put("Button.focus", Color.BLUE);
        UIManager.put("TextField.focusInputMap", new InputMap());
        
        // Apply focus indicators to all focusable components
        setupFocusIndicatorsRecursively(mainFrame);
    }
    
    // Method comment
    private static void setupFocusIndicatorsRecursively(Container container) {
        for (Component component : container.getComponents()) {
            if (component.isFocusable()) {
                // Set up enhanced focus indicator
                component.addFocusListener(new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusGained(java.awt.event.FocusEvent e) {
                        component.repaint();
                    }
                    
                    @Override
                    public void focusLost(java.awt.event.FocusEvent e) {
                        component.repaint();
                    }
                });
            }
            
            if (component instanceof Container) {
                setupFocusIndicatorsRecursively((Container) component);
            }
        }
    }
    
    // Method comment
    private static void setupFocusCycling(MainFrame mainFrame) {
        // Enable focus cycling within the application
        mainFrame.setFocusCycleRoot(true);
        
        // Set up focus cycle keys
        Set<AWTKeyStroke> forwardKeys = new HashSet<>();
        forwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
        mainFrame.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
        
        Set<AWTKeyStroke> backwardKeys = new HashSet<>();
        backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK));
        mainFrame.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
    }
}