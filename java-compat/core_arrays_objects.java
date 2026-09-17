public class arrays_objects {
  static class Point {
    final int x;
    final int y;
    Point(int x, int y) { this.x = x; this.y = y; }
    String describe() { return "(" + x + "," + y + ")"; }
  }

  public static void main(String[] args) {
    Point[] pts = new Point[3];
    pts[0] = new Point(1, 2);
    pts[1] = new Point(3, 4);
    pts[2] = new Point(5, 6);
    for (Point p : pts) System.out.println(p.describe());

    Point[] init = {new Point(0, 0), new Point(7, 8)};
    System.out.println(init.length);
    for (Point p : init) System.out.println(p.describe());

    Object[] mixed = {"str", Integer.valueOf(42), Character.valueOf('c')};
    for (Object o : mixed) System.out.println(o);

    Point[] copy = new Point[2];
    System.arraycopy(init, 0, copy, 0, 2);
    System.out.println(copy[1].describe());

    System.out.println("args:" + args.length);
    String[] la = args;
    System.out.println(la.length);
    if (args.length > 0) System.out.println(args[0]);

    Object[] objs = new Point[2];
    objs[0] = new Point(9, 9);
    System.out.println(((Point) objs[0]).describe());
    System.out.println(pts[0] == pts[0]);
    System.out.println(pts[1].describe().length());
  }
}
