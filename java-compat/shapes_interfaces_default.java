public class interfaces_default {
    interface Greeter {
        String DEFAULT_PREFIX = "Hello";
        int MAX_LENGTH = 64;

        String greet(String who);

        default String greetTwice(String who) {
            return greet(who) + " " + greet(who);
        }

        default String greetWithPrefix(String who) {
            return DEFAULT_PREFIX + ", " + greet(who);
        }

        static Greeter shouting() {
            return who -> "HEY " + who;
        }

        static String describe(Greeter g) {
            return "greeter[" + g.greet("probe") + "]";
        }
    }

    interface Counter {
        int count();

        default Counter doubled() {
            int n = count();
            return () -> n * 2;
        }
    }

    static class Polite implements Greeter {
        @Override
        public String greet(String who) {
            return "good day " + who;
        }

        @Override
        public String greetTwice(String who) {
            return "twice: " + who;
        }
    }

    abstract static class Animal {
        private final String name;

        Animal(String name) {
            this.name = name;
        }

        String name() {
            return name;
        }

        abstract String sound();

        String speak() {
            return name + " says " + sound();
        }
    }

    static class Dog extends Animal {
        private final int legs;

        Dog(String name, int legs) {
            super(name);
            this.legs = legs;
        }

        @Override
        String sound() {
            return "woof";
        }

        @Override
        String speak() {
            return super.speak() + " on " + legs + " legs";
        }
    }

    public static void main(String[] args) {
        Greeter polite = new Polite();
        System.out.println(polite.greet("Ada"));
        System.out.println(polite.greetTwice("Ada"));
        System.out.println(polite.greetWithPrefix("Ada"));
        System.out.println(Greeter.DEFAULT_PREFIX);
        System.out.println(Greeter.MAX_LENGTH);

        Greeter shout = Greeter.shouting();
        System.out.println(shout.greet("Ada"));
        System.out.println(shout.greetTwice("Ada"));
        System.out.println(Greeter.describe(shout));

        Counter three = () -> 3;
        System.out.println(three.count());
        System.out.println(three.doubled().count());

        Animal a = new Dog("Rex", 4);
        System.out.println(a.speak());
        System.out.println(((Animal) a).name());
        System.out.println(a.sound());

        Animal plain = new Animal("Generic") {
            @Override
            String sound() {
                return "?";
            }
        };
        System.out.println(plain.speak());
    }
}
