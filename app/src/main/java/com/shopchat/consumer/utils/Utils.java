package com.shopchat.consumer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.activities.SplashActivity;
import com.shopchat.consumer.views.CustomProgress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Sudipta on 8/30/2015.
 */
public class Utils {

    /**
     * @param context
     * @return
     */
    public static String getDevicePhoneNumber(Context context) {
        String phoneNumber = null;
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = tMgr.getLine1Number();
        return phoneNumber;

    }

    /**
     * @param context
     * @param subject
     * @param body
     */
    public static void callShareIntent(Context context, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, "Share with"));
    }

    /**
     * @param context
     */
    public static void showExitConfirmationDialog(final Activity context) {
        new AlertDialog.Builder(context)
                .setMessage(R.string.exit_msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * @param context
     */


    public static void showGenericDialog(Context context, int msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static void showGenericDialog(final Context context, String msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (context instanceof SplashActivity) {
                            ((SplashActivity) context).finish();
                        }
                    }
                })
                .show();
    }

    public static void showNetworkDisableDialog(final Context context, String msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void setPersistenceData(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static String getPersistenceData(Context context, String key) {
        String value = null;
        SharedPreferences sharedPreferences = getShredPreference(context);
        value = sharedPreferences.getString(key, "");
        return value;
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void setPersistenceBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static boolean getPersistenceBoolean(Context context, String key) {
        boolean value;
        SharedPreferences sharedPreferences = getShredPreference(context);
        value = sharedPreferences.getBoolean(key, false);
        return value;
    }

    /**
     * @param context
     * @return
     */
    public static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPreferences = getShredPreference(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor;
    }

    /**
     * @param context
     * @return
     */
    public static SharedPreferences getShredPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHOP_CHAT_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void hideKeyBoard(Activity context, View view) {
        view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyBoard(Activity context) {
        if (context.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * @param context
     * @return
     */
    public static boolean isConnectionPossible(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());

    }

    public static ShopChatApplication getShopChatApplication(Activity context) {
        return (ShopChatApplication) context.getApplication();
    }

    public static CustomProgress getProgressDialog(Context context) {
        // TODO Localization
        CustomProgress progressDialog = new CustomProgress(context, "Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static String getFirstCharacter(String string) {
        String character = "";
        if (!TextUtils.isEmpty(string)) {
            character = string.substring(0, 1);
        }

        return character.toUpperCase();
    }

    public static String getChatTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        String date = String.valueOf(calendar.get(Calendar.DATE));

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(calendar.getTime());

        return new StringBuilder().append(date).append(" ").append(month).append(" ").append(time).toString();
    }

    public static String getDigitFromSms(String msg) {
        return msg.replaceAll("[^0-9]", "").trim();
    }
}
