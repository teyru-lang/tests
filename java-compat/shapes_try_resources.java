public class try_resources {
    static class Resource implements AutoCloseable {
        private final String name;

        Resource(String name) {
            this.name = name;
            System.out.println("open " + name);
        }

        String read() {
            return "data-of-" + name;
        }

        @Override
        public void close() {
            System.out.println("close " + name);
        }
    }

    static String oneResource() {
        try (Resource r = new Resource("one")) {
            return r.read();
        }
    }

    static String twoResources() {
        try (Resource a = new Resource("a"); Resource b = new Resource("b")) {
            System.out.println(a.read());
            System.out.println(b.read());
            return "done";
        }
    }

    static String threeResources() {
        try (Resource a = new Resource("x"); Resource b = new Resource("y"); Resource c = new Resource("z")) {
            return a.read() + "+" + b.read() + "+" + c.read();
        }
    }

    static String existingVariable() {
        Resource pre = new Resource("pre");
        try (pre) {
            return pre.read();
        }
    }

    static String tryCatchFinally(boolean boom) {
        StringBuilder log = new StringBuilder();
        try {
            log.append("try;");
            if (boom) {
                throw new IllegalArgumentException("boom");
            }
            log.append("ok;");
            return log.toString();
        } catch (IllegalArgumentException e) {
            log.append("catch:");
            log.append(e.getMessage());
            log.append(";");
            return log.toString();
        } finally {
            System.out.println("finally ran, boom=" + boom);
        }
    }

    static String catchInsideResources(boolean boom) {
        StringBuilder log = new StringBuilder();
        try (Resource outer = new Resource("outer")) {
            try (Resource inner = new Resource("inner")) {
                log.append(outer.read());
                log.append("|");
                if (boom) {
                    throw new IllegalStateException("inner boom");
                }
                log.append(inner.read());
            } catch (IllegalStateException e) {
                log.append("caught:").append(e.getMessage());
            }
        }
        return log.toString();
    }

    public static void main(String[] args) {
        System.out.println(oneResource());
        System.out.println(twoResources());
        System.out.println(threeResources());
        System.out.println(existingVariable());
        System.out.println(tryCatchFinally(false));
        System.out.println(tryCatchFinally(true));
        System.out.println(catchInsideResources(false));
        System.out.println(catchInsideResources(true));
    }
}
