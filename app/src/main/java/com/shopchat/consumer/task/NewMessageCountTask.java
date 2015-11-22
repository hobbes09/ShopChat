package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.NewMessageCountListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

/**
 * Created by Sudipta on 10/21/2015.
 */
public class NewMessageCountTask {
    private Context context;
    private HttpConnection httpConnect;
    private NewMessageCountListener newMessageCountListener;

    public NewMessageCountTask(Context context, NewMessageCountListener newMessageCountListener) {
        this.context = context;
        this.newMessageCountListener = newMessageCountListener;
    }

    public void fetchNewMessage() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            newMessageCountListener.onFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        newMessageCountListener.onStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        newMessageCountListener.onSuccess(jsonResponse);
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
                        newMessageCountListener.onFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_no_network));
                        newMessageCountListener.onFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String url = Constants.BASE_URL + Constants.NEW_MESSAGE_COUNT_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.post(url, null, "", HttpConnection.REQUEST_COMMON, loginModel);
    }


}
