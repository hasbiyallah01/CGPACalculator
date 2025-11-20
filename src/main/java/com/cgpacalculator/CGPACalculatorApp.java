package com.cgpacalculator;

import com.cgpacalculator.controller.MainController;
import com.cgpacalculator.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;
import java.util.logging.Level;

// Main application class for the CGPA Calculator
public class CGPACalculatorApp {
    
    private static final Logger LOGGER = Logger.getLogger(CGPACalculatorApp.class.getName());
    
    // Application metadata
    public static final String APP_NAME = "CGPA Calculator";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR = "CGPA Calculator Team";
    
    // Main method - entry point for the CGPA Calculator application
    public static void main(String[] args) {
        configureApplicationProperties(); // Configure application properties
        configureLogging(); // Set up logging
        setSystemLookAndFeel(); // Set system look and feel for better native appearance
        configureSystemIntegration(); // Configure system integration
        SwingUtilities.invokeLater(() -> {
            try {
                LOGGER.info("Starting CGPA Calculator application...");
                
                showSplashScreen(); // Show splash screen (optional)
                // Initialize application using MVC pattern
                MainController controller = new MainController();
                
                LOGGER.info("CGPA Calculator application started successfully");
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to start CGPA Calculator", e);
                showErrorDialog("Application Startup Error", 
                    "Failed to start CGPA Calculator: " + e.getMessage());
                System.exit(1);
            }
        });
    }
    
    // Configures application-wide properties for better integration
    private static void configureApplicationProperties() {
        System.setProperty("apple.awt.application.name", APP_NAME); // Set application name for better OS integration
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", APP_NAME);
        System.setProperty("sun.java2d.uiScale", "1.0"); // Configure high DPI support
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        // Configure better rendering
        System.setProperty("sun.java2d.opengl", "true");
    }
    
    // Configures application logging
    private static void configureLogging() {
        // Set up basic logging configuration
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
        
        LOGGER.info("CGPA Calculator v" + APP_VERSION + " initializing...");
    }
    
    // Sets the system look and feel for better native appearance
    private static void setSystemLookAndFeel() {
        try {
            // Try to set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LOGGER.info("System look and feel applied successfully");
            
            configureUIDefaults(); // Configure additional UI properties for better appearance
            configureUIDefaults();
            
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            LOGGER.warning("Could not set system look and feel, using default: " + e.getMessage());
            // Try to set a cross-platform look and feel as fallback
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                LOGGER.info("Cross-platform look and feel applied as fallback");
            } catch (Exception fallbackException) {
                LOGGER.warning("Could not set any look and feel: " + fallbackException.getMessage());
            }
        }
    }
    
    // Configures UI defaults for better appearance and consistency
    private static void configureUIDefaults() {
        // Set better default fonts
        Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        
        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", new Font(Font.SANS_SERIF, Font.BOLD, 12));
        // Set better colors for validation feedback
        UIManager.put("TextField.errorBackground", new Color(255, 235, 235));
        UIManager.put("TextField.warningBackground", new Color(255, 248, 220));
        UIManager.put("TextField.successBackground", new Color(235, 255, 235));
        // Configure tooltip appearance
        UIManager.put("ToolTip.font", new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        UIManager.put("ToolTip.background", new Color(255, 255, 225));
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.GRAY));
    }
    
    // Configures system integration features
    private static void configureSystemIntegration() {
        // Enable better window decorations on supported platforms
        JFrame.setDefaultLookAndFeelDecorated(false);
        JDialog.setDefaultLookAndFeelDecorated(false);
        // Configure taskbar integration (Java 9+)
        if (Taskbar.isTaskbarSupported()) {
            try {
                Taskbar taskbar = Taskbar.getTaskbar();
                // Set application icon if supported
                if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                    // You can add an icon here if you have one
                }
                
                LOGGER.info("Taskbar integration configured");
            } catch (Exception e) {
                LOGGER.warning("Could not configure taskbar integration: " + e.getMessage());
            }
        }
    }
    
    // Shows a brief splash screen during application startup (optional)
    private static void showSplashScreen() {
        // This is optional - you can implement a splash screen here
        LOGGER.fine("Splash screen skipped for faster startup");
    }
    
    // Shows an error dialog for critical application errors
    private static void showErrorDialog(String title, String message) {
        try {
            JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            // If we can't even show a dialog, print to console
            System.err.println(title + ": " + message);
        }
    }
    
    // Gets the application name
    public static String getApplicationName() {
        return APP_NAME;
    }
    
    // Gets the application version
    public static String getApplicationVersion() {
        return APP_VERSION;
    }
    
    // Gets the application author
    public static String getApplicationAuthor() {
        return APP_AUTHOR;
    }
}