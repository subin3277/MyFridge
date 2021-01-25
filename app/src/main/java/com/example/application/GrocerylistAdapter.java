package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GrocerylistAdapter extends RecyclerView.Adapter implements OnPersonItemClickListener_gro{

    ArrayList<GrocerylistData> items;
    Context context;
    OnPersonItemClickListener_gro listener;

    public GrocerylistAdapter(ArrayList<GrocerylistData> items, Context context){
        this.items=items;
        this.context=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.grocery_item_list,parent,false);

        GrocerylistAdapter.VH vh=new GrocerylistAdapter.VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        GrocerylistAdapter.VH vh=(GrocerylistAdapter.VH)holder;

        GrocerylistData item = items.get(position);
        vh.idx.setText(String.valueOf(item.getIdx()));
        vh.name.setText(item.getname());

    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void setOnItemClicklistener(OnPersonItemClickListener_gro listener){
        this.listener=listener;
    }


    public void onItemClick(GrocerylistAdapter.VH holder, View view, int position) {
        if (listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }

    public GrocerylistData getItem(int position){
        return items.get(position);
    }
    class VH extends RecyclerView.ViewHolder{
        TextView idx,name;

        public VH(@NonNull final View itemView){
            super(itemView);

            idx=itemView.findViewById(R.id.groceryidx);
            name = itemView.findViewById(R.id.groceryname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (listener!=null){
                        listener.onItemClick(GrocerylistAdapter.VH.this,view,pos);
                    }

                }
            });
        }
    }






}
