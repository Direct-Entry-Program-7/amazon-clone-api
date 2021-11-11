package lk.ijse.dep7.amazonclone.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ItemDTOTest {

    @Test
    public void create(){
        ItemDTO itemDTO = new ItemDTO("I001", "Mouse", "http://some-image", "5", 5, new BigDecimal("1250.00"), "");
        System.out.println(itemDTO);
    }

}