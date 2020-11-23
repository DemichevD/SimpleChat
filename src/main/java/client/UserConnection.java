package client;

import server.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class UserConnection {
    /**
     * The <code>Connection<code> class includes methods for
     * registration new users, authorizing users.
     *
     * @author d.demichev
     * @param inputData Reads text from a character-input stream
     */
    private static final BufferedReader inputData = new BufferedReader(new InputStreamReader(System.in));


    public static User connect() {
        User user = null;
        try {
            while (true) {
                System.out.println("Do you have account?(y/n)");
                String string = inputData.readLine();
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
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return user;
    }


    private static User registrationNewUser() {
        User user = null;
        try {
            while (true) {
                DbHandler dataBase = DbHandler.getInstance();
                System.out.println("Enter new login: ");
                String userLogin = inputData.readLine();
                userLogin = userLogin.replace(" ", "");
                if (userLogin.isEmpty()) {
                    System.out.println("Login not must be empty");
                } else if (!dataBase.containsUser(userLogin)) {
                    System.out.println("Enter password: ");
                    String userPass = inputData.readLine();
                    dataBase.addNewUser(userLogin, userPass);
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


    private static User authorize() {
        User user = null;
        try {
            while (true) {
                DbHandler dataBase = DbHandler.getInstance();
                System.out.println("Enter login: ");
                String userLogin = inputData.readLine();
                System.out.println(("Enter password: "));
                String userPass = inputData.readLine();
                if (dataBase.checkUserData(userLogin, userPass)) {
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


