package com.shopchat.consumer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/13/2015.
 */
public class CategoryModel implements Parcelable {
    private String itemName;
    private String itemPictureUrl;
    private List<ProductModel> productModels;

    public CategoryModel(String itemName, String itemPictureUrl) {
        this.itemName = itemName;
        this.itemPictureUrl = itemPictureUrl;
    }

    public CategoryModel() {

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPictureUrl() {
        return itemPictureUrl;
    }

    public void setItemPictureUrl(String itemPictureUrl) {
        this.itemPictureUrl = itemPictureUrl;
    }

    public List<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductModel> productModels) {
        this.productModels = productModels;
    }

    private CategoryModel(Parcel in) {
        this.itemName = in.readString();
        this.itemPictureUrl = in.readString();
        this.productModels = new ArrayList<ProductModel>();
        in.readList(this.productModels, ProductModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.itemPictureUrl);
        dest.writeList(this.productModels);
    }

    public static final Parcelable.Creator<CategoryModel> CREATOR = new Parcelable.Creator<CategoryModel>() {
        public CategoryModel createFromParcel(Parcel source) {
            return new CategoryModel(source);
        }

        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}
