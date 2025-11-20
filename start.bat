@echo off
echo Starting CGPA Calculator Demo...

REM Create build directory if needed
if not exist "build\classes" mkdir build\classes

REM Compile and run in one step
javac -d build\classes -cp src\main\java src\main\java\com\cgpacalculator\*.java src\main\java\com\cgpacalculator\controller\*.java src\main\java\com\cgpacalculator\model\*.java src\main\java\com\cgpacalculator\view\*.java src\main\java\com\cgpacalculator\utils\*.java src\main\java\com\cgpacalculator\utils\exceptions\*.java && java -cp build\classes com.cgpacalculator.CGPACalculatorApp