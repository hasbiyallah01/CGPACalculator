package com.cgpacalculator.view;

import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.Constants;
import com.cgpacalculator.utils.CGPACalculationValidator;
import com.cgpacalculator.utils.ValidationResult;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Enhanced main frame with improved course management and validation
 */
public class EnhancedMainFrame extends JFrame {
    
    // Core components
    private final EnhancedCourseManagementPanel courseManagementPanel;
    private final JPanel currentCGPAPanel;
    private final JPanel calculationPanel;
    private final JPanel resultsPanel;
    
    // Current CGPA input components
    private final JTextField currentCGPAField;
    private final JTextField cumulativeUnitsField;
    
    // Validation feedback components
    private final JLabel cgpaValidationLabel;
    private final JLabel unitsValidationLabel;
    private final JLabel calculationReadinessLabel;
    
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
    
    public EnhancedMainFrame() {
        // Initialize components
        this.courseManagementPanel = new EnhancedCourseManagementPanel();
        
        this.currentCGPAField = createCurrentCGPAField();
        this.cumulativeUnitsField = createCumulativeUnitsField();
        
        this.cgpaValidationLabel = createValidationLabel();
        this.unitsValidationLabel = createValidationLabel();
        this.calculationReadinessLabel = createCalculationReadinessLabel();
        
        this.calculateButton = createCalculateButton();
        this.saveDataButton = createSaveDataButton();
        this.loadDataButton = createLoadDataButton();
        
        this.currentGPALabel = createResultLabel("Current GPA: --");
        this.updatedCGPALabel = createResultLabel("Updated CGPA: --");
        this.classificationLabel = createResultLabel("Classification: --");
        this.totalUnitsLabel = createResultLabel("Total Units: --");
        this.motivationalMessageArea = createMotivationalMessageArea();
        
        // Initialize layout panels
        this.currentCGPAPanel = createCurrentCGPAPanel();
        this.calculationPanel = createCalculationPanel();
        this.resultsPanel = createResultsPanel();
        
        // Set up the frame
        setupFrameProperties();
        setupMainLayout();
        setupEventHandlers();
        
        // Initial state
        updateCalculationReadiness();
    }
    
    private JTextField createCurrentCGPAField() {
        JTextField field = new JTextField(10);
        field.setToolTipText("Enter current CGPA (0.00 - 5.00) - Optional for new students");
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }
    
    private JTextField createCumulativeUnitsField() {
        JTextField field = new JTextField(10);
        field.setToolTipText("Enter current cumulative units - Optional for new students");
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }
    
    private JLabel createValidationLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        label.setForeground(Color.RED);
        return label;
    }
    
    private JLabel createCalculationReadinessLabel() {
        JLabel label = new JLabel("Add courses between 18-24 units to calculate CGPA");
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        label.setForeground(Color.BLUE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    private JButton createCalculateButton() {
        JButton button = new JButton("Calculate GPA & CGPA");
        button.setToolTipText("Calculate GPA and CGPA based on entered courses");
        button.setPreferredSize(new Dimension(180, 35));
        button.setEnabled(false); // Initially disabled
        return button;
    }
    
    private JButton createSaveDataButton() {
        JButton button = new JButton("Save Data");
        button.setToolTipText("Save current course data to file");
        return button;
    }
    
    private JButton createLoadDataButton() {
        JButton button = new JButton("Load Data");
        button.setToolTipText("Load previously saved course data");
        return button;
    }
    
    private JLabel createResultLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }
    
    private JTextArea createMotivationalMessageArea() {
        JTextArea area = new JTextArea(4, 50);
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setBackground(getBackground());
        area.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        area.setText("Welcome to the Enhanced CGPA Calculator!\n\n" +
                    "1. Add courses between 18-24 total units\n" +
                    "2. All course details must be complete\n" +
                    "3. Click Calculate when ready");
        return area;
    }
    
    private JPanel createCurrentCGPAPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Current Academic Standing (Optional for New Students)"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
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
        
        return panel;
    }
    
    private JPanel createCalculationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new TitledBorder("Calculation Control"));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveDataButton);
        buttonPanel.add(loadDataButton);
        
        // Readiness panel
        JPanel readinessPanel = new JPanel(new BorderLayout());
        readinessPanel.add(calculationReadinessLabel, BorderLayout.CENTER);
        
        panel.add(readinessPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Calculation Results"));
        
        // Results grid
        JPanel resultsGrid = new JPanel(new GridLayout(2, 2, 10, 5));
        resultsGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultsGrid.add(currentGPALabel);
        resultsGrid.add(totalUnitsLabel);
        resultsGrid.add(updatedCGPALabel);
        resultsGrid.add(classificationLabel);
        
        // Motivational message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Motivational Message"));
        JScrollPane scrollPane = new JScrollPane(motivationalMessageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(0, 100));
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(resultsGrid, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void setupFrameProperties() {
        setTitle("Enhanced CGPA Calculator - Complete Course Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(900, 700));
    }
    
    private void setupMainLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Create main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel for CGPA input and calculation control
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(currentCGPAPanel, BorderLayout.NORTH);
        topPanel.add(calculationPanel, BorderLayout.SOUTH);
        
        // Add components to main panel
        mainContentPanel.add(topPanel, BorderLayout.NORTH);
        mainContentPanel.add(courseManagementPanel, BorderLayout.CENTER);
        mainContentPanel.add(resultsPanel, BorderLayout.SOUTH);
        
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Pack and center
        pack();
        setLocationRelativeTo(null);
    }
    
    private void setupEventHandlers() {
        // Real-time validation for CGPA and units fields
        currentCGPAField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateAndUpdateReadiness(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateAndUpdateReadiness(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateAndUpdateReadiness(); }
        });
        
        cumulativeUnitsField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateAndUpdateReadiness(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateAndUpdateReadiness(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateAndUpdateReadiness(); }
        });
        
        // Listen for course changes
        courseManagementPanel.getCourseTable().getModel().addTableModelListener(e -> {
            updateCalculationReadiness();
        });
    }
    
    private void validateAndUpdateReadiness() {
        validateCGPAInput();
        validateUnitsInput();
        updateCalculationReadiness();
    }
    
    private void validateCGPAInput() {
        String cgpaText = currentCGPAField.getText().trim();
        cgpaValidationLabel.setText(" ");
        
        if (!cgpaText.isEmpty()) {
            try {
                double cgpa = Double.parseDouble(cgpaText);
                if (cgpa < Constants.MIN_CGPA || cgpa > Constants.MAX_CGPA) {
                    cgpaValidationLabel.setText("✗ CGPA must be between 0.00 and 5.00");
                    cgpaValidationLabel.setForeground(Color.RED);
                } else {
                    cgpaValidationLabel.setText("✓ Valid CGPA");
                    cgpaValidationLabel.setForeground(Color.GREEN);
                }
            } catch (NumberFormatException e) {
                cgpaValidationLabel.setText("✗ Please enter a valid decimal number");
                cgpaValidationLabel.setForeground(Color.RED);
            }
        }
    }
    
    private void validateUnitsInput() {
        String unitsText = cumulativeUnitsField.getText().trim();
        unitsValidationLabel.setText(" ");
        
        if (!unitsText.isEmpty()) {
            try {
                int units = Integer.parseInt(unitsText);
                if (units <= 0) {
                    unitsValidationLabel.setText("✗ Cumulative units must be positive");
                    unitsValidationLabel.setForeground(Color.RED);
                } else {
                    unitsValidationLabel.setText("✓ Valid units");
                    unitsValidationLabel.setForeground(Color.GREEN);
                }
            } catch (NumberFormatException e) {
                unitsValidationLabel.setText("✗ Please enter a valid whole number");
                unitsValidationLabel.setForeground(Color.RED);
            }
        }
    }
    
    private void updateCalculationReadiness() {
        List<Course> courses = courseManagementPanel.getAllCourses();
        String currentCGPA = currentCGPAField.getText().trim();
        String cumulativeUnits = cumulativeUnitsField.getText().trim();
        
        ValidationResult result = CGPACalculationValidator.validateCalculationReadiness(courses, currentCGPA, cumulativeUnits);
        
        boolean canCalculate = !result.hasErrors();
        calculateButton.setEnabled(canCalculate);
        
        if (canCalculate) {
            calculationReadinessLabel.setText("✓ Ready to calculate CGPA!");
            calculationReadinessLabel.setForeground(new Color(0, 128, 0));
        } else {
            String firstError = result.getErrorMessages().isEmpty() ? 
                "Complete all requirements to calculate CGPA" : 
                result.getErrorMessages().get(0).getMessage();
            calculationReadinessLabel.setText("⚠ " + firstError);
            calculationReadinessLabel.setForeground(Color.RED);
        }
        
        // Update total units display
        int totalUnits = courses.stream().mapToInt(Course::getUnits).sum();
        totalUnitsLabel.setText("Total Units: " + totalUnits);
    }
    
    // Public methods for external interaction
    
    public void addCalculateButtonListener(ActionListener listener) {
        calculateButton.addActionListener(listener);
    }
    
    public void addSaveDataButtonListener(ActionListener listener) {
        saveDataButton.addActionListener(listener);
    }
    
    public void addLoadDataButtonListener(ActionListener listener) {
        loadDataButton.addActionListener(listener);
    }
    
    public String getCurrentCGPAInput() {
        return currentCGPAField.getText().trim();
    }
    
    public String getCumulativeUnitsInput() {
        return cumulativeUnitsField.getText().trim();
    }
    
    public void setCurrentCGPAInput(String cgpa) {
        currentCGPAField.setText(cgpa);
    }
    
    public void setCumulativeUnitsInput(String units) {
        cumulativeUnitsField.setText(units);
    }
    
    public List<Course> getAllCourses() {
        return courseManagementPanel.getAllCourses();
    }
    
    public void setCourses(List<Course> courses) {
        courseManagementPanel.setCourses(courses);
        updateCalculationReadiness();
    }
    
    public void updateCurrentGPADisplay(double gpa) {
        currentGPALabel.setText(String.format("Current GPA: %.2f", gpa));
    }
    
    public void updateCGPADisplay(double cgpa) {
        updatedCGPALabel.setText(String.format("Updated CGPA: %.2f", cgpa));
    }
    
    public void updateClassificationDisplay(String classification) {
        classificationLabel.setText("Classification: " + classification);
        
        // Apply color coding
        Color color = getClassificationColor(classification);
        classificationLabel.setForeground(color);
    }
    
    public void updateTotalUnitsDisplay(int units) {
        totalUnitsLabel.setText("Total Units: " + units);
    }
    
    public void updateMotivationalMessage(String message) {
        motivationalMessageArea.setText(message);
    }
    
    public void clearResultDisplays() {
        currentGPALabel.setText("Current GPA: --");
        updatedCGPALabel.setText("Updated CGPA: --");
        classificationLabel.setText("Classification: --");
        totalUnitsLabel.setText("Total Units: --");
        motivationalMessageArea.setText("Enter your courses and complete all requirements to see results!");
    }
    
    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public void showInfoMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showWarningMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public boolean canCalculateCGPA() {
        return calculateButton.isEnabled();
    }
    
    public EnhancedCourseManagementPanel getCourseManagementPanel() {
        return courseManagementPanel;
    }
    
    public JButton getCalculateButton() {
        return calculateButton;
    }
    
    public JButton getSaveDataButton() {
        return saveDataButton;
    }
    
    public JButton getLoadDataButton() {
        return loadDataButton;
    }
    
    private Color getClassificationColor(String classification) {
        switch (classification) {
            case Constants.FIRST_CLASS:
                return new Color(76, 175, 80); // Green
            case Constants.SECOND_CLASS_UPPER:
                return new Color(139, 195, 74); // Light Green
            case Constants.SECOND_CLASS_LOWER:
                return new Color(255, 193, 7); // Amber
            case Constants.THIRD_CLASS:
                return new Color(255, 152, 0); // Orange
            case Constants.FAIL_CLASS:
                return new Color(244, 67, 54); // Red
            default:
                return Color.BLACK;
        }
    }
}