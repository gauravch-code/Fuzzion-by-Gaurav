# Gaurav Chintakunta

# Fuzzion: A Fuzzy Logic Expression Language

**Fuzzion** is a simple domain-specific language (DSL) for evaluating fuzzy logic expressions. It allows users to perform operations such as `AND`, `OR`, `NOT`, `XOR`, `ADD`, and `MULTIPLY` using fuzzy logic values. Additionally, it supports variable assignments and scoped evaluations.

## Introduction

Fuzzion allows users to create fuzzy logic expressions and evaluate them using a simple DSL built in Scala. Fuzzion supports standard fuzzy logic operations like `AND`, `OR`, and `NOT`, along with additional operations such as `XOR`, `ADD`, and `MULTIPLY`. The language also allows variable assignments and scoped evaluations for more complex expressions.

## Features
- Basic fuzzy logic operations: `AND`, `OR`, `NOT`, `XOR`
- Arithmetic operations: `ADD`, `MULTIPLY`
- Variable binding and assignment
- Scoped evaluations
- Test suite using ScalaTest

## Prerequisites
- Scala 3.x
- SBT (Simple Build Tool)

## Installation and Setup

To assemble and run the Fuzzion project, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/Fuzzion.git
   cd Fuzzion
   ```

2. **Install dependencies**:
   Ensure you have Scala 3.x and SBT installed on your machine.

   Install Scala and SBT on Windows.

3. **Compile the project**:
   Run the following SBT command to compile the project:
   ```bash
   sbt compile
   ```

## Running the Program

You can run the main Fuzzion application by executing the following SBT command:
```bash
sbt run
```

This will run the `FuzzionApp` object, which contains several sample expressions using the DSL. You will see the evaluation results of various fuzzy logic operations.

## Running Tests

Fuzzion includes a comprehensive test suite using ScalaTest. To run the tests:

1. Execute the following SBT command:
   ```bash
   sbt test
   ```

   This will run the tests defined in `FuzzionTest.scala`, verifying the correctness of fuzzy logic operations (`AND`, `OR`, `NOT`, `XOR`, `ADD`, `MULTIPLY`), variable assignment, and scoped evaluations.

## Language Semantics

### Basic Operations
Fuzzion is designed to support fuzzy logic, where truth values range between 0 and 1. The operations behave as follows:

- `AND`: Returns the smaller value of the two inputs.
- `OR`: Returns the larger value of the two inputs.
- `NOT`: Inverts the input value (i.e., `NOT x = 1.0 - x`).
- `XOR`: Returns the absolute difference between the two input values.

### Arithmetic Operations
- `ADD`: Returns the sum of the two inputs.
- `MULTIPLY`: Returns the product of the two inputs.

### Variable Binding and Scoping
Fuzzion supports variable binding and scoping through a `Scope` object. Variables can be assigned values within a scope and referenced within expressions. Scopes allow for isolated evaluation of expressions where variables can be re-bound or shadowed.

Example:
```scala
val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7)) // Bind variable 'x' to 0.7
val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
Fuzzion.eval(var_exp, scope) // Expected: 0.7
```

### TestGate
The `TestGate` operation evaluates whether an input is greater than zero. It returns `1.0` if the input is greater than zero, otherwise it returns `0.0`.

## Creating and Evaluating Expressions

To create and evaluate expressions in Fuzzion, you use the DSL-like syntax provided by the language. Here's a quick example:

```scala
val and_exp = Fuzzion.Literal(0.6).and(Fuzzion.Literal(0.3))
println(Fuzzion.eval(and_exp)) // Expected: 0.3

val add_exp = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
println(Fuzzion.eval(add_exp)) // Expected: 0.9

val scope = new Fuzzion.Scope()
scope.bind("x", Fuzzion.Literal(0.7))
val var_exp = Fuzzion.Variable("x").multiply(Fuzzion.Literal(0.6))
println(Fuzzion.eval(var_exp, scope)) // Expected: 0.42
```

### Syntax Overview:
- **Literals**: Represent fuzzy values. Example: `Fuzzion.Literal(0.7)`
- **Variables**: Can be defined and bound within a `Scope`. Example: `Fuzzion.Variable("x")`
- **Operations**: Operations like `and`, `or`, `xor`, `add`, and `multiply` can be performed using dot notation.
- **Evaluation**: Expressions are evaluated using `Fuzzion.eval(expression, scope)`.

## Limitations

1. **Limited Operations**: Currently, Fuzzion only supports basic fuzzy logic operations (`AND`, `OR`, `NOT`, `XOR`) and arithmetic (`ADD`, `MULTIPLY`). More complex functions like division, thresholds, or conditional logic are not implemented.
   
2. **Precision and Floating-Point Arithmetic**: Due to floating-point precision, results may not be exact for certain operations. Small differences (e.g., `1e-6`) are accounted for in tests, but real-world applications may need additional precision handling.

3. **No Support for Composite Gates**: Fuzzion does not currently support composite gates or multi-step evaluation pipelines. Operations must be executed one at a time.

4. **Fixed Scoping Model**: Scopes are relatively simple. More advanced scoping features like nested or chained scopes, or visibility control, are not supported in this implementation.

## Future Enhancements

1. **Composite Operations**: Adding support for defining custom gates and operations that combine multiple steps would greatly enhance flexibility.

2. **Additional Arithmetic Operations**: Extending Fuzzion to include division, modulus, and other arithmetic functions.

3. **Improved Precision Handling**: More robust handling of floating-point precision errors, especially in cases of small values.

4. **Configurable Logic Thresholds**: Allowing users to define custom thresholds for fuzzy logic evaluation.


