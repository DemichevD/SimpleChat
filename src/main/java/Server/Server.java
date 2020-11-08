package Server;

import Room.ChatRoom;
import Room.MainRoom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    /**
     * The <code>Server<code> class includes methods for
     * work with server
     *
     * @author d.demichev
     * @param PORT server service port
     * @param clientPool thread pool executes sending and getting the messages
     * @param serverPool thread pool executes command for the server
     * @param server server socket
     * @param client user socket
     */
    private static final int PORT = 2020;
    private static final ThreadPoolExecutor clientPool = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    private static final ThreadPoolExecutor serverPool = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
    private static ServerSocket server;
    private static Socket client;

    /**
     * This method realizes start server and connecting new users to him
     */
    private void startServer() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("Server Simple chat started");
            ChatRoom mainRoom = new MainRoom();
            serverPool.execute(new ServerThread());
            while (true) {
                client = server.accept();
                try {
                    ObjectInputStream getUser = new ObjectInputStream(client.getInputStream());
                    User user = (User) getUser.readObject();
                    mainRoom.getUserList().add(user.getName());
                    ClientThread clientThread = new ClientThread(client, mainRoom, user.getName());
                    mainRoom.getListClient().add(clientThread);
                    clientPool.execute(clientThread);
                } catch (SocketException e) {
                    System.out.println("Server was stopped");
                } catch (IOException e) {
                    client.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            clientPool.shutdownNow();
            serverPool.shutdownNow();
        }
    }

    /**
     * This method realizes stop server
     */
    static void stopServer() {
        if (server != null)
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        System.exit(0);
    }

    public void start() {
        startServer();
    }
}

