# Gaurav Chintakunta

# Fuzzion: A Fuzzy Logic Expression Language


## Introduction

**Fuzzion** is a domain-specific language (DSL) for evaluating fuzzy logic expressions with support for object-oriented features and advanced partial evaluation techniques. It allows users to perform fuzzy logic operations, define and inherit classes, invoke methods dynamically, and work with variable scoping and assignment. Fuzzion is designed to be flexible and expressive, supporting a range of arithmetic and logic operations, as well as dynamic object-oriented programming features.

This document serves as both a user guide and a report explaining the implementation and semantics of the Fuzzion language, including new features introduced in Homework 3, such as partial evaluation of expressions, conditional constructs (`IFTRUE` and `ELSERUn`), and advanced reduction rules.



## Features

### Basic Fuzzy Logic Operations

- **AND**: Returns the smaller value of the two inputs.
- **OR**: Returns the larger value of the two inputs.
- **NOT**: Inverts the input value (`NOT x = 1.0 - x`).
- **XOR**: Returns the absolute difference between the two input values.

### Arithmetic Operations

- **ADD**: Adds two values together.
- **MULTIPLY**: Returns the product of two inputs.

### Alpha-Cut Operation

- **Alpha-Cut**: Filters fuzzy values based on a threshold alpha. If the value is greater than or equal to alpha, the value is retained; otherwise, the result is `0.0`.

### Variables and Scoping

- **Variable Binding**: Variables can be bound to values or expressions.
- **Scope Management**: Supports variable scoping with nested scopes.

### Conditional Constructs

- **IFTRUE**: Evaluates a condition and executes the `then` branch if true, otherwise the `else` branch.
- **ELSERUn and THENEXECUTE**: Constructs for defining conditional execution.

### Object-Oriented Programming

- **Classes and Instances**: Define classes with variables and methods, create instances.
- **Inheritance**: Classes can inherit from parent classes.
- **Method Overriding**: Derived classes can override methods from parent classes.
- **Nested Classes**: Support for defining classes within classes.
- **Dynamic Dispatch**: Methods are dynamically dispatched based on the instance's class at runtime.

### Partial Evaluation and Advanced Reduction Rules

- **Partial Evaluation**: Expressions are partially evaluated when not all variables are defined.
- **Reduction Rules**: Implemented reduction rules for associativity and constant folding during partial evaluation.
- **Environment Table Updates**: Variables in the environment can be bound to expressions resulting from partial evaluation.

## Installation and Setup

### Prerequisites

- Scala 3.x
- SBT (Simple Build Tool)

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/Fuzzion.git
cd Fuzzion
```

### Step 2: Compile the Project

```bash
sbt compile
```

## Running the Program

Run the main Fuzzion application:

```bash
sbt run
```

This will execute the `FuzzionApp` object, which contains examples demonstrating the features of the language.

## Running Tests

Fuzzion includes a comprehensive test suite using ScalaTest. Run the tests with:

```bash
sbt test
```

This will execute the tests defined in `FuzzionTest.scala`, verifying the correctness of all language features.

## Creating and Evaluating Expressions

### Syntax and Semantics

#### Literals

Literals represent fixed numeric values, typically within the range `[0.0, 1.0]` for fuzzy logic values.

**Example:**

```scala
val literal = Fuzzion.Literal(0.7)
println(Fuzzion.eval(literal)) // Output: 0.7
```

#### Fuzzy Logic Operations

- **AND**

  ```scala
  val andExp = Fuzzion.Literal(0.6).and(Fuzzion.Literal(0.3))
  println(Fuzzion.eval(andExp)) // Expected: 0.3
  ```

- **OR**

  ```scala
  val orExp = Fuzzion.Literal(0.6).or(Fuzzion.Literal(0.3))
  println(Fuzzion.eval(orExp)) // Expected: 0.6
  ```

- **NOT**

  ```scala
  val notExp = Fuzzion.Literal(0.6).not
  println(Fuzzion.eval(notExp)) // Expected: 0.4
  ```

- **XOR**

  ```scala
  val xorExp = Fuzzion.Literal(0.7).xor(Fuzzion.Literal(0.4))
  println(Fuzzion.eval(xorExp)) // Expected: 0.3
  ```

#### Arithmetic Operations

- **ADD**

  ```scala
  val addExp = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
  println(Fuzzion.eval(addExp)) // Expected: 0.9
  ```

- **MULTIPLY**

  ```scala
  val multiplyExp = Fuzzion.Literal(0.4).multiply(Fuzzion.Literal(0.6))
  println(Fuzzion.eval(multiplyExp)) // Expected: 0.24
  ```

#### Alpha-Cut Operation

```scala
val alphaCutExp = Fuzzion.Literal(0.7).alphaCut(0.5)
println(Fuzzion.eval(alphaCutExp)) // Expected: 0.7

val alphaCutFail = Fuzzion.Literal(0.3).alphaCut(0.5)
println(Fuzzion.eval(alphaCutFail)) // Expected: 0.0
```

#### Variables and Scoping

```scala
val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7))
val varExp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
println(Fuzzion.eval(varExp, scope)) // Expected: 0.7
```

#### Conditional Constructs

- **IFTRUE**

  ```scala
  val conditionalExp = Fuzzion.IFTRUE(
    Fuzzion.GreaterEqual(Fuzzion.Variable("x"), Fuzzion.Literal(5.0)),
    Fuzzion.Assign("y", Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0))),
    Fuzzion.Assign("y", Fuzzion.Multiply(Fuzzion.Variable("x"), Fuzzion.Literal(2.0)))
  )

  val scope = new Fuzzion.Scope()
  scope.bind("x", Fuzzion.Literal(6.0))
  val result = Fuzzion.eval(conditionalExp, scope)
  println(Fuzzion.eval(Fuzzion.Variable("y"), scope)) // Expected: 9.0
  ```

- **Partial Evaluation with Undefined Variables**

  ```scala
  val scopePartial = new Fuzzion.Scope()
  val resultPartial = Fuzzion.eval(conditionalExp, scopePartial)
  println(resultPartial) // Expected: PartialExpression(IFTRUE(...))
  ```

#### Object-Oriented Features

- **Class Definition and Inheritance**

  ```scala
  val baseClass = Fuzzion.ClassDef(
    "Base",
    List(Fuzzion.MethodDef("greet", List(), Fuzzion.Literal(1.0)))
  )

  val derivedClass = Fuzzion.ClassDef(
    "Derived",
    List(Fuzzion.MethodDef("greet", List(), Fuzzion.Literal(2.0))),
    Some(baseClass)
  )
  ```

- **Creating Instances and Invoking Methods**

  ```scala
  val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
  val result = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "greet", List()))
  println(result) // Expected: 2.0
  ```

- **Accessing Class Variables**

  ```scala
  val derivedVar = Fuzzion.eval(Fuzzion.AccessClassVar(derivedInstance, "derivedVar"))
  println(derivedVar) // Expected: Value of 'derivedVar'
  ```

#### Partial Evaluation

- **Partial Evaluation of Expressions**

  When not all variables are defined, expressions are partially evaluated, resulting in a `PartialExpression`.

  **Example:**

  ```scala
  val expr = Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0))
  val result = Fuzzion.eval(expr)
  println(result) // Expected: PartialExpression(Add(Variable("x"), Literal(3.0)))
  ```

- **Simplification and Reduction Rules**

  Fuzzion implements reduction rules to simplify expressions during partial evaluation, combining constants and flattening nested operations.

  **Example:**

  ```scala
  val expr = Fuzzion.Multiply(Fuzzion.Literal(3), Fuzzion.Multiply(Fuzzion.Literal(5), Fuzzion.Variable("var")))
  val result = Fuzzion.eval(expr)
  println(result) // Expected: PartialExpression(Multiply(Literal(15.0), Variable("var")))
  ```

## Implementation Details

### Evaluator Function

The core of Fuzzion is the `eval` function, which evaluates expressions recursively. It handles literals, variables, assignments, arithmetic operations, fuzzy logic operations, conditional constructs, method invocations, and class instance creations.

- **Handling Undefined Variables**: When a variable is undefined in the current scope, it is treated as a `Variable` expression, allowing for partial evaluation.
- **Partial Expressions**: When an expression cannot be fully evaluated, `eval` returns a `PartialExpression` containing the simplified expression.

### Simplification Functions and Reduction Rules

Fuzzion uses simplification functions (`simplifyAdd`, `simplifyMultiply`) to implement reduction rules during partial evaluation.

- **Combining Constants**: Constants are combined using arithmetic operations.
- **Flattening Nested Operations**: Nested `Add` or `Multiply` expressions are flattened to simplify the expression tree.
- **Eliminating Neutral Elements**: Neutral elements (e.g., adding 0 or multiplying by 1) are eliminated.

**Example of Simplification:**

```scala
def simplifyAdd(expr: Expression, scope: Scope): Expression = {
  val terms = collectTerms(expr).map(e => evalToExpression(e, scope))
  val (constants, others) = terms.partition {
    case Literal(_) => true
    case _ => false
  }
  val constantSum = constants.foldLeft(0.0) {
    case (sum, Literal(v)) => sum + v
  }
  val newTerms = (if (constantSum == 0.0) Nil else List(Literal(constantSum))) ++ others
  newTerms.reduceOption(Add(_, _)).getOrElse(Literal(0.0))
}
```

### Partial Evaluation of Conditional Expressions

When evaluating `IFTRUE` constructs, if the condition cannot be fully evaluated, both the `then` and `else` branches are partially evaluated.

**Example:**

```scala
case IFTRUE(condition, thenBranch, elseBranch) =>
  eval(condition, scope) match {
    case v: Double =>
      if (v >= 0.5) eval(thenBranch, scope) else eval(elseBranch, scope)
    case condExpr: Expression =>
      val partiallyEvaluatedThen = eval(thenBranch, scope)
      val partiallyEvaluatedElse = eval(elseBranch, scope)
      PartialExpression(IFTRUE(condExpr, partiallyEvaluatedThen, partiallyEvaluatedElse))
  }
```

### Partial Evaluation of Method Invocations

- **Parameter Evaluation**: Parameters are evaluated, and if they cannot be fully evaluated, they remain as expressions.
- **Method Body Evaluation**: The method body is evaluated in the context of the method's scope, which includes the bound parameters.
- **Result**: If the method cannot be fully evaluated, a `PartialExpression` is returned.

### Dynamic Dispatch and Partial Evaluation

Methods are dynamically dispatched based on the instance's class at runtime. During partial evaluation, the correct method is identified, and its body is partially evaluated.

**Example:**

```scala
def findMethod(cls: ClassDef, methodName: String): Option[MethodDef] = {
  cls.methods.find(_.name == methodName).orElse(cls.parent.flatMap(findMethod(_, methodName)))
}
```


**Example Test Case:**

```scala
it should "recursively simplify nested expressions during partial evaluation" in {
  val expr = Multiply(Literal(3), Multiply(Literal(5), Variable("var")))
  val result = eval(expr)
  result shouldEqual PartialExpression(Multiply(Literal(15.0), Variable("var")))
}
```

## Limitations

1. **Limited Operations**: Only basic arithmetic and fuzzy logic operations are supported.
2. **Floating-Point Precision**: Calculations may have minor inaccuracies due to floating-point precision.
3. **Basic Scoping**: Does not support advanced scoping features like visibility modifiers.
4. **Error Handling**: Limited error handling; undefined variables result in partial expressions.
5. **No Type Checking**: Assumes all expressions evaluate to `Double`; incorrect types may cause runtime errors.
6. **Multiple Inheritance Conflicts**: Does not handle conflicts arising from multiple inheritance.
7. **No Method Overloading**: Does not support methods with the same name but different parameters.
8. **Limited Control Structures**: Only supports `IFTRUE` conditional construct; no loops or other control structures.
9. **State Persistence**: Class instances do not maintain state across multiple evaluations unless explicitly managed.

## Conclusion

Fuzzion is a powerful DSL for evaluating fuzzy logic expressions, enhanced with object-oriented features and advanced partial evaluation capabilities. It allows for flexible expression construction, supports dynamic method invocation with inheritance, and handles partial evaluations gracefully, maintaining expressions for undefined variables.

This document provided an overview of Fuzzion's features, detailed explanations of its syntax and semantics, implementation details, and test cases demonstrating its capabilities. By integrating partial evaluation and reduction rules, Fuzzion can efficiently simplify expressions and handle undefined variables, making it suitable for scenarios where full information may not be available at evaluation time.
