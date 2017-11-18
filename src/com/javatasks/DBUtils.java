package com.javatasks;

import com.javatasks.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

class DBUtils {


    private static Connection connectDB(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);

    }

    private static Connection connectDB() throws SQLException {
        String user = "root";
        String password = "1234";
        String url = "jdbc:mysql://localhost:3306/j_task2_schema?useSSL=false";
        return connectDB(url, user, password);
    }

    private static List<User> selectDB(String query) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<User> userList = new LinkedList<>();

        try {
            connection = connectDB();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getInt("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Surname"),
                        resultSet.getString("Login"),
                        resultSet.getString("Email")));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();

            }
            if (connection != null) {
                connection.close();
            }
        }
        return userList;
    }

    static void addUser(User user) throws SQLException {
        String query = "INSERT INTO User VALUES (?, ?, ?, ?, ?);";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectDB();
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getLogin());
            statement.setString(5, user.getEmail());
            statement.executeUpdate();

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    static void updateUser(int id, User user) throws SQLException {
        String query = "UPDATE User SET id=?,name=?,surname=?,login=?,email=? WHERE id= ?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectDB();
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getLogin());
            statement.setString(5, user.getEmail());
            statement.setInt(6, id);
            statement.executeUpdate();

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    static void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM User WHERE id=?;";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectDB();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    static List<User> sortUsers(int sort) throws SQLException {
        String query;
        switch (sort) {
            case 1:
                query = "SELECT * FROM User ORDER BY Name;";
                break;
            case 2:
            default:
                query = "SELECT * FROM User ORDER BY Surname;";
                break;
        }
        return selectDB(query);
    }

    static List<User> filterUsers(int filter, String mask) throws SQLException {
        String query;
        switch (filter) {
            case 1:
                query = "SELECT * FROM User WHERE Name LIKE ?";
                break;
            case 2:
            default:
                query = "SELECT * FROM User WHERE Surname LIKE ?";
                break;
        }
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<User> userList = new LinkedList<>();

        try {
            connection = connectDB();
            statement = connection.prepareStatement(query);
            statement.setString(1, "%" + mask + "%");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getInt("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Surname"),
                        resultSet.getString("Login"),
                        resultSet.getString("Email")));
            }

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return userList;
    }

    static User getUser(int id) throws SQLException {

        String query = "SELECT * FROM User WHERE id = ?";
        User user = new User();
        boolean ok = false;
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectDB();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setLogin(resultSet.getString("login"));
                user.setEmail(resultSet.getString("email"));
                ok = true;
            }

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        if (ok) {
            return user;
        }
        return null;
    }

    static List<User> getUsers() throws SQLException {
        String query = "SELECT * FROM User";
        return selectDB(query);
    }

    static void exportData(String filename) throws SQLException {
        String query = "SELECT * INTO OUTFILE ? FROM User";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectDB();
            statement = connection.prepareStatement(query);
            statement.setString(1, filename);
            resultSet = statement.executeQuery();

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    static void importData(String filename) throws SQLException {
        String query = "LOAD DATA INFILE ? INTO TABLE User;";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectDB();
            statement = connection.prepareStatement(query);
            statement.setString(1, filename);
            statement.executeUpdate();

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
