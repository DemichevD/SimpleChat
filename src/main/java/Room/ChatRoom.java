package Room;

import Server.ClientThread;

import java.io.Serializable;
import java.util.LinkedList;

public abstract class ChatRoom implements Serializable {

    /**
     * The <code>ChatRoom</code> abstract class should be extended by any class whose
     * instances are intended creating rooms in SimpleChat.
     *
     * @param name of SimpleChat room
     * @param listClient collection containing elements of <code>ClientThread<code>
     * @param userNameList collection containing of users Names
     */
    private final String nameRoom = "";
    protected final LinkedList<ClientThread> listClient = new LinkedList<>();
    protected final LinkedList<String> userNameList = new LinkedList<>();

    /**
     * Constructor for use by subclasses.
     */
    public ChatRoom() {
    }

    /**
     * This method returns name room, type String
     *
     * @return name of the given room
     */
    public abstract String getNameRoom();

    /**
     * This method sets the name of this room
     *
     * @param newName new name of room
     * @throws NullPointerException if the specified element is null
     */
    public abstract void setNameRoom(String newName);

    /**
     * This method returns collection containing elements of <code>ClientThread<code>
     *
     * @return collection LinkedList
     */
    public abstract LinkedList<ClientThread> getListClient();

    /**
     * This method returns collection containing of users Name
     *
     * @return collection LinkedList
     */
    public abstract LinkedList<String> getUserList();

    /**
     * Remove User name from collection
     *
     * @param user Name of user
     */
    public abstract void removeUser(String user);

    /**
     * Remove ClientThread from collection
     *
     * @param thread Name of user
     */
    public abstract void removeClientThread(ClientThread thread);

    /**
     * Appends the specified element to the end of this list.
     *
     * @param user the element to add
     */
    public abstract void addUserName(String user);

    /**
     * Appends the specified element to the end of this list.
     *
     * @param thread the element to add
     */
    public abstract void addClientThread(ClientThread thread);
}
