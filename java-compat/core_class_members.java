public class class_members {
  static final int LIMIT = 100;
  static final String NAME = "core";
  static final double RATIO = 0.5;
  static final char MARK = 'M';
  static final int DERIVED = LIMIT / 4;

  static int counter;
  static String tag;
  static int[] table;

  static {
    counter = 5;
    tag = "init";
    table = new int[3];
    for (int i = 0; i < table.length; i++) table[i] = i * i;
    System.out.println("static init: " + counter + " " + tag);
  }

  static {
    counter += 10;
  }

  static int add(int a, int b) { return a + b; }

  static String greet(String who) { return "hi " + who; }

  static int sum(int... xs) {
    int t = 0;
    for (int x : xs) t += x;
    return t;
  }

  static void bump() { counter++; }

  static String overload(String a) { return "one:" + a; }
  static String overload(String a, String b) { return "two:" + a + b; }

  public static void main(String[] args) {
    System.out.println(LIMIT + " " + NAME + " " + RATIO + " " + MARK + " " + DERIVED);
    System.out.println(counter + " " + tag);
    for (int v : table) System.out.print(v + " ");
    System.out.println();
    System.out.println(add(2, 3) + " " + greet("bob"));
    System.out.println(sum() + " " + sum(1) + " " + sum(1, 2, 3));
    bump(); bump();
    System.out.println(counter);

    final int local = 7;
    final String s = "fixed";
    final int computed = local * 2;
    System.out.println(local + " " + s + " " + computed);

    System.out.println(Integer.MAX_VALUE > LIMIT);
    System.out.println(NAME.length());
    System.out.println(overload("x") + " " + overload("x", "y"));
    System.out.println(NAME.toUpperCase() + tag.length());
  }
}
