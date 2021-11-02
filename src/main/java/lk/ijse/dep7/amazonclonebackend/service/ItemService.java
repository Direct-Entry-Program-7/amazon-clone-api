package lk.ijse.dep7.amazonclonebackend.service;

import lk.ijse.dep7.amazonclonebackend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemService {

    private Connection connection;

    public ItemService(Connection connection) {
        this.connection = connection;
    }

    public List<ItemDTO> getAllItems(){
        try {
            List<ItemDTO> items = new ArrayList<>();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM item");

            while (rst.next()){
                new ItemDTO(rst.getString("code"),
                        rst.getString("title"),
                        rst.getString("image"),
                        rst.getString("rating"),
                        rst.getInt("qty"),
                        rst.getBigDecimal("unit_price"),
                        rst.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
