package com.shopchat.consumer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopchat.consumer.R;
import com.shopchat.consumer.activities.LandingActivity;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.ActionBarHome;

/**
 * Created by Sudipta on 8/9/2015.
 */
public class ProfileFragment extends Fragment {

    private EditText tvName;
    private EditText tvEmail;
    private EditText tvPhone;
    private EditText tvStreet;
    private EditText tvCity;
    private EditText tvState;
    private EditText tvPin;
    private ActionBarHome actionBarHome;
    private boolean isKeyBoardVisible;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((LandingActivity) getActivity()).setSaveClickListener(saveClickListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        actionBarHome = ((LandingActivity) getActivity()).getActivityActionBar();
        populateData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, null);

        tvName = (EditText) rootView.findViewById(R.id.tv_name);
        tvEmail = (EditText) rootView.findViewById(R.id.tv_email);
        tvPhone = (EditText) rootView.findViewById(R.id.tv_phone);
      /*  tvStreet = (EditText) rootView.findViewById(R.id.tv_street_address);
        tvCity = (EditText) rootView.findViewById(R.id.tv_city);
        tvState = (EditText) rootView.findViewById(R.id.tv_state);
        tvPin = (EditText) rootView.findViewById(R.id.tv_pin_code);*/

        // To catch keyboard appear/disappear event
        final LinearLayout parentLayout = (LinearLayout) rootView.findViewById(R.id.parentLayout);
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rectRoot = new Rect();
                parentLayout.getWindowVisibleDisplayFrame(rectRoot);
                int screenHeight = parentLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - rectRoot.bottom;

                if (actionBarHome.getVisibleFragment() != null && actionBarHome.getVisibleFragment() instanceof ProfileFragment) {
                    if (keypadHeight > screenHeight * 0.05) {
                        isKeyBoardVisible = true;
                    } else {
                        isKeyBoardVisible = false;
                    }
                } else {
                    isKeyBoardVisible = false;
                }

            }
        });

        tvName.addTextChangedListener(textWatcher);
        tvEmail.addTextChangedListener(textWatcher);
        tvPhone.addTextChangedListener(textWatcher);
     /*   tvStreet.addTextChangedListener(textWatcher);
        tvCity.addTextChangedListener(textWatcher);
        tvState.addTextChangedListener(textWatcher);
        tvPin.addTextChangedListener(textWatcher);*/

        tvEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                if (result == EditorInfo.IME_ACTION_DONE) {
                    saveUpdatedData();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return rootView;
    }

    private void populateData() {
        tvName.setText(Utils.getPersistenceData(getActivity(), Constants.REGISTERED_NAME_PREFERENCE));
        tvEmail.setText(Utils.getPersistenceData(getActivity(), Constants.REGISTERED_EMAIL_PREFERENCE));
        tvPhone.setText(Utils.getPersistenceData(getActivity(), Constants.REGISTERED_PHONE_PREFERENCE));
       /* tvStreet.setText(Utils.getPersistenceData(getActivity(), Constants.PROFILE_STREET_PREFERENCE));
        tvCity.setText(Utils.getPersistenceData(getActivity(), Constants.PROFILE_CITY_PREFERENCE));
        tvState.setText(Utils.getPersistenceData(getActivity(), Constants.PROFILE_STATE_PREFERENCE));
        tvPin.setText(Utils.getPersistenceData(getActivity(), Constants.PROFILE_PIN_CODE_PREFERENCE));*/
    }


    private LandingActivity.SaveClickListener saveClickListener = new LandingActivity.SaveClickListener() {
        @Override
        public void onSaveClick() {

            saveUpdatedData();

        }


    };

    private void saveUpdatedData() {
        if (!TextUtils.isEmpty(tvName.getText())) {
            Utils.setPersistenceData(getActivity(), Constants.REGISTERED_NAME_PREFERENCE, tvName.getText().toString());
        }
        if (!TextUtils.isEmpty(tvEmail.getText())) {
            Utils.setPersistenceData(getActivity(), Constants.REGISTERED_EMAIL_PREFERENCE, tvEmail.getText().toString());
        }

        if (!TextUtils.isEmpty(tvPhone.getText())) {
            Utils.setPersistenceData(getActivity(), Constants.REGISTERED_PHONE_PREFERENCE, tvPhone.getText().toString());
        }

           /* if (!TextUtils.isEmpty(tvStreet.getText())) {
                Utils.setPersistenceData(getActivity(), Constants.PROFILE_STREET_PREFERENCE, tvStreet.getText().toString());
            }

            if (!TextUtils.isEmpty(tvCity.getText())) {
                Utils.setPersistenceData(getActivity(), Constants.PROFILE_CITY_PREFERENCE, tvCity.getText().toString());
            }

            if (!TextUtils.isEmpty(tvState.getText())) {
                Utils.setPersistenceData(getActivity(), Constants.PROFILE_STATE_PREFERENCE, tvState.getText().toString());
            }

            if (!TextUtils.isEmpty(tvPin.getText())) {
                Utils.setPersistenceData(getActivity(), Constants.PROFILE_PIN_CODE_PREFERENCE, tvPin.getText().toString());
            }*/

        // TODO Localization
        Toast.makeText(getActivity(), "Your Profile has been saved successfully!", Toast.LENGTH_SHORT).show();
        Utils.hideKeyBoard(getActivity());
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (actionBarHome != null && isKeyBoardVisible) {
                actionBarHome.showSaveMenu();
            }
        }
    };


}
