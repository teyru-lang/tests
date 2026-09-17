import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class int_stream_objects {
    public static void main(String[] args) {
        IntFunction<String> label = i -> "n" + i;
        List<String> labels = IntStream.range(0, 4).mapToObj(label).collect(Collectors.toList());
        System.out.println("mapToObj " + labels);

        List<Integer> boxed = IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());
        System.out.println("boxed " + boxed);

        IntSummaryStatistics stats = IntStream.rangeClosed(1, 10).summaryStatistics();
        System.out.println("count " + stats.getCount());
        System.out.println("sum " + stats.getSum());
        System.out.println("min " + stats.getMin());
        System.out.println("max " + stats.getMax());
        System.out.println("average " + stats.getAverage());

        List<Integer> evens = IntStream.rangeClosed(1, 10).filter(i -> i % 2 == 0).boxed().collect(Collectors.toList());
        System.out.println("evens " + evens);
    }
}
