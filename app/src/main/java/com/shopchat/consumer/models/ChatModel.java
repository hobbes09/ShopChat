package com.shopchat.consumer.models;

import java.util.List;

/**
 * Created by Sudipta on 9/28/2015.
 */
public class ChatModel {

    private String consumerChatContent;
    private ProductModel productModel;
    private RetailerModel retailerModel;
    private List<String> answerList;

    public String getConsumerChatContent() {
        return consumerChatContent;
    }

    public void setConsumerChatContent(String consumerChatContent) {
        this.consumerChatContent = consumerChatContent;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public RetailerModel getRetailerModel() {
        return retailerModel;
    }

    public void setRetailerModel(RetailerModel retailerModel) {
        this.retailerModel = retailerModel;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }
}
