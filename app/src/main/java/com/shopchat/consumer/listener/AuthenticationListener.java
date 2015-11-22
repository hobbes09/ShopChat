
package com.shopchat.consumer.listener;


import com.shopchat.consumer.models.ErrorModel;

public abstract class AuthenticationListener {

    //public abstract void onAuthenticationStart(CustomProgress progressDialog);*/

    public abstract void onAuthenticationSuccess();

    public abstract void onAuthenticationFailure(ErrorModel errorModel);

}
