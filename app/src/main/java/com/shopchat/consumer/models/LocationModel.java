package com.shopchat.consumer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sudipta on 9/10/2015.
 */
public class LocationModel implements Parcelable {
    private String locationId;
    private String locationName;

    public LocationModel(String locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public LocationModel() {

    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private LocationModel(Parcel in) {
        this.locationId = in.readString();
        this.locationName = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationId);
        dest.writeString(locationName);
    }

    public static final Parcelable.Creator<LocationModel> CREATOR = new Parcelable.Creator<LocationModel>() {
        public LocationModel createFromParcel(Parcel source) {
            return new LocationModel(source);
        }

        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };
}
