package com.shopchat.consumer;

import android.app.Application;
import android.text.TextUtils;

import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.models.ChatBoardModel;
import com.shopchat.consumer.models.ChatDisplayModel;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sudipta on 8/7/2015.
 */
public class ShopChatApplication extends Application {

    public static final String TAG = ShopChatApplication.class
            .getSimpleName();


    private static ShopChatApplication mInstance;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QZy4sVDAzc4EnBUfqqaQT4eAd";
    private static final String TWITTER_SECRET = "lR7tFN8ybzhKJDTZ0Shfobs5CJTrnAj0VBQ4wvdj5kWsNOggkb";

    private boolean isAuthenticated;
    private String authenticatedPhoneNumber;


    private boolean isRegistered;

    private List<RetailerModel> selectedRetailerList;

    private String cookieName;
    private String cookieValue;

    private LoginModel loginModel;
    private List<CategoryModel> categoryModelList;
    private List<CityModel> cityModelList;
    private List<ChatBoardModel> chatBoardModels;
    private List<ChatDisplayModel> chatDisplayModels = new ArrayList<ChatDisplayModel>();
    private MultiMap<String, ProductModel> chatMap = new MultiValueMap<String, ProductModel>();
    private int numberOfChatPage = 0;
    private int currentChatPageNumber = 0;

    private MultiMap<String, ProductModel> chatDisplayMap = new MultiValueMap<String, ProductModel>();
    private List<ProductModel> chatList = new ArrayList<ProductModel>();


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (!Utils.getPersistenceBoolean(this, Constants.IS_REGISTERED_PREFERENCE)) {
            //authenticateByOTP();
            isRegistered = false;
        } else {
            isRegistered = true;
        }

    }



    public boolean hasAuthenticationSuccess() {
        return isAuthenticated;
    }

    public String getAuthenticatedPhoneNumber() {
        return this.authenticatedPhoneNumber;
    }


    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public List<RetailerModel> getRetailerList() {
        return selectedRetailerList;
    }

    public void setRetailerList(List<RetailerModel> retailerList) {
        this.selectedRetailerList = retailerList;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public List<CategoryModel> getCategoryModelList() {
        return categoryModelList;
    }

    public void setCategoryModelList(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    public List<CityModel> getCityModelList() {
        return cityModelList;
    }

    public void setCityModelList(List<CityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }

    public List<ChatBoardModel> getChatBoardModels() {
        return chatBoardModels;
    }

    public void setChatBoardModels(List<ChatBoardModel> chatBoardModels) {
        this.chatBoardModels = chatBoardModels;
    }

    public List<ChatDisplayModel> getChatDisplayModels() {
        return chatDisplayModels;
    }

    public void setChatDisplayModels(List<ChatDisplayModel> chatDisplayModels) {
        this.chatDisplayModels = chatDisplayModels;
    }

    public MultiMap<String, ProductModel> getChatMap() {
        return chatMap;
    }

    public void setChatMap(MultiMap<String, ProductModel> chatMap) {
        this.chatMap = chatMap;
    }

    public MultiMap<String, ProductModel> getChatDisplayMap() {
        return chatDisplayMap;
    }

    public void setChatDisplayMap(MultiMap<String, ProductModel> chatDisplayMap) {
        this.chatDisplayMap = chatDisplayMap;
    }

    public int getNumberOfChatPage() {
        return numberOfChatPage;
    }

    public void setNumberOfChatPage(int numberOfChatPage) {
        this.numberOfChatPage = numberOfChatPage;
    }

    public int getCurrentChatPageNumber() {
        return currentChatPageNumber;
    }

    public void setCurrentChatPageNumber(int currentChatPageNumber) {
        this.currentChatPageNumber = currentChatPageNumber;
    }

    public List<ProductModel> getChatList() {
        return chatList;
    }

    public void setChatList(List<ProductModel> chatList) {
        this.chatList = chatList;
    }
}
