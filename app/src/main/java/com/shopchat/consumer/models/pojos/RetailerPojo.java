package com.shopchat.consumer.models.pojos;

/**
 * Created by sourin on 01/12/15.
 */
public class RetailerPojo {

    private String id;
    private long lastModifiedDate;
    private String user;
    private String products;
    private String shopName;
    private String address;
    private String parentRetailer;

    public RetailerPojo(String id, long lastModifiedDate, String user, String products, String shopName, String address, String parentRetailer) {
        this.id = id;
        this.lastModifiedDate = lastModifiedDate;
        this.user = user;
        this.products = products;
        this.shopName = shopName;
        this.address = address;
        this.parentRetailer = parentRetailer;
    }

    public RetailerPojo(){
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentRetailer() {
        return parentRetailer;
    }

    public void setParentRetailer(String parentRetailer) {
        this.parentRetailer = parentRetailer;
    }
}
