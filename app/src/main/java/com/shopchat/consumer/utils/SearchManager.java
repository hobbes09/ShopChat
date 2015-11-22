package com.shopchat.consumer.utils;

import android.text.TextUtils;

import com.shopchat.consumer.models.ChatDisplayModel;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/12/2015.
 */
public class SearchManager {
    private List<Object> searchableList;
    private String searchString;
    private SearchableEnum searchableEnum;
    private SearchListener searchListener;

    public enum SearchableEnum {
        ITEM, RETAILER, CHAT, CITY, LOCATION
    }

    public SearchManager(List<Object> searchableList, String searchString, SearchableEnum searchableEnum) {
        this.searchableList = searchableList;
        this.searchString = searchString;
        this.searchableEnum = searchableEnum;
    }

    public void initSearch() {
        switch (searchableEnum) {
            case ITEM:
                searchItem();
                break;
            case RETAILER:
                searchRetailer();
                break;
            case CHAT:
                searchChat();
                break;
            case CITY:
                searchCity();
                break;
            case LOCATION:
                break;
        }
    }

    private void searchChat() {
        if (!TextUtils.isEmpty(searchString)) {

            List<Object> searchedList = new ArrayList<Object>();
            for (Object object : searchableList) {
                ChatDisplayModel chatDisplayModel = (ChatDisplayModel) object;
                if (chatDisplayModel.getProductName().toLowerCase().contains(searchString.toLowerCase())) {
                    searchedList.add(chatDisplayModel);
                }
            }

            if (searchedList.isEmpty()) {
                searchListener.onSearchFail();
            } else {
                searchListener.onSearchSuccess(searchedList);
            }
        } else {
            searchListener.onSearchStringEmpty();
        }
    }

    private void searchRetailer() {
        if (!TextUtils.isEmpty(searchString)) {

            List<Object> searchedList = new ArrayList<Object>();
            for (Object object : searchableList) {
                RetailerModel cityModel = (RetailerModel) object;
                if (cityModel.getRetailerName().toLowerCase().contains(searchString.toLowerCase())) {
                    searchedList.add(cityModel);
                }
            }

            if (searchedList.isEmpty()) {
                searchListener.onSearchFail();
            } else {
                searchListener.onSearchSuccess(searchedList);
            }
        } else {
            searchListener.onSearchStringEmpty();
        }
    }

    private void searchCity() {
        if (!TextUtils.isEmpty(searchString)) {

            List<Object> searchedList = new ArrayList<Object>();
            for (Object object : searchableList) {
                CityModel cityModel = (CityModel) object;
                if (cityModel.getCityName().toLowerCase().contains(searchString.toLowerCase())) {
                    searchedList.add(cityModel);
                }
            }

            if (searchedList.isEmpty()) {
                searchListener.onSearchFail();
            } else {
                searchListener.onSearchSuccess(searchedList);
            }
        } else {
            searchListener.onSearchStringEmpty();
        }
    }

    private void searchItem() {

        if (!TextUtils.isEmpty(searchString)) {

            List<Object> searchedList = new ArrayList<Object>();
            for (Object object : searchableList) {
                ProductModel itemModel = (ProductModel) object;
                if (itemModel.getProductName().toLowerCase().contains(searchString.toLowerCase())) {
                    searchedList.add(itemModel);
                }
            }

            if (searchedList.isEmpty()) {
                searchListener.onSearchFail();
            } else {
                searchListener.onSearchSuccess(searchedList);
            }
        } else {
            searchListener.onSearchStringEmpty();
        }
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public interface SearchListener {
        void onSearchSuccess(List<Object> searchedList);

        void onSearchFail();

        void onSearchStringEmpty();
    }
}
