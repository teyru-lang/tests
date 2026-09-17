import java.util.ArrayList;
import java.util.List;

public class generic_methods {
    interface Labelled {
        String label();
    }

    static class Item implements Labelled {
        private final String name;
        private final int weight;
        Item(String name, int weight) { this.name = name; this.weight = weight; }
        public String label() { return name; }
        int weight() { return weight; }
    }

    static <T extends Number> double sumDoubled(List<T> xs) {
        double total = 0;
        for (T x : xs) total += x.doubleValue();
        return total * 2;
    }

    static <T extends Labelled> String joinLabels(List<T> xs) {
        StringBuilder sb = new StringBuilder();
        for (T x : xs) sb.append(x.label()).append(";");
        return sb.toString();
    }

    static double sumAll(List<? extends Number> xs) {
        double total = 0;
        for (Number n : xs) total += n.doubleValue();
        return total;
    }

    static void fillWithInts(List<? super Integer> xs, int howMany) {
        for (int i = 0; i < howMany; i++) xs.add(i * i);
    }

    static <T> List<T> repeat(T value, int times) {
        List<T> out = new ArrayList<>();
        for (int i = 0; i < times; i++) out.add(value);
        return out;
    }

    public static void main(String[] args) {
        System.out.println("sumDoubled " + sumDoubled(List.of(1, 2, 3)));
        System.out.println("sumDoubledD " + sumDoubled(List.of(1.5, 2.5)));

        List<Item> items = List.of(new Item("ax", 3), new Item("bee", 5));
        System.out.println("joinLabels " + joinLabels(items));

        System.out.println("sumAll " + sumAll(List.of(1, 2, 3, 4)));
        System.out.println("sumAllD " + sumAll(List.of(0.5, 0.25)));

        List<Number> bag = new ArrayList<>();
        fillWithInts(bag, 4);
        System.out.println("bag " + bag);

        List<Integer> intsOnly = new ArrayList<>();
        fillWithInts(intsOnly, 3);
        System.out.println("intsOnly " + intsOnly);

        System.out.println("repeat " + repeat("ha", 3));
        System.out.println("repeatInt " + repeat(7, 2));
    }
}
