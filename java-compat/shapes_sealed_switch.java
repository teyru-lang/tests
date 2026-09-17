public class sealed_switch {
    sealed interface Shape permits Circle, Square, Rect {
    }

    record Circle(double radius) implements Shape {
    }

    record Square(double side) implements Shape {
    }

    record Rect(double width, double height) implements Shape {
    }

    static String arrow(Shape s) {
        return switch (s) {
            case Circle(double r) -> "circle " + r;
            case Square(double side) -> "square " + side;
            case Rect(double w, double h) -> "rect " + w + "x" + h;
        };
    }

    static String colon(Shape s) {
        switch (s) {
            case Circle c:
                return "C" + c.radius();
            case Square q:
                return "S" + q.side();
            case Rect r:
                return "R" + r.width() + "x" + r.height();
        }
    }

    static String nullable(Shape s) {
        return switch (s) {
            case null -> "nothing";
            case Circle c -> "round " + c.radius();
            case Square q -> "flat " + q.side();
            case Rect r -> "box " + r.width() + "x" + r.height();
        };
    }

    static String describe(Shape s) {
        return switch (s) {
            case Circle(double r) when r > 10 -> "big circle";
            case Circle(double r) -> "circle of " + r;
            case Square(double side) when side == 1 -> "unit square";
            case Square(double side) -> "square of " + side;
            case Rect(double w, double h) when w == h -> "degenerate square " + w;
            case Rect(double w, double h) -> "rect " + w + " by " + h;
        };
    }

    static int sides(Shape s) {
        int total = 0;
        switch (s) {
            case Circle c -> total = 1;
            case Square q -> total = 4;
            case Rect r -> total = 4;
        }
        return total;
    }

    public static void main(String[] args) {
        Shape[] shapes = {new Circle(2.5), new Square(3.0), new Rect(4.0, 5.0), new Circle(11.0), new Square(1.0), new Rect(6.0, 6.0)};
        for (Shape s : shapes) {
            System.out.println(arrow(s) + " | " + colon(s) + " | " + nullable(s) + " | " + describe(s) + " | " + sides(s));
        }
        System.out.println(nullable(null));
    }
}
