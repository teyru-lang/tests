import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class int_stream_basics {
    public static void main(String[] args) {
        System.out.println("range " + IntStream.range(1, 6).sum());
        System.out.println("rangeClosed " + IntStream.rangeClosed(1, 6).sum());
        System.out.println("rangeCount " + IntStream.range(1, 6).count());

        OptionalInt max = IntStream.rangeClosed(3, 8).max();
        System.out.println("max " + max.getAsInt());
        OptionalInt min = IntStream.of(9, 4, 6).min();
        System.out.println("minOf " + min.getAsInt());

        OptionalDouble avg = IntStream.rangeClosed(1, 4).average();
        System.out.println("average " + avg.getAsDouble());

        int[] array = IntStream.rangeClosed(1, 4).toArray();
        int total = 0;
        for (int v : array) total += v;
        System.out.println("toArraySum " + total);

        System.out.println("filteredSum " + IntStream.rangeClosed(1, 10).filter(i -> i % 2 == 0).sum());
        System.out.println("mappedSum " + IntStream.rangeClosed(1, 4).map(i -> i * i).sum());
    }
}
