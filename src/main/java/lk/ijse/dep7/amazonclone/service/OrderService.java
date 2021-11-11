package lk.ijse.dep7.amazonclone.service;

import lk.ijse.dep7.amazonclone.dto.OrderDetailDTO;

import java.sql.*;
import java.util.List;

public class OrderService {

    private Connection connection;

    public OrderService(Connection connection) {
        this.connection = connection;
    }

    public void placeOrder(List<OrderDetailDTO> orderDetails){
        try {

            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("INSERT INTO `order` (date) VALUES (NOW())", Statement.RETURN_GENERATED_KEYS);
            if (stm.executeUpdate() != 1){
                throw new RuntimeException("Failed to save the order");
            }

            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int orderId = generatedKeys.getInt(1);

            for (OrderDetailDTO orderDetail : orderDetails) {

                stm = connection.prepareStatement("INSERT INTO order_detail VALUES (?,?,?,?)");
                stm.setInt(1, orderId);
                stm.setString(2, orderDetail.getCode());
                stm.setInt(3, orderDetail.getQty());
                stm.setBigDecimal(4, orderDetail.getUnitPrice());

                if (stm.executeUpdate() != 1){
                    throw new RuntimeException("Failed to save the order detail: " + orderDetail.getCode());
                }

                stm = connection.prepareStatement("UPDATE item SET qty = qty - ? WHERE code=?");
                stm.setInt(1, orderDetail.getQty());
                stm.setString(2, orderDetail.getCode());

                if (stm.executeUpdate() != 1){
                    throw new RuntimeException("Failed to update the stock on: " + orderDetail.getCode());
                }

            }

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException|RuntimeException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Failed to process the order request", e);
        }

    }
}
