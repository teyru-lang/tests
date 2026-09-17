import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class generic_class_collections {
    static class Box<T> {
        private T value;
        Box(T value) { this.value = value; }
        T get() { return value; }
        void set(T value) { this.value = value; }
    }

    static class Pair<A, B> {
        private final A first;
        private final B second;
        Pair(A first, B second) { this.first = first; this.second = second; }
        A first() { return first; }
        B second() { return second; }
        Pair<B, A> swap() { return new Pair<>(second, first); }
    }

    static <T> String describe(Box<T> box) {
        return "box[" + box.get() + "]";
    }

    public static void main(String[] args) {
        Box<String> sb = new Box<>("text");
        Box<Integer> ib = new Box<>(41);
        System.out.println(describe(sb));
        System.out.println(describe(ib));
        sb.set("changed");
        System.out.println(describe(sb));

        Pair<String, Integer> p = new Pair<>("age", 30);
        System.out.println(p.first() + "=" + p.second());
        Pair<Integer, String> swapped = p.swap();
        System.out.println(swapped.first() + "=" + swapped.second());

        List<Integer> nums = new ArrayList<>(List.of(5, 1, 4, 2, 3));
        Collections.sort(nums);
        System.out.println("sorted " + nums);
        Collections.reverse(nums);
        System.out.println("reversed " + nums);
        System.out.println("max " + Collections.max(nums));
        System.out.println("min " + Collections.min(nums));

        List<String> words = new ArrayList<>(List.of("pear", "fig", "apple"));
        Collections.sort(words);
        System.out.println("wordsSorted " + words);
        System.out.println("wordsMax " + Collections.max(words));
        Collections.reverse(words);
        System.out.println("wordsReversed " + words);
    }
}
