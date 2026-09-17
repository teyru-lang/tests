public class sealed_hierarchy {
    sealed abstract static class Shape permits Circle, Rect {
        abstract double area();

        String label() {
            return "shape area " + area();
        }
    }

    static final class Circle extends Shape {
        private final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        double area() {
            return 3.0 * radius * radius;
        }
    }

    static final class Rect extends Shape {
        private final double width;
        private final double height;

        Rect(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        double area() {
            return width * height;
        }
    }

    sealed interface Kind permits Basic, Fancy {
    }

    enum Basic implements Kind {
        ROUGH,
        SMOOTH
    }

    record Fancy(String label) implements Kind {
    }

    static String name(Shape s) {
        return switch (s) {
            case Circle c -> "circle " + c.area();
            case Rect r -> "rect " + r.area();
        };
    }

    static String kind(Kind k) {
        return switch (k) {
            case Basic b -> "basic " + b.name();
            case Fancy f -> "fancy " + f.label();
        };
    }

    public static void main(String[] args) {
        Shape[] shapes = {new Circle(2.0), new Rect(3.0, 4.0)};
        for (Shape s : shapes) {
            System.out.println(name(s) + " | " + s.label());
        }

        Kind[] kinds = {Basic.ROUGH, Basic.SMOOTH, new Fancy("gold")};
        for (Kind k : kinds) {
            System.out.println(kind(k));
        }

        Shape s = new Rect(2.5, 4.0);
        System.out.println(s instanceof Rect);
        System.out.println(s instanceof Circle);
        System.out.println(s.getClass().getSimpleName());
        System.out.println(s.area());
    }
}
