package com.shopchat.consumer.listener;

import com.shopchat.consumer.models.ErrorModel;

/**
 * Created by Sudipta on 9/27/2015.
 */
public abstract class ChatBoardListener {

    public abstract void onSendChatStart();

    public abstract void onSendChatSuccess();

    public abstract void onSendChatFailure(ErrorModel errorModel);
}
