public class enum_basics {
    enum Planet {
        MERCURY(3.303e+23, 2.4397e6),
        EARTH(5.976e+24, 6.37814e6),
        MARS(6.421e+23, 3.3972e6);

        private final double mass;
        private final double radius;

        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
        }

        double surfaceGravity() {
            return 6.67300E-11 * mass / (radius * radius);
        }

        double surfaceWeight(double otherMass) {
            return otherMass * surfaceGravity();
        }
    }

    enum Op {
        ADD("+") {
            @Override
            int apply(int a, int b) {
                return a + b;
            }
        },
        SUB("-") {
            @Override
            int apply(int a, int b) {
                return a - b;
            }
        },
        MUL("*") {
            @Override
            int apply(int a, int b) {
                return a * b;
            }
        };

        private final String symbol;

        Op(String symbol) {
            this.symbol = symbol;
        }

        String symbol() {
            return symbol;
        }

        abstract int apply(int a, int b);
    }

    enum Level {
        LOW,
        MEDIUM,
        HIGH
    }

    static String classify(Level level) {
        switch (level) {
            case LOW:
                return "low priority";
            case MEDIUM:
                return "medium priority";
            case HIGH:
                return "high priority";
            default:
                return "unknown";
        }
    }

    public static void main(String[] args) {
        for (Planet p : Planet.values()) {
            System.out.println(p.name() + " ordinal=" + p.ordinal() + " weight=" + p.surfaceWeight(75.0));
        }

        System.out.println(Planet.valueOf("MARS").name());
        System.out.println(Planet.EARTH == Planet.valueOf("EARTH"));
        System.out.println(Planet.values().length);

        for (Op op : Op.values()) {
            System.out.println(op.name() + " " + op.symbol() + " 6,7 -> " + op.apply(6, 7));
        }
        System.out.println(Op.ADD.apply(2, 3) + Op.MUL.apply(2, 3));

        for (Level l : Level.values()) {
            System.out.println(l + ": " + classify(l));
        }

        System.out.println(Level.HIGH.compareTo(Level.LOW));
        System.out.println(Level.HIGH.ordinal() > Level.LOW.ordinal());
    }
}
