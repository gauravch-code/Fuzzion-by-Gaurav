import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Fuzzion._

class FuzzionTest extends AnyFlatSpec with Matchers {

  // Test for AND operation with fuzzy logic
  it should "evaluate AND gate correctly with fuzzy logic inputs" in {
    val and = Fuzzion.Literal(0.7).and(Fuzzion.Literal(0.5))
    Fuzzion.eval(and) shouldEqual 0.5 // AND of (0.7, 0.5) should return the smaller value 0.5
  }

  // Test for OR operation with fuzzy logic
  it should "evaluate OR gate correctly with fuzzy logic inputs" in {
    val or = Fuzzion.Literal(0.3).or(Fuzzion.Literal(0.8))
    Fuzzion.eval(or) shouldEqual 0.8 // OR of (0.3, 0.8) should return the larger value 0.8
  }

  // Test for NOT operation with fuzzy logic
  it should "evaluate NOT gate correctly with fuzzy logic inputs" in {
    val epsilon = 1e-6 // Small tolerance for floating-point comparison

    // Use the implicit not method from ExpressionOps
    val not = Fuzzion.Literal(0.4).not
    math.abs(Fuzzion.eval(not).asInstanceOf[Double] - 0.6) should be < epsilon // NOT of 0.4 should return approximately 0.6
  }

  // Test for XOR operation with fuzzy logic
  it should "evaluate XOR gate correctly with fuzzy logic inputs" in {
    val epsilon = 1e-6 // Small tolerance
    val xor = Fuzzion.Literal(0.7).xor(Fuzzion.Literal(0.3))
    math.abs(Fuzzion.eval(xor).asInstanceOf[Double] - 0.4) should be < epsilon // XOR of (0.7, 0.3) should return approximately 0.4
  }

  // Test for Add operation with fuzzy logic
  it should "evaluate Add operation correctly with fuzzy logic inputs" in {
    val add = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
    Fuzzion.eval(add).asInstanceOf[Double] shouldEqual 0.9 // 0.4 + 0.5 should equal 0.9
  }

  // Test for Multiply operation with fuzzy logic
  it should "evaluate Multiply operation correctly with fuzzy logic inputs" in {
    val multiply = Fuzzion.Literal(0.4).multiply(Fuzzion.Literal(0.6))
    Fuzzion.eval(multiply).asInstanceOf[Double] shouldEqual 0.24 // 0.4 * 0.6 should equal 0.24
  }

  // Test for boundary values 0.0 and 1.0 in all logic gates
  it should "evaluate fuzzy logic gates correctly with boundary values 0.0 and 1.0" in {
    // AND operation
    val and_exp1 = Fuzzion.Literal(0.0).and(Fuzzion.Literal(1.0))
    Fuzzion.eval(and_exp1).asInstanceOf[Double] shouldEqual 0.0 // AND of (0.0, 1.0) should return 0.0

    // OR operation
    val or_exp1 = Fuzzion.Literal(0.0).or(Fuzzion.Literal(1.0))
    Fuzzion.eval(or_exp1).asInstanceOf[Double] shouldEqual 1.0 // OR of (0.0, 1.0) should return 1.0

    // NOT operation
    val not_exp1 = Fuzzion.Literal(0.0).not
    Fuzzion.eval(not_exp1).asInstanceOf[Double] shouldEqual 1.0 // NOT of 0.0 should return 1.0
  }

  // Test for TestGate
  it should "evaluate TestGate correctly" in {
    val testgate = Fuzzion.TestGate(Fuzzion.Literal(0.5))
    Fuzzion.eval(testgate).asInstanceOf[Double] shouldEqual 1.0 // TestGate should return 1.0 for non-zero value (0.5)

    val testgate_negative = Fuzzion.TestGate(Fuzzion.Literal(-0.3))
    Fuzzion.eval(testgate_negative).asInstanceOf[Double] shouldEqual 0.0 // Negative expression should return 0.0

    val testgate_zero = Fuzzion.TestGate(Fuzzion.Literal(0.0))
    Fuzzion.eval(testgate_zero).asInstanceOf[Double] shouldEqual 0.0 // TestGate should return 0.0 for zero value
  }

  // Test for variable binding and evaluation in scope
  it should "correctly bind variable and evaluate in scope" in {
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(0.7))
    val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
    Fuzzion.eval(var_exp, scope).asInstanceOf[Double] shouldEqual 0.7 // Variable "x" is 0.7, AND with 1.0 should return 0.7
  }

  // Test for variable assignment and retrieval
  it should "handle variable assignment and retrieval correctly" in {
    val scope = new Fuzzion.Scope()
    val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
    Fuzzion.eval(assign_exp, scope).asInstanceOf[Double] shouldEqual 0.5
    Fuzzion.eval(Fuzzion.Variable("y"), scope).asInstanceOf[Double] shouldBe 0.5 // Variable "y" should be 0.5 after assignment
  }

  // Test for alpha-cut operation
  it should "perform alpha-cut correctly" in {
    val alphaCutExp = Fuzzion.Literal(0.7).alphaCut(0.5)
    Fuzzion.eval(alphaCutExp).asInstanceOf[Double] shouldEqual 0.7 // Should retain value 0.7 since it's >= 0.5

    val alphaCutExpFail = Fuzzion.Literal(0.3).alphaCut(0.5)
    Fuzzion.eval(alphaCutExpFail).asInstanceOf[Double] shouldEqual 0.0 // Should cut to 0.0 since 0.3 < 0.5
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
      Fuzzion.MethodDef("getOuterVar", List(), Fuzzion.AccessClassVar(Fuzzion.Variable("this"), "outerVar"))
    ),
      classVars = List(Fuzzion.ClassVar("outerVar", Fuzzion.VarType("Double"), Fuzzion.Literal(10.0))),
      nestedClasses = List(
        // Nested inner class definition
        Fuzzion.ClassDef("InnerClass", List(
          Fuzzion.MethodDef("getOuterVarFromInner", List(), Fuzzion.AccessClassVar(Fuzzion.Variable("this"), "outerVar")),
          Fuzzion.MethodDef("getInnerVar", List(), Fuzzion.AccessClassVar(Fuzzion.Variable("this"), "innerVar"))
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
      List(Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(15.0))),
      Some(baseClass)
    )

    val derivedClass = Fuzzion.ClassDef(
      "Derived",
      List(Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(25.0))),
      Some(intermediateClass)
    )

    val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
    val calculateResult = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "calculate", List()))
    calculateResult.asInstanceOf[Double] shouldBe 25.0 // Expected: 25.0

    // Access inherited 'greet' method
    val greetResult = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "greet", List()))
    greetResult.asInstanceOf[Double] shouldBe 1.0 // Expected: 1.0 from Base class
  }

  //--------------------------------------------------------HOMEWORK 3 TESTS-------------------------------------------------------------------------!

  // Test for IFTRUE conditional construct with full evaluation (condition true)
  it should "evaluate IFTRUE correctly when condition is fully defined (true)" in {
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(6.0)) // x >= 5, condition is true

    val conditionalExp = Fuzzion.IFTRUE(
      Fuzzion.GreaterEqual(Fuzzion.Variable("x"), Fuzzion.Literal(5.0)),
      Fuzzion.Assign("y", Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0))),
      Fuzzion.Assign("y", Fuzzion.Multiply(Fuzzion.Variable("x"), Fuzzion.Literal(2.0)))
    )

    val result = Fuzzion.eval(conditionalExp, scope)
    result shouldEqual 9.0 // y = x + 3 = 6 + 3 = 9
    Fuzzion.eval(Fuzzion.Variable("y"), scope).asInstanceOf[Double] shouldBe 9.0
  }

  // Test for IFTRUE conditional construct with full evaluation (condition false)
  it should "evaluate IFTRUE correctly when condition is fully defined (false)" in {
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(3.0)) // x < 5, condition is false

    val conditionalExp = Fuzzion.IFTRUE(
      Fuzzion.GreaterEqual(Fuzzion.Variable("x"), Fuzzion.Literal(5.0)),
      Fuzzion.Assign("y", Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0))),
      Fuzzion.Assign("y", Fuzzion.Multiply(Fuzzion.Variable("x"), Fuzzion.Literal(2.0)))
    )

    val result = Fuzzion.eval(conditionalExp, scope)
    result shouldEqual 6.0 // y = x * 2 = 3 * 2 = 6
    Fuzzion.eval(Fuzzion.Variable("y"), scope).asInstanceOf[Double] shouldBe 6.0
  }

  // Test for IFTRUE with partial evaluation (condition undefined)
  it should "partially evaluate IFTRUE when condition is partially defined" in {
    val scope = new Fuzzion.Scope()
    // 'x' is not defined

    val conditionalExp = Fuzzion.IFTRUE(
      Fuzzion.GreaterEqual(Fuzzion.Variable("x"), Fuzzion.Literal(5.0)),
      Fuzzion.Assign("y", Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0))),
      Fuzzion.Assign("y", Fuzzion.Multiply(Fuzzion.Variable("x"), Fuzzion.Literal(2.0)))
    )

    val result = Fuzzion.eval(conditionalExp, scope)
    result shouldBe a[PartialExpression]

    // The 'y' variable should be bound to a PartialExpression representing the IFTRUE construct
    val yValue = Fuzzion.eval(Fuzzion.Variable("y"), scope)
    yValue shouldBe a[PartialExpression]
  }

  it should "handle undefined variables within IFTRUE branches during partial evaluation" in {
    // Define the condition where x is partially defined
    val conditionalExp = Fuzzion.IFTRUE(
      Fuzzion.GreaterEqual(Fuzzion.Variable("x"), Fuzzion.Literal(5.0)),
      // Then branch: Assign y = 4 * b
      Fuzzion.Assign("y", Fuzzion.Multiply(Fuzzion.Literal(4.0), Fuzzion.Variable("b"))),
      // Else branch: Assign y = 4 * b
      Fuzzion.Assign("y", Fuzzion.Multiply(Fuzzion.Literal(4.0), Fuzzion.Variable("b")))
    )

    // Evaluate with x undefined
    val scopePartial = new Fuzzion.Scope()
    val resultPartial = Fuzzion.eval(conditionalExp, scopePartial)

    // Assert that the result is a PartialExpression
    assert(resultPartial.isInstanceOf[Fuzzion.PartialExpression])

    // Access 'y' after IFTRUE, which should also be a PartialExpression
    val yValue = Fuzzion.eval(Fuzzion.Variable("y"), scopePartial)
    assert(yValue.isInstanceOf[Fuzzion.PartialExpression])

    // Optionally, check the structure of the PartialExpression
    yValue match {
      case Fuzzion.PartialExpression(Fuzzion.Multiply(Fuzzion.Literal(4.0), Fuzzion.Variable("b"))) =>
        succeed
      case _ =>
        fail("Unexpected PartialExpression structure")
    }

  }


  // Test for method invocation within IFTRUE
  it should "handle method invocations within IFTRUE with partial evaluation" in {
    // Define a class with a method that adds a variable
    val adderClass = ClassDef(
      "Adder",
      List(MethodDef("add", List(Parameter("value", "Double")), Add(Variable("value"), Literal(5.0))))
    )

    val adderInstance = Fuzzion.eval(CreateNew(adderClass)).asInstanceOf[ClassInstance]

    val scope = new Fuzzion.Scope()

    val conditionalExp = Fuzzion.IFTRUE(
      Fuzzion.GreaterEqual(Fuzzion.Variable("x"), Fuzzion.Literal(10.0)),
      InvokeMethod(adderInstance, "add", List(Fuzzion.Variable("x"))),
      Fuzzion.Literal(0.0)
    )

    // Case where 'x' is undefined
    val partialResult = Fuzzion.eval(conditionalExp, scope)
    partialResult shouldBe a[PartialExpression]

    // Now define 'x' and evaluate again
    scope.bind("x", Fuzzion.Literal(12.0))
    val fullResult = Fuzzion.eval(conditionalExp, scope)
    fullResult shouldEqual 17.0 // 12 + 5 = 17
  }



  // Test for method body evaluation with full and partial inputs
  it should "evaluate method bodies with full and partial inputs" in {
    val method = MethodDef("sum", List(Parameter("x", "Double"), Parameter("y", "Double")), Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Variable("y")))
    val classDef = ClassDef("Adder", List(method))

    val instance = Fuzzion.eval(Fuzzion.CreateNew(classDef)).asInstanceOf[Fuzzion.ClassInstance]

    // Partial evaluation
    val partialResult = Fuzzion.eval(Fuzzion.InvokeMethod(instance, "sum", List(Fuzzion.Literal(3.0), Fuzzion.Variable("z"))))
    partialResult shouldBe Fuzzion.PartialExpression(Fuzzion.Add(Fuzzion.Literal(3.0), Fuzzion.Variable("z")))

    // Full evaluation
    val scope = new Fuzzion.Scope()
    scope.bind("z", Fuzzion.Literal(7.0))
    val fullResult = Fuzzion.eval(Fuzzion.InvokeMethod(instance, "sum", List(Fuzzion.Literal(3.0), Fuzzion.Variable("z"))), scope)
    fullResult shouldEqual 10.0 // 3 + 7
  }


  // Test for environment table updates during partial evaluation
  it should "map variables to expressions during partial evaluation" in {
    val scope = new Fuzzion.Scope()

    val assignExp = Fuzzion.Assign("somevar", Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0)))
    val result = Fuzzion.eval(assignExp, scope)

    result shouldEqual Fuzzion.PartialExpression(Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0)))
    scope.resolve("somevar") shouldEqual Some(Fuzzion.PartialExpression(Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0))))
  }


  // Test for classes and inheritance with partially evaluated method bodies
  it should "handle partially evaluated method bodies in class hierarchy" in {
    val baseClass = ClassDef(
      "Base",
      List(MethodDef("greet", List(Parameter("name", "String")), Fuzzion.Add(Fuzzion.Variable("name"), Fuzzion.Literal(1.0))))
    )

    val derivedClass = ClassDef(
      "Derived",
      List(MethodDef("greet", List(Parameter("name", "String")), Fuzzion.Add(Fuzzion.Variable("name"), Fuzzion.Literal(2.0)))),
      Some(baseClass)
    )

    val instance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
    val result = Fuzzion.eval(Fuzzion.InvokeMethod(instance, "greet", List(Fuzzion.Variable("person"))))

    result shouldBe a[PartialExpression] // Partially evaluated with Variable("person")
  }

  it should "correctly evaluate THENEXECUTE and ELSERUn constructs" in {
    val scope = new Fuzzion.Scope()

    // Define THENEXECUTE and ELSERUn expressions
    val thenExecuteExp = Fuzzion.THENEXECUTE(Fuzzion.Add(Fuzzion.Literal(2.0), Fuzzion.Literal(3.0)))
    val elseRunExp = Fuzzion.ELSERUn(Fuzzion.Multiply(Fuzzion.Literal(4.0), Fuzzion.Literal(0.5)))

    // Evaluate expressions
    val thenResult = Fuzzion.eval(thenExecuteExp, scope)
    val elseResult = Fuzzion.eval(elseRunExp, scope)

    thenResult shouldEqual 5.0 // 2.0 + 3.0
    elseResult shouldEqual 2.0 // 4.0 * 0.5
  }


}
