package com.cgpacalculator.model;

import java.io.IOException;

// Interface defining file operations contract for data persistence.
// Provides well-defined save/load contracts with comprehensive error handling.
public interface FileOperations {
    
    // Saves student data to the specified file path
    // @param student the Student object to save
    // @param filePath the path where the data should be saved
    // @return true if save was successful, false otherwise
    // @throws IOException if file operations fail
    boolean saveStudentData(Student student, String filePath) throws IOException;
    
    // Loads student data from the specified file path
    // @param filePath the path to load data from
    // @return Student object loaded from file
    // @throws IOException if file operations fail or file doesn't exist
    Student loadStudentData(String filePath) throws IOException;
    
    // Checks if a file exists at the specified path
    // @param filePath the path to check
    // @return true if file exists, false otherwise
    boolean fileExists(String filePath);
    
    // Validates if the file contains valid student data
    // @param filePath the path to validate
    // @return true if file contains valid data, false otherwise
    // @throws IOException if file operations fail
    boolean isValidDataFile(String filePath) throws IOException;
    
    // Creates a backup copy of the specified file
    // @param originalFilePath the path of the file to backup
    // @param backupFilePath the path where backup should be created
    // @return true if backup was successful, false otherwise
    // @throws IOException if file operations fail
    boolean createBackup(String originalFilePath, String backupFilePath) throws IOException;
    
    // Deletes the file at the specified path
    // @param filePath the path of the file to delete
    // @return true if deletion was successful, false otherwise
    boolean deleteFile(String filePath);
}