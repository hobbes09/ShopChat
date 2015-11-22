package com.shopchat.consumer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Sudipta on 8/15/2015.
 */
public class RetailerModel implements Parcelable{
    private String retailerName;
    private String retailerId;
    private String retailerChatContent;
    private String chatTimeStamp;
    private boolean selected;



    public RetailerModel(){

    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRetailerChatContent() {
        return retailerChatContent;
    }

    public void setRetailerChatContent(String retailerChatContent) {
        this.retailerChatContent = retailerChatContent;
    }

    private RetailerModel(Parcel in) {
        this.retailerName = in.readString();
        this.retailerId = in.readString();
        this.retailerChatContent = in.readString();
        this.chatTimeStamp = in.readString();
        this.selected = (in.readByte() != 0);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.retailerName);
        dest.writeString(this.retailerId);
        dest.writeString(this.retailerChatContent);
        dest.writeString(this.chatTimeStamp);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    public static final Parcelable.Creator<RetailerModel> CREATOR = new Parcelable.Creator<RetailerModel>() {
        public RetailerModel createFromParcel(Parcel source) {
            return new RetailerModel(source);
        }

        public RetailerModel[] newArray(int size) {
            return new RetailerModel[size];
        }
    };

    public String getChatTimeStamp() {
        return chatTimeStamp;
    }

    public void setChatTimeStamp(String chatTimeStamp) {
        this.chatTimeStamp = chatTimeStamp;
    }
}
