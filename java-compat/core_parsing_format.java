public class parsing_format {
  public static void main(String[] args) {
    int i = Integer.parseInt("-123");
    int j = Integer.parseInt("ff", 16);
    long l = Long.parseLong("9000000000");
    double d = Double.parseDouble("2.5");
    System.out.println(i + " " + j + " " + l + " " + d);
    System.out.println(Integer.parseInt("007") + 1);
    System.out.println(Double.parseDouble("-0.5"));
    System.out.println(Long.parseLong("-42"));
    System.out.println(Integer.parseInt("+19"));

    System.out.println(Integer.toBinaryString(10));
    System.out.println(Integer.toBinaryString(-1));
    System.out.println(Integer.toHexString(255));
    System.out.println(Integer.toHexString(-1));
    System.out.println(Long.toBinaryString(5L));
    System.out.println(Integer.toOctalString(64));
    System.out.println(Long.toHexString(255L));

    System.out.println(Integer.compare(3, 7) + " " + Integer.compare(7, 7) + " " + Integer.compare(9, 2));
    System.out.println(Integer.bitCount(255) + " " + Integer.bitCount(7) + " " + Long.bitCount(255L));
    System.out.println(Integer.toString(42) + " " + Integer.toString(42, 16));
    System.out.println(Integer.valueOf("17") + 3);
    System.out.println(Integer.max(2, 9) + " " + Integer.min(2, 9));
    System.out.println(Integer.sum(3, 4) + " " + Long.sum(3L, 4L));

    try { Integer.parseInt("nope"); } catch (NumberFormatException e) { System.out.println("bad number"); }

    int n = 42;
    String s = "code";
    System.out.printf("n=%d s=%s%n", n, s);
    System.out.printf("%d and %s and %f%n", n, s, 2.5);
    System.out.printf("[%5.2f] [%5.2f] [%5.2f]%n", 3.5, -12.25, 0.5);
    System.out.printf("[%5d] [%-5d] [%05d]%n", 42, 42, 42);
    System.out.println(String.format("%s=%d (%s)", "answer", 42, "ok"));
    System.out.println(String.format("%d/%d/%d", 2026, 9, 17));
    System.out.println(String.format("%.1f", 100.0));
    System.out.println(String.format("%s %s %s", 1, 2.5, true));
    System.out.println(String.format("%c%c%c", 'a', 'b', 'c'));
    System.out.println(String.format("%b %b", true, false));
    System.out.println(String.format("%x %o", 255, 8));

    System.out.println("x" + 1 + 2 + 3);
    System.out.println(1 + 2 + "x" + 3);
    System.out.println("v=" + (1 + 2));
  }
}
