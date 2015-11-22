package com.shopchat.consumer.models;

/**
 * Created by Sudipta on 9/15/2015.
 */
public class ChatBoardModel {

    private String chatContent;
    private String chatTimeStamp;
    private boolean isCustomer;
    private int retailerCount;
    private RetailerModel retailerModel;
    private ProductModel productModel;

    public ChatBoardModel(String chatContent,boolean isCustomer, int retailerCount,
                          RetailerModel retailerModel, ProductModel productModel, String chatTimeStamp) {
        this.chatContent = chatContent;
        this.chatTimeStamp = chatTimeStamp;
        this.isCustomer = isCustomer;
        this.retailerCount = retailerCount;
        this.retailerModel = retailerModel;
        this.productModel = productModel;
        this.chatTimeStamp = chatTimeStamp;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getChatTimeStamp() {
        return chatTimeStamp;
    }

    public void setChatTimeStamp(String chatTimeStamp) {
        this.chatTimeStamp = chatTimeStamp;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean isCustomer) {
        this.isCustomer = isCustomer;
    }

    public int getRetailerCount() {
        return retailerCount;
    }

    public void setRetailerCount(int retailerCount) {
        this.retailerCount = retailerCount;
    }

    public RetailerModel getRetailerModel() {
        return retailerModel;
    }

    public void setRetailerModel(RetailerModel retailerModel) {
        this.retailerModel = retailerModel;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }
}
