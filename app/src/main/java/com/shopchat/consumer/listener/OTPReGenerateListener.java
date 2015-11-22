package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;

/**
 * Created by Sudipta on 10/6/2015.
 */
public abstract class OTPReGenerateListener {

    public abstract void onOTPGenerateStart();

    public abstract void onOTPGenerateSuccess();

    public abstract void onOTPGenerateFailure(ErrorModel errorModel);

}
