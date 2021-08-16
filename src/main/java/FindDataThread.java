import java.io.File;
import java.util.Scanner;
// This class will not be used in last modification of application
public class FindDataThread extends Thread {
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String rootPath = scanner.next();
        int depth = Integer.parseInt(scanner.next());
        String mask = scanner.next();

        TakeData takeData = new TakeData();
        takeData.findAll(new File(rootPath), depth, mask);
    }
}
