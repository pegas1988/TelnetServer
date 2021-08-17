import java.io.IOException;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter root directory, please: ");
        String rootPath = scanner.next();
        System.out.println("Enter connection port, please: ");
        int port = Integer.parseInt(scanner.next());

        Thread thread = new Thread(new TelnetServer(port, rootPath));
        thread.start();
    }
}
