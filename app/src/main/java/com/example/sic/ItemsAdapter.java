package com.example.sic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Item> mItems;
    private OnItemClickListener mListener;

    public ItemsAdapter(Context context, List<Item> Items)
    {
        mContext = context;
        mItems = Items;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new ImageViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Item itemCur = mItems.get(i);

        String pattern = "yyyy MMMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(itemCur.getPosted_time());
        System.out.println(date);

        imageViewHolder.prod_name.setText(itemCur.getTitle());
        imageViewHolder.prod_price.setText(itemCur.getPrice() + "$");
        imageViewHolder.prod_posted_time.setText(date);
        imageViewHolder.prod_location.setText(itemCur.getLocation());
        try {
            Picasso.with(mContext)
                    .load(itemCur.getImages().get(0))
                    .placeholder(R.drawable.img_placeholder)
                    .fit()
                    .centerCrop()
                    .into(imageViewHolder.prod_img);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView prod_img;
        public TextView prod_name, prod_price, prod_posted_time, prod_location;
        public LinearLayout iconWrapperitem;
        public ImageViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            prod_img = itemView.findViewById(R.id.ItemImage);
            prod_name = itemView.findViewById(R.id.itemName);
            prod_price = itemView.findViewById(R.id.itemPrice);
            prod_posted_time = itemView.findViewById(R.id.itemPostedTime);
            prod_location = itemView.findViewById(R.id.itemLocation);
            iconWrapperitem = itemView.findViewById(R.id.iconWrapperitem);

            iconWrapperitem.setOnClickListener(new View.OnClickListener() {
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
            iconWrapperitem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null) {
                        int postion = getAdapterPosition();
                        if (postion != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(postion);
                        }
                    }
                    return false;
                }
            });
        }

    }
}
