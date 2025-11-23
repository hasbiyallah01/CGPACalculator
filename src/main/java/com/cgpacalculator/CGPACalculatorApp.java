package com.cgpacalculator;

import com.cgpacalculator.view.EnhancedMainFrame;
import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.Constants;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Main CGPA Calculator Application
 * Full-featured calculator with save/load functionality
 */
public class CGPACalculatorApp {
    
    private static final String DATA_FILE = "cgpa_data.txt";
    private EnhancedMainFrame mainFrame;
    
    public CGPACalculatorApp() {
        initializeApplication();
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            new CGPACalculatorApp();
        });
    }
    
    private void initializeApplication() {
        // Create main frame
        mainFrame = new EnhancedMainFrame();
        
        // Add event handlers
        setupEventHandlers();
        
        // Try to load previous data
        loadDataOnStartup();
        
        // Show the application
        mainFrame.setVisible(true);
        
        // Show welcome message
        showWelcomeMessage();
    }
    
    private void setupEventHandlers() {
        // Calculate button handler
        mainFrame.addCalculateButtonListener(new CalculationHandler());
        
        // Save button handler
        mainFrame.addSaveDataButtonListener(new SaveDataHandler());
        
        // Load button handler
        mainFrame.addLoadDataButtonListener(new LoadDataHandler());
        
        // Add window closing handler to auto-save
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                handleApplicationExit();
            }
        });
    }
    
    private void loadDataOnStartup() {
        File dataFile = new File(DATA_FILE);
        if (dataFile.exists()) {
            try {
                loadCourseData();
                mainFrame.showInfoMessage("Data Loaded", 
                    "Previous course data has been loaded automatically.");
            } catch (Exception e) {
                System.err.println("Could not load previous data: " + e.getMessage());
            }
        }
    }
    
    private void showWelcomeMessage() {
        String welcomeMessage = 
            "🎓 Welcome to the Enhanced CGPA Calculator! 🎓\n\n" +
            "Key Requirements:\n" +
            "• Add courses totaling 18-24 units\n" +
            "• Complete all course details (name, units, grade)\n" +
            "• Optionally enter current CGPA and cumulative units\n\n" +
            "Features:\n" +
            "• Real-time validation and feedback\n" +
            "• Easy course management (add, edit, remove)\n" +
            "• Automatic save/load functionality\n" +
            "• Comprehensive CGPA calculation\n\n" +
            "The Calculate button will only enable when all requirements are met!";
        
        JOptionPane.showMessageDialog(mainFrame, welcomeMessage, 
            "CGPA Calculator", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleApplicationExit() {
        List<Course> courses = mainFrame.getAllCourses();
        
        if (!courses.isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(
                mainFrame,
                "Do you want to save your course data before exiting?",
                "Save Data?",
                JOptionPane.YES_NO_CANCEL_OPTION
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                saveCourseData();
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                return; // Don't exit
            }
        }
        
        System.exit(0);
    }
    
    /**
     * Handles CGPA calculation
     */
    private class CalculationHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                performCGPACalculation();
            } catch (Exception ex) {
                mainFrame.showErrorMessage("Calculation Error", 
                    "An error occurred during calculation: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        private void performCGPACalculation() {
            // Get input data
            List<Course> courses = mainFrame.getAllCourses();
            String currentCGPAStr = mainFrame.getCurrentCGPAInput();
            String cumulativeUnitsStr = mainFrame.getCumulativeUnitsInput();
            
            // Validate that calculation can proceed
            if (!mainFrame.canCalculateCGPA()) {
                mainFrame.showErrorMessage("Calculation Error", 
                    "Cannot calculate CGPA. Please complete all requirements first.");
                return;
            }
            
            // Calculate current semester GPA
            double totalGradePoints = 0.0;
            int totalUnits = 0;
            
            for (Course course : courses) {
                double gradePoints = Constants.getGradePoints(course.getLetterGrade());
                totalGradePoints += gradePoints * course.getUnits();
                totalUnits += course.getUnits();
            }
            
            double currentGPA = totalGradePoints / totalUnits;
            
            // Calculate updated CGPA
            double updatedCGPA;
            int totalCumulativeUnits;
            
            if (currentCGPAStr.isEmpty() || cumulativeUnitsStr.isEmpty()) {
                // New student - CGPA equals current GPA
                updatedCGPA = currentGPA;
                totalCumulativeUnits = totalUnits;
            } else {
                // Existing student - calculate weighted CGPA
                double currentCGPA = Double.parseDouble(currentCGPAStr);
                int cumulativeUnits = Integer.parseInt(cumulativeUnitsStr);
                
                double totalCumulativeGradePoints = (currentCGPA * cumulativeUnits) + totalGradePoints;
                totalCumulativeUnits = cumulativeUnits + totalUnits;
                updatedCGPA = totalCumulativeGradePoints / totalCumulativeUnits;
            }
            
            // Get classification
            String classification = Constants.getClassification(updatedCGPA);
            
            // Update displays
            mainFrame.updateCurrentGPADisplay(currentGPA);
            mainFrame.updateCGPADisplay(updatedCGPA);
            mainFrame.updateClassificationDisplay(classification);
            mainFrame.updateTotalUnitsDisplay(totalCumulativeUnits);
            
            // Generate motivational message
            String motivationalMessage = generateMotivationalMessage(
                updatedCGPA, classification, courses.size(), totalUnits, currentGPA);
            mainFrame.updateMotivationalMessage(motivationalMessage);
            
            // Auto-save after successful calculation
            saveCourseData();
            
            // Show success notification
            showCalculationSummary(currentGPA, updatedCGPA, classification, totalUnits);
        }
        
        private String generateMotivationalMessage(double cgpa, String classification, 
                                                 int courseCount, int totalUnits, double currentGPA) {
            StringBuilder message = new StringBuilder();
            
            message.append("🎓 CGPA CALCULATION COMPLETE! 🎓\n\n");
            message.append(String.format("📊 Results Summary:\n"));
            message.append(String.format("• Current Semester GPA: %.2f\n", currentGPA));
            message.append(String.format("• Updated CGPA: %.2f\n", cgpa));
            message.append(String.format("• Classification: %s\n", classification));
            message.append(String.format("• Courses this semester: %d\n", courseCount));
            message.append(String.format("• Total units: %d\n\n", totalUnits));
            
            // Add performance-based motivational message
            if (cgpa >= Constants.FIRST_CLASS_MIN) {
                message.append("🌟 OUTSTANDING PERFORMANCE! 🌟\n");
                message.append("You're absolutely crushing it! This is First Class excellence!");
            } else if (cgpa >= Constants.SECOND_CLASS_UPPER_MIN) {
                message.append("🎯 EXCELLENT WORK! 🎯\n");
                message.append("You're doing great! Keep pushing for that First Class!");
            } else if (cgpa >= Constants.SECOND_CLASS_LOWER_MIN) {
                message.append("👍 GOOD PROGRESS! 👍\n");
                message.append("You're on track! Focus more to reach the next level!");
            } else if (cgpa >= Constants.THIRD_CLASS_MIN) {
                message.append("⚠️ IMPROVEMENT NEEDED ⚠️\n");
                message.append("Time to step up! You have the potential to do better!");
            } else {
                message.append("🚨 URGENT ACTION REQUIRED 🚨\n");
                message.append("Serious improvement needed! Consider getting academic support!");
            }
            
            return message.toString();
        }
        
        private void showCalculationSummary(double currentGPA, double cgpa, 
                                          String classification, int totalUnits) {
            String summary = String.format(
                "Calculation Complete!\n\n" +
                "Current Semester GPA: %.2f\n" +
                "Updated CGPA: %.2f\n" +
                "Classification: %s\n" +
                "Total Units: %d\n\n" +
                "Data has been automatically saved.",
                currentGPA, cgpa, classification, totalUnits
            );
            
            mainFrame.showInfoMessage("CGPA Calculated", summary);
        }
    }
    
    /**
     * Handles saving course data
     */
    private class SaveDataHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCourseData();
        }
    }
    
    /**
     * Handles loading course data
     */
    private class LoadDataHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadCourseData();
        }
    }
    
    private void saveCourseData() {
        try {
            List<Course> courses = mainFrame.getAllCourses();
            String currentCGPA = mainFrame.getCurrentCGPAInput();
            String cumulativeUnits = mainFrame.getCumulativeUnitsInput();
            
            // Write to file in simple format
            try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
                writer.println("# CGPA Calculator Data");
                writer.println("# Saved on: " + new java.util.Date());
                writer.println("CURRENT_CGPA=" + currentCGPA);
                writer.println("CUMULATIVE_UNITS=" + cumulativeUnits);
                writer.println("COURSE_COUNT=" + courses.size());
                writer.println();
                
                for (int i = 0; i < courses.size(); i++) {
                    Course course = courses.get(i);
                    writer.println("COURSE_" + i + "_NAME=" + course.getCourseName());
                    writer.println("COURSE_" + i + "_UNITS=" + course.getUnits());
                    writer.println("COURSE_" + i + "_GRADE=" + course.getLetterGrade());
                }
            }
            
            mainFrame.showInfoMessage("Data Saved", 
                String.format("Successfully saved %d courses to %s", courses.size(), DATA_FILE));
            
        } catch (Exception e) {
            mainFrame.showErrorMessage("Save Error", 
                "Could not save data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadCourseData() {
        try {
            File dataFile = new File(DATA_FILE);
            if (!dataFile.exists()) {
                mainFrame.showWarningMessage("Load Data", 
                    "No saved data file found. Start by adding some courses!");
                return;
            }
            
            // Read from file
            Properties props = new Properties();
            try (FileReader reader = new FileReader(DATA_FILE)) {
                props.load(reader);
            }
            
            // Load basic data
            String currentCGPA = props.getProperty("CURRENT_CGPA", "");
            String cumulativeUnits = props.getProperty("CUMULATIVE_UNITS", "");
            String courseCountStr = props.getProperty("COURSE_COUNT", "0");
            
            int courseCount = Integer.parseInt(courseCountStr);
            
            // Load courses
            List<Course> courses = new ArrayList<>();
            for (int i = 0; i < courseCount; i++) {
                String name = props.getProperty("COURSE_" + i + "_NAME");
                String unitsStr = props.getProperty("COURSE_" + i + "_UNITS");
                String grade = props.getProperty("COURSE_" + i + "_GRADE");
                
                if (name != null && unitsStr != null && grade != null) {
                    int units = Integer.parseInt(unitsStr);
                    courses.add(new Course(name, units, grade));
                }
            }
            
            // Load data into UI
            mainFrame.setCourses(courses);
            
            if (!currentCGPA.isEmpty()) {
                mainFrame.setCurrentCGPAInput(currentCGPA);
            }
            
            if (!cumulativeUnits.isEmpty()) {
                mainFrame.setCumulativeUnitsInput(cumulativeUnits);
            }
            
            mainFrame.showInfoMessage("Data Loaded", 
                String.format("Successfully loaded %d courses from %s", courses.size(), DATA_FILE));
            
        } catch (Exception e) {
            mainFrame.showErrorMessage("Load Error", 
                "Could not load data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

}