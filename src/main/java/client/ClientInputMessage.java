package client;

import server.MessageCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

public class ClientInputMessage implements Runnable {
    /**
     * This <code>ClientInputMessage<code> class realizes checking and getting new messages from the server
     *
     * @author d.demichev
     * @param inputData Reads text from a character-input stream
     * @param clientLatch latch terminating the thread of user-server interaction
     */

    private final BufferedReader inputData;
    private final CountDownLatch clientLatch;
    private Client client;

    public ClientInputMessage(Socket socket, CountDownLatch latch, Client client) throws IOException {
        inputData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientLatch = latch;
        this.client = client;

    }

    @Override
    public void run() {
        String incomingMessage;
        try {
            while (true) {
                incomingMessage = inputData.readLine();
                if (incomingMessage.equals(MessageCommand.QUIT.getCommand())) {
                    break;
                }
                System.out.println(incomingMessage);
            }
        } catch (NullPointerException | SocketException e) {
            System.out.println("Error" + e);
            try {
                client.reconnectToServer();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientLatch.countDown();
        }
    }
}

