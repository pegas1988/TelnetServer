import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.stream.Collectors;

/***
 * Simple TCP server.
 * Waits for connections on a TCP port in a separate thread.
 ***/
public class TelnetServer implements Runnable {
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private Thread listener = null;
    private String dir = null;

    private TakeData takeData = new TakeData();

    TelnetServer(int port, String dir) throws IOException {
        serverSocket = new ServerSocket(port);
        listener = new Thread(this);
        listener.start();
        dir = this.dir;
    }

    public void run() {
        boolean bError = false;
        while (!bError) {
            try {
                clientSocket = serverSocket.accept();
                synchronized (clientSocket) {
                    try {
                        InputStream inputStream = getInputStream();
                        try {
                            String result = new BufferedReader(new InputStreamReader(inputStream))
                                    .lines().collect(Collectors.joining("\n"));

                            takeData.findAll(new File(dir), Integer.parseInt(getData(result)[0]), getData(result)[1]);

                            OutputStream outputStream = getOutputStream();
                            TakeData.neededList.forEach((i) -> {
                                try {
                                    outputStream.write(Objects.requireNonNull(TakeData.neededList.poll()).getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            inputStream.close();
                        }
                        clientSocket.wait();
                    } catch (Exception e) {
                        System.err.println("Exception in wait, " + e.getMessage());
                    }
                    try {
                        clientSocket.close();
                    } catch (Exception e) {
                        System.err.println("Exception in close, " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                bError = true;
            }
        }

        try {
            serverSocket.close();
        } catch (Exception e) {
            System.err.println("Exception in close, " + e.getMessage());
        }
    }

    private void disconnect() {
        synchronized (clientSocket) {
            try {
                clientSocket.notify();
            } catch (Exception e) {
                System.err.println("Exception in notify, " + e.getMessage());
            }
        }
    }

    private void stop() {
        listener.interrupt();
        try {
            serverSocket.close();
        } catch (Exception e) {
            System.err.println("Exception in close, " + e.getMessage());
        }
    }

    private InputStream getInputStream() throws IOException {
        if (clientSocket != null) {
            return (clientSocket.getInputStream());
        } else {
            return (null);
        }
    }

    private OutputStream getOutputStream() throws IOException {
        if (clientSocket != null) {
            return (clientSocket.getOutputStream());
        } else {
            return (null);
        }
    }

    private String[] getData(String input) {
        return input.split("/n");
    }
}