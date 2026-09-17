public class one_line_stmts {
  public static void main(String[] args) {
    int a = 1; int b = 2; int c = a + b; System.out.println(c);
    int d = 10; d += 5; d -= 3; d *= 2; System.out.println(d);
    String s = "ab"; s = s + "c"; System.out.println(s); System.out.println(s.toUpperCase());
    for (int i = 0; i < 3; i++) { System.out.println("i" + i); }
    int[] xs = {1, 2, 3}; int sum = 0; for (int x : xs) sum += x; System.out.println(sum);
    boolean flag = true; if (flag) System.out.println("yes"); else System.out.println("no");
    int p = 0; p++; ++p; p--; System.out.println(p);
    int q = 7; int r = q > 3 ? q * 2 : q / 2; System.out.println(r);
    System.out.println("one"); System.out.println("two"); System.out.println("three");
    int m = 0; while (m < 3) { m++; } System.out.println(m);
  }
}
