package com.cgpacalculator.view;

import com.cgpacalculator.model.Course;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;public class CourseTransferHandler extends TransferHandler {
    
    // Data flavor for course objects
    public static final DataFlavor COURSE_FLAVOR = new DataFlavor(Course.class, "Course Object");
    public static final DataFlavor COURSE_LIST_FLAVOR = new DataFlavor(List.class, "Course List");
    
    private final CourseTableModel tableModel;
    private int draggedRowIndex = -1;public CourseTransferHandler(CourseTableModel tableModel) {
        this.tableModel = tableModel;
    }
    
    // Determines the source actions supported by this handler
    @Override
    public int getSourceActions(JComponent component) {
        return COPY_OR_MOVE;
    }
    
    // Creates a transferable object for the selected course data
    @Override
    protected Transferable createTransferable(JComponent component) {
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            int selectedRow = table.getSelectedRow();
            
            if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                draggedRowIndex = selectedRow;
                Course course = tableModel.getCourseAt(selectedRow);
                return new CourseTransferable(course, selectedRow);
            }
        }
        return null;
    }
    
    // Handles the completion of the drag operation
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == MOVE && draggedRowIndex >= 0) {
            // The move operation is handled in importData, so we just reset the index
            draggedRowIndex = -1;
        }
    }
    
    // Determines if the handler can import the given data flavors
    @Override
    public boolean canImport(TransferSupport support) {
        // Check if we can handle the data flavor
        boolean canImport = support.isDataFlavorSupported(COURSE_FLAVOR) ||
                           support.isDataFlavorSupported(DataFlavor.stringFlavor);
        
        if (canImport && support.isDrop()) {
            // Set the drop action for visual feedback
            support.setDropAction(MOVE);
            return true;
        }
        
        return canImport;
    }
    
    // Handles the import of transferred data
    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        
        try {
            if (support.isDataFlavorSupported(COURSE_FLAVOR)) {
                return handleCourseImport(support);
            } else if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return handleStringImport(support);
            }
        } catch (Exception e) {
            System.err.println("Error during drag and drop import: " + e.getMessage());
        }
        
        return false;
    }
    
    // Handles importing course objects (for reordering)
    private boolean handleCourseImport(TransferSupport support) throws Exception {
        CourseTransferable transferable = (CourseTransferable) support.getTransferable();
        Course course = transferable.getCourse();
        int sourceIndex = transferable.getSourceIndex();
        
        if (support.isDrop()) {
            JTable.DropLocation dropLocation = (JTable.DropLocation) support.getDropLocation();
            int targetIndex = dropLocation.getRow();
            
            // Perform the reordering operation
            if (sourceIndex != targetIndex && targetIndex >= 0) {
                return reorderCourse(sourceIndex, targetIndex);
            }
        }
        
        return false;
    }
    
    // Handles importing string data (for course creation from text)
    private boolean handleStringImport(TransferSupport support) throws Exception {
        String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        
        // Try to parse the string as course data
        Course course = parseStringToCourse(data);
        if (course != null) {
            tableModel.addCourse(course);
            return true;
        }
        
        return false;
    }
    
    // Reorders a course from source index to target index
    private boolean reorderCourse(int sourceIndex, int targetIndex) {
        try {
            Course course = tableModel.getCourseAt(sourceIndex);
            
            // Remove from source position
            tableModel.removeCourse(sourceIndex);
            
            // Adjust target index if necessary
            if (targetIndex > sourceIndex) {
                targetIndex--;
            }
            
            // Insert at target position
            tableModel.insertCourse(targetIndex, course);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error reordering course: " + e.getMessage());
            return false;
        }
    }
    
    // Method comment
    private Course parseStringToCourse(String data) {
        try {
            // Try comma-separated format first
            String[] parts = data.split(",");
            if (parts.length < 3) {
                // Try tab-separated format
                parts = data.split("\t");
            }
            
            if (parts.length >= 3) {
                String courseName = parts[0].trim();
                int units = Integer.parseInt(parts[1].trim());
                String grade = parts[2].trim().toUpperCase();
                
                // Validate the parsed data
                if (courseName.length() > 0 && units >= 1 && units <= 6 && 
                    isValidGrade(grade)) {
                    return new Course(courseName, units, grade);
                }
            }
        } catch (NumberFormatException e) {
            // Invalid number format
        }
        
        return null;
    }
    
    // Validates if a grade string is valid
    private boolean isValidGrade(String grade) {
        return grade.matches("[ABCDEF]");
    }
    
    // Inner class for transferable course data
    private static class CourseTransferable implements Transferable {
        private final Course course;
        private final int sourceIndex;
        
        public CourseTransferable(Course course, int sourceIndex) {
            this.course = course;
            this.sourceIndex = sourceIndex;
        }
        
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{COURSE_FLAVOR, DataFlavor.stringFlavor};
        }
        
        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return COURSE_FLAVOR.equals(flavor) || DataFlavor.stringFlavor.equals(flavor);
        }
        
        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (COURSE_FLAVOR.equals(flavor)) {
                return this;
            } else if (DataFlavor.stringFlavor.equals(flavor)) {
                return course.getCourseName() + "," + course.getUnits() + "," + course.getLetterGrade();
            }
            throw new UnsupportedFlavorException(flavor);
        }
        
        public Course getCourse() {
            return course;
        }
        
        public int getSourceIndex() {
            return sourceIndex;
        }
    }
}