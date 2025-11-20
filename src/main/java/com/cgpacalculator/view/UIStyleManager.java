package com.cgpacalculator.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;public class UIStyleManager {
    
    // Modern color scheme constants
    public static final Color PRIMARY_COLOR = new Color(99, 102, 241);      // Modern indigo
    public static final Color PRIMARY_HOVER = new Color(79, 70, 229);       // Darker indigo
    public static final Color SECONDARY_COLOR = new Color(139, 92, 246);    // Purple accent
    public static final Color SUCCESS_COLOR = new Color(34, 197, 94);       // Modern green
    public static final Color WARNING_COLOR = new Color(251, 146, 60);      // Modern orange
    public static final Color ERROR_COLOR = new Color(239, 68, 68);         // Modern red
    public static final Color BACKGROUND_COLOR = new Color(249, 250, 251);  // Very light gray
    public static final Color PANEL_COLOR = new Color(255, 255, 255);       // Pure white
    public static final Color CARD_COLOR = new Color(255, 255, 255);        // Card background
    public static final Color BORDER_COLOR = new Color(229, 231, 235);      // Light border
    public static final Color HOVER_COLOR = new Color(243, 244, 246);       // Hover state
    public static final Color TEXT_COLOR = new Color(17, 24, 39);           // Dark text
    public static final Color MUTED_TEXT_COLOR = new Color(107, 114, 128);  // Muted text
    public static final Color ACCENT_COLOR = new Color(16, 185, 129);       // Teal accent
    
    // Font constants
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font MONOSPACE_FONT = new Font("Consolas", Font.PLAIN, 12);
    
    // Border constants
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );
    
    public static final Border INPUT_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(5, 8, 5, 8)
    );
    
    public static final Border FOCUSED_BORDER = BorderFactory.createLineBorder(PRIMARY_COLOR, 2);
    public static final Border ERROR_BORDER = BorderFactory.createLineBorder(ERROR_COLOR, 2);
    public static final Border SUCCESS_BORDER = BorderFactory.createLineBorder(SUCCESS_COLOR, 1);
    
    // Component styling cache
    private static final Map<String, ComponentStyle> styleCache = new HashMap<>();
    
    // Applies the application theme to the entire UI
    public static void applyApplicationTheme() {
        try {
            // Set system look and feel as base
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Customize UI defaults
            customizeUIDefaults();
            
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }
    }
    
    // Customizes UI defaults for consistent styling
    private static void customizeUIDefaults() {
        // Button styling - Keep system defaults but improve them
        UIManager.put("Button.font", BODY_FONT);
        // Don't override button colors globally - let individual styling handle it
        UIManager.put("Button.focusPainted", false);
        UIManager.put("Button.borderPainted", true);
        
        // TextField styling
        UIManager.put("TextField.font", BODY_FONT);
        UIManager.put("TextField.background", new ColorUIResource(PANEL_COLOR));
        UIManager.put("TextField.foreground", new ColorUIResource(TEXT_COLOR));
        UIManager.put("TextField.border", INPUT_BORDER);
        
        // Table styling
        UIManager.put("Table.font", BODY_FONT);
        UIManager.put("Table.background", new ColorUIResource(PANEL_COLOR));
        UIManager.put("Table.foreground", new ColorUIResource(TEXT_COLOR));
        UIManager.put("Table.selectionBackground", new ColorUIResource(SECONDARY_COLOR));
        UIManager.put("Table.selectionForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("Table.gridColor", new ColorUIResource(BORDER_COLOR));
        
        // Panel styling
        UIManager.put("Panel.background", new ColorUIResource(BACKGROUND_COLOR));
        UIManager.put("Panel.foreground", new ColorUIResource(TEXT_COLOR));
        
        // Label styling
        UIManager.put("Label.font", BODY_FONT);
        UIManager.put("Label.foreground", new ColorUIResource(TEXT_COLOR));
    }
    
    // Styles a button with the primary theme
    public static void stylePrimaryButton(JButton button) {
        button.setFont(BODY_FONT);
        
        // Use a more vibrant blue color that's easier to see
        Color primaryBlue = new Color(70, 130, 255); // Bright blue
        button.setBackground(primaryBlue);
        button.setForeground(Color.WHITE);
        
        // Create a simple but visible border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryBlue.darker(), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        // Force the button to show our styling - be more aggressive
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        
        // Override any UI defaults
        button.putClientProperty("JButton.buttonType", null);
        button.putClientProperty("JComponent.sizeVariant", null);
        
        // Ensure text is visible
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Force immediate repaint
        SwingUtilities.invokeLater(() -> {
            button.revalidate();
            button.repaint();
        });
        
        // Add hover effects with the new color
        addButtonHoverEffect(button, primaryBlue, primaryBlue.darker());
    }
    
    // Styles a button with the secondary theme
    public static void styleSecondaryButton(JButton button) {
        button.setFont(BODY_FONT);
        
        // Use a more visible gray color
        Color secondaryGray = new Color(160, 160, 160); // Medium gray
        button.setBackground(secondaryGray);
        button.setForeground(Color.BLACK);
        
        // Create a simple but visible border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(secondaryGray.darker(), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        // Force the button to show our styling - be more aggressive
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        
        // Override any UI defaults
        button.putClientProperty("JButton.buttonType", null);
        button.putClientProperty("JComponent.sizeVariant", null);
        
        // Ensure text is visible
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Force immediate repaint
        SwingUtilities.invokeLater(() -> {
            button.revalidate();
            button.repaint();
        });
        
        // Add hover effects with the new color
        addButtonHoverEffect(button, secondaryGray, secondaryGray.darker());
    }
    
    // Styles a danger/warning button
    public static void styleDangerButton(JButton button) {
        button.setFont(BODY_FONT);
        
        // Use a more vibrant red color
        Color dangerRed = new Color(220, 53, 69); // Bootstrap danger red
        button.setBackground(dangerRed);
        button.setForeground(Color.WHITE);
        
        // Create a simple but visible border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(dangerRed.darker(), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        // Force the button to show our styling - be more aggressive
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        
        // Override any UI defaults
        button.putClientProperty("JButton.buttonType", null);
        button.putClientProperty("JComponent.sizeVariant", null);
        
        // Ensure text is visible
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Force immediate repaint
        SwingUtilities.invokeLater(() -> {
            button.revalidate();
            button.repaint();
        });
        
        // Add hover effects with the new color
        addButtonHoverEffect(button, dangerRed, dangerRed.darker());
    }
    
    // Styles an input field with enhanced visual feedback
    public static void styleInputField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setBackground(PANEL_COLOR);
        field.setForeground(TEXT_COLOR);
        field.setBorder(INPUT_BORDER);
        field.setCaretColor(PRIMARY_COLOR);
        
        // Add focus effects
        addInputFieldFocusEffect(field);
    }
    
    // Styles a panel with consistent theming
    public static void stylePanel(JPanel panel, String title) {
        panel.setBackground(PANEL_COLOR);
        panel.setForeground(TEXT_COLOR);
        
        if (title != null && !title.isEmpty()) {
            panel.setBorder(BorderFactory.createTitledBorder(
                PANEL_BORDER, title, 0, 0, SUBHEADER_FONT, TEXT_COLOR
            ));
        } else {
            panel.setBorder(PANEL_BORDER);
        }
    }
    
    // Styles a table with enhanced visual appearance
    public static void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setBackground(PANEL_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Style table header
        if (table.getTableHeader() != null) {
            table.getTableHeader().setFont(SUBHEADER_FONT);
            table.getTableHeader().setBackground(BACKGROUND_COLOR);
            table.getTableHeader().setForeground(TEXT_COLOR);
            table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        }
        
        // Add alternating row colors
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
    }
    
    // Styles a label with specific type (header, body, muted, etc.)
    public static void styleLabel(JLabel label, LabelType type) {
        label.setForeground(TEXT_COLOR);
        
        switch (type) {
            case HEADER:
                label.setFont(HEADER_FONT);
                break;
            case SUBHEADER:
                label.setFont(SUBHEADER_FONT);
                break;
            case BODY:
                label.setFont(BODY_FONT);
                break;
            case SMALL:
                label.setFont(SMALL_FONT);
                break;
            case MUTED:
                label.setFont(BODY_FONT);
                label.setForeground(MUTED_TEXT_COLOR);
                break;
            case SUCCESS:
                label.setFont(BODY_FONT);
                label.setForeground(SUCCESS_COLOR);
                break;
            case WARNING:
                label.setFont(BODY_FONT);
                label.setForeground(WARNING_COLOR);
                break;
            case ERROR:
                label.setFont(BODY_FONT);
                label.setForeground(ERROR_COLOR);
                break;
        }
    }
    
    // Creates a styled scroll pane
    public static JScrollPane createStyledScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        
        // Style scrollbars
        scrollPane.getVerticalScrollBar().setBackground(BACKGROUND_COLOR);
        scrollPane.getHorizontalScrollBar().setBackground(BACKGROUND_COLOR);
        
        return scrollPane;
    }
    
    // Adds hover effect to buttons
    private static void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                    button.repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normalColor);
                    button.repaint();
                }
            }
        });
    }
    
    // Adds focus effect to input fields
    private static void addInputFieldFocusEffect(JTextField field) {
        Border originalBorder = field.getBorder();
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(FOCUSED_BORDER);
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(originalBorder);
            }
        });
    }
    
    // Sets validation state styling for input fields
    public static void setValidationState(JTextField field, ValidationState state) {
        switch (state) {
            case VALID:
                field.setBorder(SUCCESS_BORDER);
                break;
            case INVALID:
                field.setBorder(ERROR_BORDER);
                break;
            case NEUTRAL:
                field.setBorder(INPUT_BORDER);
                break;
        }
    }
    
    // Creates a loading indicator component
    public static JComponent createLoadingIndicator(String message) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(PANEL_COLOR);
        
        JLabel spinner = new JLabel("⟳");
        spinner.setFont(new Font("Arial", Font.PLAIN, 16));
        spinner.setForeground(PRIMARY_COLOR);
        
        JLabel messageLabel = new JLabel(message);
        styleLabel(messageLabel, LabelType.MUTED);
        
        panel.add(spinner);
        panel.add(messageLabel);
        
        return panel;
    }
    
    // Enum for label types
    public enum LabelType {
        HEADER, SUBHEADER, BODY, SMALL, MUTED, SUCCESS, WARNING, ERROR
    }
    
    // Enum for validation states
    public enum ValidationState {
        VALID, INVALID, NEUTRAL
    }
    
    // Component style data class
    private static class ComponentStyle {
        final Color backgroundColor;
        final Color foregroundColor;
        final Font font;
        final Border border;
        
        ComponentStyle(Color backgroundColor, Color foregroundColor, Font font, Border border) {
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
            this.font = font;
            this.border = border;
        }
    }
    
    // Custom table cell renderer for alternating row colors
    private static class AlternatingRowRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private static final Color ALTERNATE_COLOR = new Color(248, 249, 250);
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                if (row % 2 == 0) {
                    component.setBackground(PANEL_COLOR);
                } else {
                    component.setBackground(ALTERNATE_COLOR);
                }
            }
            
            return component;
        }
    }
}