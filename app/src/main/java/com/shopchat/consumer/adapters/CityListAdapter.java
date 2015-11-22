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

import java.util.List;

/**
 * Created by Sudipta on 9/10/2015.
 */
public class CityListAdapter extends BaseAdapter {

    private Context context;
    private int resourceId;
    private List<Object> cityList;

    public CityListAdapter(Context context, int resourceId, List<Object> cityList) {
        this.context = context;
        this.resourceId = resourceId;
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        CityModel cityModel = (CityModel)cityList.get(position);
        viewHolder.tvTitleCity.setText(cityModel.getCityName());

        return view;
    }

    public class ViewHolder {
        TextView tvTitleCity;
        ImageView ivRightArrow;
    }
}
