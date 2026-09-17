public class record_pattern {
    record Point(int x, int y) {
    }

    record Line(Point from, Point to) {
    }

    record Box(Line edge) {
    }

    static long abs(long v) {
        return v < 0 ? -v : v;
    }

    static String describe(Object o) {
        if (o instanceof Point(int x, int y)) {
            return "point " + x + "," + y;
        }
        if (o instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
            return "line " + x1 + "," + y1 + " -> " + x2 + "," + y2;
        }
        if (o instanceof Box(Line(Point(int x1, int y1), Point(int x2, int y2)))) {
            return "box " + (x2 - x1) + " by " + (y2 - y1);
        }
        return "unknown";
    }

    static long area(Object o) {
        return switch (o) {
            case Point(int x, int y) -> (long) x * (long) y;
            case Line(Point(int x1, int y1), Point(int x2, int y2)) -> abs((long) x2 - x1) * abs((long) y2 - y1);
            case Box(Line(Point(int x1, int y1), Point(int x2, int y2))) -> abs((long) x2 - x1);
            case null -> -1L;
            default -> 0L;
        };
    }

    static int firstQuadrantSum(Object o) {
        if (o instanceof Point(int x, int y) && x > 0 && y > 0) {
            return x + y;
        }
        return 0;
    }

    static String guarded(Object o) {
        return switch (o) {
            case Point(int x, int y) when x == y -> "diagonal " + x;
            case Point(int x, int y) when x > y -> "below " + x + "," + y;
            case Point(int x, int y) -> "above " + x + "," + y;
            default -> "not a point";
        };
    }

    public static void main(String[] args) {
        Point p = new Point(3, 4);
        Line l = new Line(new Point(1, 2), new Point(5, 6));
        Box bx = new Box(new Line(new Point(0, 0), new Point(10, 20)));

        System.out.println(describe(p));
        System.out.println(describe(l));
        System.out.println(describe(bx));
        System.out.println(describe("text"));

        System.out.println(area(p));
        System.out.println(area(l));
        System.out.println(area(bx));
        System.out.println(area(null));
        System.out.println(area(Long.valueOf(7)));

        System.out.println(firstQuadrantSum(new Point(2, 3)));
        System.out.println(firstQuadrantSum(new Point(-2, 3)));

        System.out.println(guarded(new Point(5, 5)));
        System.out.println(guarded(new Point(1, 9)));
        System.out.println(guarded(new Point(9, 1)));
        System.out.println(guarded(l));
    }
}
