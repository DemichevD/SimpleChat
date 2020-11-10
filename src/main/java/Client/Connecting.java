package Client;

import Server.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Connecting {
    /**
     * The <code>Connecting<code> class includes methods for
     * registration new users, authorizing users.
     *
     * @author d.demichev
     * @param input Reads text from a character-input stream
     */
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * This method checked the existence account of a user
     * @return User object type User
     */
    public static User connect() {
        User user = null;
        try {
            while (true) {
                System.out.println("Do you have account?(y/n)");
                String string = input.readLine();
                if (string.equals("n")) {
                    System.out.println("Registration new User");
                    user = registrationNewUser();
                    break;
                } else if (string.equals("y")) {
                    user = authorize();
                    break;
                } else
                    System.out.println("Incorrect!");
            }
        } catch (IOException  ioException) {
            ioException.printStackTrace();
        }
        return user;
    }

    /**
     * This method realizes registration of the new user and added a note about this user in the DataBase
     * @return User object type User
     */
    private static User registrationNewUser() {
        User user = null;
        try {
            while (true) {
                DbHandler dbHandler = DbHandler.getInstance();
                System.out.println("Enter new login: ");
                String userLogin = input.readLine();
                userLogin = userLogin.replace(" ", "");
                if (userLogin.isEmpty()){
                    System.out.println("Login not must be empty");
                }else if (dbHandler.containsUser(userLogin)) {
                    System.out.println("Enter password: ");
                    String userPass = input.readLine();
                    dbHandler.addNewUser(userLogin, userPass);
                    user = new User(userLogin);
                    break;
                } else
                    System.out.println("Login is busy");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * This method checked users data in DataBase
     * @return User object type User
     */
    private static User authorize() {
        User user = null;
        try {
            while (true) {
                DbHandler dbHandler = DbHandler.getInstance();
                System.out.println("Enter login: ");
                String userLogin = input.readLine();
                System.out.println(("Enter password: "));
                String userPass = input.readLine();
                if (dbHandler.checkUserData(userLogin, userPass)) {
                    user = new User(userLogin);
                    break;
                } else
                    System.out.println("Incorrect login or password");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}


