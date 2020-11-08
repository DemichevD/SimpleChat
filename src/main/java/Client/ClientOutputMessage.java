package Client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

public class ClientOutputMessage implements Runnable {
    /**
     * This <code>ClientOutputMessage<code> class realizes getting information from the user and sending to the server
     *
     * @author d.demichev
     * @param in Reads text from a character-input stream
     * @param out Writes text to a character-output stream
     * @param clientLatch latch terminating the thread of user-server interaction
     */

    private final BufferedReader in;
    private final BufferedWriter out;
    private final CountDownLatch clientLatch;

    public ClientOutputMessage(Socket socket, CountDownLatch latch) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clientLatch = latch;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String outgoingMessage;
                out.write("" + '\n');
                out.flush();
                try {
                    outgoingMessage = in.readLine();
                    if (outgoingMessage.equals("\\quit")) {
                        out.write("\\quit" + '\n');
                        out.flush();
                        break;
                    } else {
                        out.write(outgoingMessage + '\n');
                        out.flush();
                    }
                } catch (SocketException e) {
                    break;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientLatch.countDown();
        }
    }
}
