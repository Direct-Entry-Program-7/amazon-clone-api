package lk.ijse.dep7.amazonclone.service;

import lk.ijse.dep7.amazonclone.dto.UserDTO;
import org.apache.commons.codec.digest.DigestUtils;

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
            stm.setString(3, DigestUtils.sha256Hex(user.getPassword()));
            stm.executeUpdate();
            return user.getUserId();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save the user", e);
        }
    }

    public UserDTO authenticate(String userId, String password){
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT name, password FROM user WHERE user_id=?");
            stm.setString(1, userId);
            ResultSet rst = stm.executeQuery();

            if (rst.next()){
                String name = rst.getString("name");
                String hash = rst.getString("password");

                return (DigestUtils.sha256Hex(password).equals(hash))? new UserDTO(userId, name, password) : null;
            }else{
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Authentication process failed", e);
        }
    }
}
