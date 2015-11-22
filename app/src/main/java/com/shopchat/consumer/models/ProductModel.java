package com.shopchat.consumer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/12/2015.
 */
public class ProductModel implements Parcelable {

    private String productName;
    private String productId;
    private String consumerChatContent;
    private String chatTimeStamp;
    private List<RetailerModel> retailerModels;

    public ProductModel(String productName) {
        this.productName = productName;
    }

    public ProductModel() {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<RetailerModel> getRetailerModels() {
        return retailerModels;
    }

    public void setRetailerModels(List<RetailerModel> retailerModels) {
        this.retailerModels = retailerModels;
    }

    public String getConsumerChatContent() {
        return consumerChatContent;
    }

    public void setConsumerChatContent(String consumerChatContent) {
        this.consumerChatContent = consumerChatContent;
    }

    private ProductModel(Parcel in) {
        this.productName = in.readString();
        this.productId = in.readString();
        this.consumerChatContent = in.readString();
        this.chatTimeStamp = in.readString();
        this.retailerModels = new ArrayList<>();
        in.readList(this.retailerModels, RetailerModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeString(this.productId);
        dest.writeString(this.consumerChatContent);
        dest.writeString(this.chatTimeStamp);
        dest.writeList(this.retailerModels);
    }

    public static final Parcelable.Creator<ProductModel> CREATOR = new Parcelable.Creator<ProductModel>() {
        public ProductModel createFromParcel(Parcel source) {
            return new ProductModel(source);
        }

        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getChatTimeStamp() {
        return chatTimeStamp;
    }

    public void setChatTimeStamp(String chatTimeStamp) {
        this.chatTimeStamp = chatTimeStamp;
    }
}
