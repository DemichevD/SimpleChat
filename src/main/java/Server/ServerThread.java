package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerThread implements Runnable {
    /**
     * This <code>ServerThread<code> class realizes the interaction with server
     *
     * @author d.demichev
     * @param reader Reads text from a character-input stream
     */

    private static BufferedReader reader;

    @Override
    public void run() {
        String word;
        reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                word = reader.readLine();
                System.out.println("Server: " + word + '\n');
                if (word.equals("\\stop")) {
                    System.out.println("Stop-Server");
                    Server.stopServer();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

}

