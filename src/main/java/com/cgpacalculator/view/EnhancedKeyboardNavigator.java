package com.cgpacalculator.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.ArrayList;public class EnhancedKeyboardNavigator {
    
    private final MainFrame mainFrame;
    private final List<Component> navigationOrder;
    private int currentFocusIndex = 0;
    
    // Navigation section identifiers
    public static final String CGPA_SECTION = "cgpa_section";
    public static final String COURSE_SECTION = "course_section";
    public static final String RESULTS_SECTION = "results_section";
    public static final String REFERENCE_SECTION = "reference_section";public EnhancedKeyboardNavigator(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.navigationOrder = new ArrayList<>();
        setupNavigationOrder();
        setupAdvancedKeyBindings();
    }
    
    // Sets up the logical navigation order for components
    private void setupNavigationOrder() {
        navigationOrder.clear();
        
        // Add components in logical tab order
        navigationOrder.add(mainFrame.getCurrentCGPAField());
        navigationOrder.add(mainFrame.getCumulativeUnitsField());
        navigationOrder.add(mainFrame.getCoursePanel().getCourseNameField());
        navigationOrder.add(mainFrame.getCoursePanel().getUnitsSpinner());
        navigationOrder.add(mainFrame.getCoursePanel().getGradeComboBox());
        navigationOrder.add(mainFrame.getCoursePanel().getAddCourseButton());
        navigationOrder.add(mainFrame.getCoursePanel().getCourseTable());
        navigationOrder.add(mainFrame.getCoursePanel().getRemoveCourseButton());
        navigationOrder.add(mainFrame.getCoursePanel().getClearAllButton());
        navigationOrder.add(mainFrame.getCalculateButton());
        navigationOrder.add(mainFrame.getSaveDataButton());
        navigationOrder.add(mainFrame.getLoadDataButton());
    }
    
    // Sets up advanced key bindings for enhanced navigation
    private void setupAdvancedKeyBindings() {
        JRootPane rootPane = mainFrame.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();
        
        // Section-based navigation
        setupSectionNavigation(inputMap, actionMap);
        
        // Enhanced tab navigation
        setupEnhancedTabNavigation(inputMap, actionMap);
        
        // Quick access shortcuts
        setupQuickAccessShortcuts(inputMap, actionMap);
        
        // Table-specific navigation
        setupTableNavigation(inputMap, actionMap);
        
        // Form navigation shortcuts
        setupFormNavigation(inputMap, actionMap);
    }
    
    // Sets up section-based navigation shortcuts
    private void setupSectionNavigation(InputMap inputMap, ActionMap actionMap) {
        // Alt+1 through Alt+4 for section navigation
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK), CGPA_SECTION);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK), COURSE_SECTION);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK), RESULTS_SECTION);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK), REFERENCE_SECTION);
        
        actionMap.put(CGPA_SECTION, new SectionNavigationAction(CGPA_SECTION));
        actionMap.put(COURSE_SECTION, new SectionNavigationAction(COURSE_SECTION));
        actionMap.put(RESULTS_SECTION, new SectionNavigationAction(RESULTS_SECTION));
        actionMap.put(REFERENCE_SECTION, new SectionNavigationAction(REFERENCE_SECTION));
    }
    
    // Sets up enhanced tab navigation
    private void setupEnhancedTabNavigation(InputMap inputMap, ActionMap actionMap) {
        // Ctrl+Tab for section cycling
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_DOWN_MASK), "cycle_sections");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 
                    InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "cycle_sections_reverse");
        
        actionMap.put("cycle_sections", new SectionCycleAction(true));
        actionMap.put("cycle_sections_reverse", new SectionCycleAction(false));
        
        // Enhanced component navigation
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.ALT_DOWN_MASK), "smart_tab_forward");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 
                    InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "smart_tab_backward");
        
        actionMap.put("smart_tab_forward", new SmartTabAction(true));
        actionMap.put("smart_tab_backward", new SmartTabAction(false));
    }
    
    // Sets up quick access shortcuts
    private void setupQuickAccessShortcuts(InputMap inputMap, ActionMap actionMap) {
        // Quick focus shortcuts
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "focus_cgpa");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), "focus_units");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "focus_course_name");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "focus_table");
        
        actionMap.put("focus_cgpa", new QuickFocusAction(mainFrame.getCurrentCGPAField()));
        actionMap.put("focus_units", new QuickFocusAction(mainFrame.getCumulativeUnitsField()));
        actionMap.put("focus_course_name", new QuickFocusAction(mainFrame.getCoursePanel().getCourseNameField()));
        actionMap.put("focus_table", new QuickFocusAction(mainFrame.getCoursePanel().getCourseTable()));
    }
    
    // Sets up table-specific navigation
    private void setupTableNavigation(InputMap inputMap, ActionMap actionMap) {
        JTable courseTable = mainFrame.getCoursePanel().getCourseTable();
        InputMap tableInputMap = courseTable.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap tableActionMap = courseTable.getActionMap();
        
        // Enhanced table navigation
        tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "edit_selected_course");
        tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "toggle_selection");
        tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.CTRL_DOWN_MASK), "select_first_row");
        tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, InputEvent.CTRL_DOWN_MASK), "select_last_row");
        
        tableActionMap.put("edit_selected_course", new TableEditAction(courseTable));
        tableActionMap.put("toggle_selection", new TableSelectionToggleAction(courseTable));
        tableActionMap.put("select_first_row", new TableNavigationAction(courseTable, true));
        tableActionMap.put("select_last_row", new TableNavigationAction(courseTable, false));
    }
    
    // Sets up form navigation shortcuts
    private void setupFormNavigation(InputMap inputMap, ActionMap actionMap) {
        // Form completion shortcuts
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "add_course_and_continue");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear_current_input");
        
        actionMap.put("add_course_and_continue", new AddCourseAndContinueAction());
        actionMap.put("clear_current_input", new ClearCurrentInputAction());
    }
    
    // Navigates to a specific section of the application
    public void navigateToSection(String sectionId) {
        Component targetComponent = null;
        
        switch (sectionId) {
            case CGPA_SECTION:
                targetComponent = mainFrame.getCurrentCGPAField();
                break;
            case COURSE_SECTION:
                targetComponent = mainFrame.getCoursePanel().getCourseNameField();
                break;
            case RESULTS_SECTION:
                targetComponent = mainFrame.getCalculateButton();
                break;
            case REFERENCE_SECTION:
                targetComponent = mainFrame.getReferencePanel();
                break;
        }
        
        if (targetComponent != null) {
            targetComponent.requestFocusInWindow();
            highlightSection(sectionId);
        }
    }
    
    // Highlights a section temporarily for visual feedback
    private void highlightSection(String sectionId) {
        // This could be enhanced with visual highlighting effects
        // For now, we'll just ensure the component is visible
        Component targetComponent = getSectionComponent(sectionId);
        if (targetComponent != null) {
            targetComponent.requestFocusInWindow();
            
            // Scroll to make sure the component is visible
            if (targetComponent instanceof JComponent) {
                ((JComponent) targetComponent).scrollRectToVisible(targetComponent.getBounds());
            }
        }
    }
    
    // Gets the main component for a section
    private Component getSectionComponent(String sectionId) {
        switch (sectionId) {
            case CGPA_SECTION:
                return mainFrame.getCurrentCGPAField().getParent();
            case COURSE_SECTION:
                return mainFrame.getCoursePanel();
            case RESULTS_SECTION:
                return mainFrame.getCalculateButton().getParent();
            case REFERENCE_SECTION:
                return mainFrame.getReferencePanel();
            default:
                return null;
        }
    }
    
    // Action class for section navigation
    private class SectionNavigationAction extends AbstractAction {
        private final String sectionId;
        
        public SectionNavigationAction(String sectionId) {
            this.sectionId = sectionId;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            navigateToSection(sectionId);
        }
    }
    
    // Action class for section cycling
    private class SectionCycleAction extends AbstractAction {
        private final boolean forward;
        private final String[] sections = {CGPA_SECTION, COURSE_SECTION, RESULTS_SECTION, REFERENCE_SECTION};
        private int currentSectionIndex = 0;
        
        public SectionCycleAction(boolean forward) {
            this.forward = forward;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (forward) {
                currentSectionIndex = (currentSectionIndex + 1) % sections.length;
            } else {
                currentSectionIndex = (currentSectionIndex - 1 + sections.length) % sections.length;
            }
            
            navigateToSection(sections[currentSectionIndex]);
        }
    }
    
    // Action class for smart tab navigation
    private class SmartTabAction extends AbstractAction {
        private final boolean forward;
        
        public SmartTabAction(boolean forward) {
            this.forward = forward;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            int currentIndex = navigationOrder.indexOf(focusOwner);
            
            if (currentIndex >= 0) {
                int nextIndex;
                if (forward) {
                    nextIndex = (currentIndex + 1) % navigationOrder.size();
                } else {
                    nextIndex = (currentIndex - 1 + navigationOrder.size()) % navigationOrder.size();
                }
                
                Component nextComponent = navigationOrder.get(nextIndex);
                if (nextComponent.isEnabled() && nextComponent.isVisible()) {
                    nextComponent.requestFocusInWindow();
                }
            }
        }
    }
    
    // Action class for quick focus operations
    private class QuickFocusAction extends AbstractAction {
        private final Component targetComponent;
        
        public QuickFocusAction(Component targetComponent) {
            this.targetComponent = targetComponent;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (targetComponent.isEnabled() && targetComponent.isVisible()) {
                targetComponent.requestFocusInWindow();
            }
        }
    }
    
    // Action class for table editing
    private class TableEditAction extends AbstractAction {
        private final JTable table;
        
        public TableEditAction(JTable table) {
            this.table = table;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                table.editCellAt(selectedRow, 0);
                Component editor = table.getEditorComponent();
                if (editor != null) {
                    editor.requestFocusInWindow();
                }
            }
        }
    }
    
    // Action class for table selection toggle
    private class TableSelectionToggleAction extends AbstractAction {
        private final JTable table;
        
        public TableSelectionToggleAction(JTable table) {
            this.table = table;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                if (table.isRowSelected(selectedRow)) {
                    table.removeRowSelectionInterval(selectedRow, selectedRow);
                } else {
                    table.addRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        }
    }
    
    // Action class for table navigation
    private class TableNavigationAction extends AbstractAction {
        private final JTable table;
        private final boolean toFirst;
        
        public TableNavigationAction(JTable table, boolean toFirst) {
            this.table = table;
            this.toFirst = toFirst;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getRowCount() > 0) {
                int targetRow = toFirst ? 0 : table.getRowCount() - 1;
                table.setRowSelectionInterval(targetRow, targetRow);
                table.scrollRectToVisible(table.getCellRect(targetRow, 0, true));
            }
        }
    }
    
    // Action class for adding course and continuing input
    private class AddCourseAndContinueAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Trigger add course button
            mainFrame.getCoursePanel().getAddCourseButton().doClick();
            
            // Return focus to course name field for quick entry
            SwingUtilities.invokeLater(() -> {
                mainFrame.getCoursePanel().getCourseNameField().requestFocusInWindow();
            });
        }
    }
    
    // Action class for clearing current input
    private class ClearCurrentInputAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            
            if (focusOwner instanceof JTextField) {
                ((JTextField) focusOwner).setText("");
            } else if (focusOwner instanceof JComboBox) {
                ((JComboBox<?>) focusOwner).setSelectedIndex(0);
            } else if (focusOwner instanceof JSpinner) {
                ((JSpinner) focusOwner).setValue(1);
            }
        }
    }
}