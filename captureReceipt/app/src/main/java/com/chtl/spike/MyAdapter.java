package com.chtl.spike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import static android.R.attr.path;

/**
 * Created by User on 1/1/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    List<ListItems> photoList;
    int flag;
    public String clickedItem;
    Context context;
    String TAG="MyAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image1, image2, image3, image4;
        public TextView txtTitle;
        public TextView txtAddress;
        public TextView txtDate;
        public LinearLayout linear, grid;

        public ViewHolder(View v) {
            super(v);

            image1 = (ImageView) v.findViewById(R.id.imageView1);
            image2 = (ImageView) v.findViewById(R.id.imageView2);
            image3 = (ImageView) v.findViewById(R.id.imageView3);
            image4 = (ImageView) v.findViewById(R.id.imageView4);
            txtTitle = (TextView) v.findViewById(R.id.pic_title);
            txtAddress = (TextView) v.findViewById(R.id.pic_address);
            txtDate = (TextView) v.findViewById(R.id.pic_date);

            if (flag==1)
            {
                linear = (LinearLayout) v.findViewById(R.id.linear_thumbnail);
                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedItem = txtAddress.getText().toString();
                        Log.i("Recycler OnClick", "Item " + clickedItem + "Clicked in LinearLayout");
                        Intent intent=new Intent(context, CapturedImageActivity.class);
                        intent.putExtra("groupId",clickedItem);
                        intent.putExtra("flow","MainActivity to old group");
                        context.startActivity(intent);
                    }
                });
            }
            if (flag==2)
            {
                grid = (LinearLayout) v.findViewById(R.id.grid_thumbnail);
                grid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedItem = txtTitle.getText().toString();
                        Log.i("Recycler OnClick", "Item " + txtTitle.getText() + "Clicked in GridLayout");

                    }
                });
            }
        }
    }


    public MyAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ListItems> list) {
        this.photoList = list;
        notifyDataSetChanged();
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (flag==1)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlist_thumbnail, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        if (flag==2)
        {
           View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customgrid_thumbnail, parent, false);
           ViewHolder vh = new ViewHolder(v);
           return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        ListItems list = photoList.get(position);
        Log.i(TAG, "Inside onBindViewHolder. Position = "+position);

        //holder.image.setImageBitmap(resized);
        holder.txtTitle.setText(list.getName());
        holder.txtAddress.setText(list.getGroupId());
        holder.txtDate.setText(list.getDate());

        String groupId = list.getGroupId();
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        try {
            Dao<GroupReceipt, String> groupReceiptsDao = dbHelper.getGroupReceiptDao();
            QueryBuilder<GroupReceipt, String> queryBuilder = groupReceiptsDao.queryBuilder();
            List<GroupReceipt> list1 = queryBuilder.where().eq("groupId", groupId).query();

            for (int i=0; i<list1.size(); i++)
            {
                File imgFile = new  File("/storage/sdcard/TestApp_CaptureSave/"+list1.get(i).getName());
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap resized = ThumbnailUtils.extractThumbnail(myBitmap, 50, 50);
                if (i==0)
                {
                    holder.image1.setImageBitmap(resized);
                }
                if (i==1)
                {
                    holder.image2.setImageBitmap(resized);
                }
                if (i==2)
                {
                    holder.image3.setImageBitmap(resized);
                }
                if (i==3)
                {
                    holder.image4.setImageBitmap(resized);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(photoList  != null) {
            Log.i(TAG, "getItemCount = "+photoList.size());
            return photoList.size();
        }
        return 0;
    }
}