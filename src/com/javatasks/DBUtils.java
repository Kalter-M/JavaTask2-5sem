package com.javatasks;

import com.javatasks.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

class DBUtils {

    private static String url = "jdbc:mysql://localhost:3306/j_task2_schema?useSSL=false";
    private static String user = "root";
    private static String password = "1234";

    private static Connection connectDB(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return  null;
        }
    }

    private static void updateDB(String query) {

        Connection connection = null;
        Statement statement = null;

        try
        {
            connection = connectDB(url, user, password);
            statement = connection != null ? connection.createStatement() : null;
            if (statement != null) {
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.err.println("Error connection to database!\n" + e);
        } finally {
            if (statement != null ){
                try {
                    statement.close();
                } catch(Exception e) {
                    System.err.println("Error statement!\n" + e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch(Exception e) {
                    System.err.println("Error connection!\n" + e);
                }
            }
        }
    }

    private static List<User> selectDB(String query) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        List<User> userList = new LinkedList<>();

        try {
            connection = connectDB(url, user, password);
            statement = connection != null ? connection.createStatement() : null;
            resultSet = statement != null ? statement.executeQuery(query) : null;

            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getInt("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Surname"),
                        resultSet.getString("Login"),
                        resultSet.getString("Email")));
            }
        } catch (SQLException e) {
            System.err.println("Error connection to database!\n" + e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    System.err.println("Error result set!\n" + e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch(Exception e) {
                    System.err.println("Error statement!\n" + e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch(Exception e) {
                    System.err.println("Error connection!\n" + e);
                }
            }
        }
        return userList;
    }

    static void addUser(User user) {
        updateDB(
                "INSERT INTO user (`Name`, `Surname`, `Login`, `Email`)  VALUES ('" + user.getName() +
                "', '" + user.getSurname() +
                "', '" + user.getLogin() +
                "', '" + user.getEmail() + "');");
    }

    static void updateUser(int id, User user) {
        updateDB(
                "UPDATE user SET Name='" + user.getName() +
                        "', Surname='" + user.getSurname() +
                        "', Login='" + user.getLogin() +
                        "', Email='" + user.getEmail() +
                        "' WHERE Id = " + id + ";");
    }

    static void deleteUser(int id) {
        updateDB("DELETE FROM user WHERE Id=" + id + ";");
    }

    static List<User> sortUsers(int sort) {
        String query;
        switch (sort) {
            case 1:
                query = "SELECT * FROM user ORDER BY Name;";
                break;
            case 2:
            default:
                query = "SELECT * FROM user ORDER BY Surname;";
                break;
        }
        return selectDB(query);
    }

    static List<User> filterUsers(int filter){
        String query;
        String mask = "%a%";
        switch (filter){
            case 1:
                query ="SELECT * FROM user WHERE name LIKE '" + mask + "';";
                break;
            case 2:
            default:
                query ="SELECT * FROM user WHERE name LIKE '" + mask + "';";
                break;
        }
        return selectDB(query);
    }

    static User getUser(int id) {
        List<User> userList = selectDB("SELECT * FROM user WHERE Id = " + id + ";");
        if (!userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }

    static List<User> getUsers() {
        return selectDB("SELECT * FROM user");
    }


}
