public class interface_hierarchy {
    interface Base {
        String VERSION = "1.0";
        int LIMIT = 100;

        String name();

        default String describe() {
            return "Base[" + name() + "]";
        }

        default String twice() {
            return describe() + describe();
        }

        static String banner() {
            return "== teyru " + VERSION + " ==";
        }
    }

    interface Left extends Base {
        default String side() {
            return "left";
        }
    }

    interface Right extends Base {
        default String side() {
            return "right";
        }
    }

    static class Both implements Left, Right {
        @Override
        public String name() {
            return "both";
        }

        @Override
        public String side() {
            return Left.super.side() + "+" + Right.super.side();
        }
    }

    interface Counting extends Base {
        int count();

        @Override
        default String describe() {
            return "Counting(" + count() + ")";
        }
    }

    abstract static class Partial implements Counting {
        private final String label;

        Partial(String label) {
            this.label = label;
        }

        @Override
        public String name() {
            return label;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + ":" + name();
        }
    }

    static class Complete extends Partial {
        private final int total;

        Complete(String label, int total) {
            super(label);
            this.total = total;
        }

        @Override
        public int count() {
            return total;
        }
    }

    public static void main(String[] args) {
        Both both = new Both();
        System.out.println(both.name());
        System.out.println(both.describe());
        System.out.println(both.twice());
        System.out.println(both.side());
        System.out.println(Base.banner());
        System.out.println(Base.VERSION + "/" + Base.LIMIT);

        Counting c = new Complete("full", 5);
        System.out.println(c.describe());
        System.out.println(c.name());
        System.out.println(c.count());
        System.out.println(c);

        Left left = new Left() {
            @Override
            public String name() {
                return "anon";
            }
        };
        System.out.println(left.describe());
        System.out.println(left.side());

        Base[] all = {both, c, left};
        for (Base b : all) {
            System.out.println(b.describe() + " twice=" + b.twice());
        }
    }
}
