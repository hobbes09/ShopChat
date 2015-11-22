package com.shopchat.consumer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.adapters.LocationListAdapter;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

/**
 * Created by Sudipta on 8/17/2015.
 */
public class LocationActivity extends BaseActivity {

    public static final String CITY_MODEL = "city_model";
    private CityModel cityModel;

    public static Intent getIntent(Context context, CityModel cityModel) {
        Intent intent = new Intent(context, LocationActivity.class);
        intent.putExtra(CITY_MODEL, cityModel);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        cityModel = this.getIntent().getParcelableExtra(CITY_MODEL);
        setUpSupportActionBar(false, true, cityModel.getCityName());

        initViews();
    }

    private void initViews() {
        final ListView locationList = (ListView) findViewById(R.id.lv_location);

        LocationListAdapter locationListAdapter = new LocationListAdapter(this, R.layout.list_row_city, cityModel.getLocationModelList());
        locationList.setAdapter(locationListAdapter);

        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = cityModel.getCityName();
                String locationName = cityModel.getLocationModelList().get(position).getLocationName();
                Utils.setPersistenceData(LocationActivity.this, Constants.CITY_PREFERENCE, cityName);
                Utils.setPersistenceData(LocationActivity.this, Constants.LOCATION_PREFERENCE, locationName);
                callLandingActivity();
            }
        });
    }

    private void callLandingActivity() {
        startActivity(LandingActivity.getIntent(this));
        finish();
    }

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
