package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.CityLocationListener;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LocationModel;
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
 * Created by Sudipta on 9/26/2015.
 */
public class CityLocationTask {

    private Context context;
    private HttpConnection httpConnect;
    private CityLocationListener cityLocationListener;
    private CityModel cityModel;

    public CityLocationTask(Context context, CityLocationListener cityLocationListener, CityModel cityModel) {
        this.context = context;
        this.cityLocationListener = cityLocationListener;
        this.cityModel = cityModel;

    }

    public void fetchCityLocation() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            cityLocationListener.onCityLocationFetchFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        cityLocationListener.onCityLocationFetchStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        cityLocationListener.onCityLocationFetchSuccess(getCityLocationList(jsonResponse));
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_server));
                        cityLocationListener.onCityLocationFetchFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        cityLocationListener.onCityLocationFetchFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String cityLocationUrl = Constants.BASE_URL + Constants.LOCATION_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.post(cityLocationUrl, null, getRequestBody(), HttpConnection.REQUEST_COMMON, loginModel);
    }

    private String getRequestBody() {
        String requestBody = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city", cityModel.getCityName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonObject.toString();
        return requestBody;
    }

    private List<LocationModel> getCityLocationList(String response) {
        List<LocationModel> locationModels = new ArrayList<LocationModel>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int ii = 0; ii < jsonArray.length(); ii++) {
                LocationModel locationModel = new LocationModel();
                locationModel.setLocationName((String) jsonArray.get(ii));
                locationModels.add(locationModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return locationModels;
    }
}
