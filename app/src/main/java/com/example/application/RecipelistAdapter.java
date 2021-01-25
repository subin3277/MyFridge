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

public class RecipelistAdapter extends RecyclerView.Adapter implements OnPersonItemClickListener{

    ArrayList<recipeitem> items;
    Context context;
    OnPersonItemClickListener listener;

    public RecipelistAdapter(ArrayList<recipeitem> items, Context context){
        this.items=items;
        this.context=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.recipe_cardview_list,parent,false);

        VH vh=new VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        VH vh=(VH)holder;

        recipeitem item = items.get(position);
        vh.recipeidx.setText(item.getIdx());
        vh.recipename.setText(item.getTitle());

        Glide.with(context).load(item.getImgURL()).into(vh.recipepreview);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void setOnItemClicklistener(OnPersonItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(VH holder, View view, int position) {
        if (listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }

    public recipeitem getItem(int position){
        return items.get(position);
    }
    class VH extends RecyclerView.ViewHolder{
        TextView recipename, recipeidx;
        ImageView recipepreview;

        public VH(@NonNull final View itemView){
            super(itemView);

            recipeidx=itemView.findViewById(R.id.recipeidx);
            recipename = itemView.findViewById(R.id.recipename);
            recipepreview = itemView.findViewById(R.id.recipepreview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (listener!=null){
                        listener.onItemClick(VH.this,view,pos);
                    }

                }
            });
        }
    }

}
