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

public class RecipeCookAdapter extends RecyclerView.Adapter{

    ArrayList<RecipeCookItem> items;
    Context context;

    public RecipeCookAdapter(ArrayList<RecipeCookItem> items, Context context){
        this.items=items;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.recipe_cooking_list,parent,false);

        VH vh = new VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        RecipeCookAdapter.VH vh=(RecipeCookAdapter.VH)holder;

        RecipeCookItem item = items.get(position);
        vh.cookidx.setText(item.getIdx());
        vh.cookdes.setText(item.getDes());

        if (item.getImageurl()!=null){
            Glide.with(context).load(item.getImageurl()).into(vh.cookimage);
        }
        else {
            vh.cookimage.setImageResource(R.drawable.ic_baseline_local_dining_24);
        }

    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView cookdes, cookidx;
        ImageView cookimage;

        public VH(@NonNull final View itemView){
            super(itemView);

            cookidx=itemView.findViewById(R.id.reci_cook_idx);
            cookdes = itemView.findViewById(R.id.reci_cook_desc);
            cookimage = itemView.findViewById(R.id.reci_cook_image);


        }
    }

}
