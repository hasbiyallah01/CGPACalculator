package com.cgpacalculator.view;

import com.cgpacalculator.model.ClassificationManager;
import com.cgpacalculator.model.GPACalculator;
import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.Constants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;public class ResultsPanel extends JPanel {
    
    // UI Components for displaying results
    private JLabel currentGPALabel;
    private JLabel currentGPAValue;
    private JLabel totalUnitsLabel;
    private JLabel totalUnitsValue;
    private JLabel updatedCGPALabel;
    private JLabel updatedCGPAValue;
    private JLabel classificationLabel;
    private JLabel classificationValue;
    private JTextArea motivationalMessageArea;
    private JPanel calculationBreakdownPanel;
    
    // Calculation breakdown components
    private JLabel totalCreditPointsLabel;
    private JLabel totalCreditPointsValue;
    private JLabel totalCreditUnitsLabel;
    private JLabel totalCreditUnitsValue;
    private JLabel previousCGPALabel;
    private JLabel previousCGPAValue;
    private JLabel previousUnitsLabel;
    private JLabel previousUnitsValue;
    
    // Formatting
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    
    // Data for calculations
    private double currentSemesterGPA = 0.0;
    private int currentSemesterUnits = 0;
    private double updatedCGPA = 0.0;
    private double previousCGPA = 0.0;
    private int previousUnits = 0;
    private List<Course> courses;
    
    // Constructor initializes the results panel with all UI components.
    public ResultsPanel() {
        initializeComponents();
        setupLayout();
        setupTooltips();
        clearResults(); // Start with empty/default state
    }
    
    // Initializes all UI components with proper styling and properties.
    private void initializeComponents() {
        // Main panel setup
        setBorder(new TitledBorder("Results"));
        setBackground(Color.WHITE);
        
        // Current semester results
        currentGPALabel = new JLabel("Current Semester GPA:");
        currentGPAValue = new JLabel("--");
        currentGPAValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        currentGPAValue.setForeground(new Color(0, 102, 204));
        
        totalUnitsLabel = new JLabel("Total Units:");
        totalUnitsValue = new JLabel("--");
        totalUnitsValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        // Updated CGPA results
        updatedCGPALabel = new JLabel("Updated CGPA:");
        updatedCGPAValue = new JLabel("--");
        updatedCGPAValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        updatedCGPAValue.setForeground(new Color(0, 128, 0));
        
        // Classification display
        classificationLabel = new JLabel("Classification:");
        classificationValue = new JLabel("--");
        classificationValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        
        // Motivational message area
        motivationalMessageArea = new JTextArea(3, 30);
        motivationalMessageArea.setEditable(false);
        motivationalMessageArea.setWrapStyleWord(true);
        motivationalMessageArea.setLineWrap(true);
        motivationalMessageArea.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
        motivationalMessageArea.setBackground(new Color(248, 249, 250));
        motivationalMessageArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Calculation breakdown components
        setupCalculationBreakdownComponents();
    }
    
    // Sets up the calculation breakdown panel components.
    private void setupCalculationBreakdownComponents() {
        calculationBreakdownPanel = new JPanel();
        calculationBreakdownPanel.setBorder(new TitledBorder("Calculation Breakdown"));
        calculationBreakdownPanel.setBackground(new Color(250, 250, 250));
        
        // Current semester breakdown
        totalCreditPointsLabel = new JLabel("Total Credit Points:");
        totalCreditPointsValue = new JLabel("--");
        totalCreditPointsValue.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        totalCreditUnitsLabel = new JLabel("Total Credit Units:");
        totalCreditUnitsValue = new JLabel("--");
        totalCreditUnitsValue.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        // Previous academic record
        previousCGPALabel = new JLabel("Previous CGPA:");
        previousCGPAValue = new JLabel("--");
        previousCGPAValue.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        previousUnitsLabel = new JLabel("Previous Units:");
        previousUnitsValue = new JLabel("--");
        previousUnitsValue.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }
    
    // Sets up the layout for all components using GridBagLayout for precise control.
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main results panel
        JPanel mainResultsPanel = new JPanel(new GridBagLayout());
        mainResultsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Current semester GPA row
        gbc.gridx = 0; gbc.gridy = 0;
        mainResultsPanel.add(currentGPALabel, gbc);
        gbc.gridx = 1;
        mainResultsPanel.add(currentGPAValue, gbc);
        gbc.gridx = 2;
        mainResultsPanel.add(totalUnitsLabel, gbc);
        gbc.gridx = 3;
        mainResultsPanel.add(totalUnitsValue, gbc);
        
        // Updated CGPA row
        gbc.gridx = 0; gbc.gridy = 1;
        mainResultsPanel.add(updatedCGPALabel, gbc);
        gbc.gridx = 1;
        mainResultsPanel.add(updatedCGPAValue, gbc);
        gbc.gridx = 2;
        mainResultsPanel.add(classificationLabel, gbc);
        gbc.gridx = 3;
        mainResultsPanel.add(classificationValue, gbc);
        
        // Motivational message row
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel motivationLabel = new JLabel("Motivational Message:");
        motivationLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        mainResultsPanel.add(motivationLabel, gbc);
        
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        mainResultsPanel.add(motivationalMessageArea, gbc);
        
        // Setup calculation breakdown layout
        setupCalculationBreakdownLayout();
        
        // Add panels to main layout
        add(mainResultsPanel, BorderLayout.CENTER);
        add(calculationBreakdownPanel, BorderLayout.SOUTH);
    }
    
    // Sets up the layout for the calculation breakdown panel.
    private void setupCalculationBreakdownLayout() {
        calculationBreakdownPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 8, 3, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Current semester breakdown
        gbc.gridx = 0; gbc.gridy = 0;
        calculationBreakdownPanel.add(totalCreditPointsLabel, gbc);
        gbc.gridx = 1;
        calculationBreakdownPanel.add(totalCreditPointsValue, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        calculationBreakdownPanel.add(totalCreditUnitsLabel, gbc);
        gbc.gridx = 1;
        calculationBreakdownPanel.add(totalCreditUnitsValue, gbc);
        
        // Previous academic record
        gbc.gridx = 2; gbc.gridy = 0;
        calculationBreakdownPanel.add(previousCGPALabel, gbc);
        gbc.gridx = 3;
        calculationBreakdownPanel.add(previousCGPAValue, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        calculationBreakdownPanel.add(previousUnitsLabel, gbc);
        gbc.gridx = 3;
        calculationBreakdownPanel.add(previousUnitsValue, gbc);
    }
    
    // Sets up tooltips for all components to provide detailed calculation information.
    private void setupTooltips() {
        currentGPAValue.setToolTipText("GPA = Σ(Units × Grade Points) / Σ(Units)");
        updatedCGPAValue.setToolTipText("CGPA = (Previous Total Points + Current Points) / (Previous Units + Current Units)");
        classificationValue.setToolTipText("Classification based on CGPA ranges");
        motivationalMessageArea.setToolTipText("Dynamic motivational content based on your performance");
        
        // Add hover effects for interactive feedback
        addHoverEffects();
    }
    
    // Adds hover effects to make the interface more interactive.
    private void addHoverEffects() {
        MouseAdapter hoverEffect = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setOpaque(true);
                label.setBackground(new Color(240, 248, 255));
                label.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setOpaque(false);
                label.repaint();
            }
        };
        
        currentGPAValue.addMouseListener(hoverEffect);
        updatedCGPAValue.addMouseListener(hoverEffect);
        classificationValue.addMouseListener(hoverEffect);
    }
    
    // Method comment
    public void updateResults(List<Course> courses, double previousCGPA, int previousUnits) {
        this.courses = courses;
        this.previousCGPA = previousCGPA;
        this.previousUnits = previousUnits;
        
        if (courses == null || courses.isEmpty()) {
            clearResults();
            return;
        }
        
        // Calculate current semester GPA and units
        currentSemesterGPA = GPACalculator.calculateCurrentSemesterGPA(courses);
        currentSemesterUnits = GPACalculator.calculateTotalCreditUnits(courses);
        
        // Calculate updated CGPA
        if (previousUnits > 0) {
            updatedCGPA = GPACalculator.calculateUpdatedCGPA(previousCGPA, previousUnits, courses);
        } else {
            updatedCGPA = GPACalculator.calculateInitialCGPA(courses);
        }
        
        // Update display
        updateDisplayValues();
        updateClassificationDisplay();
        updateMotivationalMessage();
        updateCalculationBreakdown();
        
        // Update tooltips with current data
        updateTooltipsWithData();
    }
    
    // Updates the main display values.
    private void updateDisplayValues() {
        currentGPAValue.setText(decimalFormat.format(currentSemesterGPA));
        totalUnitsValue.setText(String.valueOf(currentSemesterUnits));
        updatedCGPAValue.setText(decimalFormat.format(updatedCGPA));
    }
    
    // Updates the classification display with color coding.
    private void updateClassificationDisplay() {
        try {
            ClassificationManager.DegreeClassification classification = 
                ClassificationManager.determineDegreeClassification(updatedCGPA);
            
            classificationValue.setText(classification.getDisplayName());
            
            // Apply color coding based on classification
            Color classificationColor = Color.decode(classification.getColorCode());
            classificationValue.setForeground(classificationColor);
            
            // Update classification tooltip with range information
            classificationValue.setToolTipText(String.format(
                "%s: %.2f - %.2f CGPA range",
                classification.getDisplayName(),
                classification.getMinimumCGPA(),
                classification.getMaximumCGPA()
            ));
            
        } catch (IllegalArgumentException e) {
            classificationValue.setText("Invalid CGPA");
            classificationValue.setForeground(Color.RED);
            classificationValue.setToolTipText("CGPA value is outside valid range (0.00-5.00)");
        }
    }
    
    // Updates the motivational message based on current performance.
    private void updateMotivationalMessage() {
        try {
            String message = ClassificationManager.generateMotivationalMessage(updatedCGPA);
            motivationalMessageArea.setText(message);
            
            // Set background color based on performance level
            Color backgroundColor;
            if (updatedCGPA >= 4.50) {
                backgroundColor = new Color(232, 245, 233); // Light green
            } else if (updatedCGPA >= 3.50) {
                backgroundColor = new Color(227, 242, 253); // Light blue
            } else if (updatedCGPA >= 2.50) {
                backgroundColor = new Color(255, 248, 225); // Light orange
            } else if (updatedCGPA >= 1.50) {
                backgroundColor = new Color(255, 235, 238); // Light red
            } else {
                backgroundColor = new Color(255, 205, 210); // Darker light red
            }
            
            motivationalMessageArea.setBackground(backgroundColor);
            
        } catch (IllegalArgumentException e) {
            motivationalMessageArea.setText("Please enter valid course data to see your motivational message.");
            motivationalMessageArea.setBackground(new Color(248, 249, 250));
        }
    }
    
    // Updates the calculation breakdown display with detailed information.
    private void updateCalculationBreakdown() {
        if (courses == null || courses.isEmpty()) {
            totalCreditPointsValue.setText("--");
            totalCreditUnitsValue.setText("--");
            previousCGPAValue.setText("--");
            previousUnitsValue.setText("--");
            return;
        }
        
        // Calculate total credit points for current semester
        double totalCreditPoints = GPACalculator.calculateTotalCreditPoints(courses);
        
        totalCreditPointsValue.setText(decimalFormat.format(totalCreditPoints));
        totalCreditUnitsValue.setText(String.valueOf(currentSemesterUnits));
        
        // Display previous academic record
        if (previousUnits > 0) {
            previousCGPAValue.setText(decimalFormat.format(previousCGPA));
            previousUnitsValue.setText(String.valueOf(previousUnits));
        } else {
            previousCGPAValue.setText("N/A (New Student)");
            previousUnitsValue.setText("N/A (New Student)");
        }
    }
    
    // Updates tooltips with current calculation data for detailed breakdowns.
    private void updateTooltipsWithData() {
        if (courses == null || courses.isEmpty()) {
            return;
        }
        
        // Build detailed GPA calculation tooltip
        StringBuilder gpaTooltip = new StringBuilder();
        gpaTooltip.append("<html><b>GPA Calculation:</b><br>");
        gpaTooltip.append("Formula: Σ(Units × Grade Points) / Σ(Units)<br><br>");
        
        double totalPoints = 0;
        int totalUnits = 0;
        for (Course course : courses) {
            double coursePoints = course.getUnits() * course.getGradePoints();
            totalPoints += coursePoints;
            totalUnits += course.getUnits();
            gpaTooltip.append(String.format("%s: %d × %.1f = %.1f<br>", 
                course.getCourseName(), course.getUnits(), course.getGradePoints(), coursePoints));
        }
        
        gpaTooltip.append(String.format("<br><b>Total: %.1f / %d = %.2f</b></html>", 
            totalPoints, totalUnits, currentSemesterGPA));
        currentGPAValue.setToolTipText(gpaTooltip.toString());
        
        // Build detailed CGPA calculation tooltip
        StringBuilder cgpaTooltip = new StringBuilder();
        cgpaTooltip.append("<html><b>CGPA Calculation:</b><br>");
        
        if (previousUnits > 0) {
            double previousTotalPoints = previousCGPA * previousUnits;
            double currentTotalPoints = totalPoints;
            double combinedPoints = previousTotalPoints + currentTotalPoints;
            int combinedUnits = previousUnits + totalUnits;
            
            cgpaTooltip.append("Formula: (Previous Total Points + Current Points) / (Previous Units + Current Units)<br><br>");
            cgpaTooltip.append(String.format("Previous: %.2f × %d = %.1f points<br>", 
                previousCGPA, previousUnits, previousTotalPoints));
            cgpaTooltip.append(String.format("Current: %.1f points<br>", currentTotalPoints));
            cgpaTooltip.append(String.format("<b>CGPA: (%.1f + %.1f) / (%d + %d) = %.2f</b>", 
                previousTotalPoints, currentTotalPoints, previousUnits, totalUnits, updatedCGPA));
        } else {
            cgpaTooltip.append("New student - CGPA equals current semester GPA<br>");
            cgpaTooltip.append(String.format("<b>CGPA: %.2f</b>", updatedCGPA));
        }
        
        cgpaTooltip.append("</html>");
        updatedCGPAValue.setToolTipText(cgpaTooltip.toString());
    }
    
    // Clears all results and resets display to default state.
    public void clearResults() {
        currentGPAValue.setText("--");
        totalUnitsValue.setText("--");
        updatedCGPAValue.setText("--");
        classificationValue.setText("--");
        classificationValue.setForeground(Color.BLACK);
        motivationalMessageArea.setText("Enter your courses and click 'Calculate GPA' to see your results and motivational message!");
        motivationalMessageArea.setBackground(new Color(248, 249, 250));
        
        totalCreditPointsValue.setText("--");
        totalCreditUnitsValue.setText("--");
        previousCGPAValue.setText("--");
        previousUnitsValue.setText("--");
        
        // Reset tooltips
        currentGPAValue.setToolTipText("GPA = Σ(Units × Grade Points) / Σ(Units)");
        updatedCGPAValue.setToolTipText("CGPA = (Previous Total Points + Current Points) / (Previous Units + Current Units)");
        classificationValue.setToolTipText("Classification based on CGPA ranges");
    }
    
    // Method comment
    public double getCurrentSemesterGPA() {
        return currentSemesterGPA;
    }
    
    // Method comment
    public double getUpdatedCGPA() {
        return updatedCGPA;
    }
    
    // Method comment
    public String getCurrentClassification() {
        return classificationValue.getText();
    }
    
    // Method comment
    public String getCurrentMotivationalMessage() {
        return motivationalMessageArea.getText();
    }
}