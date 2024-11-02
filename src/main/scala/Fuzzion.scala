object FuzzionApp {
  def main(args: Array[String]): Unit = {
    println("Running Fuzzion...")

    // Original expressions with fuzzy logic operations
    val and_exp = Fuzzion.Literal(0.6).and(Fuzzion.Literal(0.3))
    println(s"Evaluation result (0.6 AND 0.3): ${Fuzzion.eval(and_exp)}") // Expected: 0.3

    val or_exp = Fuzzion.Literal(0.6).or(Fuzzion.Literal(0.3))
    println(s"Evaluation result (0.6 OR 0.3): ${Fuzzion.eval(or_exp)}") // Expected: 0.6

    val not_exp = Fuzzion.Literal(0.6).not
    println(s"Evaluation result (NOT 0.6): ${Fuzzion.eval(not_exp)}") // Expected: 0.4

    val xor_exp = Fuzzion.Literal(0.7).xor(Fuzzion.Literal(0.4))
    println(s"Evaluation result (0.7 XOR 0.4): ${Fuzzion.eval(xor_exp)}") // Expected: approximately 0.3

    val add_exp = Fuzzion.Literal(0.4).add(Fuzzion.Literal(0.5))
    println(s"Evaluation result (0.4 ADD 0.5): ${Fuzzion.eval(add_exp)}") // Expected: 0.9

    val multiply_exp = Fuzzion.Literal(0.4).multiply(Fuzzion.Literal(0.6))
    println(s"Evaluation result (0.4 MULTIPLY 0.6): ${Fuzzion.eval(multiply_exp)}") // Expected: 0.24

    val alphaCutExp = Fuzzion.Literal(0.7).alphaCut(0.5)
    println(s"Alpha-cut result (alpha = 0.5, value = 0.7): ${Fuzzion.eval(alphaCutExp)}") // Expected: 0.7

    val alphaCutExpFail = Fuzzion.Literal(0.3).alphaCut(0.5)
    println(s"Alpha-cut result (alpha = 0.5, value = 0.3): ${Fuzzion.eval(alphaCutExpFail)}") // Expected: 0.0

    // Using variables and scopes
    val scope = new Fuzzion.Scope()
    scope.bind("x", Fuzzion.Literal(0.7)) // Bind variable 'x' to 0.7
    val var_exp = Fuzzion.Variable("x").and(Fuzzion.Literal(1.0))
    println(s"Evaluation result (x AND 1.0), where x=0.7: ${Fuzzion.eval(var_exp, scope)}") // Expected: 0.7

    // Test with variable assignment and custom evaluation
    val assign_exp = Fuzzion.Assign("y", Fuzzion.Literal(0.5))
    println(s"Evaluation of Assign (y = 0.5): ${Fuzzion.eval(assign_exp, scope)}") // Expected: 0.5

    // Using TestGate
    val testgate_exp = Fuzzion.TestGate(Fuzzion.Literal(0.5))
    println(s"TestGate result (0.5): ${Fuzzion.eval(testgate_exp, scope)}") // Expected: 1.0

    // Class, inheritance, method invocation
    val baseClass = Fuzzion.ClassDef(
      "Base",
      List(
        Fuzzion.MethodDef("greet", List(Fuzzion.Parameter("name", "String")), Fuzzion.Literal(1.0))
      ),
      classVars = List(Fuzzion.ClassVar("greeting", Fuzzion.VarType("String"), Fuzzion.Literal(1.0)))
    )

    val derivedClass = Fuzzion.ClassDef(
      "Derived",
      List(
        Fuzzion.MethodDef("greet", List(Fuzzion.Parameter("name", "String")), Fuzzion.Literal(2.0))
      ),
      Some(baseClass),
      classVars = List(Fuzzion.ClassVar("farewell", Fuzzion.VarType("String"), Fuzzion.Literal(2.0)))
    )

    // Create instance of derived class and invoke method
    val instance = Fuzzion.eval(Fuzzion.CreateNew(derivedClass)).asInstanceOf[Fuzzion.ClassInstance]
    val result = Fuzzion.eval(Fuzzion.InvokeMethod(instance, "greet", List(Fuzzion.Literal(1.0))))
    println(s"Method 'greet' result from Derived class: $result") // Expected: 2.0

    // Create instance of base class and invoke method
    val baseInstance = Fuzzion.eval(Fuzzion.CreateNew(baseClass)).asInstanceOf[Fuzzion.ClassInstance]
    val baseResult = Fuzzion.eval(Fuzzion.InvokeMethod(baseInstance, "greet", List(Fuzzion.Literal(1.0))))
    println(s"Method 'greet' result from Base class: $baseResult") // Expected: 1.0

    // Access class variable
    val varResult = Fuzzion.eval(Fuzzion.AccessClassVar(instance, "farewell"))
    println(s"Accessed class variable 'farewell': $varResult") // Expected: 2.0

    // Multiple inheritance test
    val intermediateClass = Fuzzion.ClassDef(
      "Intermediate",
      List(
        Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(15.0))
      ),
      Some(baseClass)
    )

    val multiDerivedClass = Fuzzion.ClassDef(
      "MultiDerived",
      List(
        Fuzzion.MethodDef("calculate", List(), Fuzzion.Literal(25.0))
      ),
      Some(intermediateClass)
    )

    val multiDerivedInstance = Fuzzion.eval(Fuzzion.CreateNew(multiDerivedClass)).asInstanceOf[Fuzzion.ClassInstance]
    val multiResult = Fuzzion.eval(Fuzzion.InvokeMethod(multiDerivedInstance, "calculate", List()))
    println(s"Method 'calculate' result from MultiDerived class: $multiResult") // Expected: 25.0


    // Multiple inheritance accessing intermediate class method
    val intermediateResult = Fuzzion.eval(Fuzzion.InvokeMethod(multiDerivedInstance, "greet", List(Fuzzion.Literal(1.0))))
    println(s"Method 'greet' result from MultiDerived class (inherited from Base): $intermediateResult") // Expected: 1.0
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
  case class AlphaCut(expr: Expression, alpha: Double) extends Expression // New alpha-cut operation
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
  case class CreateNew(cls: ClassDef, parentInstance: Option[ClassInstance] = None) extends Expression
  case class ClassInstance(cls: ClassDef, scope: Scope = new Scope()) extends Expression
  case class AccessClassVar(instanceExpr: Expression, varName: String) extends Expression

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
  }

  // Evaluation function
  def eval(expr: Expression, scope: Scope = new Scope()): Any = expr match {
    case Literal(value) => value
    case Variable(name) =>
      scope.resolve(name) match {
        case Some(expr) => eval(expr, scope)
        case None       => throw new Exception(s"Variable $name not found")
      }
    case Assign(name, value) =>
      val evaluatedValue = eval(value, scope)
      scope.bind(name, Literal(evaluatedValue.asInstanceOf[Double]))
      evaluatedValue
    case TestGate(expr) =>
      val result = eval(expr, scope).asInstanceOf[Double]
      if (result > 0.0) 1.0 else 0.0
    case Not(e) => 1.0 - eval(e, scope).asInstanceOf[Double] // Fuzzy logic NOT operation
    case Or(lhs, rhs) => math.max(eval(lhs, scope).asInstanceOf[Double], eval(rhs, scope).asInstanceOf[Double]) // Fuzzy logic OR operation
    case And(lhs, rhs) => math.min(eval(lhs, scope).asInstanceOf[Double], eval(rhs, scope).asInstanceOf[Double]) // Fuzzy logic AND operation
    case Xor(lhs, rhs) => math.abs(eval(lhs, scope).asInstanceOf[Double] - eval(rhs, scope).asInstanceOf[Double]) // Fuzzy logic XOR operation
    case Add(lhs, rhs) =>
      val sum = eval(lhs, scope).asInstanceOf[Double] + eval(rhs, scope).asInstanceOf[Double]
      math.min(1.0, sum) // Cap at 1.0 for fuzzy addition
    case Multiply(lhs, rhs) => eval(lhs, scope).asInstanceOf[Double] * eval(rhs, scope).asInstanceOf[Double] // Fuzzy multiplication
    case AlphaCut(expr, alpha) =>
      val value = eval(expr, scope).asInstanceOf[Double]
      if (value >= alpha) value else 0.0

    case ClassDef(name, methods, parent, classVars, nestedClasses) =>
      classVars.foreach(v => scope.bindClassVar(v.name, v.initialValue))
      methods.foreach(m => scope.bindMethod(m.name, m))
      nestedClasses.foreach(nestedClass => eval(nestedClass, scope)) // Recursively evaluate nested classes
      0.0 // Class does not evaluate to a value directly

    case CreateNew(cls, parentInstance) =>
      val newScope = new Scope(parentInstance.map(_.scope))
      cls.classVars.foreach { v =>
        newScope.bindClassVar(v.name, v.initialValue)
      }
      // Initialize parent class variables
      cls.parent.foreach { parentCls =>
        parentCls.classVars.foreach { v =>
          newScope.bindClassVar(v.name, v.initialValue)
        }
      }
      ClassInstance(cls, newScope)

    case InvokeMethod(instance: ClassInstance, methodName: String, args: List[Expression]) =>
      val clsToSearch = Option(instance.cls).getOrElse(
        throw new Exception("Class for the instance is not defined")
      )
      val method = findMethod(clsToSearch, methodName).getOrElse(
        throw new Exception(s"Method $methodName not found")
      )

      // Create a new scope for the method call, with instance.scope as parent
      val methodScope = new Scope(Some(instance.scope))

      // Bind 'this' to the instance
      methodScope.bind("this", instance)

      // Bind the method's parameters to the provided arguments
      method.params.zip(args).foreach { case (param, argExpr) =>
        val evaluatedArg = eval(argExpr, scope) // Evaluate argument in the current scope
        methodScope.bind(param.name, Literal(evaluatedArg.asInstanceOf[Double])) // Bind to method scope
      }

      // Evaluate the method body within the method scope
      eval(method.body, methodScope)

    case AccessClassVar(instanceExpr: Expression, varName: String) =>
      val instance = eval(instanceExpr, scope) match {
        case ci: ClassInstance => ci
        case other             => throw new Exception(s"Expected ClassInstance, got $other")
      }
      // Try to resolve in the current class scope
      instance.scope.resolveClassVar(varName).orElse {
        // If not found, traverse the parent class hierarchy
        def resolveInParent(clsOpt: Option[ClassDef]): Option[Expression] = {
          clsOpt match {
            case Some(cls) =>
              cls.classVars.find(_.name == varName).map { classVar =>
                // Return the initial value expression without evaluating it
                classVar.initialValue
              }.orElse {
                resolveInParent(cls.parent) // Recursively check the parent
              }
            case None => None
          }
        }

        resolveInParent(Some(instance.cls))
      }.map { expr =>
        eval(expr, instance.scope) // Now evaluate the expression
      }.getOrElse(
        0.0 // Return 0.0 if the class variable is not found
      )

    case ci: ClassInstance => ci // Handle ClassInstance expressions by returning them as-is

    case _ => throw new MatchError(s"Unsupported expression: $expr")
  }

  // Implicit class for DSL-like syntax for AND, OR, XOR, ADD, MULTIPLY, and ALPHA-CUT
  implicit class ExpressionOps(lhs: Expression) {
    def and(rhs: Expression): Expression = And(lhs, rhs)
    def or(rhs: Expression): Expression = Or(lhs, rhs)
    def xor(rhs: Expression): Expression = Xor(lhs, rhs)
    def add(rhs: Expression): Expression = Add(lhs, rhs)
    def multiply(rhs: Expression): Expression = Multiply(lhs, rhs)
    def not: Expression = Not(lhs)
    def alphaCut(alpha: Double): Expression = AlphaCut(lhs, alpha)
  }

  // Helper to find a method in a class hierarchy (for inheritance)
  def findMethod(cls: ClassDef, methodName: String): Option[MethodDef] = {
    // First check the current class, then check the parent class if necessary
    cls.methods.find(_.name == methodName).orElse(cls.parent.flatMap(findMethod(_, methodName)))
  }
}
