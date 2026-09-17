public class for_forms {
  public static void main(String[] args) {
    int n = 4;
    int total = 0;
    for (int i = 0; i < n; i++) total += i;
    System.out.println(total);

    int k = 0;
    for (;;) {
      k++;
      if (k == 5) break;
    }
    System.out.println(k);

    int acc = 0;
    for (int i = 0, j = 5; i < j; i++, j--) acc += i * 10 + j;
    System.out.println(acc);

    int i = 0, j = 1;
    for (i = 0, j = 4; i < j; i++, j--) System.out.println(i + ":" + j);

    for (int a = 0; a < 3; a++)
      for (int b = 0; b < 3; b++)
        if (b == 1) continue; else System.out.println(a + "," + b);

    int m = 0;
    for (; m < 3;) m++;
    System.out.println(m);

    for (int a = 10; a > 0; a -= 3) System.out.println(a);

    int outer = 0;
    for (int a = 0; a < 3; a++) { for (int b = 0; b < 2; b++) { outer += a * b; } }
    System.out.println(outer);
  }
}
