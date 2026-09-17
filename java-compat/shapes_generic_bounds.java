public class generic_bounds {
    interface Container<T> {
        T get();

        default String describe() {
            return "container of " + get();
        }
    }

    static class Box<T> implements Container<T> {
        private final T value;

        Box(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public String toString() {
            return "Box(" + value + ")";
        }
    }

    static class Numeric<T extends Number & Comparable<T>> {
        private final T left;
        private final T right;

        Numeric(T left, T right) {
            this.left = left;
            this.right = right;
        }

        T bigger() {
            return left.compareTo(right) >= 0 ? left : right;
        }

        double ratio() {
            return left.doubleValue() / right.doubleValue();
        }
    }

    static <T extends Comparable<T>> T maxOf(T[] values) {
        T best = values[0];
        for (T v : values) {
            if (v.compareTo(best) > 0) {
                best = v;
            }
        }
        return best;
    }

    static double sumOf(Container<? extends Number> container) {
        return container.get().doubleValue();
    }

    static String describeAll(Container<?> first, Container<?> second) {
        return first.describe() + " / " + second.describe();
    }

    static <T> T pickFirst(T[] values) {
        return values[0];
    }

    static String pairToString(Object a, Object b) {
        return a + "&" + b;
    }

    public static void main(String[] args) {
        Box<String> box = new Box<>("hello");
        System.out.println(box.get());
        System.out.println(box);
        System.out.println(box.describe());

        Container<Integer> count = new Box<>(Integer.valueOf(7));
        System.out.println(count.get() + 1);

        Numeric<Integer> nums = new Numeric<>(Integer.valueOf(3), Integer.valueOf(9));
        System.out.println(nums.bigger());
        System.out.println(nums.ratio());

        Numeric<Double> doubles = new Numeric<>(2.5, 0.5);
        System.out.println(doubles.bigger());
        System.out.println(doubles.ratio());

        System.out.println(maxOf(new String[] {"pear", "apple", "zebra"}));
        System.out.println(maxOf(new Integer[] {Integer.valueOf(4), Integer.valueOf(11), Integer.valueOf(6)}));

        System.out.println(sumOf(count));
        System.out.println(sumOf(new Box<Double>(Double.valueOf(2.5))));
        System.out.println(describeAll(box, count));
        System.out.println(pickFirst(new String[] {"a", "b"}));
        System.out.println(pickFirst(new int[][] {{1, 2}, {3, 4}}).length);
        System.out.println(pairToString(box, count));
    }
}
