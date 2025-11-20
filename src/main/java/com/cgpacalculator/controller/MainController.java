package com.cgpacalculator.controller;

import com.cgpacalculator.model.Student;
import com.cgpacalculator.model.DataPersistenceManager;
import com.cgpacalculator.model.AutoSaveManager;
import com.cgpacalculator.view.MainFrame;
import com.cgpacalculator.view.CoursePanel;

import com.cgpacalculator.view.UIStyleManager;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// MainController coordinates between view and model components using MVC pattern
public class MainController {
    
    // Model components
    private Student studentModel;
    private DataPersistenceManager dataPersistenceManager;
    private AutoSaveManager autoSaveManager;
    
    // View components  
    private MainFrame mainView;
    private CoursePanel coursePanel;
    
    // Specialized controllers and integrators
    private CalculationController calculationController;
    
    // Constructor initializes the main controller and sets up the application.
    public MainController() {
        initializeApplication();
    }
    
    // Initializes the complete application with MVC architecture.
    private void initializeApplication() {
        // Initialize model
        initializeModel();
        
        // Initialize view
        initializeView();
        
        // Initialize specialized controllers
        initializeControllers();
        
        // Set up application-level event handlers
        setupApplicationEventHandlers();
        
        // Show the application
        showApplication();
    }
    
    // Initializes the model components.
    private void initializeModel() {
        this.studentModel = new Student();
        this.dataPersistenceManager = new DataPersistenceManager();
        // AutoSaveManager requires FileManager - we'll initialize it in setupAutoSave
    }
    
    // Initializes the view components.
    private void initializeView() {
        this.mainView = new MainFrame();
        this.coursePanel = mainView.getCoursePanel();
        
        // Apply application theme
        UIStyleManager.applyApplicationTheme();
    }
    
    // Initializes specialized controllers for different application aspects.
    private void initializeControllers() {
        // Initialize calculation controller with MVC integration
        this.calculationController = new CalculationController(mainView, studentModel);
        
        // Set up auto-save functionality
        setupAutoSave();
    }
    
    // Sets up application-level event handlers.
    private void setupApplicationEventHandlers() {
        // Set up course management event handlers
        setupCourseManagementHandlers();
        
        // Set up data persistence event handlers
        setupDataPersistenceHandlers();
        
        // Set up application menu handlers
        setupMenuHandlers();
    }
    
    // Sets up event handlers for course management operations.
    private void setupCourseManagementHandlers() {
        // Add course handler
        coursePanel.addAddCourseListener(new AddCourseHandler());
        
        // Remove course handler
        coursePanel.addRemoveCourseListener(new RemoveCourseHandler());
        
        // Clear all courses handler
        coursePanel.addClearAllListener(new ClearAllCoursesHandler());
    }
    
    // Sets up event handlers for data persistence operations.
    private void setupDataPersistenceHandlers() {
        // Save data handler
        mainView.addSaveDataButtonListener(new SaveDataHandler());
        
        // Load data handler
        mainView.addLoadDataButtonListener(new LoadDataHandler());
        
        // Set up data persistence listeners
        dataPersistenceManager.addDataPersistenceListener(new DataPersistenceListener());
    }
    
    // Sets up menu event handlers.
    private void setupMenuHandlers() {
        // Menu handlers can be added here when menu functionality is implemented
        // For now, the basic button handlers cover the main functionality
    }
    
    // Shows the application window.
    private void showApplication() {
        SwingUtilities.invokeLater(() -> {
            mainView.setVisible(true);
        });
    }
    
    // Method comment
    public MainFrame getMainView() {
        return mainView;
    }
    
    // Method comment
    public Student getStudentModel() {
        return studentModel;
    }
    
    // Method comment
    public CalculationController getCalculationController() {
        return calculationController;
    }
    

    
    // Sets up auto-save functionality for the application.
    private void setupAutoSave() {
        try {
            // Initialize auto-save manager with file manager
            com.cgpacalculator.model.FileManager fileManager = new com.cgpacalculator.model.FileManager();
            this.autoSaveManager = new AutoSaveManager(fileManager);
            
            // Set current student for auto-save monitoring
            autoSaveManager.setCurrentStudent(studentModel);
            
        } catch (Exception e) {
            System.err.println("Could not set up auto-save: " + e.getMessage());
        }
    }
    
    // Event Handler Classes
    
    // Handler for adding courses to the student's course list.
    private class AddCourseHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Validate current input
                if (!coursePanel.isCurrentInputValid()) {
                    mainView.showErrorMessage("Invalid Input", "Please enter a valid course name (1-50 characters)");
                    return;
                }
                
                // Get course data from input fields
                var course = coursePanel.getCurrentCourseInput();
                
                // Validate the course
                if (!course.isValid()) {
                    mainView.showErrorMessage("Invalid Course", "Please check all course fields are properly filled");
                    return;
                }
                
                // Add course to table
                coursePanel.addCourseToTable(course);
                
                // Clear input fields for next entry
                coursePanel.clearInputFields();
                
                // Focus back to course name field for easy data entry
                SwingUtilities.invokeLater(() -> {
                    coursePanel.getCourseNameField().requestFocusInWindow();
                });
                
            } catch (Exception ex) {
                mainView.showErrorMessage("Error Adding Course", "Failed to add course: " + ex.getMessage());
            }
        }
    }
    
    // Handler for removing selected courses from the course list.
    private class RemoveCourseHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedIndex = coursePanel.getSelectedCourseIndex();
                
                if (selectedIndex == -1) {
                    mainView.showWarningMessage("No Selection", "Please select a course to remove");
                    return;
                }
                
                // Remove course from table
                coursePanel.removeCourseFromTable(selectedIndex);
                
            } catch (Exception ex) {
                mainView.showErrorMessage("Error Removing Course", "Failed to remove course: " + ex.getMessage());
            }
        }
    }
    
    // Handler for clearing all courses from the course list.
    private class ClearAllCoursesHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Confirm action with user
                int result = javax.swing.JOptionPane.showConfirmDialog(
                    mainView,
                    "Are you sure you want to clear all courses?",
                    "Confirm Clear All",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
                );
                
                if (result == javax.swing.JOptionPane.YES_OPTION) {
                    // Clear all courses
                    coursePanel.clearAllCourses();
                    
                    // Clear calculation results
                    mainView.clearResultDisplays();
                    
                    // Clear input fields
                    coursePanel.clearInputFields();
                    
                    mainView.showInfoMessage("Cleared", "All courses have been cleared");
                }
                
            } catch (Exception ex) {
                mainView.showErrorMessage("Error Clearing Courses", "Failed to clear courses: " + ex.getMessage());
            }
        }
    }
    
    // Handler for saving application data.
    private class SaveDataHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Use the data persistence manager to save data
                boolean success = dataPersistenceManager.saveStudentData(studentModel);
                
                if (success) {
                    mainView.showInfoMessage("Save Successful", "Your course data has been saved successfully.");
                } else {
                    mainView.showWarningMessage("Save Warning", "Data was saved but with some warnings. Check the console for details.");
                }
                
            } catch (Exception ex) {
                mainView.showErrorMessage("Error Saving Data", "Failed to save data: " + ex.getMessage());
            }
        }
    }
    
    // Handler for loading application data.
    private class LoadDataHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Check for unsaved changes before loading
                int choice = javax.swing.JOptionPane.showConfirmDialog(
                    mainView,
                    "Loading will replace current data. Continue?",
                    "Confirm Load",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
                );
                
                if (choice != javax.swing.JOptionPane.YES_OPTION) {
                    return;
                }
                
                // Use the data persistence manager to load data
                String filename = "student_data.json";
                var loadResult = dataPersistenceManager.loadStudentData(filename);
                
                if (loadResult != null) {
                    // Update the UI with loaded data
                    mainView.getCurrentCGPAField().setText(String.valueOf(loadResult.getCurrentCGPA()));
                    mainView.getCumulativeUnitsField().setText(String.valueOf(loadResult.getCumulativeUnits()));
                    
                    // Load courses into the course panel
                    coursePanel.clearAllCourses();
                    for (var course : loadResult.getCourses()) {
                        coursePanel.addCourseToTable(course);
                    }
                    
                    mainView.showInfoMessage("Load Successful", "Your course data has been loaded successfully.");
                } else {
                    mainView.showWarningMessage("No Data Found", "No saved data file was found or the file was empty.");
                }
                
            } catch (Exception ex) {
                mainView.showErrorMessage("Error Loading Data", "Failed to load data: " + ex.getMessage());
            }
        }
    }
    
    // Listener for data persistence events.
    private class DataPersistenceListener implements DataPersistenceManager.DataPersistenceListener {
        @Override
        public void onDataPersistenceEvent(DataPersistenceManager.DataPersistenceEvent event, String operation, String filename) {
            SwingUtilities.invokeLater(() -> {
                switch (operation) {
                    case "save":
                        mainView.setTitle("CGPA Calculator - Saved");
                        break;
                    case "load":
                        mainView.setTitle("CGPA Calculator - Loaded");
                        break;
                    case "error":
                        mainView.showErrorMessage("Data Error", "Operation failed: " + operation);
                        break;
                }
            });
        }
    }
}