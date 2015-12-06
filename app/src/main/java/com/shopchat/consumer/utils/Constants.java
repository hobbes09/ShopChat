package com.shopchat.consumer.utils;

/**
 * Created by Sudipta on 8/7/2015.
 */
public class Constants {

    public static String TAG = "ShopChat";
    public static final String BASE_URL = "http://shopchat-dev-env.elasticbeanstalk.com";
    public static final String CATEGORY_URL = "/products/getCategories.json";
    public static final String PRODUCT_URL = "/products/getProducts.json";
    public static final String CITY_URL = "/useroperation/getAllCities.json";
    public static final String LOCATION_URL = "/useroperation/getLocality.json";
    public static final String RETAILER_URL = "/products/getRetailersByProductName.json";
    public static final String CHAT_URL = "/useroperation/getAllQuestion.json";
    public static final String CHAT_SUBMIT_URL = "/useroperation/submitQuestion.json";
    public static final String OTP_REGISTRATION_URL = "/user/registration.json";
    public static final String OTP_VERIFICATION_URL = "/user/registrationConfirm.json";
    public static final String OTP_REGENERATION_URL = "/user/regenerateOtp.json";
    public static final String NEW_MESSAGE_COUNT_URL = "/useroperation/newUserDashBoardCount.json";
    public static final String LOGIN_URL = "/login";


    public static final String SHOP_CHAT_PREFERENCE = "shop_chat_preference";
    public static final String CITY_PREFERENCE = "city_preference";
    public static final String LOCATION_PREFERENCE = "location_preference";
    public static final String IS_REGISTERED_PREFERENCE = "is_registered_preference";
    public static final String REGISTERED_NAME_PREFERENCE = "registered_name_preference";
    public static final String REGISTERED_PHONE_PREFERENCE = "registered_phone_preference";
    public static final String REGISTERED_EMAIL_PREFERENCE = "registered_email_preference";
    public static final String PROFILE_STREET_PREFERENCE = "profile_street_preference";
    public static final String PROFILE_CITY_PREFERENCE = "profile_city_preference";
    public static final String PROFILE_STATE_PREFERENCE = "profile_state_preference";
    public static final String PROFILE_PIN_CODE_PREFERENCE = "profile_pin_code_preference";

    public static final String OTP_VERIFICATION_REQ = "OTP_VERIFICATION_REQ";
    public static final String OTP_DOESNT_MATCH = "OTP_DOESNT_MATCH";

    public static final String USER_NOT_FOUND = "UserNotFound";
    public static final String USER_EXISTS = "UserAlreadyExist";

    public static int SELECTED_POSITION = -1;

}
