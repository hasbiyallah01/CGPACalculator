package com.cgpacalculator.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.cgpacalculator.utils.Constants;

// ReferencePanel class that displays grading scale, calculation formulas,
// and degree classification ranges with color coding.
// Features a collapsible/expandable design for better UI organization.
public class ReferencePanel extends JPanel {
    
    // UI Components
    private final JButton toggleButton;
    private final JButton closeButton;
    private final JPanel contentPanel;
    private final JPanel gradingScalePanel;
    private final JPanel formulasPanel;
    private final JPanel classificationsPanel;
    
    // State management
    private boolean isExpanded;
    
    // Constants for UI
    private static final String EXPANDED_TEXT = "▼ Hide Reference Information";
    private static final String COLLAPSED_TEXT = "▶ Show Reference Information";
    private static final Color PANEL_BACKGROUND = new Color(248, 249, 250);
    private static final Font HEADER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    private static final Font FORMULA_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 11);
    
    // Constructor initializes the reference panel with all components
    public ReferencePanel() {
        this.isExpanded = false; // Start collapsed for better initial UI
        
        // Initialize components
        this.toggleButton = createToggleButton();
        this.closeButton = createCloseButton();
        this.gradingScalePanel = createGradingScalePanel();
        this.formulasPanel = createFormulasPanel();
        this.classificationsPanel = createClassificationsPanel();
        this.contentPanel = createContentPanel();
        
        // Setup panel layout and behavior
        setupPanelLayout();
        setupToggleBehavior();
        setupKeyboardHandling();
        
        // Apply button styling
        applyButtonStyling();
        
        // Initially hide content
        updateVisibility();
    }
    
    // Creates the toggle button for expanding/collapsing the panel
    private JButton createToggleButton() {
        JButton button = new JButton(COLLAPSED_TEXT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(HEADER_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Set initial tooltip based on collapsed state
        updateToggleButtonTooltip(button, false);
        
        // Add keyboard support
        button.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER || 
                    e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    toggleExpansion();
                }
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE && isExpanded) {
                    setExpanded(false);
                }
            }
        });
        
        // Add hover effects for better visual feedback
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(new Color(70, 130, 255)); // Primary blue color
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(Color.BLACK); // Reset to default
            }
        });
        
        return button;
    }
    
    // Creates the close button for collapsing the panel when expanded
    private JButton createCloseButton() {
        JButton button = new JButton("✕");
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        button.setPreferredSize(new Dimension(25, 25));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Close reference panel (ESC)");
        
        // Add action listener that properly calls setExpanded(false)
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExpanded(false);
            }
        });
        
        // Add keyboard support for close button
        button.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER || 
                    e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE ||
                    e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    setExpanded(false);
                    e.consume(); // Consume the event to prevent further processing
                }
            }
        });
        
        // Add focus listener for better accessibility and tooltip updates
        button.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                button.setToolTipText("Close reference panel (ESC, Enter, or Space)");
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                button.setToolTipText("Close reference panel (ESC)");
            }
        });
        
        // Add hover effects for better visual feedback
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(new Color(220, 53, 69)); // Danger red color
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(Color.BLACK); // Reset to default
            }
        });
        
        // Initially hidden since panel starts collapsed
        button.setVisible(false);
        
        return button;
    }
    
    // Creates the grading scale display panel
    private JPanel createGradingScalePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Grading Scale", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            HEADER_FONT
        ));
        panel.setBackground(PANEL_BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Header row
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel gradeHeader = new JLabel("Grade");
        gradeHeader.setFont(HEADER_FONT);
        panel.add(gradeHeader, gbc);
        
        gbc.gridx = 1;
        JLabel pointsHeader = new JLabel("Points");
        pointsHeader.setFont(HEADER_FONT);
        panel.add(pointsHeader, gbc);
        
        // Add separator
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        
        // Grade mappings
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        int row = 2;
        for (String grade : Constants.VALID_GRADES) {
            double points = Constants.getGradePoints(grade);
            
            gbc.gridx = 0; gbc.gridy = row;
            JLabel gradeLabel = new JLabel(grade);
            gradeLabel.setFont(CONTENT_FONT);
            panel.add(gradeLabel, gbc);
            
            gbc.gridx = 1;
            JLabel pointsLabel = new JLabel(String.format("%.1f", points));
            pointsLabel.setFont(CONTENT_FONT);
            panel.add(pointsLabel, gbc);
            
            row++;
        }
        
        return panel;
    }
    
    // Creates the calculation formulas display panel
    private JPanel createFormulasPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Calculation Formulas", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            HEADER_FONT
        ));
        panel.setBackground(PANEL_BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // GPA Formula
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel gpaLabel = new JLabel("GPA Formula:");
        gpaLabel.setFont(HEADER_FONT);
        panel.add(gpaLabel, gbc);
        
        gbc.gridy = 1;
        JLabel gpaFormula = new JLabel("GPA = Σ(Units × Grade Points) / Σ(Units)");
        gpaFormula.setFont(FORMULA_FONT);
        gpaFormula.setForeground(new Color(0, 0, 139)); // Dark blue
        panel.add(gpaFormula, gbc);
        
        // CGPA Formula
        gbc.gridy = 2; gbc.insets = new Insets(15, 10, 5, 10);
        JLabel cgpaLabel = new JLabel("CGPA Formula:");
        cgpaLabel.setFont(HEADER_FONT);
        panel.add(cgpaLabel, gbc);
        
        gbc.gridy = 3; gbc.insets = new Insets(5, 10, 5, 10);
        JLabel cgpaFormula = new JLabel("CGPA = (Previous Total Points + Current Points) / (Previous Total Units + Current Units)");
        cgpaFormula.setFont(FORMULA_FONT);
        cgpaFormula.setForeground(new Color(0, 0, 139)); // Dark blue
        panel.add(cgpaFormula, gbc);
        
        // Explanation
        gbc.gridy = 4; gbc.insets = new Insets(10, 10, 5, 10);
        JTextArea explanation = new JTextArea(
            "Where:\n" +
            "• Units = Credit hours for each course\n" +
            "• Grade Points = Numerical value of letter grade\n" +
            "• Total Points = Units × Grade Points for each course"
        );
        explanation.setFont(CONTENT_FONT);
        explanation.setBackground(PANEL_BACKGROUND);
        explanation.setEditable(false);
        explanation.setForeground(new Color(64, 64, 64)); // Dark gray
        panel.add(explanation, gbc);
        
        return panel;
    }
    
    // Creates the degree classifications display panel with color coding
    private JPanel createClassificationsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Degree Classifications", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            HEADER_FONT
        ));
        panel.setBackground(PANEL_BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 10, 3, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Header row
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel cgpaRangeHeader = new JLabel("CGPA Range");
        cgpaRangeHeader.setFont(HEADER_FONT);
        panel.add(cgpaRangeHeader, gbc);
        
        gbc.gridx = 1;
        JLabel classificationHeader = new JLabel("Classification");
        classificationHeader.setFont(HEADER_FONT);
        panel.add(classificationHeader, gbc);
        
        // Add separator
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        
        // Classification ranges with color coding
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        
        // First Class
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel firstClassRange = new JLabel("4.50 - 5.00");
        firstClassRange.setFont(CONTENT_FONT);
        panel.add(firstClassRange, gbc);
        
        gbc.gridx = 1;
        JLabel firstClass = new JLabel(Constants.FIRST_CLASS);
        firstClass.setFont(CONTENT_FONT);
        firstClass.setForeground(Color.decode(Constants.FIRST_CLASS_COLOR));
        firstClass.setOpaque(true);
        firstClass.setBackground(new Color(Color.decode(Constants.FIRST_CLASS_COLOR).getRed(), 
                                          Color.decode(Constants.FIRST_CLASS_COLOR).getGreen(), 
                                          Color.decode(Constants.FIRST_CLASS_COLOR).getBlue(), 30));
        firstClass.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        panel.add(firstClass, gbc);
        
        // Second Class Upper
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel secondUpperRange = new JLabel("3.50 - 4.49");
        secondUpperRange.setFont(CONTENT_FONT);
        panel.add(secondUpperRange, gbc);
        
        gbc.gridx = 1;
        JLabel secondUpper = new JLabel(Constants.SECOND_CLASS_UPPER);
        secondUpper.setFont(CONTENT_FONT);
        secondUpper.setForeground(Color.decode(Constants.SECOND_CLASS_UPPER_COLOR));
        secondUpper.setOpaque(true);
        secondUpper.setBackground(new Color(Color.decode(Constants.SECOND_CLASS_UPPER_COLOR).getRed(), 
                                           Color.decode(Constants.SECOND_CLASS_UPPER_COLOR).getGreen(), 
                                           Color.decode(Constants.SECOND_CLASS_UPPER_COLOR).getBlue(), 30));
        secondUpper.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        panel.add(secondUpper, gbc);
        
        // Second Class Lower
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel secondLowerRange = new JLabel("2.50 - 3.49");
        secondLowerRange.setFont(CONTENT_FONT);
        panel.add(secondLowerRange, gbc);
        
        gbc.gridx = 1;
        JLabel secondLower = new JLabel(Constants.SECOND_CLASS_LOWER);
        secondLower.setFont(CONTENT_FONT);
        secondLower.setForeground(Color.decode(Constants.SECOND_CLASS_LOWER_COLOR));
        secondLower.setOpaque(true);
        secondLower.setBackground(new Color(Color.decode(Constants.SECOND_CLASS_LOWER_COLOR).getRed(), 
                                           Color.decode(Constants.SECOND_CLASS_LOWER_COLOR).getGreen(), 
                                           Color.decode(Constants.SECOND_CLASS_LOWER_COLOR).getBlue(), 30));
        secondLower.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        panel.add(secondLower, gbc);
        
        // Third Class
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel thirdClassRange = new JLabel("1.50 - 2.49");
        thirdClassRange.setFont(CONTENT_FONT);
        panel.add(thirdClassRange, gbc);
        
        gbc.gridx = 1;
        JLabel thirdClass = new JLabel(Constants.THIRD_CLASS);
        thirdClass.setFont(CONTENT_FONT);
        thirdClass.setForeground(Color.decode(Constants.THIRD_CLASS_COLOR));
        thirdClass.setOpaque(true);
        thirdClass.setBackground(new Color(Color.decode(Constants.THIRD_CLASS_COLOR).getRed(), 
                                          Color.decode(Constants.THIRD_CLASS_COLOR).getGreen(), 
                                          Color.decode(Constants.THIRD_CLASS_COLOR).getBlue(), 30));
        thirdClass.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        panel.add(thirdClass, gbc);
        
        // Fail
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel failRange = new JLabel("0.00 - 1.49");
        failRange.setFont(CONTENT_FONT);
        panel.add(failRange, gbc);
        
        gbc.gridx = 1;
        JLabel fail = new JLabel(Constants.FAIL_CLASS);
        fail.setFont(CONTENT_FONT);
        fail.setForeground(Color.decode(Constants.FAIL_CLASS_COLOR));
        fail.setOpaque(true);
        fail.setBackground(new Color(Color.decode(Constants.FAIL_CLASS_COLOR).getRed(), 
                                    Color.decode(Constants.FAIL_CLASS_COLOR).getGreen(), 
                                    Color.decode(Constants.FAIL_CLASS_COLOR).getBlue(), 30));
        fail.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        panel.add(fail, gbc);
        
        return panel;
    }
    
    // Creates the main content panel that contains all reference information
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(getBackground());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        
        // Add grading scale panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.weighty = 0.0;
        panel.add(gradingScalePanel, gbc);
        
        // Add formulas panel
        gbc.gridy = 1;
        panel.add(formulasPanel, gbc);
        
        // Add classifications panel
        gbc.gridy = 2;
        panel.add(classificationsPanel, gbc);
        
        return panel;
    }
    
    // Sets up the main panel layout
    private void setupPanelLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Create header panel with proper layout to contain both buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false); // Maintain transparency
        
        // Add toggle button to the left/center area
        headerPanel.add(toggleButton, BorderLayout.CENTER);
        
        // Create a container for the close button to ensure proper positioning
        JPanel closeButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeButtonContainer.setOpaque(false);
        closeButtonContainer.add(closeButton);
        
        // Add close button container to the right
        headerPanel.add(closeButtonContainer, BorderLayout.EAST);
        
        // Add header panel at the top
        add(headerPanel, BorderLayout.NORTH);
        
        // Add content panel in the center
        add(contentPanel, BorderLayout.CENTER);
    }
    
    // Sets up the toggle behavior for expanding/collapsing the panel
    private void setupToggleBehavior() {
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleExpansion();
            }
        });
    }
    
    // Sets up global keyboard handling for the panel
    private void setupKeyboardHandling() {
        // Add key binding for ESC key to close panel when expanded
        InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = getActionMap();
        
        // Bind ESC key to close action
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "closePanel");
        actionMap.put("closePanel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isExpanded) {
                    setExpanded(false);
                }
            }
        });
        
        // Make panel focusable to receive key events
        setFocusable(true);
    }
    
    // Toggles the expansion state of the panel
    public void toggleExpansion() {
        // Store previous state for validation
        boolean previousState = isExpanded;
        
        // Toggle the state
        isExpanded = !isExpanded;
        
        // Update all visual components
        updateVisibility();
        
        // Verify the toggle worked correctly in both directions
        if (previousState == isExpanded) {
            // If state didn't change, force it to change
            System.err.println("Warning: Toggle state did not change properly, forcing update");
            isExpanded = !previousState;
            updateVisibility();
        }
        
        // Ensure proper layout revalidation and parent container updates
        // This must be done after visibility updates to ensure proper layout calculation
        SwingUtilities.invokeLater(() -> {
            revalidateLayoutHierarchy();
        });
        
        // Request focus on appropriate component after toggle
        SwingUtilities.invokeLater(() -> {
            if (isExpanded) {
                // When expanded, focus on close button for easy keyboard access
                closeButton.requestFocusInWindow();
            } else {
                // When collapsed, focus on toggle button
                toggleButton.requestFocusInWindow();
            }
        });
    }
    
    // Updates the visibility of content based on expansion state
    private void updateVisibility() {
        contentPanel.setVisible(isExpanded);
        
        // Update toggle button text and ensure arrow direction is correct
        updateToggleButtonState();
        
        // Update close button visibility - only show when expanded
        closeButton.setVisible(isExpanded);
        
        // Update tooltips for both buttons
        updateToggleButtonTooltip(toggleButton, isExpanded);
        updateCloseButtonTooltip();
    }
    
    // Updates the toggle button text and visual state
    private void updateToggleButtonState() {
        // Ensure the text is updated correctly with proper arrow direction
        String newText = isExpanded ? EXPANDED_TEXT : COLLAPSED_TEXT;
        toggleButton.setText(newText);
        
        // Force button to repaint to ensure text change is visible
        SwingUtilities.invokeLater(() -> {
            toggleButton.revalidate();
            toggleButton.repaint();
        });
        
        // Update button accessibility properties
        toggleButton.getAccessibleContext().setAccessibleDescription(
            isExpanded ? "Hide reference information panel" : "Show reference information panel"
        );
    }
    
    // Updates the toggle button tooltip based on current state
    private void updateToggleButtonTooltip(JButton button, boolean expanded) {
        if (expanded) {
            button.setToolTipText("Click to hide reference information (or press ESC)");
        } else {
            button.setToolTipText("Click to show reference information");
        }
    }
    
    // Updates the close button tooltip based on current state
    private void updateCloseButtonTooltip() {
        if (isExpanded) {
            closeButton.setToolTipText("Close reference panel (ESC, Enter, or Space)");
        }
    }
    
    // Sets the expansion state programmatically
    public void setExpanded(boolean expanded) {
        if (this.isExpanded != expanded) {
            boolean previousState = this.isExpanded;
            this.isExpanded = expanded;
            
            // Update all visual components
            updateVisibility();
            
            // Verify the state change was successful
            if (this.isExpanded != expanded) {
                System.err.println("Warning: setExpanded failed to update state properly");
                this.isExpanded = expanded; // Force the state
                updateVisibility();
            }
            
            // Ensure proper layout revalidation and parent container updates
            // This must be done after visibility updates to ensure proper layout calculation
            SwingUtilities.invokeLater(() -> {
                revalidateLayoutHierarchy();
            });
            
            // Update focus appropriately
            SwingUtilities.invokeLater(() -> {
                if (expanded) {
                    // When programmatically expanded, keep focus on toggle button
                    toggleButton.requestFocusInWindow();
                } else {
                    // When programmatically collapsed, focus on toggle button
                    toggleButton.requestFocusInWindow();
                }
            });
        }
    }
    
    // Gets the current expansion state
    public boolean isExpanded() {
        return isExpanded;
    }
    
    // Gets the preferred size when collapsed
    @Override
    public Dimension getPreferredSize() {
        if (!isExpanded) {
            // Return size for just the toggle button when collapsed
            Dimension buttonSize = toggleButton.getPreferredSize();
            Insets insets = getInsets();
            return new Dimension(
                buttonSize.width + insets.left + insets.right,
                buttonSize.height + insets.top + insets.bottom + 10
            );
        } else {
            // Return full size when expanded
            return super.getPreferredSize();
        }
    }
    
    // Gets the minimum size when collapsed
    @Override
    public Dimension getMinimumSize() {
        if (!isExpanded) {
            return getPreferredSize();
        } else {
            return super.getMinimumSize();
        }
    }
    
    // Applies styling to all buttons in the reference panel
    private void applyButtonStyling() {
        // Style the toggle button as a secondary button
        UIStyleManager.styleSecondaryButton(toggleButton);
        
        // The close button is created dynamically in setupExpandedLayout()
        // We'll need to style it when it's created
    }
    
    /**
     * Properly revalidates the layout hierarchy to ensure layout updates 
     * propagate correctly to the MainFrame and parent containers.
     * Includes comprehensive null checks for parent container safety.
     */
    private void revalidateLayoutHierarchy() {
        try {
            // First revalidate this panel
            this.revalidate();
            this.repaint();
            
            // Walk up the container hierarchy and revalidate each level
            Container current = getParent();
            int hierarchyDepth = 0;
            final int MAX_HIERARCHY_DEPTH = 20; // Prevent infinite loops
            
            while (current != null && hierarchyDepth < MAX_HIERARCHY_DEPTH) {
                try {
                    // Revalidate current container with null check
                    if (current.isValid()) {
                        current.revalidate();
                        current.repaint();
                    }
                    
                    // Check if we've reached the top-level window (MainFrame)
                    if (current instanceof JFrame || current instanceof JDialog) {
                        // Force a complete layout update for the top-level window
                        final Container topLevel = current;
                        SwingUtilities.invokeLater(() -> {
                            try {
                                if (topLevel != null && topLevel.isDisplayable()) {
                                    topLevel.validate();
                                    topLevel.repaint();
                                }
                            } catch (Exception e) {
                                System.err.println("Warning: Error during top-level container revalidation: " + e.getMessage());
                            }
                        });
                        break;
                    }
                    
                    // Move to next parent, with comprehensive null checks
                    Container nextParent = current.getParent();
                    if (nextParent == null) {
                        // Reached the top of the hierarchy
                        break;
                    }
                    if (nextParent == current) {
                        // Prevent infinite loop in case of circular reference
                        System.err.println("Warning: Circular parent reference detected in container hierarchy at depth " + hierarchyDepth);
                        break;
                    }
                    
                    current = nextParent;
                    hierarchyDepth++;
                    
                } catch (Exception e) {
                    System.err.println("Warning: Error during container revalidation at hierarchy depth " + hierarchyDepth + ": " + e.getMessage());
                    break;
                }
            }
            
            if (hierarchyDepth >= MAX_HIERARCHY_DEPTH) {
                System.err.println("Warning: Maximum hierarchy depth reached during revalidation, stopping to prevent infinite loop");
            }
            
            // Additional safety: Force layout update on the root pane if available
            SwingUtilities.invokeLater(() -> {
                try {
                    JRootPane rootPane = SwingUtilities.getRootPane(this);
                    if (rootPane != null && rootPane.isDisplayable()) {
                        rootPane.revalidate();
                        rootPane.repaint();
                        
                        // Also ensure the content pane is properly updated
                        Container contentPane = rootPane.getContentPane();
                        if (contentPane != null && contentPane.isDisplayable()) {
                            contentPane.revalidate();
                            contentPane.repaint();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Warning: Error during root pane revalidation: " + e.getMessage());
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error during layout hierarchy revalidation: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: try basic revalidation
            try {
                this.revalidate();
                this.repaint();
            } catch (Exception fallbackError) {
                System.err.println("Critical error: Even basic revalidation failed: " + fallbackError.getMessage());
            }
        }
    }
}