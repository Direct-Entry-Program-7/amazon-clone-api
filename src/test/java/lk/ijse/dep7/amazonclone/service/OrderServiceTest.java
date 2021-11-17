package lk.ijse.dep7.amazonclone.service;

import lk.ijse.dep7.amazonclone.dto.ItemDTO;
import lk.ijse.dep7.amazonclone.dto.OrderDetailDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;
    private ItemService itemService;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7_amazon_clone","root","mysql");
        this.orderService = new OrderService(connection);
        this.itemService = new ItemService(connection);
    }

    @Test
    void placeOrder() {

        ItemDTO i001 = itemService.getItem("I001");
        ItemDTO i002 = itemService.getItem("I002");

        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetailDTO("I001", 2, i001.getUnitPrice()));
        orderDetails.add(new OrderDetailDTO("I002", 1, i002.getUnitPrice()));

        assertDoesNotThrow(()->{
            orderService.placeOrder(orderDetails, "abc@ijse.lk");
        });

        ItemDTO afterTestI001 = itemService.getItem("I001");
        ItemDTO afterTestI002 = itemService.getItem("I002");

        assertEquals(i001.getQty() - 2, afterTestI001.getQty());
        assertEquals(i002.getQty() - 1, afterTestI002.getQty());

    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }
}