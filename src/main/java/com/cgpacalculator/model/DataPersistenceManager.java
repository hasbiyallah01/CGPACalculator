package com.cgpacalculator.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;public class DataPersistenceManager {
    
    private final FileManager fileManager;
    private final AutoSaveManager autoSaveManager;
    private final List<DataPersistenceListener> listeners;
    
    private String currentFilePath;
    private boolean hasUnsavedChanges;
    
    // Constructor with default configuration
    public DataPersistenceManager() {
        this.fileManager = new FileManager();
        this.autoSaveManager = new AutoSaveManager(fileManager);
        this.listeners = new ArrayList<>();
        this.hasUnsavedChanges = false;
    }
    
    // Method comment
    public DataPersistenceManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.autoSaveManager = new AutoSaveManager(fileManager);
        this.listeners = new ArrayList<>();
        this.hasUnsavedChanges = false;
    }
    
    // Method comment
    public boolean saveStudentData(Student student, String filePath) {
        try {
            boolean success = fileManager.saveStudentData(student, filePath);
            
            if (success) {
                currentFilePath = filePath;
                hasUnsavedChanges = false;
                autoSaveManager.setCurrentStudent(student);
                
                notifyListeners(DataPersistenceEvent.SAVE_SUCCESS, filePath);
            } else {
                notifyListeners(DataPersistenceEvent.SAVE_FAILED, filePath);
            }
            
            return success;
            
        } catch (IOException e) {
            notifyListeners(DataPersistenceEvent.SAVE_ERROR, filePath, e.getMessage());
            return false;
        }
    }
    
    // Method comment
    public boolean saveStudentData(Student student) {
        String defaultPath = fileManager.getDefaultSaveFilePath();
        return saveStudentData(student, defaultPath);
    }
    
    // Method comment
    public Student loadStudentData(String filePath) {
        try {
            Student student = fileManager.loadStudentData(filePath);
            
            if (student != null) {
                currentFilePath = filePath;
                hasUnsavedChanges = false;
                autoSaveManager.setCurrentStudent(student);
                
                notifyListeners(DataPersistenceEvent.LOAD_SUCCESS, filePath);
            }
            
            return student;
            
        } catch (IOException e) {
            notifyListeners(DataPersistenceEvent.LOAD_ERROR, filePath, e.getMessage());
            return null;
        }
    }
    
    // Method comment
    public Student checkAndOfferAutoSaveRestore() {
        if (autoSaveManager.hasAutoSaveFile()) {
            long ageMinutes = autoSaveManager.getAutoSaveFileAgeMinutes();
            
            // Only offer if auto-save is recent (less than 24 hours old)
            if (ageMinutes >= 0 && ageMinutes < 1440) {
                Student autoSaveStudent = autoSaveManager.loadFromAutoSave();
                
                if (autoSaveStudent != null) {
                    notifyListeners(DataPersistenceEvent.AUTOSAVE_AVAILABLE, 
                                  "Auto-save data available from " + ageMinutes + " minutes ago");
                    return autoSaveStudent;
                }
            }
        }
        
        return null;
    }
    
    // Method comment
    public void markDataAsModified(Student student) {
        hasUnsavedChanges = true;
        autoSaveManager.setCurrentStudent(student);
        autoSaveManager.markAsModified();
        
        notifyListeners(DataPersistenceEvent.DATA_MODIFIED, "Data has been modified");
    }
    
    // Method comment
    public boolean performQuickSave() {
        boolean success = autoSaveManager.performImmediateSave();
        
        if (success) {
            hasUnsavedChanges = false;
            notifyListeners(DataPersistenceEvent.QUICK_SAVE_SUCCESS, "Quick save completed");
        } else {
            notifyListeners(DataPersistenceEvent.QUICK_SAVE_FAILED, "Quick save failed");
        }
        
        return success;
    }
    
    // Method comment
    public String[] getAvailableDataFiles() {
        return fileManager.listDataFiles();
    }
    
    // Method comment
    public boolean isValidDataFile(String filePath) {
        try {
            return fileManager.isValidDataFile(filePath);
        } catch (IOException e) {
            return false;
        }
    }
    
    // Method comment
    public FileInfo getFileInformation(String filePath) {
        if (!fileManager.fileExists(filePath)) {
            return null;
        }
        
        long size = fileManager.getFileSize(filePath);
        LocalDateTime lastModified = fileManager.getLastModifiedTime(filePath);
        boolean isValid = isValidDataFile(filePath);
        
        return new FileInfo(filePath, size, lastModified, isValid);
    }
    
    // Method comment
    public boolean createBackup(String backupName) {
        if (currentFilePath == null) {
            return false;
        }
        
        try {
            String backupPath = generateBackupPath(backupName);
            boolean success = fileManager.createBackup(currentFilePath, backupPath);
            
            if (success) {
                notifyListeners(DataPersistenceEvent.BACKUP_CREATED, backupPath);
            }
            
            return success;
            
        } catch (IOException e) {
            notifyListeners(DataPersistenceEvent.BACKUP_FAILED, e.getMessage());
            return false;
        }
    }
    
    // Deletes auto-save file (typically after successful manual save)
    public void cleanupAutoSave() {
        if (autoSaveManager.deleteAutoSaveFile()) {
            notifyListeners(DataPersistenceEvent.AUTOSAVE_CLEANED, "Auto-save file cleaned up");
        }
    }
    
    // Method comment
    public void configureAutoSave(boolean enabled) {
        autoSaveManager.setAutoSaveEnabled(enabled);
        
        String message = enabled ? "Auto-save enabled" : "Auto-save disabled";
        notifyListeners(DataPersistenceEvent.AUTOSAVE_CONFIGURED, message);
    }
    
    // Method comment
    public void addDataPersistenceListener(DataPersistenceListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    // Method comment
    public void removeDataPersistenceListener(DataPersistenceListener listener) {
        listeners.remove(listener);
    }
    
    // Shuts down the persistence manager and cleans up resources
    public void shutdown() {
        autoSaveManager.shutdown();
        notifyListeners(DataPersistenceEvent.SHUTDOWN, "Data persistence manager shut down");
    }
    
    // Getters for status information
    public String getCurrentFilePath() { return currentFilePath; }
    public boolean hasUnsavedChanges() { return hasUnsavedChanges; }
    public boolean isAutoSaveEnabled() { return autoSaveManager.isAutoSaveEnabled(); }
    public long getMinutesSinceLastSave() { return autoSaveManager.getMinutesSinceLastSave(); }
    
    // Private helper methods
    private String generateBackupPath(String backupName) {
        String fileName = (backupName != null && !backupName.trim().isEmpty()) 
                         ? backupName + ".json"
                         : "backup_" + System.currentTimeMillis() + ".json";
        return fileManager.getBackupDirectory() + "/" + fileName;
    }
    
    private void notifyListeners(DataPersistenceEvent event, String message) {
        notifyListeners(event, message, null);
    }
    
    private void notifyListeners(DataPersistenceEvent event, String message, String details) {
        for (DataPersistenceListener listener : listeners) {
            try {
                listener.onDataPersistenceEvent(event, message, details);
            } catch (Exception e) {
                // Log error but don't let listener exceptions break the flow
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    // File information container class
    public static class FileInfo {
        private final String filePath;
        private final long sizeBytes;
        private final LocalDateTime lastModified;
        private final boolean isValid;
        
        public FileInfo(String filePath, long sizeBytes, LocalDateTime lastModified, boolean isValid) {
            this.filePath = filePath;
            this.sizeBytes = sizeBytes;
            this.lastModified = lastModified;
            this.isValid = isValid;
        }
        
        public String getFilePath() { return filePath; }
        public long getSizeBytes() { return sizeBytes; }
        public LocalDateTime getLastModified() { return lastModified; }
        public boolean isValid() { return isValid; }
        
        public String getFormattedSize() {
            if (sizeBytes < 1024) return sizeBytes + " B";
            if (sizeBytes < 1024 * 1024) return String.format("%.1f KB", sizeBytes / 1024.0);
            return String.format("%.1f MB", sizeBytes / (1024.0 * 1024.0));
        }
    }
    
    // Event types for data persistence operations
    public enum DataPersistenceEvent {
        SAVE_SUCCESS,
        SAVE_FAILED,
        SAVE_ERROR,
        LOAD_SUCCESS,
        LOAD_ERROR,
        DATA_MODIFIED,
        QUICK_SAVE_SUCCESS,
        QUICK_SAVE_FAILED,
        AUTOSAVE_AVAILABLE,
        AUTOSAVE_CLEANED,
        AUTOSAVE_CONFIGURED,
        BACKUP_CREATED,
        BACKUP_FAILED,
        SHUTDOWN
    }
    
    // Interface for listening to data persistence events
    public interface DataPersistenceListener {
        void onDataPersistenceEvent(DataPersistenceEvent event, String message, String details);
    }
}