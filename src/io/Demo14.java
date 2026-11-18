public class Demo14 {
    public static void main(String[] args) {
        try (PrintWriter printWriter = new PrintWriter("d:/test.txt")) {

            printWriter.printf("%d + %d = %d\n", 10, 20, 30);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}