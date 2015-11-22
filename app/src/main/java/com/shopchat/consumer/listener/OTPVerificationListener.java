package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;

/**
 * Created by Sudipta on 10/6/2015.
 */
public abstract class OTPVerificationListener {

    public abstract void onOTPVerificationStart();

    public abstract void onOTPVerificationSuccess();

    public abstract void onOTPVerificationFailure(ErrorModel errorModel);
}
