package lk.ijse.dep7.amazonclonebackend.api;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.ijse.dep7.amazonclonebackend.dto.ItemDTO;
import lk.ijse.dep7.amazonclonebackend.service.ItemService;

import javax.sql.DataSource;

@WebServlet(name = "item-servlet", value = "/items",loadOnStartup = 0)
public class ItemServlet extends HttpServlet {

    @Resource(name= "java:comp/env/jdbc/amazon")
    private DataSource dataSource;

    public void init(){
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // GET http://localhost:8080/amazon/items           <- All items
        // GET http://localhost:8080/amazon/items?code=I001 <- Item I001

        try (Connection connection = dataSource.getConnection()) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemService itemService = new ItemService(connection);
            String code = request.getParameter("code");

            response.setContentType("application/json");

            if (code != null){
                ItemDTO item = itemService.getItem(code);

                if (item != null){
                    response.getWriter().println(jsonb.toJson(item));
                }else{
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }else{
                List<ItemDTO> items = itemService.getAllItems();
                response.getWriter().println(jsonb.toJson(items));
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to obtain a new connection", ex);
        }
    }

    public void destroy() {
    }
}