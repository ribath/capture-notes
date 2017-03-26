package com.chtl.spike;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class CapturedImageActivity extends AppCompatActivity {

    Bitmap photo;
    String flow, groupId, groupName, imageFileName, currentDateTimeString;
    String TAG = "CapturedImageActivity";
    String address = Environment.getExternalStorageDirectory() + "/TestApp_CaptureSave/";
    DBHelper dbHelper;
    Dao<GroupReceipt, String> groupReceiptsDao;
    Dao<ListItems, String> ListItemsDao;
    RecyclerView g_recyclerView;
    AdapterGroup adapterGroup;
    int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image);

        Log.i(TAG, "CapturedImageActivity is created");
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        try {
            ListItemsDao = dbHelper.getListItemsDao();
            groupReceiptsDao = dbHelper.getGroupReceiptDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        g_recyclerView = (RecyclerView)findViewById(R.id.group_recycler_view);
        adapterGroup = new AdapterGroup(this);

        Bundle bundle = getIntent().getExtras();
        flow = bundle.getString("flow");
        groupName = bundle.getString("groupName");
        groupId = bundle.getString("groupId");

        Log.i(TAG, "flow = "+flow);
        Log.i(TAG, "groupName = "+groupName);
        Log.i(TAG, "groupId = "+groupId);

        if(flow.equals("MainActivity to new group"))
        {
            Log.i(TAG, "Inside MainActivity to new group condition");
            int preference = ScanConstants.OPEN_CAMERA;
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
            startActivityForResult(intent, REQUEST_CODE);
        }

        if(flow.equals("MainActivity to old group"))
        {
            Log.i(TAG, "Inside MainActivity to old group condition");
            populateRecycler();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.group_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flow = "MainActivity to old group";
                int preference = ScanConstants.OPEN_CAMERA;
                Intent intent = new Intent(CapturedImageActivity.this, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            try {
                Log.i(TAG, "onActivityResult called");
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";
                createDirectoryAndSaveFile(photo, imageFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName)
    {
        File direct = new File(Environment.getExternalStorageDirectory() + "/TestApp_CaptureSave");
        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/TestApp_CaptureSave/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/TestApp_CaptureSave/", fileName);
        if (file.exists ()){
            file.delete ();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            saveToDB();
        } catch (Exception e) {
            Log.e(TAG, "IOException", e);
            e.printStackTrace();
        }
    }

    public void saveToDB() throws SQLException {
        if (flow.equals("MainActivity to new group"))
        {
            Log.i(TAG, "saveToDb -> MainActivity to new group");
            ListItems listItems = new ListItems(groupId, groupName, address, currentDateTimeString);
            GroupReceipt groupReceipt = new GroupReceipt(groupId, imageFileName, currentDateTimeString);

            ListItemsDao.create(listItems);
            groupReceiptsDao.create(groupReceipt);
            populateRecycler();
        }
        if (flow.equals("MainActivity to old group"))
        {
            Log.i(TAG, "saveToDb -> MainActivity to old group");
            GroupReceipt groupReceipt = new GroupReceipt(groupId, imageFileName, currentDateTimeString);

            groupReceiptsDao = dbHelper.getGroupReceiptDao();
            groupReceiptsDao.create(groupReceipt);
        }
        flow = "MainActivity to old group";
    }

    public void populateRecycler()
    {
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getApplicationContext());
        g_recyclerView.setLayoutManager(LayoutManager);
        g_recyclerView.setItemAnimator(new DefaultItemAnimator());
        g_recyclerView.setAdapter(adapterGroup);
    }

    public void initData()
    {
        try {
            QueryBuilder<GroupReceipt, String> queryBuilder = groupReceiptsDao.queryBuilder();
            List<GroupReceipt> list = queryBuilder.where().eq("groupId", groupId).query();

//            for(ListItems item: list) {
//                item.setReceipt(getRecipts(item.getGroupId()););
//            }
            adapterGroup.setData(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
