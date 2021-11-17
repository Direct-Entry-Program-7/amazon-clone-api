package lk.ijse.dep7.amazonclone.service;

import lk.ijse.dep7.amazonclone.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
