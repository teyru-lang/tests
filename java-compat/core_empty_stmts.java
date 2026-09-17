public class empty_stmts {
  public static void main(String[] args) {
    ;
    int x = 5;
    if (x > 3) ; else System.out.println("small");
    if (x < 3) ; else System.out.println("not small");
    for (int i = 0; i < 3; i++) ;
    System.out.println("done");
    { ; ; }
    int y = 0;
    ;
    for (int i = 0; i < 4; i++) {
      ;
      y++;
      ;
    }
    System.out.println(y);
    int z = 0;
    do ; while (false);
    System.out.println(z);
    ;
  }
}
