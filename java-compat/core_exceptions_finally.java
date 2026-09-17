public class exceptions_finally {
  static String order = "";

  static int a() {
    order += "a";
    return 1;
  }

  static int risky(boolean boom) {
    try {
      order += "t";
      if (boom) throw new IllegalStateException("boom");
      order += "o";
      return a();
    } catch (IllegalStateException e) {
      order += "c";
      return 2;
    } finally {
      order += "f";
    }
  }

  static int returnInFinally() {
    try {
      return 10;
    } finally {
      System.out.println("finally runs even on return");
    }
  }

  static void nested() {
    try {
      try {
        throw new RuntimeException("inner");
      } finally {
        System.out.println("inner finally");
      }
    } catch (RuntimeException e) {
      System.out.println("outer catch");
    } finally {
      System.out.println("outer finally");
    }
  }

  public static void main(String[] args) {
    System.out.println(risky(false) + " " + order);
    order = "";
    System.out.println(risky(true) + " " + order);
    System.out.println(returnInFinally());
    nested();

    order = "";
    try {
      order += "1";
      throw new ArithmeticException("div");
    } catch (ArithmeticException e) {
      order += "2";
    } finally {
      order += "3";
    }
    System.out.println(order);

    int x = 0;
    try {
      x = 5;
    } finally {
      x += 1;
    }
    System.out.println(x);

    order = "";
    for (int i = 0; i < 3; i++) {
      try {
        if (i == 1) continue;
        order += i;
      } finally {
        order += "f";
      }
    }
    System.out.println(order);

    order = "";
    try {
      try {
        order += "A";
        throw new IllegalStateException("x");
      } catch (IllegalStateException e) {
        order += "B";
        throw new IllegalArgumentException("y");
      } finally {
        order += "C";
      }
    } catch (IllegalArgumentException e) {
      order += "D";
    }
    System.out.println(order);
  }
}
