package com.chtl.spike;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    Bitmap photo;
    String imageFileName, groupId, groupName;
    String currentDateTimeString;
    String TAG="Ribath";
    DBHelper dbHelper;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    int viewFlag=1;
    MenuItem item1, item2, item3;
    int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate called");
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        myAdapter = new MyAdapter(this);
        populateRecycler();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groupName = input.getText().toString();
                        groupId = "Group_"+System.currentTimeMillis();
                        Intent intent = new Intent(MainActivity.this, CapturedImageActivity.class);
                        intent.putExtra("flow", "MainActivity to new group");
                        intent.putExtra("groupName", groupName);
                        intent.putExtra("groupId", groupId);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        item1 = menu.findItem(R.id.listToggle);
        item2 = menu.findItem(R.id.gridToggle);
        item3 = menu.findItem(R.id.addGroup);
        item1.setVisible(false);
        item3.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.gridToggle:
                item1.setVisible(true);
                item2.setVisible(false);
                girdView();
                return true;
            case R.id.listToggle:
                item2.setVisible(true);
                item1.setVisible(false);
                listView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume called");
        initData();
    }



    public void getDataFromTable ()
    {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        try {
//            Dao<ListItems, String> ListItemsDao1 = dbHelper.getListItemsDao();
//            QueryBuilder<ListItems, String> queryBuilder = ListItemsDao1.queryBuilder();
//            queryBuilder.where().eq("groupId", groupName);
//            String x = queryBuilder.query().get(0).getName();
//            String y = queryBuilder.query().get(0).getAddress();
//            String z = queryBuilder.query().get(0).getDate();
            Log.i(TAG,"getDataFromTable called");
            Dao<ListItems, String> ListItemsDao = dbHelper.getListItemsDao();
            List<ListItems> list = ListItemsDao.queryForAll();
            int size = list.size();
            for (int i=0; i<size; i++)
            {
                Log.i(TAG, "groupId = "+list.get(i).getGroupId()+", name = "+list.get(i).getName()+", address = "+list.get(i).getAddress()+", date = "+list.get(i).getDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateRecycler()
    {
        myAdapter.setFlag(viewFlag);
        if (viewFlag==1)
        {
            RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(LayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(myAdapter);
        }

        if (viewFlag==2)
        {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
        }
    }

    public void initData()
    {
        try {
            Dao<ListItems, String> ListItemsDao = dbHelper.getListItemsDao();
            List<ListItems> list = ListItemsDao.queryForAll();

//            for(ListItems item: list) {
//                item.setReceipt(getRecipts(item.getGroupId()););
//            }
            myAdapter.setData(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getRecipt(String groupId) {
        // get all receipts from database that has groupdId == groupId
    }

    public void girdView()
    {
        viewFlag = 2;
        populateRecycler();
        initData();
    }

    public void listView()
    {
        viewFlag = 1;
        populateRecycler();
        initData();
    }
}