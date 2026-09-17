public class control_flow {
  public static void main(String[] args) {
    outer:
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (j == 3) continue outer;
        if (i == 2) break outer;
        System.out.println(i + "-" + j);
      }
    }

    int x = 0;
    do { x++; } while (x < 3);
    System.out.println("do " + x);

    int[] vals = {1, 2, 3, 1, 4};
    for (int v : vals) {
      switch (v) {
        case 1:
        case 2:
          System.out.println("low " + v);
          break;
        case 3:
          System.out.println("three");
        case 4:
          System.out.println("four-or-three " + v);
          break;
        default:
          System.out.println("other " + v);
      }
    }

    int n = 0;
    while (true) {
      n++;
      if (n > 2) break;
    }
    System.out.println("while " + n);

    int y = 5;
    label:
    {
      if (y > 1) break label;
      System.out.println("never printed");
    }
    System.out.println("after label " + y);

    search:
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (i * j == 6) {
          System.out.println("found " + i + "," + j);
          break search;
        }
      }
    }
  }
}
