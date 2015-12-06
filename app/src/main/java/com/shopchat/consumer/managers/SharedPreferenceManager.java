package com.shopchat.consumer.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sourin on 05/12/15.
 */
public class SharedPreferenceManager {

    private static final String NAME_SHARED_PREFERENCE = "LokalChatSharedPreference";

    public static String KEY_TOTAL_INBOX_CONVERSATIONS = "key_total_inbox_conversations";
    public static String KEY_TOTAL_INBOX_PAGES = "key_total_inbox_pages";
    public static String KEY_INBOX_LATEST_PAGE = "key_inbox_latest_page";

    public static SharedPreferences.Editor getSharedPreferenceEditor(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void saveSharedPreferences(SharedPreferences.Editor editor){
        editor.apply();
    }

    public static void saveSharedPreferencesInMainThread(SharedPreferences.Editor editor){
        editor.commit();
    }

    // ================ Operations

    public static String getValueFromSharedPreference(SharedPreferences mSharedPreferences, String key){
        return mSharedPreferences.getString(key, null);
    }

    public static void setValueInSharedPreference(SharedPreferences.Editor editor, String key, String newValue){
        editor.putString(key, newValue);
    }

    public static void deleteValueFromSharedPreference(SharedPreferences.Editor editor, String key){
        editor.remove(key);
    }

    public static void clearAllValuesFromSharedPreference(SharedPreferences.Editor editor){
        editor.clear();
    }

}
