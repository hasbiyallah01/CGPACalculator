package com.cgpacalculator.view;

import com.cgpacalculator.model.Course;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;public class ContextMenuManager {public static void setupCourseTableContextMenu(JTable courseTable, CourseTableModel tableModel) {
        JPopupMenu contextMenu = createCourseTableContextMenu(courseTable, tableModel);
        
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleContextMenuTrigger(e, courseTable, contextMenu);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                handleContextMenuTrigger(e, courseTable, contextMenu);
            }
        });
    }
    
    // Creates the context menu for the course table
    private static JPopupMenu createCourseTableContextMenu(JTable courseTable, CourseTableModel tableModel) {
        JPopupMenu contextMenu = new JPopupMenu();
        
        // Add Course menu item
        JMenuItem addCourseItem = new JMenuItem("Add New Course");
        addCourseItem.setIcon(createIcon("add"));
        addCourseItem.addActionListener(e -> {
            // Trigger add course action
            triggerAddCourseAction(courseTable);
        });
        
        // Edit Course menu item
        JMenuItem editCourseItem = new JMenuItem("Edit Selected Course");
        editCourseItem.setIcon(createIcon("edit"));
        editCourseItem.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                editCourseInline(courseTable, selectedRow);
            }
        });
        
        // Remove Course menu item
        JMenuItem removeCourseItem = new JMenuItem("Remove Selected Course");
        removeCourseItem.setIcon(createIcon("remove"));
        removeCourseItem.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                removeCourseWithConfirmation(courseTable, tableModel, selectedRow);
            }
        });
        
        // Separator
        JSeparator separator1 = new JSeparator();
        
        // Copy Course Data menu item
        JMenuItem copyCourseItem = new JMenuItem("Copy Course Data");
        copyCourseItem.setIcon(createIcon("copy"));
        copyCourseItem.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                copyCourseData(tableModel, selectedRow);
            }
        });
        
        // Duplicate Course menu item
        JMenuItem duplicateCourseItem = new JMenuItem("Duplicate Course");
        duplicateCourseItem.setIcon(createIcon("duplicate"));
        duplicateCourseItem.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                duplicateCourse(tableModel, selectedRow);
            }
        });
        
        // Separator
        JSeparator separator2 = new JSeparator();
        
        // Move Up menu item
        JMenuItem moveUpItem = new JMenuItem("Move Up");
        moveUpItem.setIcon(createIcon("up"));
        moveUpItem.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow > 0) {
                moveCourse(tableModel, selectedRow, selectedRow - 1);
                courseTable.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            }
        });
        
        // Move Down menu item
        JMenuItem moveDownItem = new JMenuItem("Move Down");
        moveDownItem.setIcon(createIcon("down"));
        moveDownItem.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < tableModel.getRowCount() - 1) {
                moveCourse(tableModel, selectedRow, selectedRow + 1);
                courseTable.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
            }
        });
        
        // Separator
        JSeparator separator3 = new JSeparator();
        
        // Clear All Courses menu item
        JMenuItem clearAllItem = new JMenuItem("Clear All Courses");
        clearAllItem.setIcon(createIcon("clear"));
        clearAllItem.addActionListener(e -> {
            clearAllCoursesWithConfirmation(courseTable, tableModel);
        });
        
        // Add all items to the context menu
        contextMenu.add(addCourseItem);
        contextMenu.add(editCourseItem);
        contextMenu.add(removeCourseItem);
        contextMenu.add(separator1);
        contextMenu.add(copyCourseItem);
        contextMenu.add(duplicateCourseItem);
        contextMenu.add(separator2);
        contextMenu.add(moveUpItem);
        contextMenu.add(moveDownItem);
        contextMenu.add(separator3);
        contextMenu.add(clearAllItem);
        
        // Add dynamic menu state updates
        contextMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                updateContextMenuState(contextMenu, courseTable, tableModel);
            }
            
            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            
            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });
        
        return contextMenu;
    }
    
    // Creates a context menu for input fields
    public static void setupInputFieldContextMenu(JTextField textField) {
        JPopupMenu contextMenu = createInputFieldContextMenu(textField);
        textField.setComponentPopupMenu(contextMenu);
    }
    
    // Creates the context menu for input fields
    private static JPopupMenu createInputFieldContextMenu(JTextField textField) {
        JPopupMenu contextMenu = new JPopupMenu();
        
        // Cut menu item
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setIcon(createIcon("cut"));
        cutItem.addActionListener(e -> textField.cut());
        
        // Copy menu item
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setIcon(createIcon("copy"));
        copyItem.addActionListener(e -> textField.copy());
        
        // Paste menu item
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setIcon(createIcon("paste"));
        pasteItem.addActionListener(e -> textField.paste());
        
        // Separator
        JSeparator separator = new JSeparator();
        
        // Select All menu item
        JMenuItem selectAllItem = new JMenuItem("Select All");
        selectAllItem.addActionListener(e -> textField.selectAll());
        
        // Clear menu item
        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.setIcon(createIcon("clear"));
        clearItem.addActionListener(e -> textField.setText(""));
        
        contextMenu.add(cutItem);
        contextMenu.add(copyItem);
        contextMenu.add(pasteItem);
        contextMenu.add(separator);
        contextMenu.add(selectAllItem);
        contextMenu.add(clearItem);
        
        // Add dynamic state updates
        contextMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                boolean hasText = textField.getText().length() > 0;
                boolean hasSelection = textField.getSelectedText() != null;
                
                cutItem.setEnabled(hasSelection);
                copyItem.setEnabled(hasSelection);
                clearItem.setEnabled(hasText);
                selectAllItem.setEnabled(hasText);
            }
            
            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            
            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });
        
        return contextMenu;
    }
    
    // Handles context menu trigger events
    private static void handleContextMenuTrigger(MouseEvent e, JTable table, JPopupMenu contextMenu) {
        if (e.isPopupTrigger()) {
            // Select the row under the mouse if not already selected
            int row = table.rowAtPoint(e.getPoint());
            if (row >= 0 && !table.isRowSelected(row)) {
                table.setRowSelectionInterval(row, row);
            }
            
            // Show the context menu
            contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    // Updates the state of context menu items based on current selection
    private static void updateContextMenuState(JPopupMenu contextMenu, JTable table, CourseTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        boolean hasSelection = selectedRow >= 0;
        boolean hasMultipleRows = tableModel.getRowCount() > 1;
        boolean canMoveUp = hasSelection && selectedRow > 0;
        boolean canMoveDown = hasSelection && selectedRow < tableModel.getRowCount() - 1;
        
        // Update menu item states
        for (int i = 0; i < contextMenu.getComponentCount(); i++) {
            if (contextMenu.getComponent(i) instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) contextMenu.getComponent(i);
                String text = item.getText();
                
                switch (text) {
                    case "Edit Selected Course":
                    case "Remove Selected Course":
                    case "Copy Course Data":
                    case "Duplicate Course":
                        item.setEnabled(hasSelection);
                        break;
                    case "Move Up":
                        item.setEnabled(canMoveUp);
                        break;
                    case "Move Down":
                        item.setEnabled(canMoveDown);
                        break;
                    case "Clear All Courses":
                        item.setEnabled(tableModel.getRowCount() > 0);
                        break;
                }
            }
        }
    }
    
    // Triggers the add course action
    private static void triggerAddCourseAction(JTable table) {
        // Find the CoursePanel parent and trigger add course
        CoursePanel coursePanel = findCoursePanel(table);
        if (coursePanel != null) {
            coursePanel.getAddCourseButton().doClick();
        }
    }
    
    // Enables inline editing for a course
    private static void editCourseInline(JTable table, int row) {
        // Start editing the course name cell
        if (table.editCellAt(row, 0)) {
            table.getEditorComponent().requestFocusInWindow();
        }
    }
    
    // Removes a course with confirmation dialog
    private static void removeCourseWithConfirmation(JTable table, CourseTableModel tableModel, int row) {
        Course course = tableModel.getCourseAt(row);
        int result = JOptionPane.showConfirmDialog(
            table,
            "Are you sure you want to remove the course '" + course.getCourseName() + "'?",
            "Confirm Course Removal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            tableModel.removeCourse(row);
        }
    }
    
    // Copies course data to clipboard
    private static void copyCourseData(CourseTableModel tableModel, int row) {
        Course course = tableModel.getCourseAt(row);
        String courseData = String.format("%s\t%d\t%s\t%.2f", 
            course.getCourseName(), 
            course.getUnits(), 
            course.getLetterGrade(),
            course.getGradePoints()
        );
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(courseData), null);
        
        // Show feedback
        JOptionPane.showMessageDialog(null, "Course data copied to clipboard", 
                                    "Copy Successful", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Duplicates a course
    private static void duplicateCourse(CourseTableModel tableModel, int row) {
        Course originalCourse = tableModel.getCourseAt(row);
        Course duplicatedCourse = new Course(
            originalCourse.getCourseName() + " (Copy)",
            originalCourse.getUnits(),
            originalCourse.getLetterGrade()
        );
        
        tableModel.insertCourse(row + 1, duplicatedCourse);
    }
    
    // Moves a course from one position to another
    private static void moveCourse(CourseTableModel tableModel, int fromIndex, int toIndex) {
        Course course = tableModel.getCourseAt(fromIndex);
        tableModel.removeCourse(fromIndex);
        tableModel.insertCourse(toIndex, course);
    }
    
    // Clears all courses with confirmation
    private static void clearAllCoursesWithConfirmation(JTable table, CourseTableModel tableModel) {
        int result = JOptionPane.showConfirmDialog(
            table,
            "Are you sure you want to remove all courses? This action cannot be undone.",
            "Confirm Clear All",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            tableModel.clearAllCourses();
        }
    }
    
    // Finds the CoursePanel parent component
    private static CoursePanel findCoursePanel(JTable table) {
        java.awt.Container parent = table.getParent();
        while (parent != null) {
            if (parent instanceof CoursePanel) {
                return (CoursePanel) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
    
    // Creates simple icons for menu items (placeholder implementation)
    private static Icon createIcon(String type) {
        // For now, return null - in a full implementation, you would load actual icons
        // This could be enhanced to load icons from resources
        return null;
    }
}