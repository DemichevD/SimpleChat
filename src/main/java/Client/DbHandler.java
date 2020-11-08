package Client;

import Room.ChatRoom;
import org.sqlite.JDBC;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DbHandler {
    /**
     * The <code>DBHandler<code> class realizes the interaction with DataBase
     *
     * @param database location of database
     * @param connection session with a specific database
     */

    private static final String database = "jdbc:sqlite:src/main/resources/Users.db";
    private static DbHandler instance = null;
    private final Connection connection;

    public static DbHandler getInstance() throws SQLException {
        return instance = new DbHandler();
    }

    private DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        connection = DriverManager.getConnection(database);
    }

    /**
     * This Method return list of all login the users who were registration on server
     *
     * @return List users login
     */
    public synchronized List<String> getAllLogin() {
        try (Statement statement = this.connection.createStatement()) {
            List<String> listLogin = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT login FROM clients");
            while (resultSet.next()) {
                listLogin.add(resultSet.getString("login"));
            }
            return listLogin;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This Method add a new note about new users at DataBase
     */
    public synchronized void addNewUser(String login, String password) {
        try {
            PreparedStatement insertUser = connection.prepareStatement("INSERT INTO 'Clients'('login','password') VALUES(?,?)");
            insertUser.setString(1, login);
            insertUser.setString(2, password);
            insertUser.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This Method check the contains database incoming user login
     *
     * @return true if the user has note in the DataBase, false otherwise
     */
    public synchronized boolean containsUser(String login) {
        boolean result = true;
        try {
            PreparedStatement findLogin = connection.prepareStatement("SELECT COUNT(*) FROM Clients WHERE login = ?");
            findLogin.setString(1, login);
            result = findLogin.executeQuery().getInt(1) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method checks the username and password combination
     *
     * @return true if combination correct, false otherwise
     */
    public synchronized boolean checkUserData(String login, String pass) {
        boolean result = false;
        try {
            PreparedStatement checkData = connection.prepareStatement("SELECT COUNT(*) FROM Clients WHERE login = ? AND password = ?");
            checkData.setString(1, login);
            checkData.setString(2, pass);
            result = checkData.executeQuery().getInt(1) != 0;
            checkData.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * This method adds the new message at history in the database
     */
    public synchronized void addMessageAtHistory(String name, String message, ChatRoom room) {
        Date dateNow = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss ");
        try {
            PreparedStatement addMessage = connection.prepareStatement("INSERT INTO 'History'('date','time','userName','textMessage', 'room') VALUES(?,?,?,?,?)");
            addMessage.setString(1, formatDate.format(dateNow));
            addMessage.setString(2, formatTime.format(dateNow));
            addMessage.setString(3, name);
            addMessage.setString(4, message);
            addMessage.setString(5, room.getNameRoom());
            addMessage.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method return message history from the database
     * @return list the message
     */
    public synchronized List<String> loadMessageHistory() {
        try (Statement statement = this.connection.createStatement()) {
            List<String> listLogin = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM history");
            while (resultSet.next()) {
                listLogin.add(String.format("%s %s : %s - %s", resultSet.getString("date"), resultSet.getString("time"), resultSet.getString("userName"), resultSet.getString("textMessage")));
            }
            return listLogin;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
