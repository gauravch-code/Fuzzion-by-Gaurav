object FuzzionApp {
  def main(args: Array[String]): Unit = {
    println("Running Fuzzion...")

    // Example: Evaluating an AND expression directly using DSL
    val and_exp = Fuzzion.Literal(0.6).and(Fuzzion.Literal(0.3)) // Using dot notation
    println(s"Evaluation result (0.6 AND 0.3): ${Fuzzion.eval(and_exp)}") // Expected: 0.3

    // Example: Evaluating an OR expression directly using DSL
    val or_exp = Fuzzion.Literal(0.6).or(Fuzzion.Literal(0.3)) // Using dot notation
    println(s"Evaluation result (0.6 OR 0.3): ${Fuzzion.eval(or_exp)}") // Expected: 0.6

    // Example: Evaluating a NOT expression directly using DSL
    val not_exp = Fuzzion.not(Fuzzion.Literal(0.6)) // NOT operation
    println(s"Evaluation result (NOT 0.6): ${Fuzzion.eval(not_exp)}") // Expected: 0.4

    // Example: Evaluating a XOR expression directly using DSL
    val xor_exp = Fuzzion.Literal(0.7).xor(Fuzzion.Literal(0.4))
    println(s"Evaluation result (0.7 XOR 0.4): ${Fuzzion.eval(xor_exp)}") // Expected: approximately 0.3

    // Example: Evaluating an ADD expression directly using DSL
    val add_exp = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
    println(s"Evaluation result (0.4 ADD 0.5): ${Fuzzion.eval(add_exp)}") // Expected: 0.9

    // Example: Evaluating a MULTIPLY expression directly using DSL
    val multiply_exp = Fuzzion.Literal(0.4).multiply(Fuzzion.Literal(0.6))
    println(s"Evaluation result (0.4 MULTIPLY 0.6): ${Fuzzion.eval(multiply_exp)}") // Expected: 0.24

    // Example: Using variables and scopes
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(0.7)) // Bind variable 'x' to 0.7
    val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0)) // Using dot notation
    println(s"Evaluation result (x AND 1.0), where x=0.7: ${Fuzzion.eval(var_exp, scope)}") // Expected: 0.7

    // Test with variable assignment and custom evaluation
    val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
    println(s"Evaluation of Assign (y = 0.5): ${Fuzzion.eval(assign_exp, scope)}") // Expected: 0.5

    // Example: Using TestGate
    val testgate_exp = Fuzzion.TestGate(Fuzzion.Literal(0.5)) // Using dot notation
    println(s"TestGate result (0.5): ${Fuzzion.eval(testgate_exp, scope)}") // Expected: 1.0
  }
}

object Fuzzion {

  trait Expression

  case class Literal(value: Double) extends Expression
  case class Variable(name: String) extends Expression
  case class Assign(name: String, expr: Expression) extends Expression
  case class TestGate(expr: Expression) extends Expression
  case class Not(expr: Expression) extends Expression
  case class Or(lhs: Expression, rhs: Expression) extends Expression
  case class And(lhs: Expression, rhs: Expression) extends Expression
  case class Xor(lhs: Expression, rhs: Expression) extends Expression
  case class Add(lhs: Expression, rhs: Expression) extends Expression
  case class Multiply(lhs: Expression, rhs: Expression) extends Expression

  class Scope {
    private val variables = scala.collection.mutable.Map[String, Literal]()

    def bind(name: String, literal: Literal): Unit = {
      variables(name) = literal
    }

    def resolve(name: String): Option[Literal] = {
      variables.get(name)
    }
  }

  def eval(expr: Expression, scope: Scope = new Scope()): Double = expr match {
    case Literal(value) => value
    case Variable(name) => scope.resolve(name).map(_.value).getOrElse(0.0)
    case Assign(name, value) =>
      val evaluatedValue = eval(value, scope)
      scope.bind(name, Literal(evaluatedValue))
      evaluatedValue
    case TestGate(expr) =>
      val result = eval(expr, scope)
      if (result > 0.0) 1.0 else 0.0
    case Not(e) => 1.0 - eval(e, scope) // Fuzzy logic NOT operation
    case Or(lhs, rhs) => math.max(eval(lhs, scope), eval(rhs, scope)) // Fuzzy logic OR operation
    case And(lhs, rhs) => math.min(eval(lhs, scope), eval(rhs, scope)) // Fuzzy logic AND operation
    case Xor(lhs, rhs) => math.abs(eval(lhs, scope) - eval(rhs, scope)) // Fuzzy logic XOR operation
    case Add(lhs, rhs) => eval(lhs, scope) + eval(rhs, scope) // Fuzzy addition
    case Multiply(lhs, rhs) => eval(lhs, scope) * eval(rhs, scope) // Fuzzy multiplication
  }

  // Helper function for NOT operation
  def not(expr: Expression): Expression = Not(expr)

  // Implicit class for DSL-like syntax for AND, OR, XOR, ADD, and MULTIPLY
  implicit class ExpressionOps(lhs: Expression) {
    def and(rhs: Expression): Expression = And(lhs, rhs)
    def or(rhs: Expression): Expression = Or(lhs, rhs)
    def xor(rhs: Expression): Expression = Xor(lhs, rhs)
    def add(rhs: Expression): Expression = Add(lhs, rhs) // New DSL for Add
    def multiply(rhs: Expression): Expression = Multiply(lhs, rhs) // New DSL for Multiply
  }
}
