package com.example.sic;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ImageViewHolder> {

    private Context pContext;
    private List<Category> mCategories;

    public CategoryAdapter(Context pContext, List<Category> mCategories) {
        this.pContext = pContext;
        this.mCategories = mCategories;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(pContext).inflate(R.layout.category_item, viewGroup, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Category categoryCur = mCategories.get(i);
        imageViewHolder.cattitle.setText(categoryCur.getCatname());
        imageViewHolder.cattitle.setBackgroundColor(Color.parseColor(categoryCur.getCattitlebg()));
        imageViewHolder.iconWrapper.setBackgroundColor(Color.parseColor(categoryCur.getCatbg()));
        Picasso.with(pContext)
                .load(categoryCur.getCaticon())
                .placeholder(R.drawable.img_placeholder)
                .fit()
                .centerCrop()
                .into(imageViewHolder.imgicon);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView cattitle;
        public ImageView imgicon;
        public LinearLayout iconWrapper;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            cattitle = itemView.findViewById(R.id.catName);
            imgicon = itemView.findViewById(R.id.catIcon);
            iconWrapper = itemView.findViewById(R.id.iconWrapper);
        }
    }
}
