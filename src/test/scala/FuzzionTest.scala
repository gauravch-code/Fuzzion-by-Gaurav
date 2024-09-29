import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Fuzzion._

class FuzzionTest extends AnyFlatSpec with Matchers {

  // Test for AND operation with fuzzy logic
  it should "evaluate AND gate correctly with fuzzy logic inputs" in {
    val and = Fuzzion.Literal(0.7).and(Fuzzion.Literal(0.5))
    assert(Fuzzion.eval(and) == 0.5) // AND of (0.7, 0.5) should return the smaller value 0.5
  }

  // Test for OR operation with fuzzy logic
  it should "evaluate OR gate correctly with fuzzy logic inputs" in {
    val or = Fuzzion.Literal(0.3).or(Fuzzion.Literal(0.8))
    assert(Fuzzion.eval(or) == 0.8) // OR of (0.3, 0.8) should return the larger value 0.8
  }

  // Test for NOT operation with fuzzy logic
  it should "evaluate NOT gate correctly with fuzzy logic inputs" in {
    val epsilon = 1e-6 // Small tolerance for floating-point comparison

    val not = Fuzzion.not(Fuzzion.Literal(0.4))
    assert(math.abs(Fuzzion.eval(not) - 0.6) < epsilon) // NOT of 0.4 should return approximately 0.6
  }

  // Test for boundary values 0.0 and 1.0 in all logic gates
  it should "evaluate fuzzy logic gates correctly with boundary values 0.0 and 1.0" in {
    // AND operation
    val and_exp1 = Fuzzion.Literal(0.0).and(Fuzzion.Literal(1.0))
    assert(Fuzzion.eval(and_exp1) == 0.0) // AND of (0.0, 1.0) should return 0.0

    // OR operation
    val or_exp1 = Fuzzion.Literal(0.0).or(Fuzzion.Literal(1.0))
    assert(Fuzzion.eval(or_exp1) == 1.0) // OR of (0.0, 1.0) should return 1.0

    // NOT operation
    val not_exp1 = Fuzzion.not(Fuzzion.Literal(0.0))
    assert(Fuzzion.eval(not_exp1) == 1.0) // NOT of 0.0 should return 1.0
  }

  // Test for TestGate without threshold
  it should "evaluate TestGate correctly" in {
    val testgate = Fuzzion.TestGate(Fuzzion.Literal(0.5))
    assert(Fuzzion.eval(testgate) == 1.0) // TestGate should return 1.0 for non-zero value (0.5)


    val testgate_negative = Fuzzion.TestGate(Fuzzion.Literal(-0.3))
    assert(Fuzzion.eval(testgate_negative) == 0.0) // Negative expression should return 0.0

    val testgate_zero = Fuzzion.TestGate(Fuzzion.Literal(0.0))
    assert(Fuzzion.eval(testgate_zero) == 0.0) // TestGate should return 0.0 for zero value
  }

  // Test for variable binding and evaluation in scope
  it should "correctly bind variable and evaluate in scope" in {
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(0.7))
    val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
    assert(Fuzzion.eval(var_exp, scope) == 0.7) // Variable "x" is 0.7, AND with 1.0 should return 0.7
  }

  // Test for variable assignment and retrieval
  it should "handle variable assignment and retrieval correctly" in {
    val scope = new Fuzzion.Scope()
    val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
    assert(Fuzzion.eval(assign_exp, scope) == 0.5)
    assert(Fuzzion.eval(Fuzzion.Variable("y"), scope) == 0.5) // Variable "y" should be 0.5 after assignment
  }
}
