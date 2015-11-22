package com.shopchat.consumer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sudipta on 10/7/2015.
 */
public class RegistrationModel implements Parcelable {

    private String name = "";
    private String phoneNumber = "";
    private String email = "";

    public RegistrationModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);

    }

    private RegistrationModel(Parcel in) {
        this.name = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();

    }

    public static final Parcelable.Creator<RegistrationModel> CREATOR = new Parcelable.Creator<RegistrationModel>() {
        public RegistrationModel createFromParcel(Parcel source) {
            return new RegistrationModel(source);
        }

        public RegistrationModel[] newArray(int size) {
            return new RegistrationModel[size];
        }
    };
}
