package lk.ijse.dep7.amazonclonebackend.service;

import lk.ijse.dep7.amazonclonebackend.dto.ItemDTO;

import java.sql.*;
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
                items.add(new ItemDTO(rst.getString("code"),
                        rst.getString("title"),
                        rst.getString("image"),
                        rst.getString("rating"),
                        rst.getInt("qty"),
                        rst.getBigDecimal("unit_price"),
                        rst.getString("description")));
            }

            return items;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch items from the DB", e);
        }
    }

    public ItemDTO getItem(String code){
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
            stm.setString(1, code);
            ResultSet rst = stm.executeQuery();

            if (rst.next()){
             return new ItemDTO(rst.getString("code"),
                     rst.getString("title"),
                     rst.getString("image"),
                     rst.getString("rating"),
                     rst.getInt("qty"),
                     rst.getBigDecimal("unit_price"),
                     rst.getString("description"));
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch the item " + code, e);
        }
    }
}
