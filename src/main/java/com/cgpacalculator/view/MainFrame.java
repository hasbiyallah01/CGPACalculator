package com.cgpacalculator.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import com.cgpacalculator.utils.ValidationUtils;
import com.cgpacalculator.utils.Constants;
import com.cgpacalculator.utils.AcademicConstraintValidator;
import com.cgpacalculator.utils.ValidationResult;

// Main application frame that serves as the primary window for the CGPA Calculator
public class MainFrame extends JFrame {
    
    // Encapsulated UI components
    private final CoursePanel coursePanel;
    private final ReferencePanel referencePanel;
    private final JPanel currentCGPAPanel;
    private final JPanel calculationPanel;
    private final JPanel resultsPanel;
    
    // Current CGPA input components
    private final JTextField currentCGPAField;
    private final JTextField cumulativeUnitsField;
    
    // Validation feedback components
    private final JLabel cgpaValidationLabel;
    private final JLabel unitsValidationLabel;
    private final JLabel dependencyValidationLabel;
    
    // Calculation control components
    private final JButton calculateButton;
    private final JButton saveDataButton;
    private final JButton loadDataButton;
    
    // Results display components
    private final JLabel currentGPALabel;
    private final JLabel updatedCGPALabel;
    private final JLabel classificationLabel;
    private final JLabel totalUnitsLabel;
    private final JTextArea motivationalMessageArea;
    
    // Menu components
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenu helpMenu;
    
    // Constructor initializes the main frame with all components and layout
    public MainFrame() {
        // Initialize core components
        this.coursePanel = new CoursePanel();
        this.referencePanel = new ReferencePanel();
        
        // Initialize input components
        this.currentCGPAField = createCurrentCGPAField();
        this.cumulativeUnitsField = createCumulativeUnitsField();
        
        // Initialize validation feedback components
        this.cgpaValidationLabel = createValidationLabel();
        this.unitsValidationLabel = createValidationLabel();
        this.dependencyValidationLabel = createValidationLabel();
        
        // Initialize control components
        this.calculateButton = createCalculateButton();
        this.saveDataButton = createSaveDataButton();
        this.loadDataButton = createLoadDataButton();
        
        // Initialize results components
        this.currentGPALabel = createResultLabel("Current GPA: --");
        this.updatedCGPALabel = createResultLabel("Updated CGPA: --");
        this.classificationLabel = createResultLabel("Classification: --");
        this.totalUnitsLabel = createResultLabel("Total Units: --");
        this.motivationalMessageArea = createMotivationalMessageArea();
        
        // Initialize layout panels
        this.currentCGPAPanel = createCurrentCGPAPanel();
        this.calculationPanel = createCalculationPanel();
        this.resultsPanel = createResultsPanel();
        
        // Initialize menu components
        this.menuBar = createMenuBar();
        this.fileMenu = createFileMenu();
        this.helpMenu = createHelpMenu();
        
        // Set up the complete frame
        setupFrameProperties();
        setupMenuBar();
        setupMainLayout();
        
        // Configure frame behavior
        configureFrameBehavior();
        
        // Setup real-time validation
        setupRealTimeValidation();
        
        // Setup enhanced features
        setupEnhancedFeatures();
    }
    
    // Creates the current CGPA input field with proper validation
    private JTextField createCurrentCGPAField() {
        JTextField field = new JTextField(8);
        field.setToolTipText("Enter current CGPA (0.00 - 5.00)");
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setName("currentCGPAField"); // For testing and identification
        return field;
    }
    
    // Creates the cumulative units input field with proper validation
    private JTextField createCumulativeUnitsField() {
        JTextField field = new JTextField(8);
        field.setToolTipText("Enter current cumulative units (positive integer)");
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setName("cumulativeUnitsField"); // For testing and identification
        return field;
    }
    
    // Creates a validation label for displaying error messages
    private JLabel createValidationLabel() {
        JLabel label = new JLabel(" "); // Space to maintain layout
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }
    
    // Creates the calculate button with descriptive properties
    private JButton createCalculateButton() {
        ColoredButton button = ColoredButton.createPrimaryButton("Calculate GPA & CGPA");
        button.setToolTipText("Calculate GPA and CGPA based on entered courses");
        button.setMnemonic(KeyEvent.VK_ENTER);
        button.setPreferredSize(new Dimension(180, 30));
        return button;
    }
    
    // Creates the save data button with descriptive properties
    private JButton createSaveDataButton() {
        ColoredButton button = ColoredButton.createSecondaryButton("Save Data");
        button.setToolTipText("Save current course data to file");
        button.setMnemonic(KeyEvent.VK_S);
        return button;
    }
    
    // Creates the load data button with descriptive properties
    private JButton createLoadDataButton() {
        ColoredButton button = ColoredButton.createSecondaryButton("Load Data");
        button.setToolTipText("Load previously saved course data");
        button.setMnemonic(KeyEvent.VK_L);
        return button;
    }
    
    // Creates a standardized result label with consistent formatting
    private JLabel createResultLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }
    
    // Creates the motivational message display area
    private JTextArea createMotivationalMessageArea() {
        JTextArea area = new JTextArea(3, 40);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setBackground(getBackground());
        area.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        area.setText("Enter your courses and click Calculate to see your results and motivational message!");
        return area;
    }
    
    // Creates the current CGPA input panel with organized layout and validation feedback
    private JPanel createCurrentCGPAPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Current Academic Standing"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        
        // Current CGPA section
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Current CGPA:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(currentCGPAField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(cgpaValidationLabel, gbc);
        
        // Cumulative Units section
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Cumulative Units:"), gbc);
        
        gbc.gridx = 3; gbc.gridy = 0;
        panel.add(cumulativeUnitsField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(unitsValidationLabel, gbc);
        
        // Dependency validation message (spans full width)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(dependencyValidationLabel, gbc);
        
        return panel;
    }
    
    // Creates the calculation control panel with organized button layout
    private JPanel createCalculationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(new TitledBorder("Actions"));
        
        panel.add(calculateButton);
        panel.add(saveDataButton);
        panel.add(loadDataButton);
        
        return panel;
    }
    
    // Creates the results display panel with organized layout
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Calculation Results"));
        
        // Create results grid panel
        JPanel resultsGrid = createResultsGridPanel();
        
        // Create motivational message panel
        JPanel messagePanel = createMotivationalMessagePanel();
        
        panel.add(resultsGrid, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Creates the grid panel for displaying calculation results
    private JPanel createResultsGridPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(currentGPALabel);
        panel.add(totalUnitsLabel);
        panel.add(updatedCGPALabel);
        panel.add(classificationLabel);
        
        return panel;
    }
    
    // Creates the panel for displaying motivational messages
    private JPanel createMotivationalMessagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Motivational Message"));
        
        JScrollPane scrollPane = new JScrollPane(motivationalMessageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(0, 80));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Creates the main menu bar
    private JMenuBar createMenuBar() {
        return new JMenuBar();
    }
    
    // Creates the File menu with standard menu items
    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        
        // New calculation menu item
        JMenuItem newItem = new JMenuItem("New Calculation");
        newItem.setMnemonic(KeyEvent.VK_N);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        
        // Save menu item
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        
        // Load menu item
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.setMnemonic(KeyEvent.VK_L);
        loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        
        // Exit menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        
        menu.add(newItem);
        menu.addSeparator();
        menu.add(saveItem);
        menu.add(loadItem);
        menu.addSeparator();
        menu.add(exitItem);
        
        return menu;
    }
    
    // Creates the Help menu with application information
    private JMenu createHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        
        // About menu item
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        
        // User Guide menu item
        JMenuItem guideItem = new JMenuItem("User Guide");
        guideItem.setMnemonic(KeyEvent.VK_G);
        
        menu.add(guideItem);
        menu.addSeparator();
        menu.add(aboutItem);
        
        return menu;
    }
    
    // Sets up the main frame properties
    private void setupFrameProperties() {
        setTitle("CGPA Calculator - Academic Performance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
    }
    
    // Sets up the menu bar for the frame
    private void setupMenuBar() {
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
    
    // Sets up the main layout of the frame with organized component placement
    private void setupMainLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Create main content panel
        JPanel mainContentPanel = createMainContentPanel();
        
        // Add components to frame
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Pack and center the frame
        pack();
        setLocationRelativeTo(null);
    }
    
    // Creates the main content panel with organized layout
    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create top panel for CGPA input and actions
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(currentCGPAPanel, BorderLayout.CENTER);
        topPanel.add(calculationPanel, BorderLayout.SOUTH);
        
        // Create center panel to hold course panel and reference panel
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(coursePanel, BorderLayout.CENTER);
        centerPanel.add(referencePanel, BorderLayout.SOUTH);
        
        // Add components to main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(resultsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Configures frame behavior and final setup
    private void configureFrameBehavior() {
        // Set default button
        getRootPane().setDefaultButton(calculateButton);
        
        // Set initial focus
        SwingUtilities.invokeLater(() -> coursePanel.getCourseNameField().requestFocusInWindow());
    }
    
    // Sets up real-time validation for CGPA and cumulative units input fields
    private void setupRealTimeValidation() {
        // Add document listeners for real-time validation
        currentCGPAField.getDocument().addDocumentListener(new CGPAValidationListener());
        cumulativeUnitsField.getDocument().addDocumentListener(new UnitsValidationListener());
        
        // Add focus listeners for additional validation feedback
        currentCGPAField.addFocusListener(new InputFieldFocusListener(currentCGPAField));
        cumulativeUnitsField.addFocusListener(new InputFieldFocusListener(cumulativeUnitsField));
    }
    
    // Document listener for real-time CGPA validation
    private class CGPAValidationListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validateCGPAInput();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            validateCGPAInput();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            validateCGPAInput();
        }
    }
    
    // Document listener for real-time cumulative units validation
    private class UnitsValidationListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validateUnitsInput();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            validateUnitsInput();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            validateUnitsInput();
        }
    }
    
    // Focus listener for input field visual feedback
    private class InputFieldFocusListener implements FocusListener {
        private final JTextField field;
        private final Color originalBackground;
        
        public InputFieldFocusListener(JTextField field) {
            this.field = field;
            this.originalBackground = field.getBackground();
        }
        
        @Override
        public void focusGained(FocusEvent e) {
            // Highlight field when focused
            field.setBackground(new Color(255, 255, 240)); // Light yellow
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            // Restore original background and perform validation
            field.setBackground(originalBackground);
            validateDependencyConstraints();
        }
    }
    
    // Validates CGPA input and provides visual feedback
    private void validateCGPAInput() {
        String cgpaText = currentCGPAField.getText().trim();
        
        // Clear previous validation state
        clearCGPAValidationFeedback();
        
        if (cgpaText.isEmpty()) {
            // Empty is valid (optional field)
            return;
        }
        
        try {
            double cgpaValue = Double.parseDouble(cgpaText);
            
            if (!ValidationUtils.isValidCGPAValue(cgpaValue)) {
                showCGPAValidationError("CGPA must be between 0.00 and 5.00");
                return;
            }
            
            // Valid CGPA - show success feedback
            showCGPAValidationSuccess();
            
        } catch (NumberFormatException e) {
            showCGPAValidationError("Please enter a valid decimal number");
        }
        
        // Always validate dependency constraints when CGPA changes
        validateDependencyConstraints();
    }
    
    // Validates cumulative units input and provides visual feedback
    private void validateUnitsInput() {
        String unitsText = cumulativeUnitsField.getText().trim();
        
        // Clear previous validation state
        clearUnitsValidationFeedback();
        
        if (unitsText.isEmpty()) {
            // Empty is valid (optional field)
            return;
        }
        
        try {
            int unitsValue = Integer.parseInt(unitsText);
            
            if (!ValidationUtils.isValidCumulativeUnits(unitsValue)) {
                showUnitsValidationError("Cumulative units must be a positive number");
                return;
            }
            
            // Valid units - show success feedback
            showUnitsValidationSuccess();
            
        } catch (NumberFormatException e) {
            showUnitsValidationError("Please enter a valid whole number");
        }
        
        // Always validate dependency constraints when units change
        validateDependencyConstraints();
    }
    
    // Validates dependency constraints between CGPA and cumulative units
    private void validateDependencyConstraints() {
        String cgpaText = currentCGPAField.getText().trim();
        String unitsText = cumulativeUnitsField.getText().trim();
        
        // Clear previous dependency validation
        clearDependencyValidationFeedback();
        
        // If both fields are empty, no validation needed
        if (cgpaText.isEmpty() && unitsText.isEmpty()) {
            return;
        }
        
        try {
            double cgpaValue = cgpaText.isEmpty() ? 0.0 : Double.parseDouble(cgpaText);
            int unitsValue = unitsText.isEmpty() ? 0 : Integer.parseInt(unitsText);
            
            // Check consistency using ValidationUtils
            if (!ValidationUtils.isConsistentAcademicData(cgpaValue, unitsValue)) {
                showDependencyValidationError("If current CGPA is provided, cumulative units must also be provided");
                return;
            }
            
            // Check if cumulative units are sufficient for CGPA calculation
            if (cgpaValue > 0.0 && unitsValue > 0 && !ValidationUtils.hasSufficientCumulativeUnitsForCGPA(unitsValue)) {
                showDependencyValidationWarning("Cumulative units below 24 may affect CGPA calculation accuracy");
                return;
            }
            
            // All dependency constraints satisfied
            showDependencyValidationSuccess();
            
        } catch (NumberFormatException e) {
            // Individual field validation will handle number format errors
            // No need to show dependency error for invalid numbers
        }
    }
    
    // Shows CGPA validation error with visual feedback
    private void showCGPAValidationError(String message) {
        cgpaValidationLabel.setText("✗ " + message);
        cgpaValidationLabel.setForeground(Color.RED);
        currentCGPAField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    // Shows CGPA validation success with visual feedback
    private void showCGPAValidationSuccess() {
        cgpaValidationLabel.setText("✓ Valid CGPA");
        cgpaValidationLabel.setForeground(new Color(0, 128, 0)); // Green
        currentCGPAField.setBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 1));
    }
    
    // Clears CGPA validation feedback
    private void clearCGPAValidationFeedback() {
        cgpaValidationLabel.setText(" ");
        currentCGPAField.setBorder(UIManager.getBorder("TextField.border"));
    }
    
    // Shows units validation error with visual feedback
    private void showUnitsValidationError(String message) {
        unitsValidationLabel.setText("✗ " + message);
        unitsValidationLabel.setForeground(Color.RED);
        cumulativeUnitsField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    // Shows units validation success with visual feedback
    private void showUnitsValidationSuccess() {
        unitsValidationLabel.setText("✓ Valid units");
        unitsValidationLabel.setForeground(new Color(0, 128, 0)); // Green
        cumulativeUnitsField.setBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 1));
    }
    
    // Clears units validation feedback
    private void clearUnitsValidationFeedback() {
        unitsValidationLabel.setText(" ");
        cumulativeUnitsField.setBorder(UIManager.getBorder("TextField.border"));
    }
    
    // Shows dependency validation error with visual feedback
    private void showDependencyValidationError(String message) {
        dependencyValidationLabel.setText("⚠ " + message);
        dependencyValidationLabel.setForeground(Color.RED);
        dependencyValidationLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
    }
    
    // Shows dependency validation warning with visual feedback
    private void showDependencyValidationWarning(String message) {
        dependencyValidationLabel.setText("⚠ " + message);
        dependencyValidationLabel.setForeground(new Color(255, 140, 0)); // Orange
        dependencyValidationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
    }
    
    // Shows dependency validation success with visual feedback
    private void showDependencyValidationSuccess() {
        dependencyValidationLabel.setText("✓ Academic data is consistent");
        dependencyValidationLabel.setForeground(new Color(0, 128, 0)); // Green
        dependencyValidationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
    }
    
    // Clears dependency validation feedback
    private void clearDependencyValidationFeedback() {
        dependencyValidationLabel.setText(" ");
        dependencyValidationLabel.setForeground(Color.BLACK);
        dependencyValidationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
    }
    
    // Public methods for external interaction and event handling
    
    // Adds action listener to the calculate button
    public void addCalculateButtonListener(ActionListener listener) {
        calculateButton.addActionListener(listener);
    }
    
    // Adds action listener to the save data button
    public void addSaveDataButtonListener(ActionListener listener) {
        saveDataButton.addActionListener(listener);
    }
    
    // Adds action listener to the load data button
    public void addLoadDataButtonListener(ActionListener listener) {
        loadDataButton.addActionListener(listener);
    }
    
    // Gets the current CGPA input value
    public String getCurrentCGPAInput() {
        return currentCGPAField.getText().trim();
    }
    
    // Gets the cumulative units input value
    public String getCumulativeUnitsInput() {
        return cumulativeUnitsField.getText().trim();
    }
    
    // Sets the current CGPA field value
    public void setCurrentCGPAInput(String cgpa) {
        currentCGPAField.setText(cgpa);
    }
    
    // Sets the cumulative units field value
    public void setCumulativeUnitsInput(String units) {
        cumulativeUnitsField.setText(units);
    }
    
    // Updates the current GPA display
    public void updateCurrentGPADisplay(double gpa) {
        currentGPALabel.setText(String.format("Current GPA: %.2f", gpa));
    }
    
    // Updates the updated CGPA display
    public void updateCGPADisplay(double cgpa) {
        updatedCGPALabel.setText(String.format("Updated CGPA: %.2f", cgpa));
    }
    
    // Updates the classification display with color coding
    public void updateClassificationDisplay(String classification) {
        classificationLabel.setText("Classification: " + classification);
        
        // Apply color coding based on classification
        Color color = getClassificationColor(classification);
        classificationLabel.setForeground(color);
    }
    
    // Updates the total units display
    public void updateTotalUnitsDisplay(int units) {
        totalUnitsLabel.setText("Total Units: " + units);
    }
    
    // Updates the motivational message display
    public void updateMotivationalMessage(String message) {
        motivationalMessageArea.setText(message);
    }
    
    // Gets the course panel for external access
    public CoursePanel getCoursePanel() {
        return coursePanel;
    }
    
    // Gets the reference panel for external access
    public ReferencePanel getReferencePanel() {
        return referencePanel;
    }
    
    // Clears all result displays
    public void clearResultDisplays() {
        currentGPALabel.setText("Current GPA: --");
        updatedCGPALabel.setText("Updated CGPA: --");
        classificationLabel.setText("Classification: --");
        totalUnitsLabel.setText("Total Units: --");
        motivationalMessageArea.setText("Enter your courses and click Calculate to see your results!");
    }
    
    // Shows an error message dialog
    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    // Shows an information message dialog
    public void showInfoMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Shows a warning message dialog
    public void showWarningMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    // Gets the current CGPA field for external access
    public JTextField getCurrentCGPAField() {
        return currentCGPAField;
    }
    
    // Gets the cumulative units field for external access
    public JTextField getCumulativeUnitsField() {
        return cumulativeUnitsField;
    }
    
    // Gets the calculate button for external access
    public JButton getCalculateButton() {
        return calculateButton;
    }
    
    // Gets the save data button for external access
    public JButton getSaveDataButton() {
        return saveDataButton;
    }
    
    // Gets the load data button for external access
    public JButton getLoadDataButton() {
        return loadDataButton;
    }
    
    // Gets the CGPA validation label for external access
    public JLabel getCgpaValidationLabel() {
        return cgpaValidationLabel;
    }
    
    // Gets the units validation label for external access
    public JLabel getUnitsValidationLabel() {
        return unitsValidationLabel;
    }
    
    // Gets the dependency validation label for external access
    public JLabel getDependencyValidationLabel() {
        return dependencyValidationLabel;
    }
    

    
    // Shows confirmation dialog for destructive actions
    public boolean showConfirmationDialog(String title, String message, String actionText) {
        Object[] options = {actionText, "Cancel"};
        int result = JOptionPane.showOptionDialog(
            this,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[1] // Default to Cancel
        );
        return result == 0;
    }
    
    // Gets the appropriate color for classification display
    private Color getClassificationColor(String classification) {
        switch (classification.toLowerCase()) {
            case "first class":
                return new Color(0, 128, 0); // Green
            case "second class upper":
                return new Color(0, 100, 200); // Blue
            case "second class lower":
                return new Color(255, 140, 0); // Orange
            case "third class":
                return new Color(255, 165, 0); // Orange
            case "fail":
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }
    
    // Public methods for validation
    
    // Validates academic constraints and displays appropriate warnings
    public void validateAcademicConstraints() {
        try {
            // Get current input values
            String cgpaText = getCurrentCGPAInput();
            String unitsText = getCumulativeUnitsInput();
            
            double currentCGPA = cgpaText.isEmpty() ? 0.0 : Double.parseDouble(cgpaText);
            int cumulativeUnits = unitsText.isEmpty() ? 0 : Integer.parseInt(unitsText);
            
            // Get courses from course panel
            List<com.cgpacalculator.model.Course> courses = coursePanel.getAllCourses();
            int totalSemesterUnits = courses.stream().mapToInt(com.cgpacalculator.model.Course::getUnits).sum();
            
            // Perform comprehensive validation
            ValidationResult validation = AcademicConstraintValidator.validateAcademicConstraintsWithDetails(
                totalSemesterUnits, courses, cumulativeUnits, currentCGPA
            );
            
            // Display validation results in course panel
            coursePanel.getValidationFeedbackPanel().displayValidationResults(validation);
            
            // Show warnings in dialog if there are critical errors
            if (validation.hasErrors()) {
                String errorSummary = validation.getAllErrorMessagesAsString();
                if (errorSummary.contains("MAXIMUM UNITS EXCEEDED") || errorSummary.contains("Duplicate")) {
                    showWarningMessage("Academic Constraint Violations", 
                                     "Critical academic constraints have been violated:\n\n" + errorSummary);
                }
            }
            
        } catch (NumberFormatException e) {
            // Handle invalid number format gracefully
            coursePanel.getValidationFeedbackPanel().displayErrorMessage(
                "Invalid input format. Please check CGPA and cumulative units values."
            );
        }
    }
    
    // Validates semester unit constraints and provides feedback
    public void validateSemesterUnits() {
        List<com.cgpacalculator.model.Course> courses = coursePanel.getAllCourses();
        int totalSemesterUnits = courses.stream().mapToInt(com.cgpacalculator.model.Course::getUnits).sum();
        
        ValidationResult validation = AcademicConstraintValidator.validateSemesterUnitConstraints(totalSemesterUnits);
        coursePanel.getValidationFeedbackPanel().displaySemesterUnitValidation(totalSemesterUnits);
    }
    
    // Validates if a course can be added without violating constraints
    public boolean canAddCourse(com.cgpacalculator.model.Course course) {
        List<com.cgpacalculator.model.Course> existingCourses = coursePanel.getAllCourses();
        
        // Check for duplicate course names
        ValidationResult duplicateValidation = AcademicConstraintValidator.validateDuplicateCourseNames(
            course.getCourseName(), existingCourses
        );
        
        if (duplicateValidation.hasErrors()) {
            coursePanel.getValidationFeedbackPanel().displayDuplicateCourseWarning(course.getCourseName());
            return false;
        }
        
        // Check course load violations
        int currentUnits = existingCourses.stream().mapToInt(com.cgpacalculator.model.Course::getUnits).sum();
        ValidationResult loadValidation = AcademicConstraintValidator.validateCourseLoadViolations(
            currentUnits, course.getUnits()
        );
        
        if (loadValidation.hasFieldError("Course Load Warning")) {
            coursePanel.getValidationFeedbackPanel().displayCourseLoadViolations(currentUnits, course.getUnits());
            // Allow addition but show warning
            return true;
        }
        
        return true;
    }
    
    // Displays cumulative unit warnings for CGPA calculation
    public void validateCumulativeUnitsForCGPA() {
        try {
            String cgpaText = getCurrentCGPAInput();
            String unitsText = getCumulativeUnitsInput();
            
            if (!cgpaText.isEmpty() && !unitsText.isEmpty()) {
                double currentCGPA = Double.parseDouble(cgpaText);
                int cumulativeUnits = Integer.parseInt(unitsText);
                
                ValidationResult validation = AcademicConstraintValidator.validateCumulativeUnitWarnings(
                    cumulativeUnits, currentCGPA
                );
                
                if (validation.hasErrors()) {
                    coursePanel.getValidationFeedbackPanel().displayCumulativeUnitWarnings(cumulativeUnits, currentCGPA);
                }
            }
        } catch (NumberFormatException e) {
            // Handle invalid input gracefully
        }
    }
    
    // Gets academic constraint summary for display
    public String getAcademicConstraintSummary() {
        try {
            // Create a temporary student object for validation
            com.cgpacalculator.model.Student tempStudent = new com.cgpacalculator.model.Student();
            
            String cgpaText = getCurrentCGPAInput();
            String unitsText = getCumulativeUnitsInput();
            
            if (!cgpaText.isEmpty()) {
                tempStudent.setCurrentCGPA(Double.parseDouble(cgpaText));
            }
            if (!unitsText.isEmpty()) {
                tempStudent.setCumulativeUnits(Integer.parseInt(unitsText));
            }
            
            // Add courses
            List<com.cgpacalculator.model.Course> courses = coursePanel.getAllCourses();
            for (com.cgpacalculator.model.Course course : courses) {
                tempStudent.addCourse(course);
            }
            
            return AcademicConstraintValidator.getAcademicConstraintSummary(tempStudent);
            
        } catch (Exception e) {
            return "Unable to generate constraint summary due to invalid input";
        }
    }
    
    // Checks if GPA calculation is allowed based on academic constraints
    public boolean isGPACalculationAllowed() {
        try {
            // Create a temporary student object for validation
            com.cgpacalculator.model.Student tempStudent = new com.cgpacalculator.model.Student();
            
            String cgpaText = getCurrentCGPAInput();
            String unitsText = getCumulativeUnitsInput();
            
            if (!cgpaText.isEmpty()) {
                tempStudent.setCurrentCGPA(Double.parseDouble(cgpaText));
            }
            if (!unitsText.isEmpty()) {
                tempStudent.setCumulativeUnits(Integer.parseInt(unitsText));
            }
            
            // Add courses
            List<com.cgpacalculator.model.Course> courses = coursePanel.getAllCourses();
            for (com.cgpacalculator.model.Course course : courses) {
                tempStudent.addCourse(course);
            }
            
            return AcademicConstraintValidator.isGPACalculationAllowed(tempStudent);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    // Validates all input fields and returns true if all are valid
    public boolean validateAllInputs() {
        validateCGPAInput();
        validateUnitsInput();
        validateDependencyConstraints();
        
        // Check if any validation errors are present
        return !cgpaValidationLabel.getText().contains("✗") && 
               !unitsValidationLabel.getText().contains("✗") && 
               !dependencyValidationLabel.getText().contains("✗");
    }
    
    // Clears all validation feedback and resets input field borders
    public void clearAllValidationFeedback() {
        clearCGPAValidationFeedback();
        clearUnitsValidationFeedback();
        clearDependencyValidationFeedback();
    }
    
    // Gets the parsed CGPA value, returns 0.0 if empty or invalid
    public double getParsedCGPAValue() {
        String cgpaText = currentCGPAField.getText().trim();
        if (cgpaText.isEmpty()) {
            return 0.0;
        }
        
        try {
            double value = Double.parseDouble(cgpaText);
            return ValidationUtils.isValidCGPAValue(value) ? value : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    // Gets the parsed cumulative units value, returns 0 if empty or invalid
    public int getParsedCumulativeUnitsValue() {
        String unitsText = cumulativeUnitsField.getText().trim();
        if (unitsText.isEmpty()) {
            return 0;
        }
        
        try {
            int value = Integer.parseInt(unitsText);
            return ValidationUtils.isValidCumulativeUnits(value) ? value : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    // Checks if current CGPA and cumulative units inputs are consistent
    public boolean areInputsConsistent() {
        double cgpa = getParsedCGPAValue();
        int units = getParsedCumulativeUnitsValue();
        return ValidationUtils.isConsistentAcademicData(cgpa, units);
    }
    
    // Forces validation of all input fields (useful for external triggers)
    public void forceValidation() {
        SwingUtilities.invokeLater(() -> {
            validateCGPAInput();
            validateUnitsInput();
            validateDependencyConstraints();
        });
    }
    
    // Sets up enhanced features including keyboard shortcuts, tooltips, and accessibility
    private void setupEnhancedFeatures() {
        // Apply application theme and styling
        UIStyleManager.applyApplicationTheme();
        
        // Set up keyboard shortcuts
        KeyboardShortcutManager.setupMainFrameShortcuts(this);
        
        // Set up enhanced keyboard navigation
        new EnhancedKeyboardNavigator(this);
        
        // Set up enhanced tooltips
        TooltipManager.setupMainFrameTooltips(this);
        
        // Set up accessibility features
        AccessibilityManager.setupMainFrameAccessibility(this);
        
        // Set up enhanced validation for input fields
        setupEnhancedInputValidation();
        
        // Apply visual styling to components
        applyAdvancedStyling();
        
        // Set up context menus for input fields and components
        setupContextMenus();
        
        // Set up drag and drop functionality
        setupDragAndDrop();
    }
    
    // Sets up enhanced input validation with visual feedback
    private void setupEnhancedInputValidation() {
        // Set up validation for CGPA field
        InputValidationManager.setupTextFieldValidation(
            currentCGPAField, cgpaValidationLabel, InputValidationManager.ValidationType.CGPA);
        
        // Set up validation for cumulative units field
        InputValidationManager.setupTextFieldValidation(
            cumulativeUnitsField, unitsValidationLabel, InputValidationManager.ValidationType.CUMULATIVE_UNITS);
    }
    
    // Applies advanced styling to all components using the UI Style Manager
    private void applyAdvancedStyling() {
        // Apply styling to main panels
        UIStyleManager.stylePanel(currentCGPAPanel, null);
        UIStyleManager.stylePanel(calculationPanel, null);
        UIStyleManager.stylePanel(resultsPanel, null);
        
        // Apply button styling
        UIStyleManager.stylePrimaryButton(calculateButton);
        UIStyleManager.styleSecondaryButton(saveDataButton);
        UIStyleManager.styleSecondaryButton(loadDataButton);
        
        // Apply input field styling
        UIStyleManager.styleInputField(currentCGPAField);
        UIStyleManager.styleInputField(cumulativeUnitsField);
        
        // Apply label styling
        UIStyleManager.styleLabel(currentGPALabel, UIStyleManager.LabelType.BODY);
        UIStyleManager.styleLabel(updatedCGPALabel, UIStyleManager.LabelType.BODY);
        UIStyleManager.styleLabel(classificationLabel, UIStyleManager.LabelType.BODY);
        UIStyleManager.styleLabel(totalUnitsLabel, UIStyleManager.LabelType.BODY);
        
        // Apply validation label styling
        UIStyleManager.styleLabel(cgpaValidationLabel, UIStyleManager.LabelType.SMALL);
        UIStyleManager.styleLabel(unitsValidationLabel, UIStyleManager.LabelType.SMALL);
        UIStyleManager.styleLabel(dependencyValidationLabel, UIStyleManager.LabelType.SMALL);
    }
    
    // Sets up context menus for input fields and interactive components
    private void setupContextMenus() {
        // Set up context menus for input fields
        ContextMenuManager.setupInputFieldContextMenu(currentCGPAField);
        ContextMenuManager.setupInputFieldContextMenu(cumulativeUnitsField);
        
        // Set up context menu for course table (handled in CoursePanel)
        if (coursePanel.getCourseTable() != null) {
            ContextMenuManager.setupCourseTableContextMenu(
                coursePanel.getCourseTable(), 
                coursePanel.getTableModel()
            );
        }
    }
    
    // Shows confirmation dialog for destructive actions using the enhanced dialog manager
    public boolean showEnhancedConfirmationDialog(String action, Object... params) {
        switch (action) {
            case "clear_all":
                int courseCount = params.length > 0 ? (Integer) params[0] : 0;
                return ConfirmationDialogManager.confirmClearAllCourses(this, courseCount);
            case "remove_course":
                String courseName = params.length > 0 ? (String) params[0] : "selected course";
                return ConfirmationDialogManager.confirmRemoveCourse(this, courseName);
            case "load_data":
                boolean hasUnsavedChanges = params.length > 0 ? (Boolean) params[0] : false;
                return ConfirmationDialogManager.confirmDataLoad(this, hasUnsavedChanges);
            case "exit_application":
                return ConfirmationDialogManager.confirmExitWithUnsavedChanges(this) != 2;
            default:
                return showConfirmationDialog("Confirm Action", 
                    "Are you sure you want to perform this action?", "Proceed");
        }
    }
    
    // Sets up drag and drop functionality for the course table
    public void setupDragAndDrop() {
        if (coursePanel.getCourseTable() != null) {
            CourseTransferHandler transferHandler = new CourseTransferHandler(coursePanel.getTableModel());
            coursePanel.getCourseTable().setTransferHandler(transferHandler);
            coursePanel.getCourseTable().setDragEnabled(true);
            coursePanel.getCourseTable().setDropMode(DropMode.INSERT_ROWS);
        }
    }
    
    // Gets the enhanced keyboard navigator for external access
    public EnhancedKeyboardNavigator getKeyboardNavigator() {
        // The navigator is created in setupEnhancedFeatures, but we need to store a reference
        // This is a placeholder - in a full implementation, you'd store the navigator as a field
        return new EnhancedKeyboardNavigator(this);
    }

}