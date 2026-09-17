import java.util.Arrays;

public class strings_split {
  public static void main(String[] args) {
    String csv = "a,b,c";
    String[] parts = csv.split(",");
    System.out.println(parts.length);
    for (String p : parts) System.out.println(p);

    String[] limited = csv.split(",", 2);
    System.out.println(limited.length + " " + limited[0] + " " + limited[1]);
    String[] limited1 = csv.split(",", 1);
    System.out.println(limited1.length + " " + limited1[0]);

    System.out.println(Arrays.toString("a,b,,".split(",", -1)));
    System.out.println(Arrays.toString("a,b,,".split(",")));
    System.out.println("a,b,,".split(",").length);
    System.out.println(Arrays.toString(",a".split(",")));
    System.out.println(",a".split(",").length);

    String[] ws = "the quick brown fox".split(" ");
    System.out.println(ws.length + " " + Arrays.toString(ws));
    System.out.println(Arrays.toString("1:2::3".split(":")));
    System.out.println(Arrays.toString("x1y22z".split("[0-9]+")));
    System.out.println(Arrays.toString("one.two.three".split("\\.")));
    System.out.println("a,b,c".split(",").length + " " + "abc".split(",").length);

    String[] e = "".split(",");
    System.out.println(e.length + " " + e[0].isEmpty());

    System.out.println("a b  c".split(" +").length);
  }
}
