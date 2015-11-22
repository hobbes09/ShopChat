package com.shopchat.consumer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.shopchat.consumer.R;
import com.shopchat.consumer.listener.AuthenticationListener;
import com.shopchat.consumer.listener.CategoryListener;
import com.shopchat.consumer.models.AuthenticationModel;
import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.task.AuthenticationTask;
import com.shopchat.consumer.task.CategoryTask;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import java.util.List;
import java.util.Timer;


public class SplashActivity extends Activity {

    private final int SPLASH_TIMEOUT_DELAY = 3000;
    private String authenticatedPhoneNumber;
    private boolean isRegistered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();


        callTimeTask();
    }

    private void callAuthenticationFailure() {
        // TODO Handle Authentication Failure.
    }

    private void callTimeTask() {
        final Timer timer = new Timer();

        if (TextUtils.isEmpty(Utils.getPersistenceData(this, Constants.REGISTERED_PHONE_PREFERENCE))) {
            callSignUpActivity();
        } else {
            initAuthentication();
        }


    }

    private void callOTPActivity() {

        Intent intent = new Intent(this, OTPActivity.class);
        startActivity(intent);

    }

    private void initCategoryFetch() {

        final CategoryListener categoryListener = new CategoryListener() {
            @Override
            public void onCategoryFetchStart() {

            }

            @Override
            public void onCategoryFetchSuccess(List<CategoryModel> categoryModelList) {
                if (!categoryModelList.isEmpty()) {
                    Utils.getShopChatApplication(SplashActivity.this).setCategoryModelList(categoryModelList);
                    callLandingActivity();
                } else {
                    Utils.showGenericDialog(SplashActivity.this, R.string.no_data);
                }

            }

            @Override
            public void onCategoryFetchFailure(ErrorModel errorModel) {

                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(SplashActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(SplashActivity.this, errorModel.getErrorMessage());
                        break;
                    default:
                        break;
                }

            }
        };

        CategoryTask categoryTask = new CategoryTask(this, categoryListener);
        categoryTask.fetchCategory();
    }

    private void initAuthentication() {

        final AuthenticationListener authenticationListener = new AuthenticationListener() {
            @Override
            public void onAuthenticationSuccess() {

                initCategoryFetch();

            }

            @Override
            public void onAuthenticationFailure(ErrorModel errorModel) {
                // TODO Localization
                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(SplashActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(SplashActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_UNAUTHORIZED:
                        if (errorModel.getErrorMessage().equalsIgnoreCase(Constants.OTP_VERIFICATION_REQ)) {
                            callOTPActivity();
                        } else if (errorModel.getErrorMessage().equalsIgnoreCase(Constants.USER_NOT_FOUND)) {
                            callSignUpActivity();
                        }

                        break;
                    default:
                        break;
                }

            }
        };

        AuthenticationModel authenticationModel = new AuthenticationModel();
        authenticationModel.setUserId(Utils.getPersistenceData(this, Constants.REGISTERED_PHONE_PREFERENCE));

        AuthenticationTask authenticationTask = new AuthenticationTask(this,
                authenticationListener, authenticationModel);
        authenticationTask.authenticate();
    }


    private void callLandingActivity() {
        startActivity(LandingActivity.getIntent(this));
        finish();
    }

    private void callSignUpActivity() {
        startActivity(SignUpActivity.getIntent(this));
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
