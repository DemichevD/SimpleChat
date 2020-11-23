package client;

import server.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {
    /**
     * The <code>Client<code> class includes methods for
     * connecting a new user to the server
     *
     * @author d.demichev
     * @param clientLatch latch terminating the thread of user-server interaction
     * @param clientSocket user socket
     * @param clientPool thread pool executes sending and getting the messages
     * @param getUser stream for sending on server data about a user
     * @param hostname server host
     * @param PORT server service port
     * @param address address the connection to the server
     */
    protected CountDownLatch clientLatch = new CountDownLatch(2);
    private Socket clientSocket;
    protected ThreadPoolExecutor clientPool;
    protected ObjectOutputStream getUser;
    private static String hostname;
    private static int PORT;

    static {
        BufferedReader inputData = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Please enter IP address: ");
            hostname = inputData.readLine();
            System.out.println("Please enter a PORT to the connect: ");
            PORT = Integer.parseInt(inputData.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final InetSocketAddress address = new InetSocketAddress(hostname, PORT);

    private void connectToServer() {
        try {
            clientSocket = new Socket();
            while (true) {
                System.out.println("Connection to server");
                if (serverAvailable()) {
                    clientSocket.connect(address, 10000);
                    if (clientSocket.isConnected() && clientSocket != null)
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void reconnectToServer() throws InterruptedException {
        if (clientSocket != null && clientSocket.isConnected()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clientPool.shutdownNow();
        connectToServer();
        connectClient();
    }


    private void connectClient() {
        try {
            User user = UserConnection.connect();
            clientPool = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
            getUser = new ObjectOutputStream(clientSocket.getOutputStream());
            getUser.writeObject(user);
            clientPool.execute(new ClientInputMessage(clientSocket, clientLatch, this));
            clientPool.execute(new ClientOutputMessage(clientSocket, clientLatch));
            clientLatch.await();
            clientPool.shutdownNow();
        } catch (InterruptedException e) {
            clientLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean serverAvailable() {
        try (Socket socket = new Socket(hostname, PORT)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void start() {
        connectToServer();
        connectClient();
    }
}
