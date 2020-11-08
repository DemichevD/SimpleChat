package Room;

import Server.ClientThread;

import java.io.Serializable;
import java.util.LinkedList;

public class MainRoom extends ChatRoom implements Serializable {

    private static String nameRoom = "MainRoom";

    @Override
    public synchronized String getNameRoom() {
        return nameRoom;
    }

    @Override
    public synchronized void setNameRoom(String newName) {
        if (newName == null)
            throw new NullPointerException();
        else {
            nameRoom = newName;
        }
    }

    @Override
    public synchronized LinkedList<ClientThread> getListClient() {
        return listClient;
    }

    @Override
    public synchronized LinkedList<String> getUserList() {
        return userNameList;
    }

    @Override
    public synchronized void removeUser(String user) {
        userNameList.remove(user);

    }

    @Override
    public synchronized void removeClientThread(ClientThread thread) {
        listClient.remove(thread);
    }

    @Override
    public void addUserName(String user) {
        userNameList.add(user);
    }

    @Override
    public void addClientThread(ClientThread thread) {
        listClient.add(thread);

    }

}

