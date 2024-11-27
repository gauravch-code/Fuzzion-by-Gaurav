object FuzzionApp {
  def main(args: Array[String]): Unit = {
    println("Running Fuzzion...")

    // 1. Basic Fuzzy Logic Operations
    val andExp = Fuzzion.Literal(0.6).and(Fuzzion.Literal(0.3))
    println(s"Evaluation result (0.6 AND 0.3): ${Fuzzion.eval(andExp)}") // Expected: 0.3

    val orExp = Fuzzion.Literal(0.6).or(Fuzzion.Literal(0.3))
    println(s"Evaluation result (0.6 OR 0.3): ${Fuzzion.eval(orExp)}") // Expected: 0.6

    val notExp = Fuzzion.Literal(0.6).not
    println(s"Evaluation result (NOT 0.6): ${Fuzzion.eval(notExp)}") // Expected: 0.4

    val xorExp = Fuzzion.Literal(0.7).xor(Fuzzion.Literal(0.4))
    println(s"Evaluation result (0.7 XOR 0.4): ${Fuzzion.eval(xorExp)}") // Expected: 0.3

    val addExp = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
    println(s"Evaluation result (0.4 ADD 0.5): ${Fuzzion.eval(addExp)}") // Expected: 0.9

    val multiplyExp = Fuzzion.Literal(0.4).multiply(Fuzzion.Literal(0.6))
    println(s"Evaluation result (0.4 MULTIPLY 0.6): ${Fuzzion.eval(multiplyExp)}") // Expected: 0.24

    val alphaCutExp = Fuzzion.Literal(0.7).alphaCut(0.5)
    println(s"Alpha-cut result (alpha = 0.5, value = 0.7): ${Fuzzion.eval(alphaCutExp)}") // Expected: 0.7

    val alphaCutExpFail = Fuzzion.Literal(0.3).alphaCut(0.5)
    println(s"Alpha-cut result (alpha = 0.5, value = 0.3): ${Fuzzion.eval(alphaCutExpFail)}") // Expected: 0.0

    // 2. Using Variables and Scopes
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(0.7)) // Bind variable 'x' to 0.7
    val varExp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
    println(s"Evaluation result (x AND 1.0), where x=0.7: ${Fuzzion.eval(varExp, scope)}") // Expected: 0.7

    // 3. Variable Assignment
    val assignExp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
    println(s"Evaluation of Assign (y = 0.5): ${Fuzzion.eval(assignExp, scope)}") // Expected: 0.5

    // 4. TestGate Example
    val testGateExp = Fuzzion.TestGate(Fuzzion.Literal(0.5))
    println(s"TestGate result (0.5): ${Fuzzion.eval(testGateExp, scope)}") // Expected: 1.0

    // 5. Class Definitions, Inheritance, and Method Invocation
    val baseClass = Fuzzion.ClassDef(
      name = "Base",
      methods = List(
        Fuzzion.MethodDef(
          name = "greet",
          params = List(Fuzzion.Parameter("name", "String")),
          body = Fuzzion.Literal(1.0) // Returns 1.0 as a greeting
        )
      ),
      classVars = List(
        Fuzzion.ClassVar(
          name = "baseVar",
          varType = Fuzzion.VarType("Double"),
          initialValue = Fuzzion.Literal(10.0)
        )
      )
    )

    val derivedClass = Fuzzion.ClassDef(
      name = "Derived",
      methods = List(
        Fuzzion.MethodDef(
          name = "greet",
          params = List(Fuzzion.Parameter("name", "String")),
          body = Fuzzion.Literal(2.0) // Overrides to return 2.0
        )
      ),
      parent = Some(baseClass),
      classVars = List(
        Fuzzion.ClassVar(
          name = "derivedVar",
          varType = Fuzzion.VarType("Double"),
          initialValue = Fuzzion.Literal(20.0) // Example initialization
        )
      )
    )

    // 5.1. Creating Instances and Invoking Methods
    val derivedInstance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
    val baseInstance = Fuzzion.eval(Fuzzion.CreateNew(baseClass)).asInstanceOf[Fuzzion.ClassInstance]

    val greetDerived = Fuzzion.eval(Fuzzion.InvokeMethod(derivedInstance, "greet", List(Fuzzion.Literal(1.0))))
    println(s"Method 'greet' result from Derived class: $greetDerived") // Expected: 2.0

    val greetBase = Fuzzion.eval(Fuzzion.InvokeMethod(baseInstance, "greet", List(Fuzzion.Literal(1.0))))
    println(s"Method 'greet' result from Base class: $greetBase") // Expected: 1.0

    // 5.2. Accessing Class Variables
    val derivedVar = Fuzzion.eval(Fuzzion.AccessClassVar(derivedInstance, "derivedVar"))
    println(s"Derived class variable 'derivedVar': $derivedVar") // Expected: 20.0

    val baseVar = Fuzzion.eval(Fuzzion.AccessClassVar(baseInstance, "baseVar"))
    println(s"Base class variable 'baseVar': $baseVar") // Expected: 10.0

    // 6. Nested Classes Accessing Parent Class Variables and Methods
    val innerClass = Fuzzion.ClassDef(
      name = "Inner",
      methods = List(
        Fuzzion.MethodDef(
          name = "getInnerVar",
          params = List(),
          body = Fuzzion.AccessClassVar(Fuzzion.Variable("this"), "derivedVar") // Accessing parent class variable
        )
      ),
      parent = Some(derivedClass),
      classVars = List(
        Fuzzion.ClassVar(
          name = "innerVar",
          varType = Fuzzion.VarType("Double"),
          initialValue = Fuzzion.Literal(30.0)
        )
      )
    )

    val innerInstance = Fuzzion.eval(Fuzzion.CreateNew(innerClass, Some(derivedInstance))).asInstanceOf[Fuzzion.ClassInstance]

    val innerVar = Fuzzion.eval(Fuzzion.AccessClassVar(innerInstance, "innerVar"))
    println(s"Inner class variable 'innerVar': $innerVar") // Expected: 30.0

    val getInnerVar = Fuzzion.eval(Fuzzion.InvokeMethod(innerInstance, "getInnerVar", List()))
    println(s"Inner class method 'getInnerVar' result: $getInnerVar") // Expected: 20.0

    // 7. Conditional Constructs (`IFTRUE`)
    val conditionalExp = Fuzzion.IFTRUE(
      Fuzzion.GreaterEqual(Fuzzion.Variable("x"), Fuzzion.Literal(5.0)),
      // Then branch: Assign y = x + 3
      Fuzzion.Assign("y", Fuzzion.Add(Fuzzion.Variable("x"), Fuzzion.Literal(3.0))),
      // Else branch: Assign y = x * 2
      Fuzzion.Assign("y", Fuzzion.Multiply(Fuzzion.Variable("x"), Fuzzion.Literal(2.0)))
    )

    // 7.1. Evaluate with x = 6.0 (condition true)
    val scopeCondTrue = new Fuzzion.Scope()
    scopeCondTrue.bind("x", Fuzzion.Literal(6.0))
    println(s"Evaluating IFTRUE with x=6.0")
    val resultTrue = Fuzzion.eval(conditionalExp, scopeCondTrue)
    println(s"Result of IFTRUE: $resultTrue") // Expected: 9.0
    println(s"Value of 'y' after IFTRUE: ${Fuzzion.eval(Fuzzion.Variable("y"), scopeCondTrue)}") // Expected: 9.0

    // 7.2. Evaluate with x = 3.0 (condition false)
    val scopeCondFalse = new Fuzzion.Scope()
    scopeCondFalse.bind("x", Fuzzion.Literal(3.0))
    println(s"Evaluating IFTRUE with x=3.0")
    val resultFalse = Fuzzion.eval(conditionalExp, scopeCondFalse)
    println(s"Result of IFTRUE: $resultFalse") // Expected: 6.0
    println(s"Value of 'y' after IFTRUE: ${Fuzzion.eval(Fuzzion.Variable("y"), scopeCondFalse)}") // Expected: 6.0

    // 7.3. Evaluate with x undefined (partial evaluation)
    val scopePartial = new Fuzzion.Scope()
    println(s"Evaluating IFTRUE with x undefined")
    val resultPartial = Fuzzion.eval(conditionalExp, scopePartial)
    println(s"Result of IFTRUE: $resultPartial") // Expected: PartialExpression(IFTRUE(...))
    println(s"Value of 'y' after IFTRUE: ${Fuzzion.eval(Fuzzion.Variable("y"), scopePartial)}") // Expected: PartialExpression(...)
  }
}

object Fuzzion {

  trait Expression

  // Base constructs
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
  case class AlphaCut(expr: Expression, alpha: Double) extends Expression
  case class IFTRUE(condition: Expression, thenBranch: Expression, elseBranch: Expression) extends Expression
  case class GreaterEqual(lhs: Expression, rhs: Expression) extends Expression

  // Newly added constructs for Homework 3
  case class ELSERUn(elseBranch: Expression) extends Expression
  case class THENEXECUTE(thenBranch: Expression) extends Expression

  // Class definitions and method constructs
  case class ClassDef(
                       name: String,
                       methods: List[MethodDef],
                       parent: Option[ClassDef] = None,
                       classVars: List[ClassVar] = List(),
                       nestedClasses: List[ClassDef] = List()
                     ) extends Expression

  case class MethodDef(name: String, params: List[Parameter], body: Expression) extends Expression
  case class Parameter(name: String, paramType: String) extends Expression
  case class ClassVar(name: String, varType: VarType, initialValue: Expression) extends Expression
  case class VarType(typeName: String) extends Expression
  case class InvokeMethod(instance: ClassInstance, methodName: String, args: List[Expression]) extends Expression
  case class CreateNew(cls: ClassDef, parentInstanceOpt: Option[ClassInstance] = None) extends Expression
  case class ClassInstance(cls: ClassDef, scope: Scope = new Scope()) extends Expression
  case class AccessClassVar(instanceExpr: Expression, varName: String) extends Expression
  case class PartialExpression(expr: Expression) extends Expression

  // Scope class to manage variable bindings and inheritance
  class Scope(val parent: Option[Scope] = None) {
    private val variables = scala.collection.mutable.Map[String, Expression]()
    private val methods = scala.collection.mutable.Map[String, MethodDef]()
    private val classVariables = scala.collection.mutable.Map[String, Expression]()

    def bind(name: String, expr: Expression): Unit = {
      variables(name) = expr
    }

    def resolve(name: String): Option[Expression] = {
      variables.get(name).orElse(parent.flatMap(_.resolve(name)))
    }

    def bindMethod(name: String, method: MethodDef): Unit = {
      methods(name) = method
    }

    def resolveMethod(name: String): Option[MethodDef] = {
      methods.get(name).orElse(parent.flatMap(_.resolveMethod(name)))
    }

    def bindClassVar(name: String, value: Expression): Unit = {
      classVariables(name) = value
    }

    def resolveClassVar(name: String): Option[Expression] = {
      classVariables.get(name).orElse(parent.flatMap(_.resolveClassVar(name)))
    }

    // Method to retrieve all variable bindings (for testing and merging purposes)
    def getAllVariables: Map[String, Expression] = variables.toMap
  }

  // Evaluator function
  def eval(expr: Expression, scope: Scope = new Scope()): Any = expr match {
    case Literal(value) => value

    case Variable(name) =>
      scope.resolve(name) match {
        case Some(Literal(v)) => v
        case Some(e: Expression) => e
        case None => Variable(name)
      }

    case Assign(name, exprInner) =>
      val evaluatedValue = eval(exprInner, scope) match {
        case v: Double => Literal(v)
        case e: Expression => e
      }
      scope.bind(name, evaluatedValue)
      evaluatedValue match {
        case Literal(v) => v
        case e: Expression => e
      }

    // Evaluating new constructs
    case ELSERUn(elseBranch) => eval(elseBranch, scope)
    case THENEXECUTE(thenBranch) => eval(thenBranch, scope)

    case Add(lhs, rhs) =>
      val simplifiedExpr = simplifyAdd(Add(lhs, rhs), scope)
      simplifiedExpr match {
        case Literal(value) => value
        case expr => PartialExpression(expr)
      }

    case Multiply(lhs, rhs) =>
      val simplifiedExpr = simplifyMultiply(Multiply(lhs, rhs), scope)
      simplifiedExpr match {
        case Literal(value) => value
        case expr => PartialExpression(expr)
      }

    case And(lhs, rhs) =>
      (eval(lhs, scope), eval(rhs, scope)) match {
        case (l: Double, r: Double) => math.min(l, r) // Logical AND
        case (l: Double, rExpr: Expression) => PartialExpression(And(Literal(l), rExpr))
        case (lExpr: Expression, r: Double) => PartialExpression(And(lExpr, Literal(r)))
        case (lExpr: Expression, rExpr: Expression) => PartialExpression(And(lExpr, rExpr))
        case _ => throw new MatchError(s"Unsupported AND operands: $lhs, $rhs")
      }

    case Or(lhs, rhs) =>
      (eval(lhs, scope), eval(rhs, scope)) match {
        case (l: Double, r: Double) => math.max(l, r) // Logical OR
        case (l: Double, rExpr: Expression) => PartialExpression(Or(Literal(l), rExpr))
        case (lExpr: Expression, r: Double) => PartialExpression(Or(lExpr, Literal(r)))
        case (lExpr: Expression, rExpr: Expression) => PartialExpression(Or(lExpr, rExpr))
        case _ => throw new MatchError(s"Unsupported OR operands: $lhs, $rhs")
      }

    case Xor(lhs, rhs) =>
      (eval(lhs, scope), eval(rhs, scope)) match {
        case (l: Double, r: Double) => math.abs(l - r)
        case (l: Double, rExpr: Expression) => PartialExpression(Xor(Literal(l), rExpr))
        case (lExpr: Expression, r: Double) => PartialExpression(Xor(lExpr, Literal(r)))
        case (lExpr: Expression, rExpr: Expression) => PartialExpression(Xor(lExpr, rExpr))
        case _ => throw new MatchError(s"Unsupported XOR operands: $lhs, $rhs")
      }

    case Not(exprInner) =>
      eval(exprInner, scope) match {
        case v: Double => math.max(0.0, 1.0 - v)
        case e: Expression => PartialExpression(Not(e))
      }

    case TestGate(innerExpr) =>
      eval(innerExpr, scope) match {
        case value: Double =>
          if (value > 0) 1.0 else 0.0 // Return 1.0 if the value is positive, otherwise 0.0
        case Literal(value: Double) =>
          if (value > 0) 1.0 else 0.0 // Return 1.0 for positive values, otherwise 0.0
        case partialExpr: Expression =>
          PartialExpression(TestGate(partialExpr)) // Handle partial expressions
        case unsupported =>
          throw new MatchError(s"Unsupported TestGate inner expression: $unsupported")
      }

    case AlphaCut(exprInner, alpha) =>
      eval(exprInner, scope) match {
        case v: Double => if (v >= alpha) v else 0.0
        case e: Expression => PartialExpression(AlphaCut(e, alpha))
      }

    case GreaterEqual(lhs, rhs) =>
      (eval(lhs, scope), eval(rhs, scope)) match {
        case (l: Double, r: Double) => if (l >= r) 1.0 else 0.0
        case (l: Double, rExpr: Expression) => PartialExpression(GreaterEqual(Literal(l), rExpr))
        case (lExpr: Expression, r: Double) => PartialExpression(GreaterEqual(lExpr, Literal(r)))
        case (lExpr: Expression, rExpr: Expression) => PartialExpression(GreaterEqual(lExpr, rExpr))
        case _ => throw new MatchError(s"Unsupported GREATER EQUAL operands: $lhs, $rhs")
      }

    case IFTRUE(condition, thenBranch, elseBranch) =>
      eval(condition, scope) match {
        case v: Double =>
          if (v >= 0.5) {
            eval(thenBranch, scope)
          } else {
            eval(elseBranch, scope)
          }
        case condExpr: Expression => // Represents a partially evaluated condition
          // Partially evaluate both branches
          val partiallyEvaluatedThen = eval(thenBranch, scope) match {
            case expr: Expression => expr
            case other => PartialExpression(thenBranch)
          }
          val partiallyEvaluatedElse = eval(elseBranch, scope) match {
            case expr: Expression => expr
            case other => PartialExpression(elseBranch)
          }
          PartialExpression(IFTRUE(condExpr, partiallyEvaluatedThen, partiallyEvaluatedElse))
        case _ =>
          throw new MatchError(s"Unsupported IFTRUE condition: $condition")
      }

    case InvokeMethod(instance: ClassInstance, methodName, args) =>
      val clsToSearch = Option(instance.cls).getOrElse(
        throw new Exception("Class for the instance is not defined")
      )
      val method = findMethod(clsToSearch, methodName).getOrElse(
        throw new Exception(s"Method $methodName not found")
      )
      val evaluatedArgs = args.map(arg => eval(arg, scope))
      // Create a new scope for method execution
      val methodScope = new Scope(Some(instance.scope))
      // Bind parameters
      method.params.zip(evaluatedArgs).foreach { case (param, argExpr) =>
        methodScope.bind(param.name, argExpr match {
          case v: Double => Literal(v)
          case e: Expression => e
        })
      }
      // Evaluate method body
      eval(method.body, methodScope)

    case AccessClassVar(instanceExpr, varName) =>
      val instance = eval(instanceExpr, scope) match {
        case ci: ClassInstance => ci
        case other => throw new Exception(s"AccessClassVar expects a ClassInstance, got: $other")
      }
      instance.scope.resolveClassVar(varName) match {
        case Some(Literal(v)) => v
        case Some(e: Expression) => e
        case None => throw new Exception(s"Class variable $varName not found")
      }

    case CreateNew(cls, parentInstanceOpt) =>
      // Create a new scope, optionally inheriting from a parent instance's scope
      val newScope = new Scope(parentInstanceOpt.map(_.scope))
      // Initialize class variables
      cls.classVars.foreach { v =>
        val value = eval(v.initialValue, newScope) match {
          case v: Double => Literal(v)
          case e: Expression => e
        }
        newScope.bindClassVar(v.name, value)
      }
      // Initialize parent class variables if inheritance exists
      cls.parent.foreach { parentCls =>
        parentCls.classVars.foreach { v =>
          val value = eval(v.initialValue, newScope) match {
            case v: Double => Literal(v)
            case e: Expression => e
          }
          newScope.bindClassVar(v.name, value)
        }
      }
      // Create the ClassInstance
      val instance = ClassInstance(cls, newScope)
      newScope.bind("this", instance)
      instance

    // Handle PartialExpression
    case PartialExpression(e) => PartialExpression(e)

    // Explicitly handle ClassInstance objects
    case ci: ClassInstance => ci

    case _ =>
      throw new MatchError(s"Unsupported expression: $expr")
  }

  // Helper method to find a method in class hierarchy
  def findMethod(cls: ClassDef, methodName: String): Option[MethodDef] = {
    cls.methods.find(_.name == methodName).orElse(cls.parent.flatMap(findMethod(_, methodName)))
  }

  // Helper functions for expression simplification
  def simplifyAdd(expr: Expression, scope: Scope): Expression = expr match {
    case Add(left, right) =>
      val leftSimplified = simplifyAdd(evalToExpression(left, scope), scope)
      val rightSimplified = simplifyAdd(evalToExpression(right, scope), scope)
      (leftSimplified, rightSimplified) match {
        case (Literal(lv), Literal(rv)) => Literal(lv + rv)
        case (Literal(0), rExpr) => rExpr
        case (lExpr, Literal(0)) => lExpr
        case (lExpr, rExpr) => Add(lExpr, rExpr)
      }
    case PartialExpression(e) => e // Unwrap PartialExpression
    case other => evalToExpression(other, scope)
  }

  def simplifyMultiply(expr: Expression, scope: Scope): Expression = expr match {
    case Multiply(left, right) =>
      val leftSimplified = simplifyMultiply(evalToExpression(left, scope), scope)
      val rightSimplified = simplifyMultiply(evalToExpression(right, scope), scope)
      (leftSimplified, rightSimplified) match {
        case (Literal(lv), Literal(rv)) => Literal(lv * rv)
        case (Literal(1), rExpr) => rExpr
        case (lExpr, Literal(1)) => lExpr
        case (Literal(0), _) => Literal(0)
        case (_, Literal(0)) => Literal(0)
        case (lExpr, rExpr) => Multiply(lExpr, rExpr)
      }
    case PartialExpression(e) => e // Unwrap PartialExpression
    case other => evalToExpression(other, scope)
  }

  def evalToExpression(expr: Expression, scope: Scope): Expression = {
    eval(expr, scope) match {
      case v: Double => Literal(v)
      case PartialExpression(e) => e // Unwrap PartialExpression
      case e: Expression => e
      case other => throw new MatchError(s"Unsupported eval result: $other")
    }
  }

  // Implicit class to add operator methods to Expression
  implicit class ExpressionOps(lhs: Expression) {
    def and(rhs: Expression): Expression = And(lhs, rhs)
    def or(rhs: Expression): Expression = Or(lhs, rhs)
    def xor(rhs: Expression): Expression = Xor(lhs, rhs)
    def add(rhs: Expression): Expression = Add(lhs, rhs)
    def multiply(rhs: Expression): Expression = Multiply(lhs, rhs)
    def not: Expression = Not(lhs)
    def alphaCut(alpha: Double): Expression = AlphaCut(lhs, alpha)
    def greaterEqual(rhs: Expression): Expression = GreaterEqual(lhs, rhs)
    def iftrue(condition: Expression, thenBranch: Expression, elseBranch: Expression): Expression =
      IFTRUE(condition, thenBranch, elseBranch)
  }
}

