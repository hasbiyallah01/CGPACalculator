package com.cgpacalculator.test;

import com.cgpacalculator.view.ReferencePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

/**
 * Manual verification application for testing accessibility and visual feedback
 * features of the ReferencePanel toggle functionality.
 * 
 * This application provides a visual interface to test:
 * - Hover effects on both toggle and close buttons
 * - Tooltip text accuracy for all button states
 * - Keyboard navigation functionality
 * - Cursor changes on button hover
 */
public class AccessibilityVerificationApp extends JFrame {
    
    private ReferencePanel referencePanel;
    private JTextArea logArea;
    private JButton toggleButton;
    private JButton closeButton;
    
    public AccessibilityVerificationApp() {
        setTitle("Reference Panel Accessibility Verification");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        initializeComponents();
        setupLayout();
        setupEventLogging();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Create the reference panel
        referencePanel = new ReferencePanel();
        
        // Get access to private buttons for testing
        try {
            Field toggleButtonField = ReferencePanel.class.getDeclaredField("toggleButton");
            toggleButtonField.setAccessible(true);
            toggleButton = (JButton) toggleButtonField.get(referencePanel);
            
            Field closeButtonField = ReferencePanel.class.getDeclaredField("closeButton");
            closeButtonField.setAccessible(true);
            closeButton = (JButton) closeButtonField.get(referencePanel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create log area for displaying test results
        logArea = new JTextArea(15, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setBackground(new Color(248, 249, 250));
        
        log("=== Reference Panel Accessibility Verification ===");
        log("This application tests all accessibility and visual feedback features.");
        log("Interact with the reference panel below and observe the logged results.");
        log("");
    }
    
    private void setupLayout() {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add reference panel
        JPanel referencePanelContainer = new JPanel(new BorderLayout());
        referencePanelContainer.setBorder(BorderFactory.createTitledBorder("Reference Panel Under Test"));
        referencePanelContainer.add(referencePanel, BorderLayout.CENTER);
        
        // Add test controls
        JPanel controlPanel = createControlPanel();
        
        // Add log area
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Test Results Log"));
        
        mainPanel.add(referencePanelContainer, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(logScrollPane, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Accessibility Test Controls"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Test buttons
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createTestButton("Test Hover Effects", this::testHoverEffects), gbc);
        
        gbc.gridx = 1;
        panel.add(createTestButton("Test Tooltips", this::testTooltips), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createTestButton("Test Keyboard Navigation", this::testKeyboardNavigation), gbc);
        
        gbc.gridx = 1;
        panel.add(createTestButton("Test Cursor Changes", this::testCursorChanges), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(createTestButton("Run All Tests", this::runAllTests), gbc);
        
        gbc.gridy = 3;
        panel.add(createTestButton("Clear Log", this::clearLog), gbc);
        
        return panel;
    }
    
    private JButton createTestButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
    
    private void setupEventLogging() {
        // Log initial state
        logCurrentState();
        
        // Add listeners to track state changes
        if (toggleButton != null) {
            toggleButton.addActionListener(e -> {
                log("Toggle button clicked");
                SwingUtilities.invokeLater(this::logCurrentState);
            });
        }
        
        if (closeButton != null) {
            closeButton.addActionListener(e -> {
                log("Close button clicked");
                SwingUtilities.invokeLater(this::logCurrentState);
            });
        }
    }
    
    private void testHoverEffects(ActionEvent e) {
        log("\n=== Testing Hover Effects ===");
        
        // Test toggle button hover
        log("Testing toggle button hover effects:");
        Color initialToggleColor = toggleButton.getForeground();
        log("  Initial toggle button color: " + colorToString(initialToggleColor));
        
        // Simulate hover on toggle button
        simulateMouseHover(toggleButton, true);
        Color hoverToggleColor = toggleButton.getForeground();
        log("  Hover toggle button color: " + colorToString(hoverToggleColor));
        
        // Check if color changed
        if (!initialToggleColor.equals(hoverToggleColor)) {
            log("  ✓ Toggle button hover effect working correctly");
        } else {
            log("  ✗ Toggle button hover effect not detected");
        }
        
        // Reset hover
        simulateMouseHover(toggleButton, false);
        Color resetToggleColor = toggleButton.getForeground();
        log("  Reset toggle button color: " + colorToString(resetToggleColor));
        
        if (initialToggleColor.equals(resetToggleColor)) {
            log("  ✓ Toggle button hover reset working correctly");
        } else {
            log("  ✗ Toggle button hover reset not working");
        }
        
        // Test close button hover (if visible)
        if (closeButton.isVisible()) {
            log("\nTesting close button hover effects:");
            Color initialCloseColor = closeButton.getForeground();
            log("  Initial close button color: " + colorToString(initialCloseColor));
            
            simulateMouseHover(closeButton, true);
            Color hoverCloseColor = closeButton.getForeground();
            log("  Hover close button color: " + colorToString(hoverCloseColor));
            
            if (!initialCloseColor.equals(hoverCloseColor)) {
                log("  ✓ Close button hover effect working correctly");
            } else {
                log("  ✗ Close button hover effect not detected");
            }
            
            simulateMouseHover(closeButton, false);
            Color resetCloseColor = closeButton.getForeground();
            log("  Reset close button color: " + colorToString(resetCloseColor));
            
            if (initialCloseColor.equals(resetCloseColor)) {
                log("  ✓ Close button hover reset working correctly");
            } else {
                log("  ✗ Close button hover reset not working");
            }
        } else {
            log("Close button not visible - expand panel to test close button hover");
        }
    }
    
    private void testTooltips(ActionEvent e) {
        log("\n=== Testing Tooltip Accuracy ===");
        
        // Test toggle button tooltips in both states
        boolean wasExpanded = referencePanel.isExpanded();
        
        // Test collapsed state
        referencePanel.setExpanded(false);
        String collapsedTooltip = toggleButton.getToolTipText();
        log("Toggle button tooltip (collapsed): \"" + collapsedTooltip + "\"");
        
        if (collapsedTooltip != null && collapsedTooltip.contains("show")) {
            log("  ✓ Collapsed tooltip is correct");
        } else {
            log("  ✗ Collapsed tooltip is incorrect");
        }
        
        // Test expanded state
        referencePanel.setExpanded(true);
        String expandedTooltip = toggleButton.getToolTipText();
        log("Toggle button tooltip (expanded): \"" + expandedTooltip + "\"");
        
        if (expandedTooltip != null && expandedTooltip.contains("hide") && expandedTooltip.contains("ESC")) {
            log("  ✓ Expanded tooltip is correct");
        } else {
            log("  ✗ Expanded tooltip is incorrect");
        }
        
        // Test close button tooltip
        if (closeButton.isVisible()) {
            String closeTooltip = closeButton.getToolTipText();
            log("Close button tooltip: \"" + closeTooltip + "\"");
            
            if (closeTooltip != null && closeTooltip.contains("ESC")) {
                log("  ✓ Close button tooltip is correct");
            } else {
                log("  ✗ Close button tooltip is incorrect");
            }
        }
        
        // Restore original state
        referencePanel.setExpanded(wasExpanded);
    }
    
    private void testKeyboardNavigation(ActionEvent e) {
        log("\n=== Testing Keyboard Navigation ===");
        
        // Test toggle button keyboard support
        log("Testing toggle button keyboard support:");
        boolean initialState = referencePanel.isExpanded();
        
        // Simulate Enter key on toggle button
        simulateKeyPress(toggleButton, java.awt.event.KeyEvent.VK_ENTER);
        boolean afterEnter = referencePanel.isExpanded();
        
        if (initialState != afterEnter) {
            log("  ✓ Toggle button responds to Enter key");
        } else {
            log("  ✗ Toggle button does not respond to Enter key");
        }
        
        // Simulate Space key on toggle button
        simulateKeyPress(toggleButton, java.awt.event.KeyEvent.VK_SPACE);
        boolean afterSpace = referencePanel.isExpanded();
        
        if (afterEnter != afterSpace) {
            log("  ✓ Toggle button responds to Space key");
        } else {
            log("  ✗ Toggle button does not respond to Space key");
        }
        
        // Test ESC key functionality
        if (referencePanel.isExpanded()) {
            log("Testing ESC key functionality:");
            simulateKeyPress(toggleButton, java.awt.event.KeyEvent.VK_ESCAPE);
            boolean afterEsc = referencePanel.isExpanded();
            
            if (!afterEsc) {
                log("  ✓ ESC key closes expanded panel");
            } else {
                log("  ✗ ESC key does not close expanded panel");
            }
        }
        
        // Test close button keyboard support (if visible)
        referencePanel.setExpanded(true);
        if (closeButton.isVisible()) {
            log("Testing close button keyboard support:");
            
            simulateKeyPress(closeButton, java.awt.event.KeyEvent.VK_ENTER);
            if (!referencePanel.isExpanded()) {
                log("  ✓ Close button responds to Enter key");
            } else {
                log("  ✗ Close button does not respond to Enter key");
            }
        }
    }
    
    private void testCursorChanges(ActionEvent e) {
        log("\n=== Testing Cursor Changes ===");
        
        // Test toggle button cursor
        Cursor toggleCursor = toggleButton.getCursor();
        log("Toggle button cursor type: " + getCursorName(toggleCursor.getType()));
        
        if (toggleCursor.getType() == Cursor.HAND_CURSOR) {
            log("  ✓ Toggle button has correct hand cursor");
        } else {
            log("  ✗ Toggle button does not have hand cursor");
        }
        
        // Test close button cursor (if visible)
        if (closeButton.isVisible()) {
            Cursor closeCursor = closeButton.getCursor();
            log("Close button cursor type: " + getCursorName(closeCursor.getType()));
            
            if (closeCursor.getType() == Cursor.HAND_CURSOR) {
                log("  ✓ Close button has correct hand cursor");
            } else {
                log("  ✗ Close button does not have hand cursor");
            }
        } else {
            log("Close button not visible - expand panel to test cursor");
        }
    }
    
    private void runAllTests(ActionEvent e) {
        log("\n" + "=".repeat(50));
        log("RUNNING ALL ACCESSIBILITY TESTS");
        log("=".repeat(50));
        
        testHoverEffects(e);
        testTooltips(e);
        testKeyboardNavigation(e);
        testCursorChanges(e);
        
        log("\n" + "=".repeat(50));
        log("ALL TESTS COMPLETED");
        log("=".repeat(50));
    }
    
    private void clearLog(ActionEvent e) {
        logArea.setText("");
        log("=== Reference Panel Accessibility Verification ===");
        log("Log cleared. Ready for new tests.");
        log("");
        logCurrentState();
    }
    
    private void simulateMouseHover(JButton button, boolean enter) {
        java.awt.event.MouseEvent event = new java.awt.event.MouseEvent(
            button, 
            enter ? java.awt.event.MouseEvent.MOUSE_ENTERED : java.awt.event.MouseEvent.MOUSE_EXITED,
            System.currentTimeMillis(), 0, 0, 0, 1, false
        );
        
        for (var listener : button.getMouseListeners()) {
            if (enter) {
                listener.mouseEntered(event);
            } else {
                listener.mouseExited(event);
            }
        }
    }
    
    private void simulateKeyPress(JButton button, int keyCode) {
        java.awt.event.KeyEvent event = new java.awt.event.KeyEvent(
            button, java.awt.event.KeyEvent.KEY_PRESSED,
            System.currentTimeMillis(), 0, keyCode, java.awt.event.KeyEvent.CHAR_UNDEFINED
        );
        
        for (var listener : button.getKeyListeners()) {
            listener.keyPressed(event);
        }
    }
    
    private void logCurrentState() {
        log("Current panel state: " + (referencePanel.isExpanded() ? "EXPANDED" : "COLLAPSED"));
        log("Toggle button text: \"" + toggleButton.getText() + "\"");
        log("Close button visible: " + closeButton.isVisible());
    }
    
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    private String colorToString(Color color) {
        return String.format("RGB(%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    private String getCursorName(int cursorType) {
        switch (cursorType) {
            case Cursor.DEFAULT_CURSOR: return "DEFAULT";
            case Cursor.CROSSHAIR_CURSOR: return "CROSSHAIR";
            case Cursor.TEXT_CURSOR: return "TEXT";
            case Cursor.WAIT_CURSOR: return "WAIT";
            case Cursor.SW_RESIZE_CURSOR: return "SW_RESIZE";
            case Cursor.SE_RESIZE_CURSOR: return "SE_RESIZE";
            case Cursor.NW_RESIZE_CURSOR: return "NW_RESIZE";
            case Cursor.NE_RESIZE_CURSOR: return "NE_RESIZE";
            case Cursor.N_RESIZE_CURSOR: return "N_RESIZE";
            case Cursor.S_RESIZE_CURSOR: return "S_RESIZE";
            case Cursor.W_RESIZE_CURSOR: return "W_RESIZE";
            case Cursor.E_RESIZE_CURSOR: return "E_RESIZE";
            case Cursor.HAND_CURSOR: return "HAND";
            case Cursor.MOVE_CURSOR: return "MOVE";
            default: return "UNKNOWN(" + cursorType + ")";
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AccessibilityVerificationApp();
        });
    }
}