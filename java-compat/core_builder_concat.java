public class builder_concat {
  public static void main(String[] args) {
    StringBuilder sb = new StringBuilder();
    sb.append("Hello");
    sb.append(", ");
    sb.append("World");
    sb.append('!');
    System.out.println(sb.toString());
    System.out.println(sb.length());

    StringBuilder sb2 = new StringBuilder("abc");
    sb2.append(123);
    sb2.append(true);
    sb2.append(2.5);
    sb2.append('z');
    System.out.println(sb2.toString());
    sb2.reverse();
    System.out.println(sb2.toString());

    StringBuilder sb3 = new StringBuilder();
    for (int i = 0; i < 5; i++) { sb3.append(i); if (i < 4) sb3.append(','); }
    System.out.println(sb3.toString());

    StringBuilder sb4 = new StringBuilder(64);
    sb4.append("cap");
    System.out.println(sb4.toString() + " " + (sb4.capacity() >= 64));

    StringBuilder sb5 = new StringBuilder("hello");
    sb5.setCharAt(0, 'H');
    sb5.insert(5, " there");
    System.out.println(sb5.toString());
    sb5.deleteCharAt(5);
    System.out.println(sb5.toString());
    System.out.println(sb5.charAt(0) + "" + sb5.length());

    String a = "num=" + 42;
    String b = 42 + "=num";
    System.out.println(a + " " + b);
    System.out.println("sum " + (3 + 4));
    System.out.println("" + 'x' + 'y');
    System.out.println('x' + 'y' + "");
    System.out.println("" + 1 + 2);
    System.out.println(1 + 2 + "");
    System.out.println("bool " + (1 < 2));
    System.out.println("char " + 'A');
    Object o = "obj";
    System.out.println("o=" + o);
    char c = 'q';
    System.out.println("c=" + c + c);
    System.out.println("a" + "b" + "c" + "d");
  }
}
