public class constructors {
  static class Box {
    final int w;
    final int h;
    final String label;

    Box() {
      this(1, 1, "unit");
      System.out.println("no-arg done");
    }

    Box(int w, int h) {
      this(w, h, "box");
    }

    Box(int w, int h, String label) {
      this.w = w;
      this.h = h;
      this.label = label;
      System.out.println("built " + label);
    }

    int area() { return w * h; }

    String describe() { return label + " " + w + "x" + h + "=" + area(); }

    Box scale(int f) { return new Box(w * f, h * f, label + "*" + f); }

    public String toString() { return "Box(" + describe() + ")"; }
  }

  static class Counter {
    private int n;
    Counter() { this(0); }
    Counter(int start) { this.n = start; }
    Counter inc() { n++; return this; }
    Counter self() { return this; }
    int value() { return n; }
  }

  static class Named {
    String name;
    Named() { this("unnamed"); }
    Named(String name) { this.name = name; }
  }

  public static void main(String[] args) {
    System.out.println("args=" + args.length);
    Box b = new Box();
    System.out.println(b.describe());
    Box c = new Box(3, 4);
    System.out.println(c.area());
    Box d = new Box(2, 5, "custom");
    System.out.println(d);
    System.out.println(d.scale(3).describe());
    Counter k = new Counter();
    System.out.println(k.inc().inc().inc().value());
    Counter k2 = new Counter(10);
    System.out.println(k2.value());
    System.out.println(k.self() == k);
    System.out.println(c.describe().toUpperCase());
    Named nn = new Named();
    Named nm = new Named("explicit");
    System.out.println(nn.name + " " + nm.name);
    System.out.println(b.w + "," + b.h + "," + b.label);
  }
}
