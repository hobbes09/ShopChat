package com.shopchat.consumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.CategoryModel;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.imageloader.ImageLoaderPlayed;

import java.util.List;

/**
 * Created by Sudipta on 8/18/2015.
 */
public class CategoryListAdapter extends BaseAdapter {
    private Context context;
    private int resourceId;
    private List<CategoryModel> itemList;

    /**
     * @param context
     * @param resourceId
     * @param itemList
     */
    public CategoryListAdapter(Context context, int resourceId, List<CategoryModel> itemList) {
        this.context = context;
        this.resourceId = resourceId;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        if(itemList != null) {
            return itemList.size();
        }else{
            return 0;
        }
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvItemTitle = (TextView) view.findViewById(R.id.tv_item_title);
            viewHolder.ivItemImage = (ImageView) view.findViewById(R.id.iv_item_image);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        CategoryModel homeItemModel = itemList.get(position);
        viewHolder.tvItemTitle.setText(homeItemModel.getItemName());
        String imageUrl = Constants.BASE_URL + "/image/" + homeItemModel.getItemPictureUrl();
        try {
            ImageLoaderPlayed imageLoaderLast = new ImageLoaderPlayed(
                    context);
            imageLoaderLast.DisplayImage(imageUrl, viewHolder.ivItemImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* switch (position) {
            case 0:
                viewHolder.ivItemImage.setImageResource(R.drawable.kitchen_essentials);
                break;
            case 1:
                viewHolder.ivItemImage.setImageResource(R.drawable.electrical_shop);
                break;
            case 2:
                viewHolder.ivItemImage.setImageResource(R.drawable.home_appliance);
                break;
            case 3:
                viewHolder.ivItemImage.setImageResource(R.drawable.computer_accessories);
                break;
            case 4:
                viewHolder.ivItemImage.setImageResource(R.drawable.cooking_needs);
                break;
            case 5:
                viewHolder.ivItemImage.setImageResource(R.drawable.repair_service);
                break;
            case 6:
                viewHolder.ivItemImage.setImageResource(R.drawable.mobile_shop);
                break;
            case 7:
                viewHolder.ivItemImage.setImageResource(R.drawable.women_care);
                break;
            default:
                break;
        }*/

        return view;
    }

    public class ViewHolder {
        TextView tvItemTitle;
        ImageView ivItemImage;

    }
}
