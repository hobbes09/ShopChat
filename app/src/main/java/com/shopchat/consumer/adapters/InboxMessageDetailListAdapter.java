package com.shopchat.consumer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.models.entities.InboxMessageDetailEntity;

/**
 * Created by sourin on 13/12/15.
 */
public class InboxMessageDetailListAdapter extends BaseAdapter {

    Context context;
    InboxMessageDetailEntity inboxMessageDetailEntity;

    private static LayoutInflater layoutInflater;

    public InboxMessageDetailListAdapter(Context context, InboxMessageDetailEntity inboxMessageDetailEntity){
        this.context = context;
        this.inboxMessageDetailEntity = inboxMessageDetailEntity;

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return this.inboxMessageDetailEntity.getAnswersMessageUnitEntity().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if(position == 0){
            return this.inboxMessageDetailEntity.getQuestionMessageUnitEntity();
        }else if(position < getCount()){
            return this.inboxMessageDetailEntity.getAnswersMessageUnitEntity().get(position - 1);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        LinearLayout ll_chat;
        TextView tv_chat_content;
        TextView tv_chat_time_stamp;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder=new Holder();
        final View rowView;

        rowView = layoutInflater.inflate(R.layout.list_row_chat_board, null);
        holder.ll_chat = (LinearLayout)rowView.findViewById(R.id.ll_chat);
        holder.tv_chat_content = (TextView)rowView.findViewById(R.id.tv_chat_content);
        holder.tv_chat_time_stamp = (TextView)rowView.findViewById(R.id.tv_chat_time_stamp);

        // set values of Holder elements
        if(position == 0){
            holder.ll_chat.setBackgroundColor(Color.GRAY);
            holder.tv_chat_content.setText(this.inboxMessageDetailEntity.getQuestionMessageUnitEntity().getSpeaker() + " : " +
                                    this.inboxMessageDetailEntity.getQuestionMessageUnitEntity().getMessage());
            holder.tv_chat_time_stamp.setText(this.inboxMessageDetailEntity.getQuestionMessageUnitEntity().getWrittenAt());
        }else {
            holder.tv_chat_content.setText(this.inboxMessageDetailEntity.getAnswersMessageUnitEntity().get(position - 1).getSpeaker() + " : " +
                    this.inboxMessageDetailEntity.getAnswersMessageUnitEntity().get(position - 1).getMessage());
            holder.tv_chat_time_stamp.setText(this.inboxMessageDetailEntity.getAnswersMessageUnitEntity().get(position - 1).getWrittenAt());
        }

        return rowView;
    }
}
