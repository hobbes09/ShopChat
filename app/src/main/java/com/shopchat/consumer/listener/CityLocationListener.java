package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LocationModel;

import java.util.List;

/**
 * Created by Sudipta on 9/26/2015.
 */
public abstract class CityLocationListener {

    public abstract void onCityLocationFetchStart();

    public abstract void onCityLocationFetchSuccess(List<LocationModel> locationModels);

    public abstract void onCityLocationFetchFailure(ErrorModel errorModel);
}
