package com.shopchat.consumer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shopchat.consumer.R;
import com.shopchat.consumer.listener.RegistrationListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.RegistrationModel;
import com.shopchat.consumer.task.RegistrationTask;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.CustomProgress;

/**
 * Created by Sudipta on 9/5/2015.
 */
public class SignUpActivity extends BaseActivity {

    public static final String PHONE_NUMBER = "phone_number";
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private String phoneNumber;
    private CustomProgress progressDialog;


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpSupportActionBar(false, false, getString(R.string.sign_up));
        changeHamburgerIcon(R.drawable.ic_launcher);

        initViews();
    }

    private void initViews() {

        editTextName = (EditText) findViewById(R.id.edt_name);
        editTextPhone = (EditText) findViewById(R.id.edt_phone);


        editTextEmail = (EditText) findViewById(R.id.edt_email);

        final Button btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPhoneNumberValidated() && hasEmailValidated()) {
                    initRegistrationTask();
                }
            }
        });
    }


    private void initRegistrationTask() {

        progressDialog = Utils.getProgressDialog(this);

        RegistrationListener registrationListener = new RegistrationListener() {
            @Override
            public void onRegistrationStart() {
                progressDialog.show();
            }

            @Override
            public void onRegistrationSuccess() {
                progressDialog.dismiss();
                saveDataInPreference();
                callOTPActivity();
            }

            @Override
            public void onRegistrationFailure(ErrorModel errorModel) {
                progressDialog.dismiss();
                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(SignUpActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(SignUpActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_BAD_REQUEST:
                        saveDataInPreference();
                        callOTPActivity();
                    case ERROR_TYPE_CONFLICT:
                        saveDataInPreference();
                        callSplashActivity();
                        break;
                    default:
                        break;
                }
            }
        };

        RegistrationModel registrationModel = new RegistrationModel();
        registrationModel.setName(editTextName.getText().toString());
        registrationModel.setPhoneNumber(editTextPhone.getText().toString());
        registrationModel.setEmail(editTextEmail.getText().toString());
        RegistrationTask registrationTask = new RegistrationTask(this, registrationListener, registrationModel);
        registrationTask.initRegistration();

    }

    private void callSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }

    private void callOTPActivity() {
        Intent intent = new Intent(this, OTPActivity.class);
        startActivity(intent);
        this.finish();
    }

    private boolean hasPhoneNumberValidated() {
        boolean hasValidated;

        if (TextUtils.isEmpty(editTextPhone.getText())) {
            hasValidated = false;
            Utils.showGenericDialog(this, R.string.phone_number_mandatory);
        } else if (editTextPhone.length() != 10) {
            hasValidated = false;
            Utils.showGenericDialog(this, R.string.phone_digit_validation);
        } else {
            hasValidated = true;
        }

        return hasValidated;
    }

    private boolean hasEmailValidated() {
        boolean hasValidated = false;

        if (TextUtils.isEmpty(editTextEmail.getText())) {
            hasValidated = true;
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText()).matches()) {
                hasValidated = true;
            } else {
                Utils.showGenericDialog(this, R.string.invalid_email);
            }
        }

        return hasValidated;
    }



    private void saveDataInPreference() {
        Utils.setPersistenceData(this, Constants.REGISTERED_NAME_PREFERENCE, editTextName.getText().toString());

        Utils.setPersistenceData(this, Constants.REGISTERED_EMAIL_PREFERENCE, editTextEmail.getText().toString());

        Utils.setPersistenceData(this, Constants.REGISTERED_PHONE_PREFERENCE, editTextPhone.getText().toString());

        Utils.setPersistenceBoolean(this, Constants.IS_REGISTERED_PREFERENCE, true);
    }

    @Override
    public void onBackPressed() {
        Utils.showExitConfirmationDialog(this);
    }
}
