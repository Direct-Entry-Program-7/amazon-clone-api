package lk.ijse.dep7.amazonclonebackend.dto;

import java.math.BigDecimal;

public class ItemDTO {
    private String code;
    private String title;
    private String image;
    private Rating rating;
    private int qty;
    private BigDecimal unitPrice;
    private String description;

    public ItemDTO() {
    }

    public ItemDTO(String code, String title, String image, Rating rating, int qty, BigDecimal unitPrice, String description) {
        this.code = code;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", rating=" + rating +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", description='" + description + '\'' +
                '}';
    }

    public enum Rating{
        FIRST, SECOND, THIRD, FOURTH, FIFTH;
    }
}


