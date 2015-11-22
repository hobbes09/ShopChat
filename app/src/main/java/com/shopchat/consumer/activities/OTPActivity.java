package com.shopchat.consumer.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shopchat.consumer.R;
import com.shopchat.consumer.listener.OTPReGenerateListener;
import com.shopchat.consumer.listener.OTPVerificationListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.task.OTPRegenerateTask;
import com.shopchat.consumer.task.OTPVerificationTask;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.CustomProgress;

/**
 * Created by Sudipta on 10/6/2015.
 */
public class OTPActivity extends BaseActivity {

    private EditText edtOTPNumber;
    private CustomProgress progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setUpSupportActionBar(false, false, getString(R.string.app_name));
        changeHamburgerIcon(R.drawable.ic_launcher);

        initViews();
    }

    private void initViews() {
        edtOTPNumber = (EditText) findViewById(R.id.edt_OTP_number);
        TextView tvResend = (TextView) findViewById(R.id.tv_resend);
        final Button btnSendCode = (Button) findViewById(R.id.btn_send_code);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasOTPFieldValidated()) {
                    initOtpVerificationTask();
                }
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initOtpRegenerationTask();
            }
        });

    }

    private void initOtpRegenerationTask() {

        progressDialog = Utils.getProgressDialog(this);

        OTPReGenerateListener otpReGenerateListener = new OTPReGenerateListener() {
            @Override
            public void onOTPGenerateStart() {
                progressDialog.show();
            }

            @Override
            public void onOTPGenerateSuccess() {
                progressDialog.dismiss();
                // TODO Localization
                Toast.makeText(OTPActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOTPGenerateFailure(ErrorModel errorModel) {
                progressDialog.dismiss();
                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(OTPActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(OTPActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_BAD_REQUEST:
                        Utils.showGenericDialog(OTPActivity.this, errorModel.getErrorMessage());
                        break;
                    default:
                        break;
                }

            }
        };

        OTPRegenerateTask otpRegenerateTask = new OTPRegenerateTask(this, otpReGenerateListener, Utils.getPersistenceData(this, Constants.REGISTERED_PHONE_PREFERENCE));
        otpRegenerateTask.generateOTP();
    }

    private void initOtpVerificationTask() {
        progressDialog = Utils.getProgressDialog(this);
        OTPVerificationListener otpVerificationListener = new OTPVerificationListener() {
            @Override
            public void onOTPVerificationStart() {
                progressDialog.show();
            }

            @Override
            public void onOTPVerificationSuccess() {
                progressDialog.dismiss();
                callSplashActivity();
            }

            @Override
            public void onOTPVerificationFailure(ErrorModel errorModel) {
                progressDialog.dismiss();
                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(OTPActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(OTPActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_BAD_REQUEST:
                        Utils.showGenericDialog(OTPActivity.this, errorModel.getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        };

        OTPVerificationTask otpGenerateTask = new OTPVerificationTask(this, otpVerificationListener,
                Utils.getPersistenceData(this, Constants.REGISTERED_PHONE_PREFERENCE), edtOTPNumber.getText().toString());
        otpGenerateTask.generateOTP();
    }

    private void callSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean hasOTPFieldValidated() {
        boolean hasValidated = false;

        if (TextUtils.isEmpty(edtOTPNumber.getText())) {
            Utils.showGenericDialog(this, R.string.enter_OTP_code);
        } else {
            hasValidated = true;
        }

        return hasValidated;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        this.registerReceiver(this.receiver, filter);
    }

    public void onPause() {
        super.onPause();

        this.unregisterReceiver(this.receiver);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String msgBody = msgs[i].getMessageBody();
                        onSMSReceived(Utils.getDigitFromSms(msgBody));
                        Log.d(Constants.TAG, Utils.getDigitFromSms(msgBody));
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    };

    private void onSMSReceived(String digitFromSms) {
        edtOTPNumber.setText(digitFromSms);
    }

    @Override
    public void onBackPressed() {
        Utils.showExitConfirmationDialog(this);
    }
}
