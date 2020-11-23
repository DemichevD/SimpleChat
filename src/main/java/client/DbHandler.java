package client;

import room.ChatRoom;
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

    private final String database = "jdbc:sqlite:src/main/resources/Users.db";
    private final Connection connection;
    private final Object clientsMonitor = new Object();
    private final Object historyMonitor = new Object();

    public static DbHandler getInstance() throws SQLException {
        DbHandler instance = null;
        return new DbHandler();
    }

    private DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        connection = DriverManager.getConnection(database);
    }


    public List<String> getAllLogin() {
        String command = "SELECT login FROM clients";
        try (Statement statement = this.connection.createStatement()) {
            List<String> listLogin = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(command);
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


    public boolean addNewUser(String login, String password) {
        synchronized (clientsMonitor) {
            String command = "INSERT INTO 'Clients'('login','password') VALUES(?,?)";
            try {
                PreparedStatement insertUser = connection.prepareStatement(command);
                insertUser.setString(1, login);
                insertUser.setString(2, password);
                insertUser.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean deleteUser(String login) {
        synchronized (clientsMonitor) {
            String command = "DELETE FROM 'Clients' WHERE login = ?";
            try {
                PreparedStatement insertUser = connection.prepareStatement(command);
                insertUser.setString(1, login);
                insertUser.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean containsUser(String login) {
        String command = "SELECT COUNT(*) FROM Clients WHERE login = ?";
        boolean result = true;
        try {
            PreparedStatement findLogin = connection.prepareStatement(command);
            findLogin.setString(1, login);
            result = findLogin.executeQuery().getInt(1) == 1;
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


    public boolean checkUserData(String login, String pass) {
        String command = "SELECT COUNT(*) FROM Clients WHERE login = ? AND password = ?";
        boolean result = false;
        try {
            PreparedStatement checkData = connection.prepareStatement(command);
            checkData.setString(1, login);
            checkData.setString(2, pass);
            result = checkData.executeQuery().getInt(1) != 0;
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


    public void addMessageAtHistory(String name, String message, ChatRoom room) {
        synchronized (historyMonitor) {
            String command = "INSERT INTO 'History'('date','time','userName','textMessage', 'room') VALUES(?,?,?,?,?)";
            Date dateNow = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss ");
            try {
                PreparedStatement addMessage = connection.prepareStatement(command);
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
    }


    public List<String> loadMessageHistory() {
        String command = "SELECT * FROM history";
        try (Statement statement = this.connection.createStatement()) {
            List<String> listLogin = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(command);
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
