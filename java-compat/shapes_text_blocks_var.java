public class text_blocks_var {
    record Config(String host, int port) {
    }

    interface Renderer {
        String render(String input);
    }

    static final String GREETING = """
            Hello, world!
            This is line two.
            """;

    static final String INDENTED = """
                indented line
                  more indented
            back
            """;

    static final String WITH_QUOTES = """
            he said "hi" and left
            a backslash \\ here
            escape: first\nsecond
            """;

    static final String SINGLE_LINE = """
            only one line""";

    static final String EMPTY_BLOCK = """
            """;

    static final String JSON = """
            {
              "name": "teyru",
              "tags": ["java", "compat"],
              "nested": {
                "value": 42
              }
            }
            """;

    static String render(Config c) {
        var name = c.host();
        var port = c.port();
        var upper = name.equals("localhost") ? "LOCAL" : "REMOTE";
        return upper + " " + name + ":" + port;
    }

    public static void main(String[] args) {
        System.out.println(GREETING);
        System.out.print(INDENTED);
        System.out.println("---");
        System.out.print(WITH_QUOTES);
        System.out.println("---");
        System.out.println(SINGLE_LINE);
        System.out.println("---");
        System.out.println(EMPTY_BLOCK.isEmpty());
        System.out.print(JSON);
        System.out.println("---");

        var config = new Config("localhost", 8080);
        System.out.println(render(config));
        var remote = new Config("example.org", 443);
        System.out.println(render(remote));

        var counter = 0;
        for (var i = 0; i < 4; i++) {
            counter = counter + i;
        }
        System.out.println(counter);

        var values = new int[] {3, 1, 4, 1, 5};
        var total = 0;
        for (var v : values) {
            total = total + v;
        }
        System.out.println(total);

        var message = GREETING + SINGLE_LINE;
        System.out.println(message.split("\n").length);

        var renderer = new Renderer() {
            @Override
            public String render(String input) {
                return "<<" + input + ">>";
            }
        };
        System.out.println(renderer.render(SINGLE_LINE));

        var nested = new Config("ab" + "c", 1);
        System.out.println(nested);
    }
}
