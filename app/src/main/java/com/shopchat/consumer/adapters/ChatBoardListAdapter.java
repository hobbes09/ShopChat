package com.shopchat.consumer.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.ChatBoardModel;
import com.shopchat.consumer.utils.Constants;
import com.shopchat.consumer.utils.Utils;

import java.util.List;

/**
 * Created by Sudipta on 9/15/2015.
 */
public class ChatBoardListAdapter extends BaseAdapter {
    private Context context;
    private List<ChatBoardModel> itemList;
    private boolean isFromInbox;

    public ChatBoardListAdapter(Context context, List<ChatBoardModel> itemList, boolean isFromInbox) {
        this.context = context;
        this.itemList = itemList;
        this.isFromInbox = isFromInbox;
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
        ChatBoardModel chatBoardModel = itemList.get(position);
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_chat_board, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvChatContent = (TextView) view.findViewById(R.id.tv_chat_content);
            viewHolder.tvrChatTimeStamp = (TextView) view.findViewById(R.id.tv_chat_time_stamp);
            viewHolder.llChatLayout = (LinearLayout) view.findViewById(R.id.ll_chat);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        if (chatBoardModel.isCustomer()) {
            layoutParams.gravity = Gravity.LEFT;
            viewHolder.llChatLayout.setBackgroundResource(R.drawable.shape_rounded_rect_orange);
            if (viewHolder.tvChatContent != null) {
                String customerName = Utils.getPersistenceData(this.context, Constants.REGISTERED_NAME_PREFERENCE);
                if (TextUtils.isEmpty(customerName)) {
                    // TODO Localization
                    viewHolder.tvChatContent.setText("Customer:" + chatBoardModel.getChatContent());
                } else {
                    viewHolder.tvChatContent.setText(customerName + ":" + chatBoardModel.getChatContent());
                }

            }

            if (viewHolder.tvrChatTimeStamp != null) {
                if (isFromInbox) {
                    viewHolder.tvrChatTimeStamp.setText(chatBoardModel.getChatTimeStamp());
                }
            }

        } else {
            layoutParams.gravity = Gravity.RIGHT;
            viewHolder.llChatLayout.setBackgroundResource(R.drawable.shape_rounded_rect_grey);
            String retailerNumber = String.valueOf(chatBoardModel.getRetailerCount());
            if (viewHolder.tvChatContent != null) {
                if (chatBoardModel.getChatContent().isEmpty()) {
                    viewHolder.tvChatContent.setText(chatBoardModel.getRetailerModel().getRetailerName() + ":"
                            + context.getString(R.string.reply_pending));
                } else {
                    viewHolder.tvChatContent.setText(chatBoardModel.getRetailerModel().getRetailerName() + ":"
                            + chatBoardModel.getChatContent());
                }

            }

            if (viewHolder.tvrChatTimeStamp != null) {
                if (isFromInbox) {
                    viewHolder.tvrChatTimeStamp.setText(chatBoardModel.getChatTimeStamp());
                }
            }
        }

        viewHolder.llChatLayout.setLayoutParams(layoutParams);

        if (viewHolder.tvrChatTimeStamp != null) {

            if (!isFromInbox) {
                viewHolder.tvrChatTimeStamp.setText(Utils.getChatTimeStamp());
            }

        }

        return view;
    }

    public class ViewHolder {
        TextView tvChatContent;
        TextView tvrChatTimeStamp;
        LinearLayout llChatLayout;
    }
}
