package com.cgpacalculator.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;public class ValidationResult {
    
    private boolean isValidationSuccessful;
    private final List<String> successMessages;
    private final Map<String, String> fieldErrorMessages;
    private final List<String> generalErrorMessages;public ValidationResult() {
        this.isValidationSuccessful = true;
        this.successMessages = new ArrayList<>();
        this.fieldErrorMessages = new HashMap<>();
        this.generalErrorMessages = new ArrayList<>();
    }
    
    // Method comment
    public void addSuccessMessage(String successMessage) {
        if (successMessage != null && !successMessage.trim().isEmpty()) {
            successMessages.add(successMessage.trim());
        }
    }
    
    // Method comment
    public void addErrorMessage(String fieldName, String errorMessage) {
        if (fieldName != null && errorMessage != null) {
            fieldErrorMessages.put(fieldName.trim(), errorMessage.trim());
            isValidationSuccessful = false;
        }
    }
    
    // Method comment
    public void addGeneralErrorMessage(String errorMessage) {
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
            generalErrorMessages.add(errorMessage.trim());
            isValidationSuccessful = false;
        }
    }
    
    // Method comment
    public boolean isSuccessful() {
        return isValidationSuccessful;
    }
    
    // Method comment
    public boolean hasErrors() {
        return !isValidationSuccessful;
    }
    
    // Method comment
    public List<String> getSuccessMessages() {
        return new ArrayList<>(successMessages);
    }
    
    // Method comment
    public Map<String, String> getFieldErrorMessages() {
        return new HashMap<>(fieldErrorMessages);
    }
    
    // Method comment
    public List<String> getGeneralErrorMessages() {
        return new ArrayList<>(generalErrorMessages);
    }
    
    // Method comment
    public String getFieldErrorMessage(String fieldName) {
        return fieldErrorMessages.get(fieldName);
    }
    
    // Method comment
    public boolean hasFieldError(String fieldName) {
        return fieldErrorMessages.containsKey(fieldName);
    }
    
    // Method comment
    public int getTotalErrorCount() {
        return fieldErrorMessages.size() + generalErrorMessages.size();
    }
    
    // Method comment
    public int getSuccessMessageCount() {
        return successMessages.size();
    }
    
    // Method comment
    public String getAllErrorMessagesAsString() {
        StringBuilder errorStringBuilder = new StringBuilder();
        
        // Add field-specific errors
        for (Map.Entry<String, String> fieldError : fieldErrorMessages.entrySet()) {
            if (errorStringBuilder.length() > 0) {
                errorStringBuilder.append("\n");
            }
            errorStringBuilder.append(fieldError.getKey())
                             .append(": ")
                             .append(fieldError.getValue());
        }
        
        // Add general errors
        for (String generalError : generalErrorMessages) {
            if (errorStringBuilder.length() > 0) {
                errorStringBuilder.append("\n");
            }
            errorStringBuilder.append(generalError);
        }
        
        return errorStringBuilder.toString();
    }
    
    // Method comment
    public String getAllSuccessMessagesAsString() {
        return String.join("\n", successMessages);
    }
    
    // Clears all validation results and resets to initial state.
    public void clearAllResults() {
        isValidationSuccessful = true;
        successMessages.clear();
        fieldErrorMessages.clear();
        generalErrorMessages.clear();
    }
    
    // Method comment
    public void mergeValidationResult(ValidationResult otherValidationResult) {
        if (otherValidationResult == null) {
            return;
        }
        
        // Merge success messages
        successMessages.addAll(otherValidationResult.getSuccessMessages());
        
        // Merge field error messages
        fieldErrorMessages.putAll(otherValidationResult.getFieldErrorMessages());
        
        // Merge general error messages
        generalErrorMessages.addAll(otherValidationResult.getGeneralErrorMessages());
        
        // Update success status
        if (otherValidationResult.hasErrors()) {
            isValidationSuccessful = false;
        }
    }
    
    // Method to get all error messages as ValidationMessage objects
    public List<ValidationMessage> getErrorMessages() {
        List<ValidationMessage> messages = new ArrayList<>();
        
        // Add field-specific errors
        for (Map.Entry<String, String> fieldError : fieldErrorMessages.entrySet()) {
            messages.add(new ValidationMessage(fieldError.getKey(), fieldError.getValue()));
        }
        
        // Add general errors
        for (String generalError : generalErrorMessages) {
            messages.add(new ValidationMessage("General", generalError));
        }
        
        return messages;
    }
    
    // Inner class for validation messages
    public static class ValidationMessage {
        private final String field;
        private final String message;
        
        public ValidationMessage(String field, String message) {
            this.field = field;
            this.message = message;
        }
        
        public String getField() {
            return field;
        }
        
        public String getMessage() {
            return message;
        }
        
        @Override
        public String toString() {
            return field + ": " + message;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder resultStringBuilder = new StringBuilder("ValidationResult{");
        resultStringBuilder.append("successful=").append(isValidationSuccessful);
        resultStringBuilder.append(", successCount=").append(successMessages.size());
        resultStringBuilder.append(", errorCount=").append(getTotalErrorCount());
        
        if (hasErrors()) {
            resultStringBuilder.append(", errors=[").append(getAllErrorMessagesAsString()).append("]");
        }
        
        resultStringBuilder.append("}");
        return resultStringBuilder.toString();
    }
}