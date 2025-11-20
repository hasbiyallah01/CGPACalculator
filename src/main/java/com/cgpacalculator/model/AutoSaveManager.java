package com.cgpacalculator.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Manages automatic saving of student data with configurable intervals
public class AutoSaveManager {
    
    private static final int DEFAULT_AUTO_SAVE_INTERVAL_MINUTES = 5;
    private static final String AUTO_SAVE_FILE_PREFIX = "autosave_";
    
    private final FileManager fileManager;
    private final ScheduledExecutorService autoSaveExecutor;
    private final AutoSaveConfiguration configuration;
    
    private Student currentStudent;
    private LocalDateTime lastSaveTime;
    private LocalDateTime lastModificationTime;
    private boolean isAutoSaveEnabled;
    private boolean hasUnsavedChanges;
    
    // Constructor with default configuration
    public AutoSaveManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.configuration = new AutoSaveConfiguration();
        this.autoSaveExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "AutoSave-Thread");
            thread.setDaemon(true);
            return thread;
        });
        this.isAutoSaveEnabled = true;
        this.hasUnsavedChanges = false;
        
        initializeAutoSave();
    }
    
    // Constructor with custom configuration
    public AutoSaveManager(FileManager fileManager, AutoSaveConfiguration configuration) {
        this.fileManager = fileManager;
        this.configuration = configuration;
        this.autoSaveExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "AutoSave-Thread");
            thread.setDaemon(true);
            return thread;
        });
        this.isAutoSaveEnabled = configuration.isEnabled();
        this.hasUnsavedChanges = false;
        
        initializeAutoSave();
    }
    
    // Sets the current student data to be auto-saved
    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        markAsModified();
    }
    
    // Marks the current data as modified
    public void markAsModified() {
        this.lastModificationTime = LocalDateTime.now();
        this.hasUnsavedChanges = true;
    }
    
    // Performs immediate save of current student data
    public boolean performImmediateSave() {
        if (currentStudent == null) {
            return false;
        }
        
        try {
            String autoSaveFilePath = getAutoSaveFilePath();
            boolean success = fileManager.saveStudentData(currentStudent, autoSaveFilePath);
            
            if (success) {
                lastSaveTime = LocalDateTime.now();
                hasUnsavedChanges = false;
            }
            
            return success;
            
        } catch (IOException e) {
            System.err.println("Auto-save failed: " + e.getMessage());
            return false;
        }
    }
    
    // Checks if auto-save file exists
    public boolean hasAutoSaveFile() {
        return fileManager.fileExists(getAutoSaveFilePath());
    }
    
    // Loads data from auto-save file
    public Student loadFromAutoSave() {
        try {
            String autoSaveFilePath = getAutoSaveFilePath();
            if (fileManager.fileExists(autoSaveFilePath)) {
                return fileManager.loadStudentData(autoSaveFilePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to load auto-save data: " + e.getMessage());
        }
        return null;
    }
    
    // Deletes the auto-save file
    public boolean deleteAutoSaveFile() {
        return fileManager.deleteFile(getAutoSaveFilePath());
    }
    
    // Gets the age of auto-save file in minutes
    public long getAutoSaveFileAgeMinutes() {
        LocalDateTime lastModified = fileManager.getLastModifiedTime(getAutoSaveFilePath());
        if (lastModified != null) {
            return ChronoUnit.MINUTES.between(lastModified, LocalDateTime.now());
        }
        return -1;
    }
    
    // Enables or disables auto-save functionality
    public void setAutoSaveEnabled(boolean enabled) {
        this.isAutoSaveEnabled = enabled;
        this.configuration.setEnabled(enabled);
    }
    
    // Checks if auto-save is currently enabled
    public boolean isAutoSaveEnabled() {
        return isAutoSaveEnabled;
    }
    
    // Checks if there are unsaved changes
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }
    
    // Gets the time since last save in minutes
    public long getMinutesSinceLastSave() {
        if (lastSaveTime != null) {
            return ChronoUnit.MINUTES.between(lastSaveTime, LocalDateTime.now());
        }
        return -1;
    }
    
    // Gets the time since last modification in minutes
    public long getMinutesSinceLastModification() {
        if (lastModificationTime != null) {
            return ChronoUnit.MINUTES.between(lastModificationTime, LocalDateTime.now());
        }
        return -1;
    }
    
    // Shuts down the auto-save manager and cleans up resources
    public void shutdown() {
        if (hasUnsavedChanges && currentStudent != null) {
            performImmediateSave();
        }
        
        autoSaveExecutor.shutdown();
        try {
            if (!autoSaveExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                autoSaveExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            autoSaveExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    // Private helper methods
    private void initializeAutoSave() {
        // Schedule periodic auto-save checks
        autoSaveExecutor.scheduleAtFixedRate(
            this::performAutoSaveCheck,
            configuration.getIntervalMinutes(),
            configuration.getIntervalMinutes(),
            TimeUnit.MINUTES
        );
    }
    
    private void performAutoSaveCheck() {
        if (!isAutoSaveEnabled || currentStudent == null || !hasUnsavedChanges) {
            return;
        }
        // Check if enough time has passed since last modification
        long minutesSinceModification = getMinutesSinceLastModification();
        if (minutesSinceModification >= configuration.getDelayAfterModificationMinutes()) {
            performImmediateSave();
        }
    }
    
    private String getAutoSaveFilePath() {
        return fileManager.getDataDirectory() + "/" + AUTO_SAVE_FILE_PREFIX + "student_data.json";
    }
    
    // Configuration class for auto-save settings
    public static class AutoSaveConfiguration {
        private boolean enabled;
        private int intervalMinutes;
        private int delayAfterModificationMinutes;
        
        // Default configuration constructor
        public AutoSaveConfiguration() {
            this.enabled = true;
            this.intervalMinutes = DEFAULT_AUTO_SAVE_INTERVAL_MINUTES;
            this.delayAfterModificationMinutes = 2;
        }
        
        // Custom configuration constructor
        public AutoSaveConfiguration(boolean enabled, int intervalMinutes, int delayAfterModificationMinutes) {
            this.enabled = enabled;
            this.intervalMinutes = Math.max(1, intervalMinutes);
            this.delayAfterModificationMinutes = Math.max(1, delayAfterModificationMinutes);
        }
        
        // Getters and setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public int getIntervalMinutes() { return intervalMinutes; }
        public void setIntervalMinutes(int intervalMinutes) { 
            this.intervalMinutes = Math.max(1, intervalMinutes); 
        }
        
        public int getDelayAfterModificationMinutes() { return delayAfterModificationMinutes; }
        public void setDelayAfterModificationMinutes(int delayAfterModificationMinutes) { 
            this.delayAfterModificationMinutes = Math.max(1, delayAfterModificationMinutes); 
        }
    }
}