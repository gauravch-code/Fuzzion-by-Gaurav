# Gaurav Chintakunta
---
# **Fuzzion: A Fuzzy Logic Domain-Specific Language (DSL)**

### **Description**
Fuzzion is a domain-specific language (DSL) designed for designers of fuzzy logic systems. It allows users to create and evaluate simulated fuzzy logic gates using variables and scopes to implement fuzzy logic functions. The language is built using Scala, and tests are provided via ScalaTest to ensure correctness and functionality.

---

## **Getting Started**

### **Prerequisites**

Before you can run the Fuzzion program, ensure that you have the following tools installed on your machine:

1. **Java Development Kit (JDK)** (Version 22 or higher)
2. **Scala** (Version 3.5.1)
3. **SBT (Simple Build Tool)**
4. **Git** or **GitHub Desktop**
5. **IntelliJ IDEA** (Optional, but recommended)

---

## **Assembling and Deploying the Project**

### **Step 1: Compile the Project**
You need to download the zip file from the GitHub repository. Then, you need to compile the project before running it. In the terminal (inside the `fuzzion` directory), run the following command:

```bash
sbt compile
```

This command will download all necessary dependencies and compile the Scala code.

### **Step 2: Cleaning the Project**
To clean the project (remove compiled files), you can use:

```bash
sbt clean
```

---

## **Running the Program**

You can run the Fuzzion program in two ways:

### **Option 1: Running from IntelliJ IDEA**

1. Open **IntelliJ IDEA**.
2. Navigate to `src/main/scala/FuzzionApp.scala`.
3. Right-click on the `main` method and select **Run 'FuzzionApp'**.

### **Option 2: Running from the Command Line**

To run the program directly from the terminal, use the following command:

```bash
sbt run
```

---

## **Running the Tests**

Fuzzion includes automated tests to verify the correctness of the logic gates and their evaluation. Tests are written using **ScalaTest**.

### **Option 1: Running Tests in IntelliJ IDEA**
1. Navigate to the file `src/test/scala/FuzzionTest.scala`.
2. Right-click on the file and select **Run 'FuzzionTest'** to run all tests.

### **Option 2: Running Tests from the Command Line**
Run the following command in the terminal:

```bash
sbt test
```

After running the tests, you should see a message indicating whether all tests passed:
```bash
[info] All tests passed.
```

---

## **Understanding the Fuzzion Language**

### **Overview**
Fuzzion allows users to simulate fuzzy logic gates, bind variables, assign values, and evaluate expressions within a defined scope. Below are the primary components of the language.

### **1. Expressions**
Expressions in Fuzzion can be **literals**, **variables**, **assignments**, or **logical gates**.

#### **1.1. Literals**
A literal represents a fixed numerical value between `0.0` and `1.0`.

**Example:**
```scala
val literal = Fuzzion.Literal(1.0)
```

#### **1.2. Variables**
Variables represent named values whose values are determined by the scope.

**Example:**
```scala
val variable = Fuzzion.Variable("x")
scope.bind("x", Fuzzion.Literal(0.7)) // Binds 0.7 to x
```

#### **1.3. Logical Gates**
Fuzzion supports fuzzy logic gates like **AND**, and it also supports assignments and scopes for variable management.

- **AND Gate**: The `AND` gate evaluates two expressions using fuzzy logic. The result is determined by the evaluation of both expressions. Fuzzion uses fuzzy logic, so the output can be a value between `0.0` and `1.0`.

  **Example:**
  ```scala
  val andExpr = Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5)) // Result: 0.5
  ```

- **TestGate**: The `TestGate` evaluates an expression and checks whether its result meets a specific threshold. If the result is greater than or equal to the threshold, it returns `1.0`; otherwise, it returns `0.0`.

  **Example:**
  ```scala
  val testGate = Fuzzion.TestGate(Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5)), 0.5) // Result: 1.0
  ```

#### **1.4. Assignment and Scopes**
Assignments allow you to bind variables to expressions within a scope, and scopes manage the lifecycle of these variables. Variables can be reassigned or shadowed within the scope.

- **Assign**: The `Assign` operation binds a variable to a specific expression. The result is stored in the scope.

  **Example**:
  ```scala
  val assignExpr = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
  Fuzzion.eval(assignExpr, scope) // Assigns 0.5 to y in the scope
  ```

- **Scope**: A scope is where variables are stored and managed. When evaluating an expression, the scope is checked for variable values, and new values can be assigned or reassigned.

  **Example**:
  ```scala
  val scope = new Fuzzion.Scope()
  scope.bind("x", Fuzzion.Literal(0.7)) // Binds 0.7 to x
  val result = Fuzzion.eval(Fuzzion.Variable("x"), scope) // Result: 0.7
  ```

### **1.5. Test Cases**
Fuzzion comes with several test cases to verify the correctness of its operations, including:
1. **AND Gate Test**: Ensures that the AND operation works correctly for different inputs.
   - **Test Example**:
   ```scala
   val exp = Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.0))
   assert(Fuzzion.eval(exp) == 0.0) // Correct evaluation of AND gate
   ```

2. **Assignment Test**: Verifies that variables can be assigned values within a scope and that they can be correctly evaluated afterward.
   - **Test Example**:
   ```scala
   val scope = new Fuzzion.Scope()
   val assignExpr = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
   assert(Fuzzion.eval(assignExpr, scope) == 0.5)
   assert(Fuzzion.eval(Fuzzion.Variable("y"), scope) == 0.5)
   ```

3. **Variable Binding Test**: Ensures that variables bound in a scope can be retrieved and used in expressions.
   - **Test Example**:
   ```scala
   val scope = new Fuzzion.Scope()
   scope.bind("x", Fuzzion.Literal(0.7))
   val varExpr = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
   assert(Fuzzion.eval(varExpr, scope) == 0.7) // Uses value of x from the scope
   ```

---

## **Limitations of the Current Implementation**

While Fuzzion is functional for basic operations, it has several limitations:

### **1. Limited Logical Gates**
Fuzzion currently only supports `AND` and `TestGate`. Other gates like `OR`, `XOR`, and `NAND` are not implemented.

### **2. Simple Parsing**
The parser is basic and can only handle simple expressions like `"AND true false"`. It does not support complex expressions with parentheses or nesting.

### **3. No Nested Scopes**
The current implementation supports only a single flat scope. There is no functionality for nested or hierarchical scopes.

### **4. No Error Handling**
If a variable is not found

 in the scope, Fuzzion defaults to `0.0` without throwing any errors or warnings. A more robust error-handling mechanism is needed.

### **5. No Type Checking**
Fuzzion treats all expressions as numerical values and does not enforce a type system. Adding type safety would enhance the language’s robustness.

Here are additional limitations for the current implementation of **Fuzzion** that you can include in the README:

---

## **Limitations of the Current Implementation**

While Fuzzion provides basic functionality for fuzzy logic evaluation, the current implementation has several notable limitations:

### **1. Limited Logical Gates**
Fuzzion currently supports only a few basic logic gates (`AND` and `TestGate`). Other commonly used gates such as `OR`, `XOR`, `NAND`, and `NOT` are missing. This limits the expressive power of the language when implementing more complex fuzzy logic operations.

### **2. Simple and Rigid Parsing**
The Fuzzion parser is highly simplistic. It only supports parsing basic expressions such as `AND true false` or `NOT true`. The parser lacks:
- **Parentheses Handling**: No support for grouping expressions with parentheses.
- **Complex Expressions**: You cannot nest expressions or combine multiple operations like `AND`, `OR`, and `NOT` together.
- **Custom User-defined Functions**: Users cannot define their own functions or logic gates. All logic is hardcoded within the language.

### **3. No Support for Multiple Data Types**
All expressions and variables in Fuzzion are treated as numeric values (`Double`) between `0.0` and `1.0`, without support for other data types. This restricts the DSL from supporting richer logic expressions that require more complex data types like **Booleans**, **Strings**, or **Collections**.

### **4. Lack of Nested Scopes**
Fuzzion only supports a single flat scope for variable binding. In more advanced use cases, nested scopes (e.g., inner scopes within loops or functions) are needed to properly manage variable shadowing and to handle more complex logic operations. Fuzzion does not provide a mechanism for creating and managing nested scopes.

### **5. No Support for Control Flow**
The language lacks control flow structures like **conditionals** (`if-else`), **loops**, and **case-switch** statements. This makes it impossible to express dynamic decisions or repeated operations based on the results of previous logic gate evaluations.

### **6. Static Variable Management**
Variable management in Fuzzion is static. Once a variable is bound in a scope, it cannot be dynamically updated unless explicitly re-assigned. There is no concept of **variable mutation**, **dynamic scoping**, or **closures**, which limits the flexibility in using variables within different contexts or functions.

### **7. No Error Handling or Debugging Support**
Fuzzion does not provide a comprehensive error-handling mechanism. For example:
- **Undefined Variables**: If a variable is not found in the current scope, it defaults to `0.0` without any error message.
- **Syntax Errors**: The parser does not catch invalid expressions or syntax errors. Users are left without useful feedback if they misuse the language.
- **Runtime Errors**: There are no mechanisms to catch or recover from runtime errors. If an invalid operation is attempted, the program may silently fail or produce incorrect results.


### **8. Limited Extensibility**
Fuzzion's current architecture is not designed to be easily extensible. Adding new features, gates, or operations requires modifying the core source code directly, rather than enabling users to extend the language via a plugin system or configuration file.


### **9. No Input/Output Integration**
The current implementation does not provide any mechanisms for interacting with external systems or files. There are no APIs or functions to read inputs from files, databases, or external services, and there is no way to export the results of expressions to files or other formats.

---

