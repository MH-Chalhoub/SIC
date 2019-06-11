package com.example.sic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Popular> mPopulars;
    private OnItemClickListener mListener;

    public PopularAdapter(Context context, List<Popular> populars)
    {
        mContext = context;
        mPopulars = populars;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.popular_item, viewGroup, false);
        return new ImageViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Popular popularCur = mPopulars.get(i);
        imageViewHolder.prod_name.setText(popularCur.getProduct_title());
        imageViewHolder.prod_price.setText(popularCur.getProduct_price() + "$");
        Picasso.with(mContext)
                .load(popularCur.getProduct_image())
                .placeholder(R.drawable.img_placeholder)
                .fit()
                .centerCrop()
                .into(imageViewHolder.prod_img);
        if(popularCur.getProduct_favorite() == 0){
            imageViewHolder.prod_favorite.setImageResource(R.drawable.btn_favourite);
        }
        else {
            imageViewHolder.prod_favorite.setImageResource(R.drawable.btn_favorite_black);
        }
    }

    @Override
    public int getItemCount() {
        return mPopulars.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView prod_name, prod_price;
        public ImageView prod_img;
        public ImageButton prod_favorite;
        public ImageViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            prod_name = itemView.findViewById(R.id.prodName);
            prod_price = itemView.findViewById(R.id.prodPrice);
            prod_img = itemView.findViewById(R.id.prodImage);
            prod_favorite = itemView.findViewById(R.id.prodFavorite);
            prod_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //prod_favorite.setImageResource(R.drawable.btn_favorite_black);
                    if(listener != null){
                        int postion = getAdapterPosition();
                        if(postion != RecyclerView.NO_POSITION){
                            listener.onItemClick(postion);
                        }
                    }
                }
            });
        }

    }
}
