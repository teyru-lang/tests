public class exceptions_custom {
    static class AppException extends Exception {
        private final int code;

        AppException(String message, int code) {
            super(message);
            this.code = code;
        }

        AppException(String message, int code, Throwable cause) {
            super(message, cause);
            this.code = code;
        }

        int code() {
            return code;
        }
    }

    static class NotFoundException extends AppException {
        NotFoundException(String what) {
            super("not found: " + what, 404);
        }
    }

    static class ValidationException extends RuntimeException {
        ValidationException(String message) {
            super(message);
        }
    }

    static String lookup(String key) throws AppException {
        if ("missing".equals(key)) {
            throw new NotFoundException(key);
        }
        if (key == null || key.isEmpty()) {
            throw new ValidationException("empty key");
        }
        return "value-" + key;
    }

    static String classify(Exception e) {
        if (e instanceof NotFoundException nf) {
            return "not-found code=" + nf.code();
        }
        if (e instanceof AppException app) {
            return "app code=" + app.code();
        }
        if (e instanceof RuntimeException re) {
            return "runtime " + re.getClass().getSimpleName();
        }
        return "other " + e.getClass().getSimpleName();
    }

    static String run(String key) {
        try {
            return lookup(key);
        } catch (AppException e) {
            return "APP:" + e.getMessage() + " code=" + e.code() + " type=" + e.getClass().getSimpleName() + " msg=" + e.getMessage();
        } catch (ValidationException e) {
            return "VAL:" + e.getMessage() + " type=" + e.getClass().getSimpleName();
        } catch (Exception e) {
            return "GEN:" + e.getClass().getSimpleName();
        } finally {
            System.out.println("lookup finished for " + key);
        }
    }

    static String chained() {
        try {
            throw new AppException("outer failed", 500, new IllegalStateException("root cause"));
        } catch (AppException e) {
            Throwable cause = e.getCause();
            return e.getMessage() + " / " + e.code() + " / " + cause.getClass().getSimpleName() + " / " + cause.getMessage();
        }
    }

    static String polymorphic(Exception e) {
        return classify(e);
    }

    public static void main(String[] args) {
        System.out.println(run("alpha"));
        System.out.println(run("missing"));
        try {
            run("");
        } catch (RuntimeException e) {
            System.out.println("unreachable");
        }
        System.out.println(chained());
        System.out.println(polymorphic(new NotFoundException("thing")));
        System.out.println(polymorphic(new ValidationException("bad input")));
        System.out.println(polymorphic(new AppException("plain", 7)));
        System.out.println(polymorphic(new IllegalStateException("state")));

        Exception generic = new ValidationException("wrapped");
        RuntimeException asRuntime = (RuntimeException) generic;
        System.out.println(asRuntime instanceof ValidationException);
        System.out.println(generic instanceof AppException);

        try {
            String s = null;
            System.out.println(s.isEmpty());
        } catch (NullPointerException e) {
            System.out.println("npe caught: " + e.getClass().getSimpleName());
        }
    }
}
