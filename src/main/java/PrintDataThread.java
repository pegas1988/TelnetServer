// This class will not be used in last modification of application
public class PrintDataThread extends Thread {
    @Override
    public void run() {
        System.out.println(TakeData.neededList.poll());
    }
}
