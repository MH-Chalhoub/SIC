package com.example.sic;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by akshayejh on 19/12/17.
 */

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder>{

    public List<String> fileNameList;
    public List<String> fileDoneList;
    public List<Bitmap> fileImageList;

    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList, List<Bitmap> fileImageList){

        this.fileDoneList = fileDoneList;
        this.fileNameList = fileNameList;
        this.fileImageList = fileImageList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String fileName = fileNameList.get(position);
        holder.fileNameView.setText(fileName);

        int nh = (int) ( fileImageList.get(position).getHeight() * (512.0 / fileImageList.get(position).getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(fileImageList.get(position), 512, nh, true);
        holder.fileImageView.setImageBitmap(scaled);

        String fileDone = fileDoneList.get(position);

        if(fileDone.equals("uploading")){

            holder.fileDoneView.setImageResource(R.mipmap.progress);

        } else {

            holder.fileDoneView.setImageResource(R.mipmap.checked);
        }

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TextView fileNameView;
        public ImageView fileDoneView, fileImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            fileNameView = (TextView) mView.findViewById(R.id.upload_filename);
            fileDoneView = (ImageView) mView.findViewById(R.id.upload_loading);
            fileImageView = (ImageView) mView.findViewById(R.id.upload_icon);


        }

    }

}
