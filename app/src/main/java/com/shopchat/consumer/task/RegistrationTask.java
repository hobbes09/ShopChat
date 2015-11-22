package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.listener.RegistrationListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.RegistrationModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sudipta on 10/7/2015.
 */
public class RegistrationTask {

    private Context context;
    private HttpConnection httpConnect;
    private RegistrationListener registrationListener;
    private RegistrationModel registrationModel;

    public RegistrationTask(Context context, RegistrationListener registrationListener, RegistrationModel registrationModel) {
        this.context = context;
        this.registrationListener = registrationListener;
        this.registrationModel = registrationModel;

    }

    public void initRegistration() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            registrationListener.onRegistrationFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        registrationListener.onRegistrationStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        registrationListener.onRegistrationSuccess();
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        String jsonUnsuccessResponse = (String) message.obj;
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_BAD_REQUEST);
                        errorModel.setErrorMessage(getMessage(jsonUnsuccessResponse));
                        if (getMessage(jsonUnsuccessResponse).equalsIgnoreCase(Constants.USER_EXISTS)) {
                            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_CONFLICT);
                        }
                        // TODO used actual text
                        registrationListener.onRegistrationFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        registrationListener.onRegistrationFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String otpGenerateUrl = Constants.BASE_URL + Constants.OTP_REGISTRATION_URL;
        httpConnect.post(otpGenerateUrl, null, getRequest(), HttpConnection.REQUEST_OTP_REGISTRATION, new LoginModel());
    }

    private String getMessage(String jsonUnsuccessResponse) {
        String response = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonUnsuccessResponse);
            response = jsonObject.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getRequest() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", registrationModel.getPhoneNumber());
            jsonObject.put("email", registrationModel.getEmail());
            jsonObject.put("name", registrationModel.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }


}
