package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;

/**
 * Created by Sudipta on 10/21/2015.
 */
public abstract class NewMessageCountListener {

    public abstract void onStart();
    public abstract void onSuccess(String response);
    public abstract void onFailure(ErrorModel errorModel);
}
