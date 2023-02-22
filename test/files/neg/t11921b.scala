// scalac: -Werror

object test1 {

  class C {
    val x = 0
  }
  object Test {
    val x = 1
    class D extends C {
      println(x)  // error
    }
    def f() =
      new C {
        println(x)  // error
      }
  }
}

object test2 {
  def c(y: Float) = {
    class D {
      val y = 2
    }
    new D {
      println(y)  // error
    }
  }
}

object test3 {
  def c(y: Float) = {
    class D {
      val y = 2
    }
    class E extends D {
      class F {
        println(y)  // error
      }
    }
  }
}

object test4 {

  class C {
    val x = 0
  }
  object Test {
    val x = 1
    class D extends C {
      def x(y: Int) = 3
      val y: Int = this.x // OK
      val z: Int = x      // OK
    }
  }
}

object global

class C {
  val global = 42
}
object D extends C {
  println(global)    // OK, since global is defined in package (https://github.com/scala/scala/pull/10220/files#r1109773904)
}

object test5 {
  class A { val x = 1 }
  class B extends A
  object Uhu {
    val x = 2
    class C extends B {
      class Inner {
        def t = x // ambiguous, message mentions parent B
      }
    }
  }
}

object test6 {
  trait I {
    val a = 1
    def a(x: Int) = ""
  }
  class C {
    val a = ""
    trait J extends I {
      val t = a // error
    }
  }
}


object test7 {
  trait T {
    // overloaded a
    val a = ""
    def a(x: Int) = ""
  }

  trait I {
    val a = 1
  }

  class C extends T {
    trait J {
      self: I =>
      // no warning here. when checking for an outer `a`, we find an OverloadedSymbol with the two definitions in `T`.
      // The owner of the overloaded symbol is `C`, but the alternatives have owner `T`.
      val t = a
    }
  }
}
