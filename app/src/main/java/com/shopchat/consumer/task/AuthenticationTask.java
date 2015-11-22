
package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.AuthenticationListener;
import com.shopchat.consumer.models.AuthenticationModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class AuthenticationTask {
    private Context context;
    private HttpConnection httpConnect;
    private AuthenticationListener authenticationListener;
    private AuthenticationModel authenticationModel;

    public AuthenticationTask(Context context, AuthenticationListener listener,
                              AuthenticationModel authenticationModel) {
        this.context = context;
        this.authenticationListener = listener;
        this.authenticationModel = authenticationModel;
    }

    public void authenticate() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            authenticationListener.onAuthenticationFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:

                       /* authenticationListener.onAuthenticationStart(Utils
                                .getProgressDialog(context));*/
                        break;
                    case HttpConnection.DID_SUCCEED:
                        LoginModel loginModel = (LoginModel) message.obj;
                        ((ShopChatApplication) context.getApplicationContext()).setLoginModel(loginModel);
                        authenticationListener.onAuthenticationSuccess();
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        String msg = (String) message.obj;
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_UNAUTHORIZED);
                        errorModel.setErrorMessage(msg);
                        authenticationListener.onAuthenticationFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        authenticationListener.onAuthenticationFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", authenticationModel.getUserId()));
        //nameValuePairs.add(new BasicNameValuePair("username", "9674642375"));
        nameValuePairs.add(new BasicNameValuePair("password", ""));
        final String authenticationUrl = Constants.BASE_URL + Constants.LOGIN_URL;
        httpConnect.post(authenticationUrl, nameValuePairs, null, HttpConnection.REQUEST_LOGIN, new LoginModel());

    }

}
