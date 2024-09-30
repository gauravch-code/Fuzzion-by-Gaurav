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

    // Example: Using variables and scopes
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(0.7)) // Bind variable 'x' to 0.7
    val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0)) // Using dot notation
    println(s"Evaluation result (x AND 1.0), where x=0.7: ${Fuzzion.eval(var_exp, scope)}") // Expected: 0.7

    // Test with variable assignment and custom evaluation
    val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
    println(s"Evaluation of Assign (y = 0.5): ${Fuzzion.eval(assign_exp, scope)}") // Expected: 0.5

    // Example: Using TestGate without thresholds
    val testgate_exp = Fuzzion.TestGate(Fuzzion.Literal(0.5)) // Using dot notation
    println(s"TestGate result (0.5): ${Fuzzion.eval(testgate_exp, scope)}") // Expected: 1.0
  }
}

object Fuzzion {

  trait Expression

  case class Literal(value: Double) extends Expression
  case class Variable(name: String) extends Expression
  case class Assign(name: String, expr: Expression) extends Expression
  case class TestGate(expr: Expression) extends Expression // No threshold
  case class Not(expr: Expression) extends Expression
  case class Or(lhs: Expression, rhs: Expression) extends Expression
  case class And(lhs: Expression, rhs: Expression) extends Expression
  case class Xor(lhs: Expression, rhs: Expression) extends Expression

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
    case Literal(value) =>
      value
    case Variable(name) =>
      scope.resolve(name).map(_.value).getOrElse(0.0)
    case Assign(name, value) =>
      val evaluatedValue = eval(value, scope)
      scope.bind(name, Literal(evaluatedValue))
      evaluatedValue
    case TestGate(expr) =>
      val result = eval(expr, scope)
      if (result > 0.0) 1.0 else 0.0 // Returns 1.0 for non-zero, 0.0 for zero
    case Not(e) =>
      val result = eval(e, scope)
      1.0 - result // Fuzzy logic NOT operation
    case Or(lhs, rhs) =>
      val leftEval = eval(lhs, scope)
      val rightEval = eval(rhs, scope)
      math.max(leftEval, rightEval) // Fuzzy logic OR operation (returns max of the two)
    case And(lhs, rhs) =>
      val leftEval = eval(lhs, scope)
      val rightEval = eval(rhs, scope)
      math.min(leftEval, rightEval) // Fuzzy logic AND operation (returns min of the two)
    case Xor(lhs, rhs) => // Fuzzy logic XOR operation
      val leftEval = eval(lhs, scope)
      val rightEval = eval(rhs, scope)
      math.max(leftEval, rightEval) - math.min(leftEval, rightEval) // Fuzzy logic XOR operation (returns the difference between max and min)
  }

  // Helper function for NOT operation
  def not(expr: Expression): Expression = Not(expr)

  // Implicit class for DSL-like syntax for AND and OR
  implicit class ExpressionOps(lhs: Expression) {
    def and(rhs: Expression): Expression = And(lhs, rhs)
    def or(rhs: Expression): Expression = Or(lhs, rhs)
    def xor(rhs: Expression): Expression = Xor(lhs, rhs)
  }
}
