public class arrays_basic {
  static class Point {
    final int x;
    final int y;
    Point(int x, int y) { this.x = x; this.y = y; }
    String describe() { return "(" + x + "," + y + ")"; }
  }

  public static void main(String[] args) {
    int[] a = {5, 3, 9, 1};
    System.out.println(a.length);
    for (int i = 0; i < a.length; i++) System.out.print(a[i] + " ");
    System.out.println();
    for (int v : a) System.out.print(v * 2 + " ");
    System.out.println();

    int[][] grid = new int[3][4];
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 4; j++)
        grid[i][j] = i * 10 + j;
    for (int[] row : grid) {
      for (int v : row) System.out.print(v + " ");
      System.out.println();
    }
    System.out.println(grid.length + " " + grid[0].length);

    int[][] jag = {{1}, {2, 3}, {4, 5, 6}};
    for (int[] row : jag) System.out.println(row.length);
    System.out.println(jag[2][1] + jag[1][0]);

    int[][] ragged = new int[3][];
    ragged[0] = new int[1];
    ragged[1] = new int[2];
    ragged[2] = new int[3];
    for (int i = 0; i < ragged.length; i++) System.out.println(ragged[i].length);
    System.out.println(ragged.length);

    String[] words = {"alpha", "beta"};
    for (String w : words) System.out.println(w.toUpperCase());

    char[][] letters = new char[2][3];
    letters[0][0] = 'x';
    letters[1][2] = 'y';
    System.out.println("" + letters[0][0] + letters[1][2]);
    System.out.println(letters.length + " " + letters[0].length);

    double[] ds = {1.5, 2.5};
    System.out.println(ds.length + " " + ds[1]);

    boolean[] bs = new boolean[3];
    System.out.println(bs.length + " " + bs[0]);

    int[] none = {};
    System.out.println(none.length);

    Point[] pts = new Point[3];
    pts[0] = new Point(1, 2);
    pts[1] = new Point(3, 4);
    pts[2] = new Point(5, 6);
    for (Point pt : pts) System.out.println(pt.describe());

    Point[] init = {new Point(0, 0), new Point(7, 8)};
    System.out.println(init.length);
    for (Point pt : init) System.out.println(pt.describe());

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
