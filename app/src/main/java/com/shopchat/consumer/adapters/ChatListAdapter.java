package com.shopchat.consumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.ChatDisplayModel;
import com.shopchat.consumer.models.ProductModel;
import com.shopchat.consumer.utils.Utils;

import java.util.List;

/**
 * Created by Sudipta on 8/19/2015.
 */
public class ChatListAdapter extends BaseAdapter {
    private Context context;
    private int resourceId;
    private List<Object> itemList;
    private ProductModel productModel;

    public ChatListAdapter(Context context, int resourceId, List<Object> itemList, ProductModel productModel) {
        this.context = context;
        this.resourceId = resourceId;
        this.itemList = itemList;
        this.productModel = productModel;
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
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvItemTitle = (TextView) view.findViewById(R.id.tv_item_title);
            viewHolder.tvTitleCircle = (TextView) view.findViewById(R.id.tv_title_circle);
            viewHolder.tvItemDescription = (TextView) view.findViewById(R.id.tv_item_description);
            viewHolder.tvDateTime = (TextView) view.findViewById(R.id.tv_date_time);
            viewHolder.tvDateTime.setVisibility(View.GONE);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ChatDisplayModel chatDisplayModel = (ChatDisplayModel) itemList.get(position);
        viewHolder.tvItemTitle.setText(chatDisplayModel.getProductName());
        viewHolder.tvTitleCircle.setText(Utils.getFirstCharacter(chatDisplayModel.getProductName()));
        if (chatDisplayModel.getRetailerConsolidatedChatContent().isEmpty()) {
            viewHolder.tvItemDescription.setText(context.getString(R.string.reply_pending));
        } else {
            viewHolder.tvItemDescription.setText(chatDisplayModel.getRetailerConsolidatedChatContent());
        }

        viewHolder.tvDateTime.setText(chatDisplayModel.getTimeStamp());

        if (productModel != null && chatDisplayModel.getProductId().equalsIgnoreCase(productModel.getProductId())) {
            //viewHolder.tvItemTitle.setTypeface(null, Typeface.BOLD);

        }

        return view;
    }

    public class ViewHolder {
        TextView tvTitleCircle;
        TextView tvItemTitle;
        TextView tvDateTime;
        TextView tvItemDescription;

    }
}
