public class nested_classes {
    static class StaticNested {
        private final int value;

        StaticNested(int value) {
            this.value = value;
        }

        int value() {
            return value;
        }

        String describe() {
            return "static nested " + value;
        }
    }

    private int outerField = 7;

    class Inner {
        private final int value;

        Inner(int value) {
            this.value = value;
        }

        int sum() {
            return outerField + value;
        }

        int outerFieldOf(nested_classes other) {
            return other.outerField;
        }

        String describe() {
            return "inner " + value + " of " + outerField;
        }
    }

    static StaticNested makeStatic() {
        return new StaticNested(41);
    }

    int runLocalClass(int base) {
        class Accumulator {
            private int total = 0;

            void add(int v) {
                total = total + v;
            }

            int total() {
                return total + base;
            }

            String label() {
                return "accumulator(" + total + ")";
            }
        }

        Accumulator acc = new Accumulator();
        for (int i = 1; i <= 4; i++) {
            acc.add(i);
        }
        System.out.println(acc.label());
        return acc.total();
    }

    public static void main(String[] args) {
        StaticNested sn = new StaticNested(5);
        System.out.println(sn.value());
        System.out.println(sn.describe());
        System.out.println(new StaticNested(6).describe());
        System.out.println(nested_classes.makeStatic().describe());

        nested_classes outer = new nested_classes();
        Inner inner = outer.new Inner(10);
        System.out.println(inner.sum());
        System.out.println(inner.describe());

        nested_classes other = new nested_classes();
        other.outerField = 100;
        System.out.println(inner.outerFieldOf(other));

        System.out.println(outer.runLocalClass(1000));
        System.out.println(other.runLocalClass(0));

        StaticNested[] arr = {new StaticNested(1), new StaticNested(2)};
        int total = 0;
        for (StaticNested s : arr) {
            total = total + s.value();
        }
        System.out.println(total);
    }
}
