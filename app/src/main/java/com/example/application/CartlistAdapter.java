package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartlistAdapter extends RecyclerView.Adapter implements OnPersonItemClickListener_cart {

    ArrayList<cartitem> items;
    Context context;
    OnPersonItemClickListener_cart listener;

    public CartlistAdapter(ArrayList<cartitem> items, Context context){
        this.items=items;
        this.context=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_list,parent,false);

        CartlistAdapter.VH vh=new CartlistAdapter.VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        CartlistAdapter.VH vh=(CartlistAdapter.VH)holder;

        cartitem item = items.get(position);
        vh.cartname.setText(item.getName());
        vh.cartprice.setText(item.getPrice());

        Glide.with(context).load(item.getImage()).into(vh.cartimage);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void setOnItemClicklistener(OnPersonItemClickListener_cart listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(CartlistAdapter.VH holder, View view, int position) {
        if (listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }

    public cartitem getItem(int position){
        return items.get(position);
    }
    class VH extends RecyclerView.ViewHolder{
        TextView cartname,cartprice;
        ImageView cartimage;

        public VH(@NonNull final View itemView){
            super(itemView);

            cartname=itemView.findViewById(R.id.cart_name);
            cartimage = itemView.findViewById(R.id.cart_image);
            cartprice = itemView.findViewById(R.id.cart_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (listener!=null){
                        listener.onItemClick(CartlistAdapter.VH.this,view,pos);
                    }

                }
            });
        }
    }


}
