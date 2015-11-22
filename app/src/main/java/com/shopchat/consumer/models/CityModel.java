package com.shopchat.consumer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/10/2015.
 */
public class CityModel implements Parcelable {
    private String cityId;
    private String cityName;
    private List<LocationModel> locationModelList;

    public CityModel(String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public CityModel() {

    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<LocationModel> getLocationModelList() {
        return locationModelList;
    }

    public void setLocationModelList(List<LocationModel> locationModelList) {
        this.locationModelList = locationModelList;
    }

    private CityModel(Parcel in) {
        this.cityId = in.readString();
        this.cityName = in.readString();
        this.locationModelList = new ArrayList<>();
        in.readList(this.locationModelList, LocationModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityId);
        dest.writeString(cityName);
        dest.writeList(locationModelList);
    }

    public static final Parcelable.Creator<CityModel> CREATOR = new Parcelable.Creator<CityModel>() {
        public CityModel createFromParcel(Parcel source) {
            return new CityModel(source);
        }

        public CityModel[] newArray(int size) {
            return new CityModel[size];
        }
    };
}
