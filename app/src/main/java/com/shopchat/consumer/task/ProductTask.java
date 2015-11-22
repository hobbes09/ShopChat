package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.ProductListener;
import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/26/2015.
 */
public class ProductTask {
    private Context context;
    private HttpConnection httpConnect;
    private ProductListener productListener;
    private CategoryModel categoryModel;

    public ProductTask(Context context, ProductListener productListener, CategoryModel categoryModel) {
        this.context = context;
        this.productListener = productListener;
        this.categoryModel = categoryModel;

    }

    public void fetchProduct() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            productListener.onProductFetchFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        productListener.onProductFetchStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        productListener.onProductFetchSuccess(getProductList(jsonResponse));
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_server));
                        productListener.onProductFetchFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        productListener.onProductFetchFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String productUrl = Constants.BASE_URL + Constants.PRODUCT_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.post(productUrl, null, getRequestBody(), HttpConnection.REQUEST_COMMON, loginModel);
    }

    private String getRequestBody() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category", categoryModel.getItemName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }

    private List<ProductModel> getProductList(String response) {
        List<ProductModel> productModels = new ArrayList<ProductModel>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int ii = 0; ii < jsonArray.length(); ii++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(ii);
                ProductModel productModel = new ProductModel();
                productModel.setProductId(jsonObject.getString("id"));
                productModel.setProductName(jsonObject.getString("productName"));
                productModels.add(productModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productModels;
    }
}
