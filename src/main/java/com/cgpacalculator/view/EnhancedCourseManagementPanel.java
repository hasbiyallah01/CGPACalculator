package com.cgpacalculator.view;

import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.Constants;
import com.cgpacalculator.utils.CGPACalculationValidator;
import com.cgpacalculator.utils.ValidationResult;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced course management panel with better visibility and editing capabilities
 */
public class EnhancedCourseManagementPanel extends JPanel {
    
    private final DefaultTableModel tableModel;
    private final JTable courseTable;
    private final JScrollPane tableScrollPane;
    
    // Input components
    private final JTextField courseNameField;
    private final JSpinner unitsSpinner;
    private final JComboBox<String> gradeComboBox;
    
    // Action buttons
    private final JButton addCourseButton;
    private final JButton updateCourseButton;
    private final JButton removeCourseButton;
    private final JButton clearAllButton;
    
    // Status components
    private final JLabel statusLabel;
    private final JLabel totalUnitsLabel;
    private final JLabel courseCountLabel;
    private final JProgressBar unitsProgressBar;
    
    // Validation feedback
    private final JTextArea validationArea;
    
    public EnhancedCourseManagementPanel() {
        // Initialize table model with column names
        String[] columnNames = {"Course Name", "Units", "Grade", "Grade Points", "Credit Points"};
        this.tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column < 3; // Only first 3 columns are editable
            }
        };
        
        // Initialize components
        this.courseTable = createCourseTable();
        this.tableScrollPane = createTableScrollPane();
        
        this.courseNameField = createCourseNameField();
        this.unitsSpinner = createUnitsSpinner();
        this.gradeComboBox = createGradeComboBox();
        
        this.addCourseButton = createButton("Add Course", "Add new course to the list");
        this.updateCourseButton = createButton("Update Course", "Update selected course");
        this.removeCourseButton = createButton("Remove Course", "Remove selected course");
        this.clearAllButton = createButton("Clear All", "Remove all courses");
        
        this.statusLabel = new JLabel("No courses added yet");
        this.totalUnitsLabel = new JLabel("Total Units: 0");
        this.courseCountLabel = new JLabel("Courses: 0");
        this.unitsProgressBar = createUnitsProgressBar();
        
        this.validationArea = createValidationArea();
        
        setupLayout();
        setupEventHandlers();
        updateButtonStates();
        updateStatus();
    }
    
    private JTable createCourseTable() {
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // Course Name
        table.getColumnModel().getColumn(1).setPreferredWidth(60);  // Units
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Grade
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Grade Points
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Credit Points
        
        return table;
    }
    
    private JScrollPane createTableScrollPane() {
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Course List"));
        return scrollPane;
    }
    
    private JTextField createCourseNameField() {
        JTextField field = new JTextField(20);
        field.setToolTipText("Enter course name (1-50 characters)");
        return field;
    }
    
    private JSpinner createUnitsSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(3, 1, 6, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setToolTipText("Select course units (1-6)");
        return spinner;
    }
    
    private JComboBox<String> createGradeComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(Constants.VALID_GRADES);
        comboBox.setSelectedIndex(0);
        comboBox.setToolTipText("Select letter grade");
        return comboBox;
    }
    
    private JButton createButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        return button;
    }
    
    private JProgressBar createUnitsProgressBar() {
        JProgressBar progressBar = new JProgressBar(0, Constants.MAX_SEMESTER_UNITS);
        progressBar.setStringPainted(true);
        progressBar.setString("0 / 24 units");
        return progressBar;
    }
    
    private JTextArea createValidationArea() {
        JTextArea area = new JTextArea(3, 40);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setBackground(getBackground());
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        area.setText("Add courses between 18-24 total units to calculate CGPA");
        return area;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Course Management"));
        
        // Input panel
        JPanel inputPanel = createInputPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Status panel
        JPanel statusPanel = createStatusPanel();
        
        // Validation panel
        JPanel validationPanel = new JPanel(new BorderLayout());
        validationPanel.setBorder(BorderFactory.createTitledBorder("Status & Validation"));
        validationPanel.add(new JScrollPane(validationArea), BorderLayout.CENTER);
        
        // Top panel (input + buttons)
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Bottom panel (status + validation)
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(statusPanel, BorderLayout.NORTH);
        bottomPanel.add(validationPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Course Entry"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Course Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        panel.add(courseNameField, gbc);
        
        // Units
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Units:"), gbc);
        gbc.gridx = 3;
        panel.add(unitsSpinner, gbc);
        
        // Grade
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 5;
        panel.add(gradeComboBox, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        panel.add(addCourseButton);
        panel.add(updateCourseButton);
        panel.add(removeCourseButton);
        panel.add(clearAllButton);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Course Summary"));
        
        panel.add(courseCountLabel);
        panel.add(totalUnitsLabel);
        panel.add(statusLabel);
        panel.add(unitsProgressBar);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Add course button
        addCourseButton.addActionListener(e -> addCourse());
        
        // Update course button
        updateCourseButton.addActionListener(e -> updateSelectedCourse());
        
        // Remove course button
        removeCourseButton.addActionListener(e -> removeSelectedCourse());
        
        // Clear all button
        clearAllButton.addActionListener(e -> clearAllCourses());
        
        // Table selection listener
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCourseToInputs();
                updateButtonStates();
            }
        });
        
        // Table model listener for updates
        tableModel.addTableModelListener(e -> {
            updateStatus();
            updateValidation();
        });
    }
    
    private void addCourse() {
        String courseName = courseNameField.getText().trim();
        int units = (Integer) unitsSpinner.getValue();
        String grade = (String) gradeComboBox.getSelectedItem();
        
        // Validate input
        if (courseName.isEmpty()) {
            showError("Course name is required");
            return;
        }
        
        if (courseName.length() > Constants.MAX_COURSE_NAME_LENGTH) {
            showError("Course name too long (max " + Constants.MAX_COURSE_NAME_LENGTH + " characters)");
            return;
        }
        
        // Check for duplicates
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (courseName.equalsIgnoreCase((String) tableModel.getValueAt(i, 0))) {
                showError("Course name already exists");
                return;
            }
        }
        
        // Check unit limits
        int currentTotal = getTotalUnits();
        if (currentTotal + units > Constants.MAX_SEMESTER_UNITS) {
            showError("Adding this course would exceed maximum units (" + Constants.MAX_SEMESTER_UNITS + ")");
            return;
        }
        
        // Add course to table
        Course course = new Course(courseName, units, grade);
        addCourseToTable(course);
        
        // Clear inputs
        clearInputs();
        
        showSuccess("Course added successfully");
    }
    
    private void updateSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a course to update");
            return;
        }
        
        String courseName = courseNameField.getText().trim();
        int units = (Integer) unitsSpinner.getValue();
        String grade = (String) gradeComboBox.getSelectedItem();
        
        if (courseName.isEmpty()) {
            showError("Course name is required");
            return;
        }
        
        // Check for duplicates (excluding current row)
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (i != selectedRow && courseName.equalsIgnoreCase((String) tableModel.getValueAt(i, 0))) {
                showError("Course name already exists");
                return;
            }
        }
        
        // Update table
        Course course = new Course(courseName, units, grade);
        updateCourseInTable(selectedRow, course);
        
        showSuccess("Course updated successfully");
    }
    
    private void removeSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a course to remove");
            return;
        }
        
        String courseName = (String) tableModel.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove course: " + courseName + "?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            clearInputs();
            showSuccess("Course removed successfully");
        }
    }
    
    private void clearAllCourses() {
        if (tableModel.getRowCount() == 0) {
            showError("No courses to clear");
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove all " + tableModel.getRowCount() + " courses?",
            "Confirm Clear All",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            clearInputs();
            showSuccess("All courses cleared");
        }
    }
    
    private void loadSelectedCourseToInputs() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            courseNameField.setText((String) tableModel.getValueAt(selectedRow, 0));
            unitsSpinner.setValue((Integer) tableModel.getValueAt(selectedRow, 1));
            gradeComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 2));
        }
    }
    
    private void clearInputs() {
        courseNameField.setText("");
        unitsSpinner.setValue(3);
        gradeComboBox.setSelectedIndex(0);
    }
    
    private void updateButtonStates() {
        boolean hasSelection = courseTable.getSelectedRow() >= 0;
        boolean hasCourses = tableModel.getRowCount() > 0;
        
        updateCourseButton.setEnabled(hasSelection);
        removeCourseButton.setEnabled(hasSelection);
        clearAllButton.setEnabled(hasCourses);
    }
    
    private void updateStatus() {
        int courseCount = tableModel.getRowCount();
        int totalUnits = getTotalUnits();
        
        courseCountLabel.setText("Courses: " + courseCount);
        totalUnitsLabel.setText("Total Units: " + totalUnits);
        
        // Update progress bar
        unitsProgressBar.setValue(totalUnits);
        unitsProgressBar.setString(totalUnits + " / " + Constants.MAX_SEMESTER_UNITS + " units");
        
        // Update status message
        if (courseCount == 0) {
            statusLabel.setText("No courses added yet");
        } else if (totalUnits < Constants.MIN_SEMESTER_UNITS) {
            statusLabel.setText("Need " + (Constants.MIN_SEMESTER_UNITS - totalUnits) + " more units");
        } else if (totalUnits > Constants.MAX_SEMESTER_UNITS) {
            statusLabel.setText("Exceeds maximum by " + (totalUnits - Constants.MAX_SEMESTER_UNITS) + " units");
        } else {
            statusLabel.setText("Ready for CGPA calculation");
        }
    }
    
    private void updateValidation() {
        List<Course> courses = getAllCourses();
        String currentCGPA = ""; // Will be provided by parent component
        String cumulativeUnits = ""; // Will be provided by parent component
        
        ValidationResult result = CGPACalculationValidator.validateCalculationReadiness(courses, currentCGPA, cumulativeUnits);
        
        StringBuilder message = new StringBuilder();
        
        if (result.hasErrors()) {
            message.append("Issues to resolve:\n");
            for (ValidationResult.ValidationMessage error : result.getErrorMessages()) {
                message.append("• ").append(error.getMessage()).append("\n");
            }
        } else {
            message.append("✓ All course requirements met!\n");
            message.append("Ready to calculate CGPA with ").append(courses.size()).append(" courses totaling ").append(getTotalUnits()).append(" units.");
        }
        
        validationArea.setText(message.toString());
    }
    
    private void addCourseToTable(Course course) {
        double gradePoints = Constants.getGradePoints(course.getLetterGrade());
        double creditPoints = course.getUnits() * gradePoints;
        
        Object[] rowData = {
            course.getCourseName(),
            course.getUnits(),
            course.getLetterGrade(),
            String.format("%.1f", gradePoints),
            String.format("%.1f", creditPoints)
        };
        
        tableModel.addRow(rowData);
    }
    
    private void updateCourseInTable(int row, Course course) {
        double gradePoints = Constants.getGradePoints(course.getLetterGrade());
        double creditPoints = course.getUnits() * gradePoints;
        
        tableModel.setValueAt(course.getCourseName(), row, 0);
        tableModel.setValueAt(course.getUnits(), row, 1);
        tableModel.setValueAt(course.getLetterGrade(), row, 2);
        tableModel.setValueAt(String.format("%.1f", gradePoints), row, 3);
        tableModel.setValueAt(String.format("%.1f", creditPoints), row, 4);
    }
    
    private int getTotalUnits() {
        int total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            total += (Integer) tableModel.getValueAt(i, 1);
        }
        return total;
    }
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = (String) tableModel.getValueAt(i, 0);
            int units = (Integer) tableModel.getValueAt(i, 1);
            String grade = (String) tableModel.getValueAt(i, 2);
            courses.add(new Course(name, units, grade));
        }
        return courses;
    }
    
    public void setCourses(List<Course> courses) {
        tableModel.setRowCount(0);
        for (Course course : courses) {
            addCourseToTable(course);
        }
    }
    
    public boolean canCalculateCGPA(String currentCGPA, String cumulativeUnits) {
        return CGPACalculationValidator.canCalculate(getAllCourses(), currentCGPA, cumulativeUnits);
    }
    
    public String getCalculationReadinessSummary(String currentCGPA, String cumulativeUnits) {
        return CGPACalculationValidator.getCalculationReadinessSummary(getAllCourses(), currentCGPA, cumulativeUnits);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        // Could show a temporary success message or update status
        statusLabel.setText(message);
        Timer timer = new Timer(3000, e -> updateStatus());
        timer.setRepeats(false);
        timer.start();
    }
    
    // Getters for external access
    public JTable getCourseTable() { return courseTable; }
    public JButton getAddCourseButton() { return addCourseButton; }
    public JButton getUpdateCourseButton() { return updateCourseButton; }
    public JButton getRemoveCourseButton() { return removeCourseButton; }
    public JButton getClearAllButton() { return clearAllButton; }
    public JTextField getCourseNameField() { return courseNameField; }
    public JSpinner getUnitsSpinner() { return unitsSpinner; }
    public JComboBox<String> getGradeComboBox() { return gradeComboBox; }
}