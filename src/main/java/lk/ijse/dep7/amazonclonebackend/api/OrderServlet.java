package lk.ijse.dep7.amazonclonebackend.api;

import jakarta.annotation.Resource;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.ijse.dep7.amazonclonebackend.dto.OrderDetailDTO;
import lk.ijse.dep7.amazonclonebackend.service.OrderService;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "order-servlet", value = "/orders")
public class OrderServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/amazon")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String contentType = request.getContentType();

        if (contentType == null || !contentType.equals("application/json")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            Jsonb jsonb = JsonbBuilder.create();
            List<OrderDetailDTO> orderDetails = jsonb.fromJson(request.getReader(), new ArrayList<OrderDetailDTO>(){}.getClass().getGenericSuperclass());

            try {

                if (orderDetails.isEmpty()) {
                    throw new RuntimeException("There should be at leas one order detail");
                }

                for (OrderDetailDTO orderDetail : orderDetails) {

                    if (orderDetail.getCode() == null || !orderDetail.getCode().matches("I\\d+")) {
                        throw new RuntimeException("Invalid item code");
                    }

                    if (orderDetail.getQty() <= 0) {
                        throw new RuntimeException("Invalid qty for the item: " + orderDetail.getCode());
                    }

                    if (orderDetail.getUnitPrice() == null || orderDetail.getUnitPrice().compareTo(new BigDecimal("0")) <= 0) {
                        throw new RuntimeException("Invalid unit price for the item: " + orderDetail.getCode());
                    }

                }

            } catch (RuntimeException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                return;
            }

            OrderService orderService = new OrderService(connection);
            orderService.placeOrder(orderDetails);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (JsonbException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to obtain a connection");
        }

    }
}
