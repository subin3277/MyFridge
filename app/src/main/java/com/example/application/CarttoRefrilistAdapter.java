package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarttoRefrilistAdapter extends RecyclerView.Adapter implements OnPersonItemClickListener_carttorefri {

    ArrayList<carttorefriitem> items;
    Context context;
    OnPersonItemClickListener_carttorefri listener;

    public CarttoRefrilistAdapter(ArrayList<carttorefriitem> items, Context context){
        this.items=items;
        this.context=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_ingre_list,parent,false);

        CarttoRefrilistAdapter.VH vh=new CarttoRefrilistAdapter.VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        CarttoRefrilistAdapter.VH vh=(CarttoRefrilistAdapter.VH)holder;

        carttorefriitem item = items.get(position);
        vh.ingre_idx.setText(item.getIngre_idx());
        vh.ingre_name.setText(item.getIngre_name());


    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void setOnItemClicklistener(OnPersonItemClickListener_carttorefri listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(CarttoRefrilistAdapter.VH holder, View view, int position) {
        if (listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }

    public carttorefriitem getItem(int position){
        return items.get(position);
    }
    class VH extends RecyclerView.ViewHolder{
        TextView ingre_idx,ingre_name;

        public VH(@NonNull final View itemView){
            super(itemView);

            ingre_idx = itemView.findViewById(R.id.cart_ingre_idx);
            ingre_name=itemView.findViewById(R.id.cart_ingre_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (listener!=null){
                        listener.onItemClick(CarttoRefrilistAdapter.VH.this,view,pos);
                    }

                }
            });
        }
    }

}
