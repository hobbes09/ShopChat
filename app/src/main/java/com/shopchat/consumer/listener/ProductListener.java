package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.ProductModel;

import java.util.List;

/**
 * Created by Sudipta on 9/26/2015.
 */
public abstract class ProductListener {

    public abstract void onProductFetchStart();

    public abstract void onProductFetchSuccess(List<ProductModel> productModels);

    public abstract void onProductFetchFailure(ErrorModel errorModel);
}
