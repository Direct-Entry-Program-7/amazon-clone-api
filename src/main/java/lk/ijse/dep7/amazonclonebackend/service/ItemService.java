package lk.ijse.dep7.amazonclonebackend.service;

import java.sql.Connection;

public class ItemService {

    private Connection connection;

    public ItemService(Connection connection) {
        this.connection = connection;
    }


}
