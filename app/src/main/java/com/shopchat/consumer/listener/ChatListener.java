package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.ProductModel;

import org.apache.commons.collections4.MultiMap;

import java.util.List;

/**
 * Created by Sudipta on 9/26/2015.
 */
public abstract class ChatListener {

    public abstract void onChatFetchStart();

    //public abstract void onChatFetchSuccess(MultiMap<String, ProductModel> chatMap);
    public abstract void onChatFetchSuccess(List<ProductModel> chatList);

    public abstract void onChatFetchFailure(ErrorModel errorModel);
}

