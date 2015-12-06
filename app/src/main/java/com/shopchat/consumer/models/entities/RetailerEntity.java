package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 01/12/15.
 */
public class RetailerEntity {

    private String retailerId;
    private String shopName;

    public RetailerEntity(String retailerId, String shopName) {
        this.retailerId = retailerId;
        this.shopName = shopName;
    }

    public RetailerEntity(){}

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
