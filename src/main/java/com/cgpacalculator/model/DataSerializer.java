package com.cgpacalculator.model;

import com.cgpacalculator.utils.ValidationUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;public class DataSerializer {
    
    private static final String JSON_INDENT = "  ";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;public static String serializeStudentToJson(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student object cannot be null");
        }
        
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        
        // Add metadata
        jsonBuilder.append(JSON_INDENT).append("\"metadata\": {\n");
        jsonBuilder.append(JSON_INDENT).append(JSON_INDENT)
                   .append("\"version\": \"1.0\",\n");
        jsonBuilder.append(JSON_INDENT).append(JSON_INDENT)
                   .append("\"lastModified\": \"")
                   .append(LocalDateTime.now().format(DATE_FORMATTER))
                   .append("\",\n");
        jsonBuilder.append(JSON_INDENT).append(JSON_INDENT)
                   .append("\"applicationName\": \"CGPA Calculator\"\n");
        jsonBuilder.append(JSON_INDENT).append("},\n");
        
        // Add student data
        jsonBuilder.append(JSON_INDENT).append("\"studentData\": {\n");
        jsonBuilder.append(JSON_INDENT).append(JSON_INDENT)
                   .append("\"currentCGPA\": ").append(student.getCurrentCGPA()).append(",\n");
        jsonBuilder.append(JSON_INDENT).append(JSON_INDENT)
                   .append("\"cumulativeUnits\": ").append(student.getCumulativeUnits()).append("\n");
        jsonBuilder.append(JSON_INDENT).append("},\n");
        
        // Add courses array
        jsonBuilder.append(JSON_INDENT).append("\"courses\": [\n");
        List<Course> courses = student.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            jsonBuilder.append(serializeCourseToJson(course, JSON_INDENT + JSON_INDENT));
            if (i < courses.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }
        jsonBuilder.append(JSON_INDENT).append("]\n");
        
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
    
    // Method comment
    public static Student deserializeStudentFromJson(String jsonData) {
        if (jsonData == null || jsonData.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON data cannot be null or empty");
        }
        
        try {
            // Simple JSON parsing for our specific format
            Student student = new Student();
            
            // Extract student data
            double cgpa = extractDoubleValue(jsonData, "currentCGPA");
            int units = extractIntValue(jsonData, "cumulativeUnits");
            
            student.setCurrentCGPA(cgpa);
            student.setCumulativeUnits(units);
            
            // Extract and parse courses
            String coursesSection = extractCoursesSection(jsonData);
            List<Course> courses = parseCoursesFromJson(coursesSection);
            
            for (Course course : courses) {
                student.addCourse(course);
            }
            
            return student;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage(), e);
        }
    }
    
    // Method comment
    public static boolean isValidJsonFormat(String jsonData) {
        try {
            if (jsonData == null || jsonData.trim().isEmpty()) {
                return false;
            }
            
            // Check for required sections
            return jsonData.contains("\"metadata\"") &&
                   jsonData.contains("\"studentData\"") &&
                   jsonData.contains("\"courses\"") &&
                   jsonData.contains("\"currentCGPA\"") &&
                   jsonData.contains("\"cumulativeUnits\"");
                   
        } catch (Exception e) {
            return false;
        }
    }
    
    // Method comment
    public static String createDefaultStudentJson() {
        Student defaultStudent = new Student();
        return serializeStudentToJson(defaultStudent);
    }
    
    // Private helper methods for JSON serialization
    private static String serializeCourseToJson(Course course, String indent) {
        StringBuilder courseJson = new StringBuilder();
        courseJson.append(indent).append("{\n");
        courseJson.append(indent).append(JSON_INDENT)
                  .append("\"courseName\": \"").append(escapeJsonString(course.getCourseName())).append("\",\n");
        courseJson.append(indent).append(JSON_INDENT)
                  .append("\"units\": ").append(course.getUnits()).append(",\n");
        courseJson.append(indent).append(JSON_INDENT)
                  .append("\"letterGrade\": \"").append(course.getLetterGrade()).append("\",\n");
        courseJson.append(indent).append(JSON_INDENT)
                  .append("\"gradePoints\": ").append(course.getGradePoints()).append("\n");
        courseJson.append(indent).append("}");
        return courseJson.toString();
    }
    
    // Private helper methods for JSON deserialization
    private static double extractDoubleValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*([0-9.]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return 0.0;
    }
    
    private static int extractIntValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*([0-9]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }
    
    private static String extractStringValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }
    
    private static String extractCoursesSection(String json) {
        int coursesStart = json.indexOf("\"courses\": [");
        if (coursesStart == -1) {
            return "[]";
        }
        
        int arrayStart = json.indexOf('[', coursesStart);
        int arrayEnd = findMatchingBracket(json, arrayStart);
        
        return json.substring(arrayStart + 1, arrayEnd);
    }
    
    private static List<Course> parseCoursesFromJson(String coursesSection) {
        List<Course> courses = new java.util.ArrayList<>();
        
        if (coursesSection.trim().isEmpty()) {
            return courses;
        }
        
        // Split courses by objects
        String[] courseObjects = splitJsonObjects(coursesSection);
        
        for (String courseJson : courseObjects) {
            if (courseJson.trim().isEmpty()) continue;
            
            String courseName = extractStringValue(courseJson, "courseName");
            int units = extractIntValue(courseJson, "units");
            String letterGrade = extractStringValue(courseJson, "letterGrade");
            
            Course course = new Course(courseName, units, letterGrade);
            courses.add(course);
        }
        
        return courses;
    }
    
    private static String[] splitJsonObjects(String jsonArray) {
        List<String> objects = new java.util.ArrayList<>();
        int braceCount = 0;
        int start = 0;
        
        for (int i = 0; i < jsonArray.length(); i++) {
            char c = jsonArray.charAt(i);
            if (c == '{') {
                if (braceCount == 0) {
                    start = i;
                }
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    objects.add(jsonArray.substring(start, i + 1));
                }
            }
        }
        
        return objects.toArray(new String[0]);
    }
    
    private static int findMatchingBracket(String json, int startIndex) {
        int bracketCount = 1;
        for (int i = startIndex + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '[') {
                bracketCount++;
            } else if (c == ']') {
                bracketCount--;
                if (bracketCount == 0) {
                    return i;
                }
            }
        }
        return json.length() - 1;
    }
    
    private static String escapeJsonString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}