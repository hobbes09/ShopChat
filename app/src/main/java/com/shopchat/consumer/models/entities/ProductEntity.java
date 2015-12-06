package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 01/12/15.
 */
public class ProductEntity {

    private String productId;
    private String category;
    private String categoryDescription;
    private String productName;
    private String productDescription;
    private String imageurl;

    public ProductEntity(String productId, String category, String categoryDescription, String productName, String productDescription, String imageurl) {
        this.productId = productId;
        this.category = category;
        this.categoryDescription = categoryDescription;
        this.productName = productName;
        this.productDescription = productDescription;
        this.imageurl = imageurl;
    }

    public ProductEntity(){
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
