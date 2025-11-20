package com.cgpacalculator.view;

import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.Constants;
import com.cgpacalculator.utils.GradeConverter;
import com.cgpacalculator.utils.AcademicConstraintValidator;
import com.cgpacalculator.utils.ValidationResult;
import com.cgpacalculator.view.ValidationFeedbackPanel;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;public class CoursePanel extends JPanel {
    
    // Encapsulated UI components
    private final JTable courseTable;
    private final CourseTableModel tableModel;
    private final JScrollPane tableScrollPane;
    
    // Input components for new course entry
    private final JTextField courseNameField;
    private final JSpinner unitsSpinner;
    private final JComboBox<String> gradeComboBox;
    
    // Action buttons with descriptive names
    private final JButton addCourseButton;
    private final JButton removeCourseButton;
    private final JButton clearAllButton;
    
    // Layout panels for organized structure
    private final JPanel inputPanel;
    private final JPanel buttonPanel;
    private final JPanel tablePanel;
    
    // Validation feedback components
    private final ValidationFeedbackPanel validationFeedbackPanel;
    private final JLabel courseNameValidationLabel;
    private final JLabel unitsValidationLabel;
    
    // Constructor initializes all components and sets up the layout
    public CoursePanel() {
        // Initialize components
        this.tableModel = new CourseTableModel();
        this.courseTable = createCourseTable();
        this.tableScrollPane = createTableScrollPane();
        
        this.courseNameField = createCourseNameField();
        this.unitsSpinner = createUnitsSpinner();
        this.gradeComboBox = createGradeComboBox();
        
        this.addCourseButton = createAddCourseButton();
        this.removeCourseButton = createRemoveCourseButton();
        this.clearAllButton = createClearAllButton();
        
        // Initialize validation components
        this.validationFeedbackPanel = new ValidationFeedbackPanel();
        this.courseNameValidationLabel = createValidationLabel();
        this.unitsValidationLabel = createValidationLabel();
        
        // Initialize layout panels
        this.inputPanel = createInputPanel();
        this.buttonPanel = createButtonPanel();
        this.tablePanel = createTablePanel();
        
        // Set up the main layout
        setupMainLayout();
        
        // Configure panel properties
        configurePanelProperties();
        
        // Setup real-time validation
        setupRealTimeValidation();
        
        // Setup enhanced features
        setupEnhancedFeatures();
    }
    
    // Sets up enhanced features for the course panel
    private void setupEnhancedFeatures() {
        // Set up enhanced tooltips
        TooltipManager.setupCoursePanelTooltips(this);
        
        // Set up keyboard shortcuts
        KeyboardShortcutManager.setupCoursePanelShortcuts(this);
        
        // Set up accessibility features
        AccessibilityManager.setupCoursePanelAccessibility(this);
        
        // Set up drag and drop functionality
        setupDragAndDrop();
        
        // Set up context menus
        setupContextMenus();
        
        // Apply advanced styling
        applyAdvancedStyling();
        AccessibilityManager.setupCoursePanelAccessibility(this);
        
        // Set up drag-and-drop functionality
        setupDragAndDrop();
        
        // Set up context menus
        setupContextMenus();
        
        // Apply advanced styling
        applyAdvancedStyling();
    }
    
    // Creates and configures the course table with proper settings
    private JTable createCourseTable() {
        JTable table = new JTable(tableModel);
        
        // Configure table appearance and behavior
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths for better display
        configureTableColumnWidths(table);
        
        return table;
    }
    
    // Configures optimal column widths for the course table
    private void configureTableColumnWidths(JTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // Course Name
        table.getColumnModel().getColumn(1).setPreferredWidth(60);  // Units
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Grade
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Grade Points
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Credit Points
    }
    
    // Creates the scroll pane for the table with proper configuration
    private JScrollPane createTableScrollPane() {
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setPreferredSize(new Dimension(520, 200));
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        return scrollPane;
    }
    
    // Creates the course name input field with validation
    private JTextField createCourseNameField() {
        JTextField field = new JTextField(20);
        field.setToolTipText("Enter course name (1-50 characters)");
        field.setName("courseNameField"); // For testing and identification
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
    
    // Creates the units spinner with appropriate range and validation
    private JSpinner createUnitsSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 1, 6, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setToolTipText("Select course units (1-6)");
        
        // Make spinner editor non-editable to prevent invalid input
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setEditable(false);
        
        return spinner;
    }
    
    // Creates the grade selection combo box with all valid grades
    private JComboBox<String> createGradeComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(Constants.VALID_GRADES);
        comboBox.setSelectedIndex(0); // Default to 'A'
        comboBox.setToolTipText("Select letter grade");
        return comboBox;
    }
    
    // Creates the add course button with descriptive properties
    private JButton createAddCourseButton() {
        ColoredButton button = ColoredButton.createPrimaryButton("Add Course");
        button.setToolTipText("Add the entered course to the list");
        button.setMnemonic('A');
        return button;
    }
    
    // Creates the remove course button with descriptive properties
    private JButton createRemoveCourseButton() {
        ColoredButton button = ColoredButton.createSecondaryButton("Remove Selected");
        button.setToolTipText("Remove the selected course from the list");
        button.setMnemonic('R');
        return button;
    }
    
    // Creates the clear all button with descriptive properties
    private JButton createClearAllButton() {
        ColoredButton button = ColoredButton.createDangerButton("Clear All");
        button.setToolTipText("Remove all courses from the list");
        button.setMnemonic('C');
        return button;
    }
    
    // Creates the input panel with organized layout for course entry
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Course Entry"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Course Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        panel.add(courseNameField, gbc);
        
        // Course Name Validation
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(courseNameValidationLabel, gbc);
        
        // Units
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Units:"), gbc);
        gbc.gridx = 1;
        panel.add(unitsSpinner, gbc);
        
        // Units Validation
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(unitsValidationLabel, gbc);
        
        // Grade
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 1;
        panel.add(gradeComboBox, gbc);
        
        return panel;
    }
    
    // Creates the button panel with organized layout for actions
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        panel.add(addCourseButton);
        panel.add(removeCourseButton);
        panel.add(clearAllButton);
        
        return panel;
    }
    
    // Creates the table panel with proper layout and border
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Course List"));
        panel.add(tableScrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    // Sets up the main layout of the course panel
    private void setupMainLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Create top panel for input and buttons
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(validationFeedbackPanel, BorderLayout.SOUTH);
    }
    
    // Configures general panel properties
    private void configurePanelProperties() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(600, 400));
    }
    
    // Public methods for external interaction
    
    // Adds an action listener to the add course button
    public void addAddCourseListener(ActionListener listener) {
        addCourseButton.addActionListener(listener);
    }
    
    // Adds an action listener to the remove course button
    public void addRemoveCourseListener(ActionListener listener) {
        removeCourseButton.addActionListener(listener);
    }
    
    // Adds an action listener to the clear all button
    public void addClearAllListener(ActionListener listener) {
        clearAllButton.addActionListener(listener);
    }
    
    // Gets the currently entered course data from input fields
    public Course getCurrentCourseInput() {
        String courseName = courseNameField.getText().trim();
        int units = (Integer) unitsSpinner.getValue();
        String grade = (String) gradeComboBox.getSelectedItem();
        
        return new Course(courseName, units, grade);
    }
    
    // Clears all input fields
    public void clearInputFields() {
        courseNameField.setText("");
        unitsSpinner.setValue(3);
        gradeComboBox.setSelectedIndex(0);
    }
    
    // Gets the index of the currently selected row in the table
    public int getSelectedCourseIndex() {
        return courseTable.getSelectedRow();
    }
    
    // Gets all courses from the table model
    public List<Course> getAllCourses() {
        return tableModel.getAllCourses();
    }
    
    // Adds a course to the table
    public void addCourseToTable(Course course) {
        tableModel.addCourse(course);
    }
    
    // Removes a course from the table at the specified index
    public void removeCourseFromTable(int index) {
        tableModel.removeCourse(index);
    }
    
    // Clears all courses from the table
    public void clearAllCourses() {
        tableModel.clearAllCourses();
    }
    
    // Sets the courses in the table (for loading data)
    public void setCourses(List<Course> courses) {
        tableModel.setCourses(courses);
    }
    
    // Validates the current input fields
    public boolean isCurrentInputValid() {
        String courseName = courseNameField.getText().trim();
        return !courseName.isEmpty() && courseName.length() <= 50;
    }
    
    // Gets the course name field for external validation
    public JTextField getCourseNameField() {
        return courseNameField;
    }
    
    // Gets the units spinner for external validation
    public JSpinner getUnitsSpinner() {
        return unitsSpinner;
    }
    
    // Gets the grade combo box for external validation
    public JComboBox<String> getGradeComboBox() {
        return gradeComboBox;
    }
    
    // Adds a listener for course data changes (for real-time updates)
    public void addCourseChangeListener(ActionListener listener) {
        // Add listeners to input fields for real-time updates
        courseNameField.addActionListener(listener);
        unitsSpinner.addChangeListener(e -> listener.actionPerformed(null));
        gradeComboBox.addActionListener(listener);
    }
    
    // Adds a listener for course addition events
    public void addCourseAddListener(ActionListener listener) {
        addCourseButton.addActionListener(listener);
    }
    
    // Adds a listener for course removal events
    public void addCourseRemoveListener(ActionListener listener) {
        removeCourseButton.addActionListener(listener);
    }
    
    // Sets up real-time validation for course input fields
    private void setupRealTimeValidation() {
        // Add document listeners for real-time validation
        courseNameField.getDocument().addDocumentListener(new CourseNameValidationListener());
        
        // Add focus listeners for additional validation feedback
        courseNameField.addFocusListener(new CourseInputFocusListener(courseNameField));
        
        // Add change listener for units spinner
        unitsSpinner.addChangeListener(e -> validateUnitsInput());
        
        // Add action listener for grade combo box
        gradeComboBox.addActionListener(e -> validateCurrentCourseInput());
    }
    
    // Document listener for real-time course name validation
    private class CourseNameValidationListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validateCourseNameInput();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            validateCourseNameInput();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            validateCourseNameInput();
        }
    }
    
    // Focus listener for course input field visual feedback
    private class CourseInputFocusListener implements FocusListener {
        private final JTextField field;
        private final Color originalBackground;
        
        public CourseInputFocusListener(JTextField field) {
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
            validateCurrentCourseInput();
        }
    }
    
    // Validates course name input and provides visual feedback
    private void validateCourseNameInput() {
        String courseName = courseNameField.getText().trim();
        
        // Clear previous validation state
        clearCourseNameValidationFeedback();
        
        if (courseName.isEmpty()) {
            // Empty is invalid for course name
            showCourseNameValidationError("Course name is required");
            return;
        }
        
        if (courseName.length() < Constants.MIN_COURSE_NAME_LENGTH || 
            courseName.length() > Constants.MAX_COURSE_NAME_LENGTH) {
            showCourseNameValidationError(
                String.format("Course name must be %d-%d characters", 
                            Constants.MIN_COURSE_NAME_LENGTH, Constants.MAX_COURSE_NAME_LENGTH)
            );
            return;
        }
        
        // Check for duplicate course names
        ValidationResult duplicateValidation = AcademicConstraintValidator.validateDuplicateCourseNames(
            courseName, getAllCourses()
        );
        
        if (duplicateValidation.hasErrors()) {
            showCourseNameValidationError("Course name already exists");
            return;
        }
        
        // Valid course name - show success feedback
        showCourseNameValidationSuccess();
    }
    
    // Validates units input and provides visual feedback
    private void validateUnitsInput() {
        int units = (Integer) unitsSpinner.getValue();
        String courseName = courseNameField.getText().trim();
        
        // Clear previous validation state
        clearUnitsValidationFeedback();
        
        ValidationResult unitsValidation = AcademicConstraintValidator.validateCourseUnitConstraints(
            courseName.isEmpty() ? "Current Course" : courseName, units
        );
        
        if (unitsValidation.hasErrors()) {
            showUnitsValidationError("Units must be between 1-6");
        } else {
            showUnitsValidationSuccess();
        }
        
        // Update semester unit validation
        updateSemesterUnitValidation();
    }
    
    // Validates current course input comprehensively
    private void validateCurrentCourseInput() {
        validateCourseNameInput();
        validateUnitsInput();
        updateSemesterUnitValidation();
    }
    
    // Updates semester unit validation based on current courses
    private void updateSemesterUnitValidation() {
        List<Course> currentCourses = getAllCourses();
        int totalUnits = currentCourses.stream().mapToInt(Course::getUnits).sum();
        
        // Add current input if valid
        if (isCurrentInputValid()) {
            totalUnits += (Integer) unitsSpinner.getValue();
        }
        
        ValidationResult semesterValidation = AcademicConstraintValidator.validateSemesterUnitConstraints(totalUnits);
        validationFeedbackPanel.displayValidationResults(semesterValidation);
    }
    
    // Shows course name validation error with visual feedback
    private void showCourseNameValidationError(String message) {
        courseNameValidationLabel.setText("✗ " + message);
        courseNameValidationLabel.setForeground(Color.RED);
        courseNameField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    // Shows course name validation success with visual feedback
    private void showCourseNameValidationSuccess() {
        courseNameValidationLabel.setText("✓ Valid course name");
        courseNameValidationLabel.setForeground(new Color(0, 128, 0)); // Green
        courseNameField.setBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 1));
    }
    
    // Clears course name validation feedback
    private void clearCourseNameValidationFeedback() {
        courseNameValidationLabel.setText(" ");
        courseNameField.setBorder(UIManager.getBorder("TextField.border"));
    }
    
    // Shows units validation error with visual feedback
    private void showUnitsValidationError(String message) {
        unitsValidationLabel.setText("✗ " + message);
        unitsValidationLabel.setForeground(Color.RED);
    }
    
    // Shows units validation success with visual feedback
    private void showUnitsValidationSuccess() {
        unitsValidationLabel.setText("✓ Valid units");
        unitsValidationLabel.setForeground(new Color(0, 128, 0)); // Green
    }
    
    // Clears units validation feedback
    private void clearUnitsValidationFeedback() {
        unitsValidationLabel.setText(" ");
    }
    
    // Gets the validation feedback panel for external access
    public ValidationFeedbackPanel getValidationFeedbackPanel() {
        return validationFeedbackPanel;
    }
    
    // Gets the add course button for external access
    public JButton getAddCourseButton() {
        return addCourseButton;
    }
    
    // Gets the remove course button for external access
    public JButton getRemoveCourseButton() {
        return removeCourseButton;
    }
    
    // Gets the clear all button for external access
    public JButton getClearAllButton() {
        return clearAllButton;
    }
    
    // Gets the course table for external access
    public JTable getCourseTable() {
        return courseTable;
    }
    
    // Shows confirmation dialog before removing a course
    public boolean confirmRemoveCourse(String courseName) {
        return ConfirmationDialogManager.confirmRemoveCourse(this, courseName);
    }
    
    // Shows confirmation dialog before clearing all courses
    public boolean confirmClearAllCourses(int courseCount) {
        return ConfirmationDialogManager.confirmClearAllCourses(this, courseCount);
    }
    
    // Enables or disables the remove course button based on selection
    public void updateRemoveButtonState() {
        boolean hasSelection = courseTable.getSelectedRow() >= 0;
        removeCourseButton.setEnabled(hasSelection);
    }
    
    // Enables or disables the clear all button based on course count
    public void updateClearAllButtonState() {
        boolean hasCourses = tableModel.getRowCount() > 0;
        clearAllButton.setEnabled(hasCourses);
    }
    
    // Sets up enhanced tooltips for all components
    public void setupEnhancedTooltips() {
        TooltipManager.setupCoursePanelTooltips(this);
    }
    
    // Sets up keyboard shortcuts for course panel actions
    public void setupKeyboardShortcuts() {
        KeyboardShortcutManager.setupCoursePanelShortcuts(this);
    }
    
    // Validates if a course can be added without violating constraints
    public ValidationResult validateCourseAddition(Course course) {
        return AcademicConstraintValidator.validateCourseAddition(null, course); // Student will be provided by controller
    }
    
    // Displays comprehensive validation feedback for all courses
    public void displayComprehensiveValidation(List<Course> allCourses, int totalUnits) {
        ValidationResult comprehensiveValidation = AcademicConstraintValidator.validateAcademicConstraintsWithDetails(
            totalUnits, allCourses, 0, 0.0 // CGPA details will be provided by controller
        );
        
        validationFeedbackPanel.displayValidationResults(comprehensiveValidation);
    }
    
    // Clears all validation feedback
    public void clearAllValidationFeedback() {
        clearCourseNameValidationFeedback();
        clearUnitsValidationFeedback();
        validationFeedbackPanel.clearAllValidationMessages();
    }
    
    // Sets up drag-and-drop functionality for the course table
    private void setupDragAndDrop() {
        // Enable drag and drop on the course table
        courseTable.setDragEnabled(true);
        courseTable.setDropMode(DropMode.INSERT_ROWS);
        
        // Set up the custom transfer handler
        CourseTransferHandler transferHandler = new CourseTransferHandler(tableModel);
        courseTable.setTransferHandler(transferHandler);
        
        // Enable selection of rows during drag operations
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateRemoveButtonState();
            }
        });
    }
    
    // Sets up context menus for the course table and input fields
    private void setupContextMenus() {
        // Set up context menu for the course table
        ContextMenuManager.setupCourseTableContextMenu(courseTable, tableModel);
        
        // Set up context menus for input fields
        ContextMenuManager.setupInputFieldContextMenu(courseNameField);
    }
    
    // Applies advanced styling to all components in the course panel
    private void applyAdvancedStyling() {
        // Style the main panel
        UIStyleManager.stylePanel(this, "Course Management");
        
        // Style input fields
        UIStyleManager.styleInputField(courseNameField);
        
        // Style buttons
        UIStyleManager.stylePrimaryButton(addCourseButton);
        UIStyleManager.styleSecondaryButton(removeCourseButton);
        UIStyleManager.styleDangerButton(clearAllButton);
        
        // Style the table
        UIStyleManager.styleTable(courseTable);
        
        // Style the table scroll pane
        tableScrollPane.setBorder(UIStyleManager.PANEL_BORDER);
        
        // Style input and button panels
        UIStyleManager.stylePanel(inputPanel, null);
        UIStyleManager.stylePanel(buttonPanel, null);
        UIStyleManager.stylePanel(tablePanel, null);
        
        // Style validation labels
        UIStyleManager.styleLabel(courseNameValidationLabel, UIStyleManager.LabelType.ERROR);
        UIStyleManager.styleLabel(unitsValidationLabel, UIStyleManager.LabelType.ERROR);
    }
    
    // Gets the table model for external access (needed by advanced UI features)
    public CourseTableModel getTableModel() {
        return tableModel;
    }
}
