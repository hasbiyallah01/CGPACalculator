# Enhanced CGPA Calculator

A comprehensive Java Swing application for calculating Cumulative Grade Point Average (CGPA) with advanced features, enhanced course management, and strict validation requirements.

## 🆕 Enhanced Features

### ✅ Unit Requirements (18-24 Units)
- **Minimum Requirement**: Must have at least 18 units total before CGPA calculation
- **Maximum Limit**: Cannot exceed 24 units per semester
- **Real-time Validation**: Progress bar shows current unit status
- **Visual Feedback**: Clear indicators when requirements are met

### ✅ Enhanced Course Management
- **Visible Course List**: All courses displayed in an organized table
- **Easy Editing**: Click on any course to edit details
- **Remove/Update**: Individual course removal and updating
- **Duplicate Prevention**: Automatic detection of duplicate course names
- **Real-time Calculations**: Grade points and credit points calculated automatically

### ✅ Complete Data Validation
- **No Partial Calculations**: CGPA calculation only proceeds when ALL requirements are met
- **Required Fields**: All course names, units, and grades must be complete
- **Consistent Data**: Current CGPA and cumulative units must be provided together
- **Clear Error Messages**: Specific feedback on what needs to be completed

### ✅ Improved User Experience
- **Status Dashboard**: Real-time summary of courses and units
- **Calculation Readiness**: Clear indication when ready to calculate
- **Enhanced Validation**: Comprehensive error checking and user guidance
- **Better Visual Design**: Organized layout with clear sections

## 🚀 Quick Start

### Running the Enhanced Version
```bash
# Compile and run the enhanced demo
javac -cp src src/main/java/com/cgpacalculator/test/EnhancedCGPACalculatorDemo.java
java -cp src com.cgpacalculator.test.EnhancedCGPACalculatorDemo
```

### Key Requirements
1. **Add Courses**: Enter between 18-24 total units
2. **Complete All Fields**: Every course must have name, units, and grade
3. **Optional Previous Data**: Current CGPA and cumulative units (for continuing students)
4. **Calculate**: Button only enables when all requirements are met

## 📋 How to Use

### Step 1: Add Courses
- Enter course name (1-50 characters)
- Select units (1-6 per course)
- Choose grade (A, B, C, D, E, F)
- Click "Add Course"

### Step 2: Manage Courses
- **View**: All courses shown in table with calculations
- **Edit**: Select course and modify details, then click "Update Course"
- **Remove**: Select course and click "Remove Course"
- **Clear All**: Remove all courses at once

### Step 3: Monitor Progress
- **Course Count**: Shows number of courses added
- **Total Units**: Displays current unit total
- **Progress Bar**: Visual indicator of unit requirements (18-24)
- **Status**: Clear message about calculation readiness

### Step 4: Optional Previous Academic Data
- **Current CGPA**: Enter if you're a continuing student
- **Cumulative Units**: Enter total units from previous semesters
- **Note**: Both fields must be provided together or left empty

### Step 5: Calculate
- **Validation**: System checks all requirements automatically
- **Calculate Button**: Only enabled when ready
- **Results**: Shows current GPA, updated CGPA, classification, and motivational message

## 🎯 Validation Rules

### Course Requirements
- ✅ Minimum 18 total units required
- ✅ Maximum 24 total units allowed
- ✅ Each course: 1-6 units
- ✅ All course names must be unique
- ✅ All fields must be completed

### Academic Data Consistency
- ✅ If current CGPA provided → cumulative units required
- ✅ If cumulative units provided → current CGPA required
- ✅ Both can be empty (new students)
- ✅ CGPA must be 0.00-5.00 if provided

### Calculation Prevention
- ❌ Cannot calculate with incomplete courses
- ❌ Cannot calculate below 18 units
- ❌ Cannot calculate above 24 units
- ❌ Cannot calculate with missing data
- ❌ Cannot calculate with invalid grades

## 🏆 Grade Classifications

| CGPA Range | Classification |
|------------|----------------|
| 4.50 - 5.00 | First Class |
| 3.50 - 4.49 | Second Class Upper |
| 2.50 - 3.49 | Second Class Lower |
| 1.50 - 2.49 | Third Class |
| 0.00 - 1.49 | Fail |

## 📁 Project Structure

```
src/main/java/com/cgpacalculator/
├── model/
│   └── Course.java
├── utils/
│   ├── Constants.java
│   ├── CGPACalculationValidator.java
│   └── ValidationResult.java
├── view/
│   ├── EnhancedMainFrame.java
│   └── EnhancedCourseManagementPanel.java
└── test/
    └── EnhancedCGPACalculatorDemo.java
```

## 🔧 Technical Features

- **Real-time Validation**: Instant feedback on all inputs
- **Comprehensive Error Handling**: Clear, specific error messages
- **Accessibility**: Keyboard navigation and screen reader support
- **Responsive Design**: Adapts to different screen sizes
- **Data Persistence**: Save/load functionality for course data

## 🎨 User Interface Highlights

- **Clean Layout**: Organized sections for easy navigation
- **Visual Indicators**: Color-coded validation and status messages
- **Progress Tracking**: Real-time unit progress bar
- **Table Management**: Sortable, editable course table
- **Contextual Help**: Tooltips and guidance throughout

## 🚨 Important Notes

1. **Strict Validation**: The calculator will NOT perform calculations until ALL requirements are met
2. **Unit Range**: Exactly 18-24 units required - no exceptions
3. **Complete Data**: Every course must have all fields filled
4. **Consistency**: Academic data must be complete or empty together
5. **No Shortcuts**: All validation rules are enforced

This enhanced version ensures academic integrity by requiring complete, valid data before any CGPA calculations can be performed.