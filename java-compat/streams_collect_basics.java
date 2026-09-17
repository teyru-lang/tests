import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class collect_basics {
    public static void main(String[] args) {
        List<String> words = List.of("apple", "fig", "kiwi", "pear", "fig");

        List<String> asList = words.stream().collect(Collectors.toList());
        System.out.println("toList " + asList);

        Set<String> asSet = words.stream().collect(Collectors.toSet());
        System.out.println("toSet " + new TreeSet<>(asSet));

        String joined = words.stream().collect(Collectors.joining(", "));
        System.out.println("joining " + joined);

        String joined2 = words.stream().collect(Collectors.joining("-", "[", "]"));
        System.out.println("joining3 " + joined2);

        long count = words.stream().collect(Collectors.counting());
        System.out.println("counting " + count);

        int totalLetters = words.stream().collect(Collectors.summingInt(w -> w.length()));
        System.out.println("summingInt " + totalLetters);

        double avgLetters = words.stream().collect(Collectors.averagingInt(w -> w.length()));
        System.out.println("averagingInt " + avgLetters);
    }
}
