package com.cgpacalculator.view;

import com.cgpacalculator.model.Course;
import com.cgpacalculator.utils.GradeConverter;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;public class CourseTableModel extends AbstractTableModel {
    
    // Column definitions with descriptive names
    private static final String[] COLUMN_NAMES = {
        "Course Name", "Units", "Grade", "Grade Points", "Credit Points"
    };
    
    private static final Class<?>[] COLUMN_CLASSES = {
        String.class, Integer.class, String.class, Double.class, Double.class
    };
    
    // Encapsulated course data
    private final List<Course> courseList;
    
    // Constructor initializes the course list
    public CourseTableModel() {
        this.courseList = new ArrayList<>();
    }
    
    // Returns the number of rows in the table
    @Override
    public int getRowCount() {
        return courseList.size();
    }
    
    // Returns the number of columns in the table
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }
    
    // Returns the column name for display
    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }
    
    // Returns the class type for each column
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }
    
    // Determines which cells are editable
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Only course name, units, and grade are editable
        return columnIndex <= 2;
    }
    
    // Returns the value at the specified cell
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= courseList.size()) {
            return null;
        }
        
        Course course = courseList.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return course.getCourseName();
            case 1: return course.getUnits();
            case 2: return course.getLetterGrade();
            case 3: return course.getGradePoints();
            case 4: return course.calculateCreditPoints();
            default: return null;
        }
    }
    
    // Sets the value at the specified cell and updates the model
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= courseList.size()) {
            return;
        }
        
        Course course = courseList.get(rowIndex);
        
        try {
            switch (columnIndex) {
                case 0: // Course Name
                    if (value instanceof String) {
                        course.setCourseName((String) value);
                    }
                    break;
                case 1: // Units
                    if (value instanceof Integer) {
                        course.setUnits((Integer) value);
                    } else if (value instanceof String) {
                        course.setUnits(Integer.parseInt((String) value));
                    }
                    break;
                case 2: // Grade
                    if (value instanceof String) {
                        String grade = (String) value;
                        course.setLetterGrade(grade);
                        // Grade points are automatically updated in the Course class
                    }
                    break;
            }
            
            // Notify listeners that data has changed
            fireTableRowsUpdated(rowIndex, rowIndex);
            
        } catch (NumberFormatException e) {
            // Handle invalid number input gracefully
            System.err.println("Invalid number format: " + value);
        }
    }
    
    // Adds a new course to the table model
    public void addCourse(Course course) {
        if (course != null) {
            courseList.add(course);
            int newRowIndex = courseList.size() - 1;
            fireTableRowsInserted(newRowIndex, newRowIndex);
        }
    }
    
    // Removes a course at the specified index
    public void removeCourse(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < courseList.size()) {
            courseList.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    // Returns the course at the specified index
    public Course getCourse(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < courseList.size()) {
            return courseList.get(rowIndex);
        }
        return null;
    }
    
    // Returns a copy of all courses in the model
    public List<Course> getAllCourses() {
        return new ArrayList<>(courseList);
    }
    
    // Clears all courses from the model
    public void clearAllCourses() {
        int size = courseList.size();
        if (size > 0) {
            courseList.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    // Updates the entire course list and refreshes the table
    public void setCourses(List<Course> courses) {
        courseList.clear();
        if (courses != null) {
            courseList.addAll(courses);
        }
        fireTableDataChanged();
    }
    
    // Gets the course at the specified index (alias for getCourse for consistency)
    public Course getCourseAt(int rowIndex) {
        return getCourse(rowIndex);
    }
    
    // Inserts a course at the specified index
    public void insertCourse(int index, Course course) {
        if (course != null && index >= 0 && index <= courseList.size()) {
            courseList.add(index, course);
            fireTableRowsInserted(index, index);
        }
    }
}