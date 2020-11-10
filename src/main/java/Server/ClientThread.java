package Server;

import Room.ChatRoom;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientThread implements Runnable {
    /**
     * This class realizes the interaction server and users
     *
     * @author d.demichev
     * @param in Reads text from a character-input stream
     * @param out Writes text to a character-output stream
     * @param room the Chatroom in which users communicate
     */

    private final BufferedWriter out;
    private final BufferedReader in;
    private final ChatRoom room;
    private final String userName;

    public ClientThread(Socket socket, ChatRoom room, String name) throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.room = room;
        this.userName = name;
    }

    @Override
    public void run() {

        try {
            ChatMessage.sendNewUser(room, userName);
            ChatMessage.sendHelpOption(this);
            ChatMessage.loadHistoryMessage(this);
            String incomingMessage;
            label:
            while (true) {
                incomingMessage = in.readLine();
                if (incomingMessage.replace(" ", "").equals(""))
                    continue;
                switch (incomingMessage) {
                    case "\\quit":
                        ChatMessage.sendUserDisconnect(room, userName);
                        out.write("\\quit" + '\n');
                        out.flush();
                        break label;
                    case "\\help":
                        ChatMessage.sendInfo(this);
                        continue;
                    case "\\users":
                        ChatMessage.sendListOnlineUsers(this, room);
                        continue;
                }
                ChatMessage.sendToAll(incomingMessage, room, userName);
                ChatMessage.addMessageInDB(userName, incomingMessage, room);
            }
        } catch (SocketException e) {
            room.removeUser(userName);
            room.removeClientThread(this);
            ChatMessage.sendUserDisconnect(room, userName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            room.removeUser(userName);
            room.removeClientThread(this);
        }
    }

    public BufferedWriter getOut() {
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientThread that = (ClientThread) o;
        return Objects.equals(out, that.out) &&
                Objects.equals(in, that.in) &&
                Objects.equals(room, that.room) &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(out, in, room, userName);
    }

    @Override
    public String toString() {
        return "ClientThread{" +
                "out=" + out +
                ", in=" + in +
                ", room=" + room +
                ", userName='" + userName + '\'' +
                '}';
    }
}