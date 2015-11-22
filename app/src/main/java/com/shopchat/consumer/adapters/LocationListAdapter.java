package com.shopchat.consumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.CityModel;
import com.shopchat.consumer.models.LocationModel;

import java.util.List;

/**
 * Created by Sudipta on 9/11/2015.
 */
public class LocationListAdapter extends BaseAdapter {
    private Context context;
    private int resourceId;
    private List<LocationModel> locationList;


    public LocationListAdapter(Context context, int resourceId, List<LocationModel> locationList) {
        this.context = context;
        this.resourceId = resourceId;
        this.locationList = locationList;
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Object getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitleCity = (TextView) view.findViewById(R.id.tv_city_title);
            viewHolder.ivRightArrow = (ImageView) view.findViewById(R.id.iv_right_arrow);
            viewHolder.ivRightArrow.setVisibility(View.GONE);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        LocationModel locationModel = locationList.get(position);
        viewHolder.tvTitleCity.setText(locationModel.getLocationName());

        return view;
    }

    public class ViewHolder {
        TextView tvTitleCity;
        ImageView ivRightArrow;
    }
}
