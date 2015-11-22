package com.shopchat.consumer.task;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.ChatListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/26/2015.
 */
public class ChatTask {

    private Context context;
    private HttpConnection httpConnect;
    private ChatListener chatListener;
    private String pageCounter = "0";

    public ChatTask(Context context, ChatListener chatListener, String pageCounter) {
        this.context = context;
        this.chatListener = chatListener;
        this.pageCounter = pageCounter;
    }

    public void fetchChat() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            chatListener.onChatFetchFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        chatListener.onChatFetchStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        chatListener.onChatFetchSuccess(getChatList(jsonResponse));
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_server));
                        chatListener.onChatFetchFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        chatListener.onChatFetchFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String chatUrl = Constants.BASE_URL + Constants.CHAT_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.post(chatUrl, null, getRequestBody(), HttpConnection.REQUEST_COMMON, loginModel);
    }

    private String getRequestBody() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNumber", pageCounter);
            jsonObject.put("size", "5");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }

    public List<ProductModel> getChatList(String response){
        List<ProductModel> productModelList = new ArrayList<ProductModel>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray contentArray = jsonObject.getJSONArray("content");
            for (int ii = 0; ii < contentArray.length(); ii++) {
                //ChatModel chatModel = new ChatModel();
                JSONObject contentObject = contentArray.getJSONObject(ii);
                JSONObject productObject = contentObject.getJSONObject("product");

                ProductModel productModel = new ProductModel();
                productModel.setProductId(productObject.getString("id"));
                productModel.setProductName(productObject.getString("productName"));
                productModel.setChatTimeStamp(productObject.getString("lastModifiedDate"));

                List<RetailerModel> retailerModelList = new ArrayList<RetailerModel>();
                JSONArray answerArray = contentObject.getJSONArray("answers");
                for (int jj = 0; jj < answerArray.length(); jj++) {
                    JSONObject answerObject = answerArray.getJSONObject(jj);
                    JSONObject retailerObject = answerObject.getJSONObject("retailer");
                    RetailerModel retailerModel = new RetailerModel();
                    retailerModel.setRetailerId(retailerObject.getString("id"));
                    retailerModel.setRetailerName(retailerObject.getString("shopName"));
                    retailerModel.setRetailerChatContent(answerObject.getString("answerText"));
                    retailerModel.setChatTimeStamp(answerObject.getString("lastModifiedDate"));
                    retailerModelList.add(retailerModel);
                }

                productModel.setRetailerModels(retailerModelList);
                productModel.setConsumerChatContent(contentObject.getString("questionText"));
                productModelList.add(productModel);

                JSONObject pageObject = jsonObject.getJSONObject("page");
                String totalPage = pageObject.getString("totalPages");
                Utils.getShopChatApplication((Activity) context).setNumberOfChatPage(Integer.parseInt(totalPage));

                String currentPage = pageObject.getString("number");
                Utils.getShopChatApplication((Activity) context).setCurrentChatPageNumber(Integer.parseInt(currentPage));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return productModelList;

    }

    private MultiMap<String, ProductModel> getChatMap(String response) {
        MultiMap<String, ProductModel> chatMap = new MultiValueMap<String, ProductModel>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray contentArray = jsonObject.getJSONArray("content");
            for (int ii = 0; ii < contentArray.length(); ii++) {
                //ChatModel chatModel = new ChatModel();
                JSONObject contentObject = contentArray.getJSONObject(ii);
                JSONObject productObject = contentObject.getJSONObject("product");

                ProductModel productModel = new ProductModel();
                productModel.setProductId(productObject.getString("id"));
                productModel.setProductName(productObject.getString("productName"));
                productModel.setChatTimeStamp(productObject.getString("lastModifiedDate"));

                List<RetailerModel> retailerModelList = new ArrayList<RetailerModel>();
                JSONArray answerArray = contentObject.getJSONArray("answers");
                for (int jj = 0; jj < answerArray.length(); jj++) {
                    JSONObject answerObject = answerArray.getJSONObject(jj);
                    JSONObject retailerObject = answerObject.getJSONObject("retailer");
                    RetailerModel retailerModel = new RetailerModel();
                    retailerModel.setRetailerId(retailerObject.getString("id"));
                    retailerModel.setRetailerName(retailerObject.getString("shopName"));
                    retailerModel.setRetailerChatContent(answerObject.getString("answerText"));
                    retailerModel.setChatTimeStamp(answerObject.getString("lastModifiedDate"));
                    retailerModelList.add(retailerModel);
                }

                productModel.setRetailerModels(retailerModelList);
                productModel.setConsumerChatContent(contentObject.getString("questionText"));
                chatMap.put(productModel.getProductId(), productModel);

                JSONObject pageObject = jsonObject.getJSONObject("page");
                String totalPage = pageObject.getString("totalPages");
                Utils.getShopChatApplication((Activity) context).setNumberOfChatPage(Integer.parseInt(totalPage));

                String currentPage = pageObject.getString("number");
                Utils.getShopChatApplication((Activity) context).setCurrentChatPageNumber(Integer.parseInt(currentPage));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return chatMap;
    }

}
