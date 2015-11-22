package com.shopchat.consumer.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shopchat.consumer.R;
import com.shopchat.consumer.ShopChatApplication;
import com.shopchat.consumer.listener.CityListener;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.network.HttpConnection;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudipta on 9/25/2015.
 */
public class CityTask {

    private Context context;
    private HttpConnection httpConnect;
    private CityListener cityListener;

    public CityTask(Context context, CityListener cityListener) {
        this.context = context;
        this.cityListener = cityListener;

    }

    public void fetchCity() {
        if (!Utils.isConnectionPossible(this.context)) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_NO_NETWORK);
            // TODO used actual text
            errorModel.setErrorMessage(context.getResources().getString(R.string.error_no_network));
            cityListener.onCityFetchFailure(errorModel);

        } else {
            initNetworkTask();
        }
    }

    private void initNetworkTask() {

        Handler networkHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case HttpConnection.DID_START:
                        cityListener.onCityFetchedStart();
                        break;
                    case HttpConnection.DID_SUCCEED:
                        String jsonResponse = (String) message.obj;
                        cityListener.onCityFetchSuccess(getCityList(jsonResponse));
                        break;
                    case HttpConnection.DID_UNSUCCESS:
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel.setErrorMessage(context.getResources().getString(R.string.error_server));
                        cityListener.onCityFetchFailure(errorModel);
                        break;
                    case HttpConnection.DID_ERROR:
                        ErrorModel errorModel2 = new ErrorModel();
                        errorModel2.setErrorType(ErrorModel.Error.ERROR_TYPE_SERVER);
                        // TODO used actual text
                        errorModel2.setErrorMessage(context.getResources().getString(R.string.error_server));
                        cityListener.onCityFetchFailure(errorModel2);
                        break;
                    default:
                        break;
                }
            }
        };

        httpConnect = new HttpConnection(networkHandler);

        final String cityUrl = Constants.BASE_URL + Constants.CITY_URL;
        LoginModel loginModel = ((ShopChatApplication) context.getApplicationContext()).getLoginModel();
        httpConnect.get(cityUrl, loginModel);
    }

    private List<CityModel> getCityList(String response) {
        List<CityModel> cityModelArrayList = new ArrayList<CityModel>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int ii = 0; ii < jsonArray.length(); ii++) {
                CityModel cityModel = new CityModel();
                cityModel.setCityName((String) jsonArray.get(ii));
                cityModelArrayList.add(cityModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cityModelArrayList;
    }

}
