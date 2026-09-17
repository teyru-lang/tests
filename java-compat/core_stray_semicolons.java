public class stray_semicolons {
  enum Color { RED, GREEN, BLUE };

  static int counter;
  static String label;

  interface Greeter { String greet(String who); }

  static class Holder {
    int value;
    ;
    Holder(int v) { this.value = v; }
    int doubled() { return value * 2; };
  };

  public static void main(String[] args) {
    Color c = Color.GREEN;
    System.out.println(c + " " + c.ordinal());
    counter = 7;
    label = "L";
    System.out.println(counter + label);
    Holder h = new Holder(21);
    System.out.println(h.doubled());
    Greeter g = new Greeter() {
      public String greet(String who) { return "hi " + who; }
    };
    System.out.println(g.greet("ann"));
    Runnable r = new Runnable() {
      public void run() { System.out.println("ran"); }
    };
    r.run();
    int later;
    later = 42;
    System.out.println(later);
    String s;
    s = "text";
    System.out.println(s);
    int[] arr;
    arr = new int[] {1, 2, 3};
    System.out.println(arr.length);
    double dd;
    dd = 2.5;
    System.out.println(dd);
  }
}
