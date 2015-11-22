package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.CategoryListener;
import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/24/2015.
 */
public class CategoryTask {

    private Context context;
    private HttpConnection httpConnect;
    private CategoryListener categoryListener;

    public CategoryTask(Context context, CategoryListener categoryListener) {
        this.context = context;
        this.categoryListener = categoryListener;

    }

    public void fetchCategory() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(this.context.getResources().getString(R.string.error_no_network));
            categoryListener.onCategoryFetchFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        categoryListener.onCategoryFetchStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        categoryListener.onCategoryFetchSuccess(getCategoryList(jsonResponse));
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_server));
                        categoryListener.onCategoryFetchFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        categoryListener.onCategoryFetchFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String categoryUrl = Constants.BASE_URL + Constants.CATEGORY_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.get(categoryUrl, loginModel);
    }

    private List<CategoryModel> getCategoryList(String response) {
        List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int ii = 0; ii < jsonArray.length(); ii++) {
                CategoryModel categoryModel = new CategoryModel();
                JSONObject jObject = jsonArray.getJSONObject(ii);
                categoryModel.setItemName(jObject.getString("category"));
                categoryModel.setItemPictureUrl(jObject.getString("imageurl"));
                categoryModelList.add(categoryModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categoryModelList;
    }
}
