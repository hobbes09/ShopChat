package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;

/**
 * Created by Sudipta on 10/7/2015.
 */
public abstract class RegistrationListener {
    public abstract void onRegistrationStart();

    public abstract void onRegistrationSuccess();

    public abstract void onRegistrationFailure(ErrorModel errorModel);


}
