import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class comparator_ops {
    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>(List.of(7, 2, 9, 4, 2));
        nums.sort(Comparator.naturalOrder());
        System.out.println("natural " + nums);
        nums.sort(Comparator.reverseOrder());
        System.out.println("reverse " + nums);

        List<String> words = new ArrayList<>(List.of("pear", "fig", "apple", "kiwi"));
        words.sort(Comparator.comparing(String::length));
        System.out.println("byLen " + words);
        words.sort(Comparator.comparing(String::length).thenComparing(w -> w));
        System.out.println("byLenThenWord " + words);
        words.sort(Comparator.comparing(String::length).reversed().thenComparing(w -> w));
        System.out.println("lenDescThenWord " + words);

        List<String> copy = new ArrayList<>(List.of("b", "d", "a", "c"));
        copy.sort(Comparator.naturalOrder());
        System.out.println("sorted " + copy);
        System.out.println("min " + copy.get(0) + " max " + copy.get(copy.size() - 1));
    }
}
