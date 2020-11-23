package server;

import client.DbHandler;
import room.ChatRoom;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The <code>ChatMessage<code> class realizes methods for sending messages
 *
 * @author d.demichev
 */

public class ChatMessage {

    /**
     * This method realizes sending message for all users in this room
     *
     * @param message text messages
     * @param room    room which users located now
     * @param name    name User who sending the message
     */
    public void sendToAll(String message, ChatRoom room, String name, ClientThread thread) {
        try {
            for (ClientThread ct : room.getListClient()) {
                if (ct.equals(thread))
                    continue;
                ct.getOutgoingData().write(name + ": " + message + '\n');
                ct.getOutgoingData().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes sending information message for all users in this room
     * about disconnecting user
     *
     * @param room room which users located now
     * @param name name User who disconnect from the server
     */
    public void sendUserDisconnect(ChatRoom room, String name) {
        try {
            for (ClientThread ct : room.getListClient()) {
                ct.getOutgoingData().write(name + " disconnect from " + room.getNameRoom() + '\n');
                ct.getOutgoingData().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes sending information message for all users in this room
     * about connecting user
     *
     * @param room room which users located now
     * @param name name User who connect to the server
     */
    public void sendNewUser(ChatRoom room, String name) {
        try {
            for (ClientThread ct : room.getListClient()) {
                ct.getOutgoingData().write(name + " connect to " + room.getNameRoom() + '\n');
                ct.getOutgoingData().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes sending information message about available commands on the server
     * for the current user
     *
     * @param thread thread current user
     */
    public void sendInfo(ClientThread thread) {
        try {
            thread.getOutgoingData().write(MessageCommand.QUIT.getAllData() + '\n');
            thread.getOutgoingData().write(MessageCommand.USERS.getAllData() + '\n');
            thread.getOutgoingData().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes sending information message about online users on the server
     * for the current user
     *
     * @param thread thread current user
     * @param room   current room
     */

    public void sendListOnlineUsers(ClientThread thread, ChatRoom room) {
        try {
            thread.getOutgoingData().write("Users online: " + room.getUserList().stream().sorted().collect(Collectors.joining(",")) + '\n');
            thread.getOutgoingData().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes sending information message about option '\help'
     * for the current user
     *
     * @param thread thread current user
     */
    public void sendHelpOption(ClientThread thread) {
        try {
            thread.getOutgoingData().write(MessageCommand.HELP.getAllData() + '\n');
            thread.getOutgoingData().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes adding a message in the DataBase
     *
     * @param name    user name
     * @param message user message
     * @param room    Chat room
     */
    public void addMessageInDB(String name, String message, ChatRoom room) {
        try {
            DbHandler dataBase = DbHandler.getInstance();
            dataBase.addMessageAtHistory(name, message, room);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes loading message history
     *
     * @param thread current user
     */
    public void loadHistoryMessage(ClientThread thread) {
        try {
            DbHandler dataBase = DbHandler.getInstance();
            List<String> history = dataBase.loadMessageHistory();
            for (String str : history) {
                thread.getOutgoingData().write(str + '\n');
                thread.getOutgoingData().flush();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
