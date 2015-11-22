package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.listener.OTPReGenerateListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sudipta on 10/10/2015.
 */
public class OTPRegenerateTask {
    private Context context;
    private HttpConnection httpConnect;
    private OTPReGenerateListener otpReGenerateListener;
    private String phoneNumber;

    public OTPRegenerateTask(Context context, OTPReGenerateListener otpReGenerateListener, String phoneNumber) {
        this.context = context;
        this.otpReGenerateListener = otpReGenerateListener;
        this.phoneNumber = phoneNumber;
    }

    public void generateOTP() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            otpReGenerateListener.onOTPGenerateFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        otpReGenerateListener.onOTPGenerateStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        otpReGenerateListener.onOTPGenerateSuccess();
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_BAD_REQUEST);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getString(R.string.otp_sending_failed));
                        otpReGenerateListener.onOTPGenerateFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        otpReGenerateListener.onOTPGenerateFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String otpRegenerationUrl = Constants.BASE_URL + Constants.OTP_REGENERATION_URL;
        httpConnect.post(otpRegenerationUrl, null, getRequest(), HttpConnection.REQUEST_OTP_REGENERATION, new LoginModel());
    }

    private String getRequest() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNumber", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }
}
