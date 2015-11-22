package com.shopchat.consumer.models;

/**
 * Created by Sudipta on 9/29/2015.
 */
public class ChatDisplayModel {

    private String productId;
    private String productName;
    private String retailerConsolidatedChatContent;
    private String timeStamp;
    private boolean isRead;
    private String consumerChatContent;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRetailerConsolidatedChatContent() {
        return retailerConsolidatedChatContent;
    }

    public void setRetailerConsolidatedChatContent(String retailerConsolidatedChatContent) {
        this.retailerConsolidatedChatContent = retailerConsolidatedChatContent;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getConsumerChatContent() {
        return consumerChatContent;
    }

    public void setConsumerChatContent(String consumerChatContent) {
        this.consumerChatContent = consumerChatContent;
    }
}
