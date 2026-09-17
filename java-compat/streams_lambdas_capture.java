import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class lambdas_capture {
    static int callCount = 0;
    static final String PREFIX = "v-";

    public static void main(String[] args) {
        int base = 10;
        final String suffix = "!";
        List<Integer> nums = List.of(1, 2, 3, 4);

        List<Integer> added = nums.stream().map(n -> n + base).collect(Collectors.toList());
        System.out.println("captureLocal " + added);

        List<String> tagged = nums.stream().map(n -> PREFIX + n + suffix).collect(Collectors.toList());
        System.out.println("captureStatic " + tagged);

        IntUnaryOperator scale = n -> {
            int doubled = n * 2;
            int bumped = doubled + base;
            return bumped;
        };
        System.out.println("blockLambda " + scale.applyAsInt(5));

        IntBinaryOperator combine = (a, b) -> {
            callCount++;
            if (a > b) {
                return a - b;
            } else {
                return b - a;
            }
        };
        System.out.println("combine " + combine.applyAsInt(9, 4));
        System.out.println("combine " + combine.applyAsInt(4, 9));
        System.out.println("callCount " + callCount);

        IntSupplier gen = () -> {
            callCount = callCount + 1;
            return callCount * 100;
        };
        System.out.println("gen " + gen.getAsInt());
        System.out.println("gen " + gen.getAsInt());

        Supplier<List<String>> makeList = () -> {
            List<String> out = new ArrayList<>();
            for (String w : List.of("a", "b")) {
                out.add(w + base);
            }
            return out;
        };
        System.out.println("makeList " + makeList.get());
        System.out.println("finalCallCount " + callCount);
    }
}
