package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.OTPVerificationListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sudipta on 10/6/2015.
 */
public class OTPVerificationTask {

    private Context context;
    private HttpConnection httpConnect;
    private OTPVerificationListener otpVerificationListener;
    private String phoneNumber;
    private String otp;

    public OTPVerificationTask(Context context, OTPVerificationListener otpVerificationListener, String phoneNumber, String otp) {
        this.context = context;
        this.otpVerificationListener = otpVerificationListener;
        this.phoneNumber = phoneNumber;
        this.otp = otp;

    }

    public void generateOTP() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            otpVerificationListener.onOTPVerificationFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        otpVerificationListener.onOTPVerificationStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        otpVerificationListener.onOTPVerificationSuccess();
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_BAD_REQUEST);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getString(R.string.otp_not_match));
                        otpVerificationListener.onOTPVerificationFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        otpVerificationListener.onOTPVerificationFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String otpVerificationUrl = Constants.BASE_URL + Constants.OTP_VERIFICATION_URL;
        httpConnect.post(otpVerificationUrl, null, getRequest(),HttpConnection.REQUEST_OTP_CONFIRMATION,new LoginModel());
    }

    private String getRequest() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNumber", phoneNumber);
            jsonObject.put("otp", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }



}
