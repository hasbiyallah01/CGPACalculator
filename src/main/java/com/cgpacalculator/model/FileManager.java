package com.cgpacalculator.model;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Manages file operations for student data persistence
public class FileManager implements FileOperations {
    
    private static final String DEFAULT_DATA_DIRECTORY = "data";
    private static final String DEFAULT_BACKUP_DIRECTORY = "data/backups";
    private static final String FILE_EXTENSION = ".json";
    private static final DateTimeFormatter BACKUP_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    private String dataDirectory;
    private String backupDirectory;
    
    // Default constructor using default directories
    public FileManager() {
        this.dataDirectory = DEFAULT_DATA_DIRECTORY;
        this.backupDirectory = DEFAULT_BACKUP_DIRECTORY;
        initializeDirectories();
    }
    
    // Constructor with custom directories
    public FileManager(String dataDirectory, String backupDirectory) {
        this.dataDirectory = dataDirectory;
        this.backupDirectory = backupDirectory;
        initializeDirectories();
    }
    
    @Override
    public boolean saveStudentData(Student student, String filePath) throws IOException {
        if (student == null) {
            throw new IllegalArgumentException("Student object cannot be null");
        }
        
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        try {
            // Create backup if file already exists
            if (fileExists(filePath)) {
                createBackupOfExistingFile(filePath);
            }
            
            // Serialize student data to JSON
            String jsonData = DataSerializer.serializeStudentToJson(student);
            
            // Ensure parent directory exists
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            // Write data to file
            writeStringToFile(jsonData, filePath);
            
            return true;
            
        } catch (Exception e) {
            throw new IOException("Failed to save student data: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Student loadStudentData(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        if (!fileExists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }
        
        try {
            // Read file content
            String jsonData = readStringFromFile(filePath);
            
            // Validate JSON format
            if (!DataSerializer.isValidJsonFormat(jsonData)) {
                throw new IllegalArgumentException("Invalid file format");
            }
            
            // Deserialize JSON to Student object
            return DataSerializer.deserializeStudentFromJson(jsonData);
            
        } catch (Exception e) {
            throw new IOException("Failed to load student data: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        
        try {
            return Files.exists(Paths.get(filePath));
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isValidDataFile(String filePath) throws IOException {
        if (!fileExists(filePath)) {
            return false;
        }
        
        try {
            String content = readStringFromFile(filePath);
            return DataSerializer.isValidJsonFormat(content);
        } catch (Exception e) {
            throw new IOException("Cannot validate file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean createBackup(String originalFilePath, String backupFilePath) throws IOException {
        if (!fileExists(originalFilePath)) {
            throw new IOException("Original file does not exist: " + originalFilePath);
        }
        
        try {
            Path originalPath = Paths.get(originalFilePath);
            Path backupPath = Paths.get(backupFilePath);
            
            // Ensure backup directory exists
            Files.createDirectories(backupPath.getParent());
            
            // Copy file
            Files.copy(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            
            return true;
            
        } catch (Exception e) {
            throw new IOException("Failed to create backup: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteFile(String filePath) {
        if (!fileExists(filePath)) {
            return false;
        }
        
        try {
            Files.delete(Paths.get(filePath));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Gets the default save file path for student data
    public String getDefaultSaveFilePath(String studentName) {
        String fileName = (studentName != null && !studentName.trim().isEmpty()) 
                         ? sanitizeFileName(studentName) + FILE_EXTENSION
                         : "student_data" + FILE_EXTENSION;
        return Paths.get(dataDirectory, fileName).toString();
    }
    
    // Gets the default save file path without student name
    public String getDefaultSaveFilePath() {
        return getDefaultSaveFilePath(null);
    }
    
    // Lists all data files in the data directory
    public String[] listDataFiles() {
        try {
            Path dataPath = Paths.get(dataDirectory);
            if (!Files.exists(dataPath)) {
                return new String[0];
            }
            
            return Files.list(dataPath)
                       .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                       .map(Path::toString)
                       .toArray(String[]::new);
                       
        } catch (Exception e) {
            return new String[0];
        }
    }
    
    // Gets file size in bytes, returns -1 if file doesn't exist
    public long getFileSize(String filePath) {
        try {
            if (fileExists(filePath)) {
                return Files.size(Paths.get(filePath));
            }
        } catch (Exception e) {
            // Ignore exception
        }
        return -1;
    }
    
    // Gets last modified time of file, returns null if file doesn't exist
    public LocalDateTime getLastModifiedTime(String filePath) {
        try {
            if (fileExists(filePath)) {
                return LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(Paths.get(filePath)).toInstant(),
                    java.time.ZoneId.systemDefault()
                );
            }
        } catch (Exception e) {
            // Ignore exception
        }
        return null;
    }
    
    // Private helper methods
    private void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(dataDirectory));
            Files.createDirectories(Paths.get(backupDirectory));
        } catch (Exception e) {
            // Log error but don't fail initialization
            System.err.println("Warning: Could not create directories: " + e.getMessage());
        }
    }
    
    private void createBackupOfExistingFile(String filePath) throws IOException {
        String timestamp = LocalDateTime.now().format(BACKUP_DATE_FORMAT);
        String fileName = Paths.get(filePath).getFileName().toString();
        String backupFileName = timestamp + "_" + fileName;
        String backupPath = Paths.get(backupDirectory, backupFileName).toString();
        
        createBackup(filePath, backupPath);
    }
    
    private void writeStringToFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(filePath), 
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(content);
        }
    }
    
    private String readStringFromFile(String filePath) throws IOException {
        try {
            return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IOException("Cannot read file: " + filePath, e);
        }
    }
    
    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "student_data";
        
        // Remove invalid characters for file names
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_")
                      .replaceAll("_{2,}", "_")
                      .toLowerCase();
    }
    
    // Getters for directory paths
    public String getDataDirectory() {
        return dataDirectory;
    }
    
    public String getBackupDirectory() {
        return backupDirectory;
    }
}