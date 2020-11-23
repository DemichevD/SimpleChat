package room;

import server.ClientThread;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Objects;

public abstract class ChatRoom implements Serializable {

    /**
     * The <code>ChatRoom</code> abstract class should be extended by any class whose
     * instances are intended creating rooms in SimpleChat.
     *
     * @param namRoom of SimpleChat room
     * @param listClient collection containing elements of <code>ClientThread<code>
     * @param userNameList collection containing of users Names
     */
    private final String nameRoom = "";
    protected final CopyOnWriteArraySet<ClientThread> listClient = new CopyOnWriteArraySet<>();
    protected final CopyOnWriteArraySet<String> userNameList = new CopyOnWriteArraySet<>();



    public ChatRoom() {
    }


    public abstract String getNameRoom();


    public abstract void setNameRoom(String newName);


    public abstract CopyOnWriteArraySet<ClientThread> getListClient();


    public abstract CopyOnWriteArraySet<String> getUserList();


    public abstract void removeUser(String user);


    public abstract void removeClientThread(ClientThread thread);


    public abstract void addUserName(String user);


    public abstract void addClientThread(ClientThread thread);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(nameRoom, chatRoom.nameRoom) &&
                Objects.equals(listClient, chatRoom.listClient) &&
                Objects.equals(userNameList, chatRoom.userNameList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameRoom, listClient, userNameList);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "nameRoom='" + nameRoom + '\'' +
                ", listClient=" + listClient +
                ", userNameList=" + userNameList +
                '}';
    }
}
