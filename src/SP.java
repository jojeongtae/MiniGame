public class SP {
    public static void s(String message, long delay) {
        System.out.println(message);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
