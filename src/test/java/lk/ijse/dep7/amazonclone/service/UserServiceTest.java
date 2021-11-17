package lk.ijse.dep7.amazonclone.service;

import lk.ijse.dep7.amazonclone.dto.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_amazon_clone","root","mysql");
        this.userService = new UserService(connection);
    }

    @Test
    void saveUser() {
        UserDTO user = new UserDTO("suranga@ijse.lk", "Ranjith Suranga", "abc");
        String userId = this.userService.saveUser(user);
        assertTrue(userId.equals(user.getUserId()));
    }

    @Test
    void authenticate() {
        UserDTO user = userService.authenticate("suranga@ijse.lk", "abc");
        assertNotNull(user);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

}