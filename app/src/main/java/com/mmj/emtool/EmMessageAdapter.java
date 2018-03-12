package com.mmj.emtool;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by mmj on 18-2-11.
 */

public class EmMessageAdapter extends RecyclerView.Adapter<EmMessageAdapter.ViewHolder> {
    private Context mContext;
    private List<EmMessage> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView msg;
        TextView msgName;
        LinearLayout backmsg;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            backmsg = (LinearLayout) view.findViewById(R.id.back_msg);
            msg=(TextView) view.findViewById(R.id.msg);
            msgName = (TextView) view.findViewById(R.id.msg_name);
        }
    }
    public EmMessageAdapter(List<EmMessage> emMessageList){
        mMsgList = emMessageList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.msg_item,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        EmMessage emMsg = mMsgList.get(position);
        holder.msg.setText(emMsg.getMsg());
        holder.msgName.setText(emMsg.getName());
        holder.backmsg.setBackgroundResource(emMsg.getImageId());
        //Glide.with(mContext).load(emMsg.getImageId()).into();
    }
        @Override
        public int getItemCount(){
        return mMsgList.size();
    }

    }

