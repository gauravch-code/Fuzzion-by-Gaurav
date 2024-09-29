# Gaurav Chintakunta

Here’s a detailed **README.md** file that includes all the necessary information for users to clone, assemble, deploy, and run the Fuzzion program and its tests, along with explanations about the language’s semantics and limitations. This is tailored to guide users through every step, assuming they are new to the project.

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
4. **Git** or **Github Desktop**
5. **IntelliJ IDEA**

---

## **Cloning the Repository**

To start working with Fuzzion, you need to clone the project from the repository.

1. **Open a Terminal or Command Prompt**:
    - **Windows**: Press `Windows + R`, type `cmd`, and press Enter.
    - **macOS/Linux**: Open the Terminal from your Applications folder or taskbar.

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
Fuzzion allows users to simulate fuzzy logic gates, bind variables, and evaluate expressions within a defined scope. Below are the primary components of the language.

### **1. Expressions**
Expressions in Fuzzion can be **literals**, **variables**, or **logical gates**.

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
Fuzzion supports simple fuzzy logic gates like `AND` and `TestGate`.

- **AND Gate**: Evaluates two expressions using fuzzy logic.

  **Example:**
  ```scala
  val andExpr = Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5)) // Result: 0.5
  ```

- **TestGate**: Tests whether the result of an expression meets a threshold.

  **Example:**
  ```scala
  val testGate = Fuzzion.TestGate(Fuzzion.Literal(1.0).and(Fuzzion.Literal(0.5)), 0.5) // Result: 1.0
  ```

### **2. Scopes and Variable Binding**
A **scope** is where variables are stored and managed. Variables are bound to values within a scope and can be evaluated within the same scope.

**Example:**
```scala
val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7))
val result = Fuzzion.eval(Fuzzion.Variable("x"), scope) // Result: 0.7
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
If a variable is not found in the scope, Fuzzion defaults to `0.0` without throwing any errors or warnings. A more robust error-handling mechanism is needed.

### **5. No Type Checking**
Fuzzion treats all expressions as numerical values and does not enforce a type system. Adding type safety would enhance the language’s robustness.

---

## **Conclusion**

Fuzzion is a lightweight DSL for designing and evaluating fuzzy logic gates. It supports variable binding, scope management, and basic logic operations. Though the language has limitations, it offers a foundation for working with fuzzy logic in a simple and intuitive manner.
