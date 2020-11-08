package Client;

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
     * @param in Reads text from a character-input stream
     * @param clientLatch latch terminating the thread of user-server interaction
     */

    private final BufferedReader in;
    private final CountDownLatch clientLatch;

    public ClientInputMessage(Socket socket, CountDownLatch latch) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientLatch = latch;
    }

    @Override
    public void run() {
        String incomingMessage;
        try {
            while (true) {
                incomingMessage = in.readLine();
                if (incomingMessage.equals("\\quit")) {
                    break;
                }
                System.out.println(incomingMessage);
            }
        } catch (NullPointerException | SocketException e) {
            System.out.println("Error" + e);
            try {
                Client.reconnectToServer();
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

