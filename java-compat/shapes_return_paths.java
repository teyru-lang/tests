public class return_paths {
    static int bySwitchStatement(int x) {
        switch (x) {
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 30;
            default:
                return 99;
        }
    }

    static int byLoopForever(int x) {
        while (true) {
            if (x < 0) {
                return -1;
            }
            if (x == 0) {
                return 0;
            }
            x = x - 1;
        }
    }

    static int byTryFinally(int x) {
        try {
            if (x > 0) {
                return 1;
            }
            return 2;
        } finally {
            return x + 100;
        }
    }

    static int byForEver(int[] data) {
        for (int i = 0; ; i++) {
            if (i >= data.length) {
                return data.length;
            }
            if (data[i] == 0) {
                return i;
            }
        }
    }

    static int byDoWhileTrue(int x) {
        do {
            if (x % 2 == 0) {
                return x / 2;
            }
            x = x + 1;
        } while (true);
    }

    static String byThrow(int x) {
        switch (x) {
            case 0:
                return "zero";
            default:
                throw new IllegalStateException("not zero");
        }
    }

    public static void main(String[] args) {
        System.out.println(bySwitchStatement(1));
        System.out.println(bySwitchStatement(3));
        System.out.println(bySwitchStatement(7));
        System.out.println(byLoopForever(4));
        System.out.println(byLoopForever(-2));
        System.out.println(byTryFinally(5));
        System.out.println(byTryFinally(-5));
        System.out.println(byForEver(new int[] {5, 6, 0, 8}));
        System.out.println(byForEver(new int[] {5, 6, 8}));
        System.out.println(byDoWhileTrue(4));
        System.out.println(byDoWhileTrue(7));
        System.out.println(byThrow(0));
        try {
            byThrow(1);
        } catch (IllegalStateException e) {
            System.out.println("caught " + e.getMessage());
        }
    }
}
