# Gaurav Chintakunta

# Fuzzion: A Fuzzy Logic Expression Language

**Fuzzion** is a domain-specific language (DSL) for evaluating fuzzy logic expressions with support for object-oriented features. It allows users to perform fuzzy logic operations, define and inherit classes, invoke methods dynamically, and work with variable scoping and assignment. Fuzzion is built with flexibility in mind, allowing for a range of arithmetic and logic operations, as well as dynamic object-oriented programming features.

## Introduction

Fuzzion allows users to create fuzzy logic expressions and evaluate them using a simple, expressive DSL. It supports basic fuzzy logic operations like `AND`, `OR`, `NOT`, `XOR`, as well as more advanced features like arithmetic operations (`ADD`, `MULTIPLY`), alpha-cut filtering, variable binding and scoping, and object-oriented constructs like classes, inheritance, method overriding, and dynamic dispatch.

## Features

- **Basic Fuzzy Logic Operations**: `AND`, `OR`, `NOT`, `XOR`
- **Arithmetic Operations**: `ADD`, `MULTIPLY`
- **Alpha-Cut Operation**: Filter fuzzy values based on a threshold
- **Variable Binding and Assignment**
- **Scoped Evaluations**
- **Object-Oriented Programming**: Classes, inheritance, method overriding, nested classes, dynamic dispatch
- **Test Suite**: Comprehensive tests using ScalaTest

## Prerequisites

- Scala 3.x
- SBT (Simple Build Tool)

## Installation and Setup

### Step 1: Clone the Repository

To set up Fuzzion, first clone the project repository:

```bash
git clone https://github.com/your-username/Fuzzion.git
cd Fuzzion
```

### Step 2: Compile the Project

Run the following SBT command to compile the project:

```bash
sbt compile
```

## Running the Program

You can run the main Fuzzion application by executing the following SBT command:

```bash
sbt run
```

This will run the `FuzzionApp` object, which contains several sample expressions using the DSL and object-oriented features. You will see the evaluation results of various fuzzy logic operations, alpha-cut functionality, and object-oriented expressions.

## Running Tests

Fuzzion includes a comprehensive test suite using ScalaTest. To run the tests:

```bash
sbt test
```

This will run the tests defined in `FuzzionTest.scala`, verifying the correctness of fuzzy logic operations, alpha-cut, variable assignment, scoping, class inheritance, method overriding, dynamic method dispatch, and more.

## Creating and Evaluating Expressions

Fuzzion allows users to create fuzzy logic expressions using a DSL-like syntax. Expressions can be built using literals, variables, and a range of operations. Evaluation is handled by the `Fuzzion.eval` method.

### Syntax and Semantics

#### **Literals**

Literals represent fixed values between 0 and 1 (fuzzy logic values). They form the simplest expression type in Fuzzion.

**Example**:
```scala
val literal = Fuzzion.Literal(0.7)
println(Fuzzion.eval(literal)) // Output: 0.7
```

#### **Fuzzy Logic Operations**

- **AND**: Returns the smaller value of the two inputs.
- **OR**: Returns the larger value of the two inputs.
- **NOT**: Inverts the input value (`NOT x = 1.0 - x`).
- **XOR**: Returns the absolute difference between the two input values.

**Example**:
```scala
val andExp = Fuzzion.Literal(0.6).and(Fuzzion.Literal(0.3))
println(Fuzzion.eval(andExp)) // Expected: 0.3
```

#### **Arithmetic Operations**

- **ADD**: Adds two values together, capped at 1.0.
- **MULTIPLY**: Returns the product of two inputs.

**Example**:
```scala
val addExp = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
println(Fuzzion.eval(addExp)) // Expected: 0.9

val multiplyExp = Fuzzion.Literal(0.4).multiply(Fuzzion.Literal(0.6))
println(Fuzzion.eval(multiplyExp)) // Expected: 0.24
```

#### **Alpha-Cut Operation**

The alpha-cut operation filters fuzzy values based on a threshold. If the value is greater than or equal to the alpha threshold, the value is retained. If not, the result is `0.0`.

**Example**:
```scala
val alphaCutExp = Fuzzion.Literal(0.7).alphaCut(0.5)
println(Fuzzion.eval(alphaCutExp)) // Expected: 0.7

val alphaCutFail = Fuzzion.Literal(0.3).alphaCut(0.5)
println(Fuzzion.eval(alphaCutFail)) // Expected: 0.0
```

#### **Variables and Scoping**

Fuzzion allows binding variables within scopes. Variables can be assigned values and referenced later. Scoping ensures that variable assignments are local to a block of expressions and can be shadowed.

**Example**:
```scala
val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7))
val varExp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
println(Fuzzion.eval(varExp, scope)) // Expected: 0.7
```

### Object-Oriented Features

Fuzzion supports object-oriented programming constructs, allowing users to define classes, methods, and variables, along with support for inheritance, method overriding, and nested classes.

#### **Class Definitions**

Classes are defined using `ClassDef` with methods (`MethodDef`) and class variables (`ClassVar`). Classes can inherit from parent classes, and methods can be overridden in derived classes.

**Syntax**:
```scala
val baseClass = Fuzzion.ClassDef("Base", List(Fuzzion.MethodDef("greet", List(), Fuzzion.Literal(1.0))))
val derivedClass = Fuzzion.ClassDef("Derived", List(Fuzzion.MethodDef("greet", List(), Fuzzion.Literal(2.0))), Some(baseClass))

val instance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
println(Fuzzion.eval(Fuzzion.InvokeMethod(instance, "greet", List()))) // Expected: 2.0
```

#### **Inheritance and Method Overriding**

Inheritance allows derived classes to override methods from the parent class. If a method is not overridden, the parent class’s method will be used.

**Example**:
```scala
val baseClass = Fuzzion.ClassDef("Base", List(Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(10.0))))
val derivedClass = Fuzzion.ClassDef("Derived", List(Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(20.0))), Some(baseClass))

val instance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
println(Fuzzion.eval(Fuzzion.InvokeMethod(instance, "calculate", List()))) // Expected: 20.0
```

#### **Nested Classes**

Nested classes can access the outer class’s variables and methods. Scoping rules ensure that inner classes have access to outer class variables.

**Example**:
```scala
val outerClass = Fuzzion.ClassDef("OuterClass", List(
  Fuzzion.MethodDef("getOuterVar", List(), Fuzzion.AccessClassVar(Variable("this"), "outerVar"))
), classVars = List(Fuzzion.ClassVar("outerVar", Fuzzion.VarType("Double"), Fuzzion.Literal(10.0))),
nestedClasses = List(
  Fuzzion.ClassDef("InnerClass", List(Fuzzion.MethodDef("getInnerVar", List(), Fuzzion.AccessClassVar(Variable("this"), "innerVar"))),
  classVars = List(Fuzzion.ClassVar("innerVar", Fuzzion.VarType("Double"), Fuzzion.Literal(20.0)))))
)

val outerInstance = Fuzzion.eval(Fuzzion.CreateNew(outerClass)).asInstanceOf[Fuzzion.ClassInstance]
val innerClass = outerClass.nestedClasses.head
val innerInstance = Fuzzion.eval(Fuzzion.CreateNew(innerClass, Some(outerInstance))).asInstanceOf[Fuzzion.ClassInstance]

println(Fuzzion.eval(Fuzzion.InvokeMethod(innerInstance, "getInnerVar", List()))) // Expected: 20.0
```

#### **Dynamic Dispatch**

Dynamic dispatch allows Fuzzion to determine which method to invoke based on the runtime type of the instance. This allows for flexible method overriding and ensures that the correct method is called based on the actual object type.

**Example**:
```scala
val baseClass = Fuzzion.ClassDef("Base", List(Fuzzion.MethodDef("getValue", List(), Fuzzion.Literal(10.0))))
val derivedClass = Fuzzion.ClassDef("Derived", List(Fuzzion.MethodDef

("getValue", List(), Fuzzion.Literal(20.0))), Some(baseClass))

val baseInstance = Fuzzion.eval(Fuzzion.CreateNew(baseClass)).asInstanceOf[Fuzzion.ClassInstance]
val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]

println(Fuzzion.eval(Fuzzion.InvokeMethod(baseInstance, "getValue", List()))) // Expected: 10.0
println(Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "getValue", List()))) // Expected: 20.0
```

## Limitations

1. **Limited Operations**: Fuzzion only supports arithmetic (`ADD`, `MULTIPLY`), basic fuzzy logic operations (`AND`, `OR`, `NOT`, `XOR`) and (`alpha-cut`) operation.

2. **Precision and Floating-Point Arithmetic**: Floating-point precision can cause minor inaccuracies in calculations.

3. **Basic Scoping**: Fuzzion uses a simple scoping model without advanced features like visibility control (`private`, `protected`, `public`).

4. **No Error Handling**: Errors like undefined variables return default values instead of throwing exceptions.

5. **No Type Checking**: Fuzzion assumes all expressions are `Double`. No type checking is performed, so incorrect input types may result in undefined behavior.

6. **Multiple Inheritance Conflicts**: Fuzzion does not resolve conflicts in method or variable names when multiple inheritance is used.

7. **No Method Overloading**: Fuzzion does not support method overloading (same method name with different parameters).

8. **Limited Built-in Functions**: Fuzzion lacks built-in functions like math functions, string manipulation, or more advanced control structures (e.g., conditionals, loops).

9. **No State Persistence**: Class instances do not maintain state across multiple evaluations.
