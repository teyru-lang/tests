public class numbers_casts {
  public static void main(String[] args) {
    byte b = 100;
    short s = b;
    int i = s;
    long l = i;
    float f = l;
    double d = f;
    System.out.println(b + " " + s + " " + i + " " + l + " " + f + " " + d);

    double dd = 123.75;
    int t = (int) dd;
    long tt = (long) dd;
    float tf = (float) dd;
    System.out.println(t + " " + tt + " " + tf);

    long big = 300L;
    byte nb = (byte) big;
    short ns = (short) big;
    System.out.println(nb + " " + ns);

    System.out.println(7 / 2 + " " + (-7) / 2 + " " + 7 % 2 + " " + (-7) % 2 + " " + 7 % -2);
    System.out.println(-7 / -2 + " " + (-7) % -2);
    System.out.println(6 / 3 + " " + (-6) / 3 + " " + 6 % -3);

    System.out.println(1 << 33);
    System.out.println(-1 >>> 28);
    System.out.println(5 >> 1);
    System.out.println(-8 >> 1);
    System.out.println(3 << 2);

    System.out.println(Integer.MIN_VALUE + " " + Integer.MAX_VALUE);
    System.out.println(Long.MIN_VALUE + " " + Long.MAX_VALUE);
    System.out.println(Byte.MIN_VALUE + " " + Byte.MAX_VALUE + " " + Short.MAX_VALUE);
    System.out.println(Integer.MIN_VALUE - 1 == Integer.MAX_VALUE);

    char c = 'A';
    int ci = c + 1;
    char c2 = (char) ci;
    System.out.println(ci + " " + c2);

    System.out.println(1 + 2 + "x" + 3 + 4);
    System.out.println((int) 'a' + " " + (int) 'z');

    float fsum = 0.5f + 0.25f;
    System.out.println(fsum);
    System.out.println(1.0f / 4);

    long mix = 5 + 10L;
    System.out.println(mix);
    System.out.println((byte) (b + 1));
    System.out.println(2147483647 + 1 == Integer.MIN_VALUE);
  }
}
