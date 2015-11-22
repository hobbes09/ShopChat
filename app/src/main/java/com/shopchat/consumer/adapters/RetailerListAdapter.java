package com.shopchat.consumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.RetailerModel;
import com.shopchat.consumer.utils.Utils;

import java.util.List;

/**
 * Created by Sudipta on 8/14/2015.
 */
public class RetailerListAdapter extends BaseAdapter {
    private Context context;
    private int resourceId;
    private List<Object> itemList;

    /**
     * @param context
     * @param resourceId
     * @param itemList
     */
    public RetailerListAdapter(Context context, int resourceId, List<Object> itemList) {
        this.context = context;
        this.resourceId = resourceId;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.itemList.get(position);
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
            viewHolder.tvItemTitle = (TextView) view.findViewById(R.id.tv_item_title);
            viewHolder.cbRetailer = (CheckBox) view.findViewById(R.id.cb_retailer);
            viewHolder.cbRetailer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    ((RetailerModel) itemList.get(getPosition)).setSelected(buttonView.isChecked());
                }
            });
            view.setTag(viewHolder);
            view.setTag(R.id.cb_retailer, viewHolder.cbRetailer);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvItemTitle.setText(((RetailerModel) (itemList.get(position))).getRetailerName());
        viewHolder.cbRetailer.setTag(position);
        viewHolder.cbRetailer.setChecked(((RetailerModel) (itemList.get(position))).isSelected());
        viewHolder.tvTitleCircle = (TextView) view.findViewById(R.id.tv_title_circle);
        viewHolder.tvTitleCircle.setText(Utils.getFirstCharacter(((RetailerModel) (itemList.get(position))).getRetailerName()));

        return view;
    }

    public class ViewHolder {
        TextView tvTitleCircle;
        TextView tvItemTitle;
        CheckBox cbRetailer;

    }
}
