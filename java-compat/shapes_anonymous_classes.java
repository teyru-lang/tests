public class anonymous_classes {
    interface Transformer {
        String apply(String input);
    }

    abstract static class Formatter {
        private final String tag;

        Formatter(String tag) {
            this.tag = tag;
        }

        String tag() {
            return tag;
        }

        abstract String body(String input);

        String render(String input) {
            return "[" + tag + "] " + body(input);
        }
    }

    static String useTransformer(Transformer t, String input) {
        return t.apply(input);
    }

    static String useFormatter(Formatter f, String input) {
        return f.render(input);
    }

    public static void main(String[] args) {
        Transformer exclaim = new Transformer() {
            @Override
            public String apply(String input) {
                return input + "!";
            }
        };
        System.out.println(exclaim.apply("hi"));

        final int factor = 3;
        Transformer repeat = new Transformer() {
            private int calls = 0;

            @Override
            public String apply(String input) {
                calls = calls + 1;
                return input + "/" + calls + "/" + factor;
            }
        };
        System.out.println(repeat.apply("a"));
        System.out.println(repeat.apply("b"));

        Formatter upper = new Formatter("up") {
            @Override
            String body(String input) {
                return input + " processed by " + tag();
            }
        };
        System.out.println(upper.render("data"));

        System.out.println(useTransformer(new Transformer() {
            @Override
            public String apply(String input) {
                return "<" + input + ">";
            }
        }, "wrapped"));

        System.out.println(useFormatter(new Formatter("inline") {
            @Override
            String body(String input) {
                return input + input;
            }
        }, "xy"));

        Transformer[] chain = {
            new Transformer() {
                @Override
                public String apply(String input) {
                    return input + "A";
                }
            },
            new Transformer() {
                @Override
                public String apply(String input) {
                    return input + "B";
                }
            }
        };
        String acc = "-";
        for (Transformer t : chain) {
            acc = t.apply(acc);
        }
        System.out.println(acc);

        Object anon = new Object() {
            @Override
            public String toString() {
                return "anonymous object";
            }
        };
        System.out.println(anon.toString());
    }
}
