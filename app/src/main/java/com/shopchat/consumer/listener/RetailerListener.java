package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.RetailerModel;

import java.util.List;

/**
 * Created by Sudipta on 9/26/2015.
 */
public abstract class RetailerListener {

    public abstract void onRetailerFetchStart();

    public abstract void onRetailerFetchSuccess(List<RetailerModel> retailerModels);

    public abstract void onRetailerFetchFailure(ErrorModel errorModel);
}
