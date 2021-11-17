package lk.ijse.dep7.amazonclone.service;

import lk.ijse.dep7.amazonclone.dto.UserDTO;

import java.sql.*;

public class UserService {

    private Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    public String saveUser(UserDTO user){
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO user (user_id, name, password)  VALUE (?,?,?)");
            stm.setString(1, user.getUserId());
            stm.setString(2, user.getName());
            stm.setString(3, user.getPassword());
            stm.executeUpdate();
            return user.getUserId();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save the user", e);
        }
    }

    public UserDTO authenticate(String userId, String password){
        try {
            Statement stm = connection.createStatement();
            String sql = String.format("SELECT name FROM user WHERE user_id='%s' AND password='%s'", userId, password);
            ResultSet rst = stm.executeQuery(sql);
            return rst.next()? new UserDTO(userId, rst.getString("name"), password): null;
        } catch (SQLException e) {
            throw new RuntimeException("Authentication process failed", e);
        }
    }
}