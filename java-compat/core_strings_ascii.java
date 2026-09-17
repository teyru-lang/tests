public class strings_ascii {
  public static void main(String[] args) {
    String s = "Hello World";
    System.out.println(s.toUpperCase());
    System.out.println(s.toLowerCase());
    String t = "  padded  ";
    System.out.println("[" + t.trim() + "]");
    System.out.println(t.trim().length());
    System.out.println(s.startsWith("Hello") + " " + s.startsWith("World") + " " + s.endsWith("World"));
    System.out.println(s.equals("Hello World") + " " + s.equalsIgnoreCase("HELLO WORLD"));
    System.out.println("".isEmpty() + " " + s.isEmpty());
    System.out.println(s.contains("lo W") + " " + s.contains("xyz"));
    System.out.println(s.replace("World", "Java"));
    System.out.println(s.replace('l', 'L'));
    System.out.println(s.substring(6) + "|" + s.substring(0, 5));
    System.out.println(("apple".compareTo("banana") < 0) + " " + ("b".compareTo("b") == 0) + " " + ("cherry".compareTo("apple") > 0));
    System.out.println(s.charAt(0) + "" + s.charAt(4));
    char[] cs = "abc".toCharArray();
    System.out.println(cs.length + " " + cs[2]);
    for (char c : "loop".toCharArray()) System.out.print(c + "-");
    System.out.println();
    String u = "aaa".concat("bbb");
    System.out.println(u + " " + u.indexOf('b'));
    System.out.println("Repeat".repeat(3));
    System.out.println("  x  ".strip());
    System.out.println("a-b-c".replace("-", "+"));
    System.out.println("MixedCase".toUpperCase().toLowerCase().length());
    System.out.println(s.lastIndexOf('o') + " " + s.indexOf('o'));
    System.out.println("abc".equals("abc") + " " + "ABC".equals("abc"));
    System.out.println("pad".isEmpty() + " " + "x".isEmpty());
    System.out.println("Trailing   ".trim() + "!");
  }
}
