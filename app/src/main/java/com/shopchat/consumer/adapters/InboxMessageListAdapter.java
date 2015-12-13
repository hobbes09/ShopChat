package com.shopchat.consumer.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shopchat.consumer.R;
import com.shopchat.consumer.activities.InboxMessageDetailActivity;
import com.shopchat.consumer.activities.LandingActivity;
import com.shopchat.consumer.models.entities.InboxMessageEntity;

import java.util.ArrayList;

/**
 * Created by sourin on 12/12/15.
 */
public class InboxMessageListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InboxMessageEntity> inboxMessageEntities;

    private static LayoutInflater layoutInflater;

    public InboxMessageListAdapter(Context context, ArrayList<InboxMessageEntity> inboxMessageEntities) {
        this.context = context;
        this.inboxMessageEntities = inboxMessageEntities;

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return this.inboxMessageEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return this.inboxMessageEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView tvMessageLogo;
        TextView tvMessageHeader;
        TextView tvMessageDetails;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder=new Holder();
        final View rowView;

        rowView = layoutInflater.inflate(R.layout.list_row_inbox, null);
        holder.tvMessageLogo = (TextView)rowView.findViewById(R.id.tvMessageLogo);
        holder.tvMessageHeader = (TextView)rowView.findViewById(R.id.tvMessageHeader);
        holder.tvMessageDetails = (TextView)rowView.findViewById(R.id.tvMessageDetails);

        // set values of Holder elements
        holder.tvMessageLogo.setText(String.valueOf(this.inboxMessageEntities.get(position).getMessageHeader().toCharArray()[0]));
        holder.tvMessageHeader.setText(this.inboxMessageEntities.get(position).getMessageHeader());
        holder.tvMessageDetails.setText(this.inboxMessageEntities.get(position).getMessageDetail());

        rowView.setTag(this.inboxMessageEntities.get(position).getQuestionId());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Inbox>>>", "You clicked :" + rowView.getTag());
                Intent launchInboxMessageDetailActivity = new Intent(LandingActivity.mLandingActivityContext, InboxMessageDetailActivity.class);
                Bundle questionInformation = new Bundle();
                questionInformation.putString("QUESTION_ID", rowView.getTag().toString());
                launchInboxMessageDetailActivity.putExtras(questionInformation);
                LandingActivity.mLandingActivityContext.startActivity(launchInboxMessageDetailActivity);
            }
        });
        return rowView;
    }
}
