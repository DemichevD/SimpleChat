package client;

import server.MessageCommand;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

public class ClientOutputMessage implements Runnable {
    /**
     * This <code>ClientOutputMessage<code> class realizes getting information from the user and sending to the server
     *
     * @author d.demichev
     * @param inputData Reads text from a character-input stream
     * @param outgoingData Writes text to a character-output stream
     * @param clientLatch latch terminating the thread of user-server interaction
     */

    private final BufferedReader inputData;
    private final BufferedWriter outgoingData;
    private final CountDownLatch clientLatch;

    public ClientOutputMessage(Socket socket, CountDownLatch latch) throws IOException {
        inputData = new BufferedReader(new InputStreamReader(System.in));
        outgoingData = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clientLatch = latch;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String outgoingMessage;
                outgoingData.write("" + '\n');
                outgoingData.flush();
                try {
                    outgoingMessage = inputData.readLine();
                    if (outgoingMessage.equals(MessageCommand.QUIT.getCommand())) {
                        outgoingData.write(MessageCommand.QUIT.getCommand() + '\n');
                        outgoingData.flush();
                        break;
                    } else {
                        outgoingData.write(outgoingMessage + '\n');
                        outgoingData.flush();
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
