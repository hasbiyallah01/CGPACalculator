@echo off
title CGPA Calculator - Enhanced Version
cd /d "%~dp0"

echo ========================================
echo    Enhanced CGPA Calculator
echo ========================================
echo.
echo Features:
echo - Requires 18-24 total units
echo - All courses visible and editable  
echo - Complete data validation
echo - Automatic save/load functionality
echo - No partial calculations allowed
echo.

REM Create build directory if needed
if not exist "build\classes" mkdir build\classes

echo Compiling Java files...
javac -d build\classes -cp src\main\java src\main\java\com\cgpacalculator\*.java src\main\java\com\cgpacalculator\model\*.java src\main\java\com\cgpacalculator\view\*.java src\main\java\com\cgpacalculator\utils\*.java

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed! Please check for errors.
    pause
    exit /b 1
)

echo Compilation successful!
echo Starting application...
echo.

java -cp build\classes com.cgpacalculator.CGPACalculatorApp

echo.
echo Application closed.
pause