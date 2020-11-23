package server;

import room.ChatRoom;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;


public class ClientThread implements Runnable {
    /**
     * This class realizes the interaction server and users
     *
     * @author d.demichev
     * @param in Reads text from a character-input stream
     * @param out Writes text to a character-output stream
     * @param room the Chatroom in which users communicate
     */

    private final BufferedWriter outgoingData;
    private final BufferedReader inputData;
    private final ChatRoom room;
    private final String userName;

    public ClientThread(Socket socket, ChatRoom room, String name) throws IOException {
        outgoingData = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        inputData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.room = room;
        this.userName = name;
    }

    @Override
    public void run() {
        ChatMessage chatMessage = new ChatMessage();
        try {
            chatMessage.sendNewUser(room, userName);
            chatMessage.sendHelpOption(this);
            chatMessage.loadHistoryMessage(this);
            String incomingMessage;
            label:
            while (true) {
                incomingMessage = inputData.readLine();
                if (incomingMessage.replace(" ", "").equals(""))
                    continue;
                if (incomingMessage.equals(MessageCommand.QUIT.getCommand())) {
                    chatMessage.sendUserDisconnect(room, userName);
                    outgoingData.write(MessageCommand.QUIT.getCommand() + '\n');
                    outgoingData.flush();
                    break;
                } else if (incomingMessage.equals(MessageCommand.HELP.getCommand())) {
                    chatMessage.sendInfo(this);
                    continue;
                } else if (incomingMessage.equals(MessageCommand.USERS.getCommand())) {
                    chatMessage.sendListOnlineUsers(this, room);
                    continue;
                }
                chatMessage.sendToAll(incomingMessage, room, userName, this);
                chatMessage.addMessageInDB(userName, incomingMessage, room);
            }
        } catch (SocketException e) {
            room.removeUser(userName);
            room.removeClientThread(this);
            chatMessage.sendUserDisconnect(room, userName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            room.removeUser(userName);
            room.removeClientThread(this);
        }
    }

    public BufferedWriter getOutgoingData() {
        return outgoingData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientThread that = (ClientThread) o;
        return Objects.equals(outgoingData, that.outgoingData) &&
                Objects.equals(inputData, that.inputData) &&
                Objects.equals(room, that.room) &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outgoingData, inputData, room, userName);
    }

    @Override
    public String toString() {
        return "ClientThread{" +
                "out=" + outgoingData +
                ", in=" + inputData +
                ", room=" + room +
                ", userName='" + userName + '\'' +
                '}';
    }
}