package Server;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * The <code>User</code> class represents the user of SimpleChat
     *
     * @param name of user
     */
    String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
