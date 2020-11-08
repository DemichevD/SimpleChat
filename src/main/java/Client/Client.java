package Client;

import Server.User;

import java.io.IOException;
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
    protected static final CountDownLatch clientLatch = new CountDownLatch(2);
    private static Socket clientSocket;
    protected static ThreadPoolExecutor clientPool;
    protected static ObjectOutputStream getUser;
    private static final String hostname = "localhost";
    private static final int PORT = 2020;
    private static final InetSocketAddress address = new InetSocketAddress(hostname, PORT);

    /**
     * This method realizes the connection new clientSocket to the server
     */
    private static void connectToServer() {
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

    /**
     * This method realizes the reconnect clientSocket to the server if the server is not available
     */
    protected static void reconnectToServer() throws InterruptedException {
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

    /**
     * This method realizes authorization of the new user, sending user info on the server, executes
     * thread pool for sending and getting the messages
     */
    private static void connectClient() {
        try {
            User user = Connecting.connect();
            clientPool = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
            getUser = new ObjectOutputStream(clientSocket.getOutputStream());
            getUser.writeObject(user);
            clientPool.execute(new ClientInputMessage(clientSocket, clientLatch));
            clientPool.execute(new ClientOutputMessage(clientSocket, clientLatch));
            clientLatch.await();
            clientPool.shutdownNow();
        } catch (InterruptedException e) {
            clientLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks server availability
     *
     * @return true if server available of false otherwise
     */
    private static boolean serverAvailable() {
        try (Socket checkServer = new Socket(hostname, PORT)) {
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
