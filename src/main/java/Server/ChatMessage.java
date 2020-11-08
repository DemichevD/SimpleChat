package Server;

import Client.DbHandler;
import Room.ChatRoom;

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
    public static void sendToAll(String message, ChatRoom room, String name) {
        try {
            for (ClientThread ct : room.getListClient()) {
                ct.getOut().write(name + ": " + message + '\n');
                ct.getOut().flush();
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
    public static void sendUserDisconnect(ChatRoom room, String name) {
        try {
            for (ClientThread ct : room.getListClient()) {
                ct.getOut().write(name + " disconnect from " + room.getNameRoom() + '\n');
                ct.getOut().flush();
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
    public static void sendNewUser(ChatRoom room, String name) {
        try {
            for (ClientThread ct : room.getListClient()) {
                ct.getOut().write(name + " connect to " + room.getNameRoom() + '\n');
                ct.getOut().flush();
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
    public static void sendInfo(ClientThread thread) {
        try {
            thread.getOut().write("\\quit - disconnect from server" + '\n');
            thread.getOut().write("\\users - list online users" + '\n');
            thread.getOut().flush();
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

    public static void sendListOnlineUsers(ClientThread thread, ChatRoom room) {
        try {
            thread.getOut().write("Users online: " + room.getUserList().stream().sorted().collect(Collectors.joining(",")) + '\n');
            thread.getOut().flush();
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
    public static void sendHelpOption(ClientThread thread) {
        try {
            thread.getOut().write("Enter \\help for getting information about available command on the server " + '\n');
            thread.getOut().flush();
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
    public static void addMessageInDB(String name, String message, ChatRoom room) {
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            dbHandler.addMessageAtHistory(name, message, room);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method realizes loading message history
     *
     * @param thread current user
     */
    public static void loadHistoryMessage(ClientThread thread) {
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            List<String> history = dbHandler.loadMessageHistory();
            for (String str : history) {
                thread.getOut().write(str + '\n');
                thread.getOut().flush();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
