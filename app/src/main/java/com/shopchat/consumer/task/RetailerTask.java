package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.RetailerListener;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.models.RetailerModel;
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
public class RetailerTask {
    private Context context;
    private HttpConnection httpConnect;
    private RetailerListener retailerListener;
    private ProductModel productModel;

    public RetailerTask(Context context, RetailerListener retailerListener, ProductModel productModel) {
        this.context = context;
        this.retailerListener = retailerListener;
        this.productModel = productModel;

    }

    public void fetchRetailer() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            retailerListener.onRetailerFetchFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        retailerListener.onRetailerFetchStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        retailerListener.onRetailerFetchSuccess(getRetailerList(jsonResponse));
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
                        retailerListener.onRetailerFetchFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_no_network));
                        retailerListener.onRetailerFetchFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String retailerUrl = Constants.BASE_URL + Constants.RETAILER_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.post(retailerUrl, null, getRequestBody(), HttpConnection.REQUEST_COMMON, loginModel);
    }

    private String getRequestBody() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productName", productModel.getProductName());
            final String selectedLocation = Utils.getPersistenceData(this.context, Constants.LOCATION_PREFERENCE);
            jsonObject.put("locality", selectedLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }

    private List<RetailerModel> getRetailerList(String response) {
        List<RetailerModel> retailerModels = new ArrayList<RetailerModel>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int ii = 0; ii < jsonArray.length(); ii++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(ii);
                RetailerModel retailerModel = new RetailerModel();
                retailerModel.setRetailerId(jsonObject.getString("id"));
                retailerModel.setRetailerName(jsonObject.getString("shopName"));
                retailerModels.add(retailerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retailerModels;
    }
}
