package com.shopchat.consumer.task;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.ChatBoardListener;
import com.shopchat.consumer.models.ChatBoardModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sudipta on 9/27/2015.
 */
public class ChatBoardTask {

    private Context context;
    private HttpConnection httpConnect;
    private ChatBoardListener chatBoardListener;
    private String message;
    private RetailerModel retailerModel;
    private ProductModel productModel;

    public ChatBoardTask(Context context, ChatBoardListener chatBoardListener) {
        this.context = context;
        this.chatBoardListener = chatBoardListener;
        this.message = message;
        this.retailerModel = retailerModel;
        this.productModel = productModel;

    }

    public void sendChat() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            chatBoardListener.onSendChatFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        chatBoardListener.onSendChatStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        chatBoardListener.onSendChatSuccess();
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_server));
                        chatBoardListener.onSendChatFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        chatBoardListener.onSendChatFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String chatSubmitUrl = Constants.BASE_URL + Constants.CHAT_SUBMIT_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.post(chatSubmitUrl, null, getRequestBody(), HttpConnection.REQUEST_COMMON, loginModel);
    }

    private String getRequestBody() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("question", getMessage());
            jsonObject.put("retailers", getRetailerId());
            jsonObject.put("product", getProduct());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }

    private String getRetailerId() {
        List<ChatBoardModel> chatBoardList = Utils.getShopChatApplication((Activity) this.context).getChatBoardModels();
        StringBuilder retailerBuilder = new StringBuilder();
        for (ChatBoardModel chatBoardModel : chatBoardList) {
            if (!chatBoardModel.isCustomer()) {
                RetailerModel retailerModel = chatBoardModel.getRetailerModel();
                if (retailerModel != null) {
                    retailerBuilder.append(retailerModel.getRetailerId());
                    retailerBuilder.append(",");
                }
            }
        }

        return retailerBuilder.toString();
    }

    private String getProduct() {
        List<ChatBoardModel> chatBoardList = Utils.getShopChatApplication((Activity) this.context).getChatBoardModels();
        ChatBoardModel chatBoardModel = chatBoardList.get(0);
        return chatBoardModel.getProductModel().getProductName();

    }

    private String getMessage() {
        List<ChatBoardModel> chatBoardList = Utils.getShopChatApplication((Activity) this.context).getChatBoardModels();
        ChatBoardModel chatBoardModel = chatBoardList.get(0);
        return chatBoardModel.getChatContent();
    }

}
