package com.shopchat.consumer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.adapters.CityListAdapter;
import com.shopchat.consumer.listener.CityLocationListener;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.ErrorModel;
import com.shopchat.consumer.models.LocationModel;
import com.shopchat.consumer.task.CityLocationTask;
import com.shopchat.consumer.utils.SearchManager;
import com.shopchat.consumer.utils.Utils;
import com.shopchat.consumer.views.CustomProgress;

import java.util.List;

/**
 * Created by Sudipta on 9/10/2015.
 */
public class CityActivity extends BaseActivity {

    private List cityModelList;
    private ListView lvCity;
    private CustomProgress progressDialog;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, CityActivity.class);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        setUpSupportActionBar(false, false, getString(R.string.select_location));
        hideHamburgerIcon();

        initViews();
    }

    private void initViews() {
        lvCity = (ListView) findViewById(R.id.lv_city);
        final EditText searchEditText = (EditText) findViewById(R.id.edtText_search);
        searchEditText.addTextChangedListener(textWatcher);

       /* CityModel cityModel1 = new CityModel("1", "Bengaluru");
        CityModel cityModel2 = new CityModel("2", "Delhi");
        CityModel cityModel3 = new CityModel("3", "Kolkata");
        CityModel cityModel4 = new CityModel("4", "Chennai");
        CityModel cityModel5 = new CityModel("5", "Mumbai");*/

        cityModelList = Utils.getShopChatApplication(this).getCityModelList();
       /* cityModelList.add(cityModel1);
        cityModelList.add(cityModel2);
        cityModelList.add(cityModel3);
        cityModelList.add(cityModel4);
        cityModelList.add(cityModel5);*/

        CityListAdapter cityListAdapter = new CityListAdapter(this, R.layout.list_row_city, cityModelList);
        lvCity.setAdapter(cityListAdapter);

        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CityModel cityModel = (CityModel) parent.getAdapter().getItem(position);
                initCityLocationFetch(cityModel);
            }
        });

    }

    private void initCityLocationFetch(final CityModel cityModel) {
        progressDialog = Utils.getProgressDialog(this);
        CityLocationListener cityLocationListener = new CityLocationListener() {
            @Override
            public void onCityLocationFetchStart() {
                progressDialog.show();
            }

            @Override
            public void onCityLocationFetchSuccess(List<LocationModel> locationModels) {
                progressDialog.dismiss();
                if (!locationModels.isEmpty()) {
                    cityModel.setLocationModelList(locationModels);
                    CityActivity.this.startActivity(LocationActivity.getIntent(CityActivity.this, cityModel));
                } else {
                    Utils.showGenericDialog(CityActivity.this, R.string.no_data);
                }

            }

            @Override
            public void onCityLocationFetchFailure(ErrorModel errorModel) {
                progressDialog.dismiss();


                switch (errorModel.getErrorType()) {
                    case ERROR_TYPE_NO_NETWORK:
                        Utils.showNetworkDisableDialog(CityActivity.this, errorModel.getErrorMessage());
                        break;
                    case ERROR_TYPE_SERVER:
                        Utils.showGenericDialog(CityActivity.this, errorModel.getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
        };

        CityLocationTask cityLocationTask = new CityLocationTask(this, cityLocationListener, cityModel);
        cityLocationTask.fetchCityLocation();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            SearchManager searchManager = new SearchManager(cityModelList, s.toString(), SearchManager.SearchableEnum.CITY);
            searchManager.setSearchListener(searchListener);
            searchManager.initSearch();
        }
    };

    SearchManager.SearchListener searchListener = new SearchManager.SearchListener() {
        @Override
        public void onSearchSuccess(List<Object> searchedList) {
            CityListAdapter cityListAdapter = new CityListAdapter(CityActivity.this, R.layout.list_row_city, searchedList);
            lvCity.setAdapter(cityListAdapter);
            lvCity.invalidate();
        }

        @Override
        public void onSearchFail() {
            // TODO Localization
            //Toast.makeText(CityActivity.this, "No data found!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSearchStringEmpty() {
            CityListAdapter cityListAdapter = new CityListAdapter(CityActivity.this, R.layout.list_row_city, cityModelList);
            lvCity.setAdapter(cityListAdapter);
            lvCity.invalidate();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_close) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
