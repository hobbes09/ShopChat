package com.shopchat.consumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.FaqModel;

import java.util.List;

/**
 * Created by Sudipta on 9/17/2015.
 */
public class FaqAdapter extends BaseAdapter {
    private Context context;
    private List<FaqModel> itemList;

    public FaqAdapter(Context context, List<FaqModel> itemList) {
        this.context = context;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_row_faq, parent, false);

        return view;
    }
}
