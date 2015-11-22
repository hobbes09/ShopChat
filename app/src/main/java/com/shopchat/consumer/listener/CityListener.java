package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.ErrorModel;

import java.util.List;

/**
 * Created by Sudipta on 9/25/2015.
 */
public abstract class CityListener {

    public abstract void onCityFetchedStart();

    public abstract void onCityFetchSuccess(List<CityModel> cityModelList);

    public abstract void onCityFetchFailure(ErrorModel errorModel);

}
