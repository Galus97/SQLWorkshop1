package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_DATABASE_QUERY = "CREATE DATABASE IF NOT EXISTS SQLWorkshop\n" +
            "CHARACTER SET utf8mb4\n" +
            "COLLATE utf8mb4_unicode_ci;";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE if not exists users\n" +
            "(\n" +
            "    id INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "    email VARCHAR(255) UNIQUE,\n" +
            "    username VARCHAR(255),\n" +
            "    password VARCHAR(60)\n" +
            ");";

    private static final String CREATE_USER_QUERY = "INSERT INTO users (USERNAME, EMAIL, PASSWORD)\n" +
            "VALUES (?, ?, ?);";

    private static final String SHOW_USER_QUERY = "SELECT * FROM users WHERE id = ?;";

    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?;";

    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? where id = ?;";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users;";


    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SHOW_USER_QUERY);
            preparedStatement.setString(1, String.valueOf(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setUserName(resultSet.getString(2));
                user.setEmail(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try(Connection connection = DbUtil.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setString(1, String.valueOf(userId));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            User[] users = new User[0];
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users = addToArray(users, user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private User[] addToArray(User[] array, User user){
        User[] arr = Arrays.copyOf(array, array.length + 1);
        arr[arr.length - 1] = user;
        return arr;
    }
}