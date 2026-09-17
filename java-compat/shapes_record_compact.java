public class record_compact {
    record Temperature(double celsius) {
        Temperature {
            if (celsius < -273.15) {
                throw new IllegalArgumentException("below absolute zero");
            }
        }

        Temperature(double celsius, boolean fahrenheit) {
            this(fahrenheit ? (celsius - 32.0) * 5.0 / 9.0 : celsius);
        }

        double fahrenheit() {
            return celsius * 9.0 / 5.0 + 32.0;
        }

        static Temperature freezing() {
            return new Temperature(0.0);
        }

        boolean colderThan(Temperature other) {
            return celsius < other.celsius;
        }
    }

    record Rgb(int r, int g, int b) {
        static final String[] HEX = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

        Rgb {
            r = clamp(r);
            g = clamp(g);
            b = clamp(b);
        }

        static int clamp(int v) {
            if (v < 0) {
                return 0;
            }
            return v > 255 ? 255 : v;
        }

        Rgb scaled(int numerator, int denominator) {
            return new Rgb(r * numerator / denominator, g * numerator / denominator, b * numerator / denominator);
        }

        String hex() {
            String out = "#";
            int[] parts = {r, g, b};
            for (int v : parts) {
                out = out + HEX[v / 16] + HEX[v % 16];
            }
            return out;
        }
    }

    public static void main(String[] args) {
        Temperature t = new Temperature(100.0);
        System.out.println(t);
        System.out.println(t.celsius());
        System.out.println(t.fahrenheit());

        Temperature body = new Temperature(98.6, true);
        System.out.println(body.celsius());
        System.out.println(Temperature.freezing());
        System.out.println(Temperature.freezing().colderThan(body));

        System.out.println(t.equals(new Temperature(100.0)));
        System.out.println(t.equals(body));
        System.out.println(!t.equals(Temperature.freezing()));

        Rgb raw = new Rgb(300, -5, 128);
        System.out.println(raw);
        System.out.println(raw.r());
        System.out.println(raw.g());
        System.out.println(raw.b());
        System.out.println(raw.hex());
        System.out.println(raw.scaled(1, 2));
        System.out.println(raw.equals(new Rgb(255, 0, 128)));
        System.out.println(raw.equals(raw.scaled(1, 2)));
        System.out.println(new Rgb(255, 255, 255).hex());

        try {
            new Temperature(-300.0);
            System.out.println("no throw");
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }
    }
}
