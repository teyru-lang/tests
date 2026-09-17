public class math_ops {
  public static void main(String[] args) {
    System.out.println(Math.max(3, 7) + " " + Math.min(3, 7));
    System.out.println(Math.max(-3L, -7L) + " " + Math.min(2.5, 1.5));
    System.out.println(Math.abs(-5) + " " + Math.abs(5) + " " + Math.abs(-5L) + " " + Math.abs(-2.5));
    System.out.println(Math.abs(Integer.MIN_VALUE));
    System.out.println(Math.sqrt(144.0));
    System.out.println(Math.sqrt(2.0) > 1.41 && Math.sqrt(2.0) < 1.42);
    System.out.println(Math.pow(2.0, 10.0));
    System.out.println(Math.floor(3.7) + " " + Math.ceil(3.2) + " " + Math.floor(-3.2) + " " + Math.ceil(-3.7));
    System.out.println(Math.round(2.5) + " " + Math.round(-2.5) + " " + Math.round(2.4));
    System.out.println(Math.round(3.5f) + " " + Math.round(-3.5f));
    System.out.println(Math.max(Math.max(1, 2), 3));
    System.out.println(Math.min(Math.min(9, 4), 6));
    System.out.println(Math.signum(-3.5) + " " + Math.signum(0.0) + " " + Math.signum(9.0));
    System.out.println(Math.abs(-0.0));
    System.out.println(Math.hypot(3.0, 4.0));
    System.out.println(Math.floorDiv(-7, 2) + " " + Math.floorMod(-7, 2));
    System.out.println(Math.floorDiv(7, 2) + " " + Math.floorMod(7, 2));
    System.out.println(Math.max('a', 'z') + " " + Math.min(1.5f, 2.5f));
    System.out.println(Math.abs(-1.5f));
    System.out.println(Math.pow(3.0, 2.0));
    System.out.println(Math.cbrt(27.0));
    System.out.println(Math.max(0.0, -0.0) + " " + Math.min(0.0, -0.0));
  }
}
