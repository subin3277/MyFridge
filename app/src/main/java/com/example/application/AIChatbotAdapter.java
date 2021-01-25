package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AIChatbotAdapter extends RecyclerView.Adapter {

    ArrayList<AIChatData> items;
    Context context;

    public AIChatbotAdapter(ArrayList<AIChatData> items, Context context){
        this.items=items;
        this.context=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.chattin_message,parent,false);

        AIChatbotAdapter.VH vh=new AIChatbotAdapter.VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        AIChatbotAdapter.VH vh=(AIChatbotAdapter.VH)holder;

        AIChatData item = items.get(position);
        vh.msg.setText(item.getMsg());
        vh.res.setText(item.getResponse());

    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public AIChatData getItem(int position){
        return items.get(position);
    }
    class VH extends RecyclerView.ViewHolder{
        TextView msg,res;

        public VH(@NonNull final View itemView){
            super(itemView);

            msg=itemView.findViewById(R.id.chatmessage);
            res=itemView.findViewById(R.id.responsemessage);

        }
    }

}
