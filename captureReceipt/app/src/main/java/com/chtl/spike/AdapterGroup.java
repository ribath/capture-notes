package com.chtl.spike;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by User on 1/15/2017.
 */

public class AdapterGroup extends RecyclerView.Adapter<AdapterGroup.ViewHolder> {

    List<GroupReceipt> childPhotos;
    Context context;
    public String clickedItem;

    public AdapterGroup(Context context) {
        this.context = context;
    }

    public void setData(List<GroupReceipt> list) {
        this.childPhotos = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupReceipt list = childPhotos.get(position);

        File imgFile = new  File("/storage/sdcard/TestApp_CaptureSave/"+list.getName());
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Bitmap resized = ThumbnailUtils.extractThumbnail(myBitmap, 50, 50);

        holder.image.setImageBitmap(resized);
        holder.txtTitle.setText(list.getName());
        holder.txtAddress.setText(list.getGroupId());
        holder.txtDate.setText(list.getDate());
    }

    @Override
    public int getItemCount() {
        if(childPhotos  != null) {
            return childPhotos.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView txtTitle, txtAddress, txtDate;
        public LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            txtTitle = (TextView) itemView.findViewById(R.id.pic_title);
            txtAddress = (TextView) itemView.findViewById(R.id.pic_address);
            txtDate = (TextView) itemView.findViewById(R.id.pic_date);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickedItem = txtTitle.getText().toString();
                    Log.i("Recycler OnClick", "Item " + clickedItem + "Clicked in LinearLayout");
                    Intent intent=new Intent(context, ImagePreviewActivity.class);
                    intent.putExtra("imageId",clickedItem);
                    context.startActivity(intent);
                }
            });
        }
    }
}
