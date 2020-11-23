package room;

import server.ClientThread;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;

public class MainRoom extends ChatRoom implements Serializable {

    private static String nameRoom = "MainRoom";

    @Override
    public String getNameRoom() {
        return nameRoom;
    }

    @Override
    public void setNameRoom(String newName) {
        if (newName == null)
            throw new NullPointerException();
        else {
            nameRoom = newName;
        }
    }

    @Override
    public  CopyOnWriteArraySet<ClientThread> getListClient() {
        return listClient;
    }

    @Override
    public CopyOnWriteArraySet<String> getUserList() {
        return userNameList;
    }

    @Override
    public void removeUser(String user) {
        userNameList.remove(user);

    }

    @Override
    public void removeClientThread(ClientThread thread) {
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

