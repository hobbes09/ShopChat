package com.shopchat.consumer.models.pojos;

/**
 * Created by sourin on 01/12/15.
 */
public class ProductPojo {

    private String id;
    private long lastModifiedDate;
    private String category;
    private String categoryDescription;
    private String productName;
    private String productDescription;
    private String imageurl;

    public ProductPojo(String id, long lastModifiedDate, String category, String categoryDescription, String productName, String productDescription, String imageurl) {
        this.id = id;
        this.lastModifiedDate = lastModifiedDate;
        this.category = category;
        this.categoryDescription = categoryDescription;
        this.productName = productName;
        this.productDescription = productDescription;
        this.imageurl = imageurl;
    }

    public ProductPojo(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
