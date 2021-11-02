package lk.ijse.dep7.amazonclonebackend.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemDTOTest {

    @Test
    public void create(){
        ItemDTO itemDTO = new ItemDTO("I001", "Mouse", "http://some-image", ItemDTO.Rating.FOURTH, 5, new BigDecimal("1250.00"), "");
        System.out.println(itemDTO);
    }

}