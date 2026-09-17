public class exceptions_basic {
  static class AppError extends Exception {
    private final int code;
    AppError(String msg, int code) { super(msg); this.code = code; }
    int code() { return code; }
  }

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

  static void nestedFinally() {
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

  static int parseIntStrict(String s) throws AppError {
    if (s == null || s.isEmpty()) throw new AppError("empty input", 400);
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new AppError("not a number: " + s, 422);
    }
  }

  public static void main(String[] args) {
    for (String s : new String[] {"12", "abc", ""}) {
      try {
        System.out.println("parsed " + parseIntStrict(s));
      } catch (AppError e) {
        System.out.println("error " + e.getMessage() + " code=" + e.code());
      }
    }

    try {
      String n = null;
      System.out.println(n.length());
    } catch (RuntimeException e) {
      System.out.println("caught runtime");
    }

    try {
      Object o = "text";
      Integer bad = (Integer) o;
      System.out.println(bad);
    } catch (ClassCastException e) {
      System.out.println("caught cast");
    }

    try {
      int[] arr = new int[2];
      arr[5] = 1;
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("caught bounds");
    }

    try {
      throw new IllegalArgumentException("bad arg");
    } catch (IllegalArgumentException e) {
      System.out.println("arg " + e.getMessage());
    }

    Throwable t = new AppError("outer", 500);
    System.out.println(t.getMessage());
    System.out.println(t instanceof AppError);

    try {
      throw new AppError("wrap", 1);
    } catch (Exception e) {
      System.out.println("as exception " + e.getMessage());
    }

    Exception checked = new AppError("nullable", 2);
    if (checked != null) System.out.println("not null: " + checked.getMessage());

    System.out.println(risky(false) + " " + order);
    order = "";
    System.out.println(risky(true) + " " + order);
    System.out.println(returnInFinally());
    nestedFinally();

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

    System.out.println("done");
  }
}
