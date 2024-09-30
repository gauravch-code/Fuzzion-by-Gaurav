# Gaurav Chintakunta
---

# **Fuzzion: A DSL for Fuzzy Logic**

## **Description**

**Fuzzion** is a domain-specific language (DSL) created for designers of **fuzzy logic** systems. With Fuzzion, users can simulate and evaluate fuzzy logic gates using variables and scopes to implement fuzzy logic functions. This language allows users to create and evaluate expressions involving fuzzy logic operations like **AND**, **OR**, **NOT** and **XOR**, along with support for **TestGate**. The language is written in **Scala** and includes unit tests implemented using **ScalaTest**.

---

## **Prerequisites**

Before setting up and running the Fuzzion project, ensure you have the following installed on your system:

- **Java Development Kit (JDK) 8 or higher**
- **Scala 3.5.1**
- **SBT (Simple Build Tool) 1.10.2 or later**
- **IntelliJ IDEA** (recommended, with Scala plugin)

---

## **Cloning the Repository**

To get started, clone the repository from GitHub.

1. **Open your terminal**:
    **Windows**: In Windows, open Command prompt.
  

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repository/fuzzion
   ```
   Replace `your-repository` with the actual repository URL.

3. **Navigate into the Project Directory**:
   ```bash
   cd fuzzion
   ```

---

## **Building and Running the Project**

Once you have cloned the repository, follow the steps below to build and run the Fuzzion program.

### **Building the Project**

To compile and build the project, use the following command inside the project directory:

```bash
sbt clean compile
```

This command will download necessary dependencies and compile the source code.

### **Running the Program**

After compiling the project, run the program using the following command:

```bash
sbt run
```

The program will evaluate several sample fuzzy logic expressions, including **AND**, **OR**, and **NOT** operations, and will output the results to the console.

---

## **Running the Tests**

Fuzzion comes with unit tests written in ScalaTest to verify the functionality of the fuzzy logic gates and variable handling.

### **Running Tests in IntelliJ IDEA**

1. Open **IntelliJ IDEA**.
2. Navigate to the `src/test/scala/FuzzionTest.scala` file.
3. Right-click on the file and select **Run 'FuzzionTest'**.

### **Running Tests from the Command Line**

To run tests from the terminal using SBT, use the following command:

```bash
sbt test
```

The output will show whether the tests passed or failed.

---

## **Creating and Evaluating Expressions in Fuzzion**

Fuzzion provides constructs for creating and evaluating fuzzy logic expressions. Below are the key components:

### **Literals**

A **Literal** in Fuzzion represents a value between `0.0` and `1.0`. This is commonly used in fuzzy logic expressions.

**Example:**
```scala
val literal = Fuzzion.Literal(0.7)
```

### **Variables**

A **Variable** represents a named value that can be bound to a literal within a scope. Variables must be bound in the scope to be used in expressions.

**Example:**
```scala
val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7)) // Binds the variable 'x' to 0.7
val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
val result = Fuzzion.eval(var_exp, scope) // Expected result: 0.7
```

### **Logical Gates**

Fuzzion supports fuzzy logic gates, including **AND**, **OR**, **NOT**, and **TestGate**.

#### **AND Gate**

The **AND** gate evaluates two expressions and returns the minimum value of the two.

**Example:**
```scala
val and_exp = Fuzzion.Literal(0.6).and(Fuzzion.Literal(0.3))
val result = Fuzzion.eval(and_exp) // Expected result: 0.3
```

#### **OR Gate**

The **OR** gate evaluates two expressions and returns the maximum value of the two.

**Example:**
```scala
val or_exp = Fuzzion.Literal(0.6).or(Fuzzion.Literal(0.3))
val result = Fuzzion.eval(or_exp) // Expected result: 0.6
```

#### **NOT Gate**

The **NOT** gate inverts the value of an expression, returning `1.0 - value`.

**Example:**
```scala
val not_exp = Fuzzion.not(Fuzzion.Literal(0.6))
val result = Fuzzion.eval(not_exp) // Expected result: 0.4
```

#### **TestGate**

The **TestGate** evaluates an expression and returns `1.0` if the value is non-zero, or `0.0` if the value is zero or negative.

**Example:**
```scala
val testgate_exp = Fuzzion.TestGate(Fuzzion.Literal(0.5))
val result = Fuzzion.eval(testgate_exp) // Expected result: 1.0
```

---

## **Semantics of Fuzzion**

### **Expression Evaluation**

Expressions in Fuzzion are created using a combination of literals, variables, and logical gates. These expressions are then evaluated using the `eval` function.

- **Literal**: A literal is evaluated to its value (e.g., `Fuzzion.eval(Fuzzion.Literal(0.7))` returns `0.7`).
- **Variable**: A variable's value is retrieved from the current scope (e.g., `Fuzzion.eval(Fuzzion.Variable("x"), scope)`).
- **Logical Gates**: Logical gates like **AND**, **OR**, and **NOT** return values based on fuzzy logic rules.
- **TestGate**: Evaluates whether a value is non-zero or zero.

### **Scopes and Variables**

Variables in Fuzzion are bound to values within a scope, and these bindings determine how variables are evaluated when used in expressions. The `Scope` class manages the variable bindings.

**Example:**
```scala
val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7))
val result = Fuzzion.eval(Fuzzion.Variable("x"), scope) // Result: 0.7
```

---

## **Limitations**

Fuzzion is functional but comes with a few limitations. Here are its major limitations:

### **1. No Nested Scopes**

Fuzzion is limited to one flat scope. This implies that you are unable to have hierarchical or layered scopes, which are crucial when working with increasingly complicated systems that call for scoped evaluations or variable shadowing. It is more difficult to manage scope-dependent logic or reuse variables within particular sub-operations when nested scopes are not present since users are forced to define all variable definitions in a global context.

### **2. Simple Parsing System**

Fuzzion's expression parser at the moment is quite basic and limited to handling simple expressions. Parentheses, operator precedence, and more intricate, nested expressions are not supported. Because users are unable to construct sophisticated logical expressions using standard notation rules, this restricts the language's flexibility. Building complex expressions using Fuzzion would be simpler with an improved parser.

### **3. No Type Checking**

Since Fuzzion lacks a formal type system, all expressions are handled as unvalidated numerical values. When users try to mix incompatible actions or pass invalid expressions, this can result in unexpected behavior. These problems might be avoided by implementing a type system, which would enforce guidelines for the use of literals, variables, and logic gates.

### **4. Lack of Error Handling**

Fuzzion does not handle errors well. For instance, the program will default to returning `0.0` rather than issuing an error when attempting to evaluate an undefined variable. This can make it more difficult for users to troubleshoot and debug their programs. Better error reporting would enhance the user experience and assist users in recognizing and fixing logical errors.


### **5. Scalability for Large Expressions**

Fuzzion's present implementation is meant for expressions that range from simple to somewhat complicated. The speed could deteriorate if more variables and expressions are used because of the dependence on simple data structures (such maps for scope handling). To handle large-scale fuzzy logic systems, future implementations could streamline the evaluation procedure and add more effective data structures.

### **6. No Support for Complex Expression Evaluation**

More complex expression compositions, like combining several gates into a single, huge expression using operator precedence or parentheses, are not supported by Fuzzion. Users cannot readily design large chains of logic without manually breaking them down into smaller portions, and they can only evaluate one operation at a time.

---

