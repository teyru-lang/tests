import java.util.Arrays;
import java.util.List;

public class arrays_util {
  public static void main(String[] args) {
    int[] a = {5, 3, 9, 1, 7};
    System.out.println(Arrays.toString(a));
    Arrays.sort(a);
    System.out.println(Arrays.toString(a));

    int[] b = new int[5];
    Arrays.fill(b, 4);
    System.out.println(Arrays.toString(b));
    System.out.println(Arrays.toString(b));

    int[] c = Arrays.copyOf(a, 3);
    System.out.println(Arrays.toString(c));
    int[] d = Arrays.copyOfRange(a, 1, 3);
    System.out.println(Arrays.toString(d));
    System.out.println(Arrays.equals(a, a) + " " + Arrays.equals(a, b));

    int[] src = {1, 2, 3, 4, 5};
    int[] dst = new int[5];
    System.arraycopy(src, 1, dst, 0, 3);
    System.out.println(Arrays.toString(dst));
    System.arraycopy(src, 0, src, 1, 4);
    System.out.println(Arrays.toString(src));

    String[] s = {"pear", "apple", "fig"};
    Arrays.sort(s);
    System.out.println(Arrays.toString(s));

    List<String> list = Arrays.asList("a", "b", "c");
    System.out.println(list.size() + " " + list.get(1) + " " + list);
    System.out.println(list.contains("b") + " " + list.indexOf("c"));

    Integer[] boxed = {3, 1, 2};
    Arrays.sort(boxed);
    System.out.println(Arrays.toString(boxed));

    System.out.println(Arrays.binarySearch(new int[] {1, 3, 5, 7}, 5));
    System.out.println(Arrays.binarySearch(new int[] {1, 3, 5, 7}, 4));

    char[] chars = {'d', 'a', 'c'};
    Arrays.sort(chars);
    System.out.println(Arrays.toString(chars));
    System.out.println(Arrays.hashCode(new int[] {1, 2, 3}) != 0);
  }
}
