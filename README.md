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

## **Cloning the Repository**

To start working with Fuzzion, you need to clone the project from the repository.

1. **Open a Terminal or Command Prompt**:
    In Windows, open Command Prompt.
2. **Run the following command to clone the repository**:
   ```bash
   git clone https://github.com/your-repository/fuzzion
   ```

3. **Move into the project directory**:
   ```bash
   cd fuzzion
   ```

---

## **Assembling and Deploying the Project**

### **Step 1: Compile the Project**
You need to compile the project before running it. In the terminal (inside the `fuzzion` directory), run the following command:

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
Fuzzion allows users to simulate fuzzy logic gates, assign variables, and evaluate expressions within a defined scope. Below is a detailed guide on how to create and evaluate different expressions.

---

## **Creating and Evaluating Expressions in Fuzzion**

### **1. Creating Expressions in Fuzzion**

Fuzzion supports different types of expressions, which include literals, variables, assignments, and logic gates. Here’s how you can create them:

#### **1.1. Literals**

A literal is a simple numeric value between `0.0` and `1.0`. It represents a fuzzy truth value.

**How to Create a Literal:**
```scala
val literal = Fuzzion.Literal(1.0)
```
In this case, the literal `1.0` represents a fully "true" value in fuzzy logic.

#### **1.2. Variables**

Variables in Fuzzion represent named entities whose values are determined within a scope. You can assign a variable and retrieve its value dynamically during expression evaluation.

**How to Create and Bind a Variable:**
```scala
val variable = Fuzzion.Variable("x")
scope.bind("x", Fuzzion.Literal(0.7)) // Binds variable "x" to the value 0.7 in the scope
```
Here, variable `x` is bound to the value `0.7` within the scope.

#### **1.3. Logic Gates (AND, TestGate)**

Fuzzion supports logic gates, such as `AND` and `TestGate`, to perform fuzzy logic operations on expressions.

- **AND Gate**: The `AND` gate evaluates two expressions and returns the second value if both are non-zero.

**How to Create an AND Expression:**
```scala
val and_exp = Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5)) // Returns 0.5
```

- **TestGate**: This gate evaluates an expression and compares its result with a given threshold. If the result meets or exceeds the threshold, it returns `1.0`; otherwise, it returns `0.0`.

**How to Create a TestGate Expression:**
```scala
val test_gate = Fuzzion.TestGate(Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5)), 0.5) // Returns 1.0
```

#### **1.4. Assignment and Scopes**
Assignments allow you to bind variables to expressions within a scope, and scopes manage the lifecycle of these variables. Variables can be reassigned or shadowed within the scope.

- **Assign**: The `Assign` operation binds a variable to a specific expression. The result is stored in the scope.

  **Example**:
  ```scala
  val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
  Fuzzion.eval(assignExpr, scope) // Assigns 0.5 to y in the scope
  ```

- **Scope**: A scope is where variables are stored and managed. When evaluating an expression, the scope is checked for variable values, and new values can be assigned or reassigned.

  **Example**:
  ```scala
  val scope = new Fuzzion.Scope()
  scope.bind("x", Fuzzion.Literal(0.7)) // Binds 0.7 to x
  val result = Fuzzion.eval(Fuzzion.Variable("x"), scope) // Result: 0.7
  ```

---

### **2. Evaluating Expressions in Fuzzion**

Once you’ve created expressions, you can evaluate them using the `eval` function. Fuzzion evaluates literals, variables, and logic gates differently depending on their type.

#### **2.1. Evaluating Literals**

A `Literal` simply returns its value when evaluated.

**How to Evaluate a Literal:**
```scala
val literal = Fuzzion.Literal(1.0)
val result = Fuzzion.eval(literal) // Returns 1.0
```

#### **2.2. Evaluating Variables**

Variables are evaluated by retrieving their value from the scope in which they are bound. If a variable is not found in the scope, it defaults to `0.0`.

**How to Evaluate a Variable:**
```scala
val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7))
val result = Fuzzion.eval(Fuzzion.Variable("x"), scope) // Returns 0.7
```

#### **2.3. Evaluating AND Expressions**

An `AND` gate evaluates two expressions and returns the second one, provided the first one is non-zero.

**How to Evaluate an AND Expression:**
```scala
val and_exp = Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5))
val result = Fuzzion.eval(andExpr) // Returns 0.5
```

#### **2.4. Evaluating TestGate**

The `TestGate` compares the result of an expression to a threshold. If the result of the expression is greater than or equal to the threshold, it returns `1.0`; otherwise, it returns `0.0`.

**How to Evaluate a TestGate Expression:**
```scala


val test_gate = Fuzzion.TestGate(Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5)), 0.5)
val result = Fuzzion.eval(testGate) // Returns 1.0
```

#### **2.5. Evaluating Assignments**

The `Assign` expression binds a value to a variable in a given scope. The result of the evaluation is the value assigned to the variable.

**How to Evaluate an Assignment:**
```scala
val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
val result = Fuzzion.eval(assignExpr, scope) // Returns 0.5 and assigns it to "y"
```

After the assignment, the variable `y` will have the value `0.5` in the current scope.

---

### **3. Example: Creating and Evaluating Expressions**

Here’s an example of how you can create and evaluate expressions in Fuzzion:

```scala
val scope = new Fuzzion.Scope()

// Step 1: Bind a variable "x" to 0.7
scope.bind("x", Fuzzion.Literal(0.7))

// Step 2: Create an AND expression involving the variable "x"
val expr = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))

// Step 3: Evaluate the expression within the scope
val result = Fuzzion.eval(expr, scope) // Result will be 0.7
println(s"Evaluation result: $result")
```

---

### **4. Test Cases for Expressions**

To ensure that your expressions are created and evaluated correctly, Fuzzion includes several test cases:

- **AND Gate Test**:
  ```scala
  val exp = Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.0))
  assert(Fuzzion.eval(exp) == 0.0)
  ```

- **Assignment and Retrieval Test**:
  ```scala
  val scope = new Fuzzion.Scope()
  val assignExpr = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
  assert(Fuzzion.eval(assignExpr, scope) == 0.5)
  assert(Fuzzion.eval(Fuzzion.Variable("y"), scope) == 0.5)
  ```

- **Variable Binding Test**:
  ```scala
  val scope = new Fuzzion.Scope()
  scope.bind("x", Fuzzion.Literal(0.7))
  val varExpr = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
  assert(Fuzzion.eval(varExpr, scope) == 0.7)
  ```

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

### **8. Hardcoded Logic Thresholds**
For the `TestGate` logic, the threshold is hardcoded and evaluated against a specific number. There is no way to dynamically alter the threshold based on user input or different evaluation contexts, which limits the flexibility of the fuzzy logic gate comparisons.

### **9. No Input/Output Integration**
The current implementation does not provide any mechanisms for interacting with external systems or files. There are no APIs or functions to read inputs from files, databases, or external services, and there is no way to export the results of expressions to files or other formats.


