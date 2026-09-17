import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class grouping_by {
    public static void main(String[] args) {
        List<String> words = List.of("apple", "fig", "kiwi", "pear", "plum", "ox");

        Map<Integer, List<String>> byLength =
                words.stream().collect(Collectors.groupingBy(w -> w.length()));
        for (Map.Entry<Integer, List<String>> e : new TreeMap<Integer, List<String>>(byLength).entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }

        Map<Integer, Long> counts =
                words.stream().collect(Collectors.groupingBy(w -> w.length(), Collectors.counting()));
        for (Map.Entry<Integer, Long> e : new TreeMap<Integer, Long>(counts).entrySet()) {
            System.out.println("count " + e.getKey() + " -> " + e.getValue());
        }

        Map<String, List<String>> parts = words.stream()
                .collect(Collectors.groupingBy(w -> w.length() % 2 == 0 ? "even" : "odd"));
        for (Map.Entry<String, List<String>> e : new TreeMap<String, List<String>>(parts).entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}
