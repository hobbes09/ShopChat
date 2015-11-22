package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.models.ErrorModel;

import java.util.List;

/**
 * Created by Sudipta on 9/24/2015.
 */
public abstract class CategoryListener {
    public abstract void onCategoryFetchStart();

    public abstract void onCategoryFetchSuccess(List<CategoryModel> categoryModelList);

    public abstract void onCategoryFetchFailure(ErrorModel errorModel);
}
