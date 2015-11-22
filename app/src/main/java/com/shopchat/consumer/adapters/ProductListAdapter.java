package com.shopchat.consumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.utils.Utils;

import java.util.List;

/**
 * Created by Sudipta on 8/12/2015.
 */
public class ProductListAdapter extends BaseAdapter {
    private Context context;
    private int resourceId;
    private List<Object> itemList;

    /**
     * @param context
     * @param resourceId
     * @param itemList
     */
    public ProductListAdapter(Context context, int resourceId, List<Object> itemList) {
        this.context = context;
        this.resourceId = resourceId;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resourceId, parent, false);

        TextView tvItemTitle = (TextView) view.findViewById(R.id.tv_item_title);

        tvItemTitle.setText(((ProductModel) itemList.get(position)).getProductName());

        TextView tvTitleCircle = (TextView) view.findViewById(R.id.tv_title_circle);
        tvTitleCircle.setText(Utils.getFirstCharacter(((ProductModel) itemList.get(position)).getProductName()));

        return view;
    }
}
