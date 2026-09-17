public class varargs_generics {
    static int sum(int... values) {
        int total = 0;
        for (int v : values) {
            total = total + v;
        }
        return total;
    }

    static long sum(long... values) {
        long total = 0L;
        for (long v : values) {
            total = total + v;
        }
        return total;
    }

    static String join(String separator, String... parts) {
        String out = "";
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                out = out + separator;
            }
            out = out + parts[i];
        }
        return out;
    }

    static String joinAll(String... parts) {
        return join("-", parts);
    }

    static String describe(int first, String... rest) {
        return "first=" + first + " rest=" + rest.length + " [" + join(",", rest) + "]";
    }

    static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static <T extends Number> double average(T[] values) {
        double total = 0;
        for (T v : values) {
            total = total + v.doubleValue();
        }
        return total / values.length;
    }

    static class Pair<K, V> {
        private final K key;
        private final V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        K key() {
            return key;
        }

        V value() {
            return value;
        }

        Pair<V, K> swap() {
            return new Pair<>(value, key);
        }

        @Override
        public String toString() {
            return "Pair(" + key + " => " + value + ")";
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Pair<?, ?> p)) {
                return false;
            }
            return key.equals(p.key) && value.equals(p.value);
        }

        @Override
        public int hashCode() {
            return 31 * key.hashCode() + value.hashCode();
        }
    }

    static class Point3 {
        final int x;
        final int y;

        Point3(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point3(" + x + "," + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point3 p)) {
                return false;
            }
            return p.x == x && p.y == y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }

    public static void main(String[] args) {
        System.out.println(sum(1, 2, 3));
        System.out.println(sum(new int[] {4, 5, 6}));
        int[] arr = {7, 8};
        System.out.println(sum(arr));
        System.out.println(sum(1L, 2L, 3L));
        System.out.println(sum(new long[] {10L, 20L}));

        System.out.println(joinAll("a", "b", "c"));
        System.out.println(joinAll(new String[] {"p", "q"}));
        System.out.println(join("-", "a", "b", "c"));
        System.out.println(join(",", new String[] {"x", "y", "z"}));
        System.out.println(describe(1));
        System.out.println(describe(1, "p", "q"));

        System.out.println(max("apple", "pear"));
        System.out.println(max(Integer.valueOf(3), Integer.valueOf(9)));
        System.out.println(max(2.5, 2.25));

        System.out.println(average(new Integer[] {2, 4, 6, 8}));
        System.out.println(average(new Double[] {1.5, 2.5}));

        Pair<String, Integer> p = new Pair<>("one", Integer.valueOf(1));
        System.out.println(p);
        System.out.println(p.key() + "/" + p.value());
        System.out.println(p.swap());
        System.out.println(p.equals(new Pair<>("one", Integer.valueOf(1))));
        System.out.println(p.equals(p.swap()));
        System.out.println(p.hashCode());

        Point3 pt = new Point3(3, 4);
        System.out.println(pt);
        System.out.println(pt.equals(new Point3(3, 4)));
        System.out.println(pt.equals(new Point3(4, 3)));
        System.out.println(pt.hashCode());
    }
}
