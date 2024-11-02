import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Fuzzion._

class FuzzionTest extends AnyFlatSpec with Matchers {

  // Test for AND operation with fuzzy logic
  it should "evaluate AND gate correctly with fuzzy logic inputs" in {
    val and = Fuzzion.Literal(0.7).and(Fuzzion.Literal(0.5))
    assert(Fuzzion.eval(and).asInstanceOf[Double] == 0.5) // AND of (0.7, 0.5) should return the smaller value 0.5
  }

  // Test for OR operation with fuzzy logic
  it should "evaluate OR gate correctly with fuzzy logic inputs" in {
    val or = Fuzzion.Literal(0.3).or(Fuzzion.Literal(0.8))
    assert(Fuzzion.eval(or).asInstanceOf[Double] == 0.8) // OR of (0.3, 0.8) should return the larger value 0.8
  }

  // Test for NOT operation with fuzzy logic
  it should "evaluate NOT gate correctly with fuzzy logic inputs" in {
    val epsilon = 1e-6 // Small tolerance for floating-point comparison

    // Use the implicit not method from ExpressionOps
    val not = Fuzzion.Literal(0.4).not
    assert(math.abs(Fuzzion.eval(not).asInstanceOf[Double] - 0.6) < epsilon) // NOT of 0.4 should return approximately 0.6
  }

  // Test for XOR operation with fuzzy logic
  it should "evaluate XOR gate correctly with fuzzy logic inputs" in {
    val epsilon = 1e-6 // Small tolerance
    val xor = Fuzzion.Literal(0.7).xor(Fuzzion.Literal(0.3))
    assert(math.abs(Fuzzion.eval(xor).asInstanceOf[Double] - 0.4) < epsilon) // XOR of (0.7, 0.3) should return approximately 0.4
  }

  // Test for Add operation with fuzzy logic
  it should "evaluate Add operation correctly with fuzzy logic inputs" in {
    val add = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
    assert(Fuzzion.eval(add).asInstanceOf[Double] == 0.9) // 0.4 + 0.5 should equal 0.9
  }

  // Test for Multiply operation with fuzzy logic
  it should "evaluate Multiply operation correctly with fuzzy logic inputs" in {
    val multiply = Fuzzion.Literal(0.4).multiply(Fuzzion.Literal(0.6))
    assert(Fuzzion.eval(multiply).asInstanceOf[Double] == 0.24) // 0.4 * 0.6 should equal 0.24
  }

  // Test for boundary values 0.0 and 1.0 in all logic gates
  it should "evaluate fuzzy logic gates correctly with boundary values 0.0 and 1.0" in {
    // AND operation
    val and_exp1 = Fuzzion.Literal(0.0).and(Fuzzion.Literal(1.0))
    assert(Fuzzion.eval(and_exp1).asInstanceOf[Double] == 0.0) // AND of (0.0, 1.0) should return 0.0

    // OR operation
    val or_exp1 = Fuzzion.Literal(0.0).or(Fuzzion.Literal(1.0))
    assert(Fuzzion.eval(or_exp1).asInstanceOf[Double] == 1.0) // OR of (0.0, 1.0) should return 1.0

    // NOT operation
    val not_exp1 = Fuzzion.Literal(0.0).not
    assert(Fuzzion.eval(not_exp1).asInstanceOf[Double] == 1.0) // NOT of 0.0 should return 1.0
  }

  // Test for TestGate
  it should "evaluate TestGate correctly" in {
    val testgate = Fuzzion.TestGate(Fuzzion.Literal(0.5))
    assert(Fuzzion.eval(testgate).asInstanceOf[Double] == 1.0) // TestGate should return 1.0 for non-zero value (0.5)

    val testgate_negative = Fuzzion.TestGate(Fuzzion.Literal(-0.3))
    assert(Fuzzion.eval(testgate_negative).asInstanceOf[Double] == 0.0) // Negative expression should return 0.0

    val testgate_zero = Fuzzion.TestGate(Fuzzion.Literal(0.0))
    assert(Fuzzion.eval(testgate_zero).asInstanceOf[Double] == 0.0) // TestGate should return 0.0 for zero value
  }

  // Test for variable binding and evaluation in scope
  it should "correctly bind variable and evaluate in scope" in {
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(0.7))
    val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
    assert(Fuzzion.eval(var_exp, scope).asInstanceOf[Double] == 0.7) // Variable "x" is 0.7, AND with 1.0 should return 0.7
  }

  // Test for variable assignment and retrieval
  it should "handle variable assignment and retrieval correctly" in {
    val scope = new Fuzzion.Scope()
    val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
    assert(Fuzzion.eval(assign_exp, scope).asInstanceOf[Double] == 0.5)
    assert(Fuzzion.eval(Fuzzion.Variable("y"), scope).asInstanceOf[Double] == 0.5) // Variable "y" should be 0.5 after assignment
  }

  // Test for alpha-cut operation
  it should "perform alpha-cut correctly" in {
    val alphaCutExp = Fuzzion.Literal(0.7).alphaCut(0.5)
    assert(Fuzzion.eval(alphaCutExp).asInstanceOf[Double] == 0.7) // Should retain value 0.7 since it's >= 0.5

    val alphaCutExpFail = Fuzzion.Literal(0.3).alphaCut(0.5)
    assert(Fuzzion.eval(alphaCutExpFail).asInstanceOf[Double] == 0.0) // Should cut to 0.0 since 0.3 < 0.5
  }

  //--------------------------------------------------------HOMEWORK 2 TESTS-------------------------------------------------------------------------!

  // Test for class inheritance and method overriding
  it should "invoke the correct overridden method in derived class" in {
    // Define the base class with a method 'calculate' that returns 10.0
    val baseClass = Fuzzion.ClassDef(
      "Base",
      List(
        Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(10.0))
      )
    )

    // Define the derived class that overrides 'calculate' to return 20.0
    val derivedClass = Fuzzion.ClassDef(
      "Derived",
      List(
        Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(20.0))
      ),
      Some(baseClass)
    )

    // Create an instance of the derived class
    val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]

    // Invoke the 'calculate' method on the derived class instance
    val result = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "calculate", List()))

    // Assert that the result is 20.0, indicating the derived method was called
    result.asInstanceOf[Double] shouldBe 20.0
  }

  // Test for method inheritance when not overridden
  it should "use base class method when derived class does not override it" in {
    // Define the base class with a method 'calculate' that returns 10.0
    val baseClass = Fuzzion.ClassDef(
      "Base",
      List(
        Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(10.0))
      )
    )

    // Define the derived class that does not override 'calculate'
    val derivedClass = Fuzzion.ClassDef(
      "Derived",
      List(), // No methods
      Some(baseClass)
    )

    // Create an instance of the derived class
    val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]

    // Invoke the 'calculate' method on the derived class instance
    val result = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "calculate", List()))

    // Assert that the result is 10.0, indicating the base class method was called
    result.asInstanceOf[Double] shouldBe 10.0
  }

  // Test for dynamically dispatching correct methods
  it should "dynamically dispatch the correct method at runtime" in {
    val baseClass = Fuzzion.ClassDef("Base", List(
      Fuzzion.MethodDef("getValue", List(), Fuzzion.Literal(1.0))
    ))

    val derivedClass = Fuzzion.ClassDef("Derived", List(
      Fuzzion.MethodDef("getValue", List(), Fuzzion.Literal(2.0))
    ), Some(baseClass))

    val baseInstance = Fuzzion.eval(Fuzzion.CreateNew(baseClass)).asInstanceOf[Fuzzion.ClassInstance]
    val baseResult = Fuzzion.eval(Fuzzion.InvokeMethod(baseInstance, "getValue", List()))
    baseResult.asInstanceOf[Double] shouldBe 1.0 // Base class method result

    val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
    val derivedResult = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "getValue", List()))
    derivedResult.asInstanceOf[Double] shouldBe 2.0 // Derived class method result
  }

  // Test for class instantiation
  it should "instantiate classes and initialize class variables correctly" in {
    val testClass = Fuzzion.ClassDef(
      "TestClass",
      List(), // No methods
      classVars = List(Fuzzion.ClassVar("varA", Fuzzion.VarType("Double"), Fuzzion.Literal(5.0)))
    )

    val instance = Fuzzion.eval(Fuzzion.CreateNew(testClass)).asInstanceOf[Fuzzion.ClassInstance]
    val result = Fuzzion.eval(Fuzzion.AccessClassVar(instance, "varA"))
    result.asInstanceOf[Double] shouldBe 5.0 // Class variable 'varA' should be initialized to 5.0
  }

  // Test for Nested Classes and Scope Access
  it should "allow nested classes to access parent class variables and methods" in {
    // Outer class definition
    val outerClass = Fuzzion.ClassDef("OuterClass", List(
      Fuzzion.MethodDef("getOuterVar", List(), Fuzzion.AccessClassVar(Variable("this"), "outerVar"))
    ),
      classVars = List(Fuzzion.ClassVar("outerVar", Fuzzion.VarType("Double"), Fuzzion.Literal(10.0))),
      nestedClasses = List(
        // Nested inner class definition
        Fuzzion.ClassDef("InnerClass", List(
          Fuzzion.MethodDef("getOuterVarFromInner", List(), Fuzzion.AccessClassVar(Variable("this"), "outerVar")),
          Fuzzion.MethodDef("getInnerVar", List(), Fuzzion.AccessClassVar(Variable("this"), "innerVar"))
        ), classVars = List(Fuzzion.ClassVar("innerVar", Fuzzion.VarType("Double"), Fuzzion.Literal(20.0))))
      ))

    // Create instance of the outer class
    val outerInstance = Fuzzion.eval(Fuzzion.CreateNew(outerClass)).asInstanceOf[Fuzzion.ClassInstance]

    // Create instance of the inner class (nested within outer)
    val innerClass = outerClass.nestedClasses.head
    val innerInstance = Fuzzion.eval(Fuzzion.CreateNew(innerClass, Some(outerInstance))).asInstanceOf[Fuzzion.ClassInstance]

    // Access the outer variable from the outer class method
    val outerVarResult = Fuzzion.eval(Fuzzion.InvokeMethod(outerInstance, "getOuterVar", List()))
    outerVarResult.asInstanceOf[Double] shouldBe 10.0 // Expected: 10.0

    // Access the outer variable from the inner class method
    val innerOuterVarResult = Fuzzion.eval(Fuzzion.InvokeMethod(innerInstance, "getOuterVarFromInner", List()))
    innerOuterVarResult.asInstanceOf[Double] shouldBe 10.0 // Expected: 10.0

    // Access the inner variable from the inner class method
    val innerVarResult = Fuzzion.eval(Fuzzion.InvokeMethod(innerInstance, "getInnerVar", List()))
    innerVarResult.asInstanceOf[Double] shouldBe 20.0 // Expected: 20.0
  }

  // Test for Multiple Inheritance
  it should "correctly handle multiple levels of inheritance" in {
    val baseClass = Fuzzion.ClassDef(
      "Base",
      List(Fuzzion.MethodDef("greet", List(), Fuzzion.Literal(1.0)))
    )

    val intermediateClass = Fuzzion.ClassDef(
      "Intermediate",
      List(),
      Some(baseClass)
    )

    val derivedClass = Fuzzion.ClassDef(
      "Derived",
      List(),
      Some(intermediateClass)
    )

    val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
    val greetResult = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "greet", List()))

    greetResult.asInstanceOf[Double] shouldBe 1.0 // Expected to invoke 'greet' from the base class
  }

}
