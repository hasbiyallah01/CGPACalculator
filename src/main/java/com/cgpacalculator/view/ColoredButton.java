package com.cgpacalculator.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom button class that forces solid background colors
 * and overrides any Look and Feel styling
 */
public class ColoredButton extends JButton {
    
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    public ColoredButton(String text, Color backgroundColor, Color textColor) {
        super(text);
        this.normalColor = backgroundColor;
        this.hoverColor = backgroundColor.darker();
        this.pressedColor = backgroundColor.darker().darker();
        
        // Force our custom UI
        setUI(new ColoredButtonUI());
        
        // Set basic properties
        setForeground(textColor);
        setBackground(backgroundColor);
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(true);
        setFocusPainted(false);
        
        // Add mouse listeners for hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
        
        // Set cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    // Custom UI class that forces our colors
    private class ColoredButtonUI extends BasicButtonUI {
        
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Determine current color based on state
            Color currentColor = normalColor;
            if (isPressed) {
                currentColor = pressedColor;
            } else if (isHovered) {
                currentColor = hoverColor;
            }
            
            // Fill background
            g2d.setColor(currentColor);
            g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
            
            // Draw border
            g2d.setColor(currentColor.darker());
            g2d.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
            
            // Draw text
            g2d.setColor(getForeground());
            g2d.setFont(getFont());
            
            FontMetrics fm = g2d.getFontMetrics();
            String text = getText();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            
            int x = (c.getWidth() - textWidth) / 2;
            int y = (c.getHeight() - textHeight) / 2 + fm.getAscent();
            
            g2d.drawString(text, x, y);
            
            g2d.dispose();
        }
    }
    
    // Factory methods for different button types
    public static ColoredButton createPrimaryButton(String text) {
        return new ColoredButton(text, new Color(70, 130, 255), Color.WHITE);
    }
    
    public static ColoredButton createSecondaryButton(String text) {
        return new ColoredButton(text, new Color(160, 160, 160), Color.BLACK);
    }
    
    public static ColoredButton createDangerButton(String text) {
        return new ColoredButton(text, new Color(220, 53, 69), Color.WHITE);
    }
}