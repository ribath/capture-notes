package com.chtl.spike;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.sql.SQLException;

public class ImagePreviewActivity extends AppCompatActivity {

    ImageView imageView;
    String clickedItem;
    String group, title, date;
    String TAG = "ImagePreviewActivity";
    EditText ETgroup, ETtitle, ETdate;
    MenuItem delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            clickedItem = extras.getString("imageId");
        }

        Log.i(TAG, clickedItem);

        imageView = (ImageView)findViewById(R.id.imageView);
        ETgroup = (EditText)findViewById(R.id.group);
        ETtitle = (EditText)findViewById(R.id.pic);
        ETdate = (EditText)findViewById(R.id.date);

        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        try {
            Dao<GroupReceipt, String> GroupReceiptDao = dbHelper.getGroupReceiptDao();
            QueryBuilder<GroupReceipt, String> queryBuilder = GroupReceiptDao.queryBuilder();
            queryBuilder.where().eq("name", clickedItem);
            group = queryBuilder.query().get(0).getGroupId();
            title = queryBuilder.query().get(0).getName();
            date = queryBuilder.query().get(0).getDate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        imageView.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/TestApp_CaptureSave/"+clickedItem));
        ETgroup.setHint(group);
        ETtitle.setHint(title);
        ETdate.setHint(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        delete = menu.findItem(R.id.deleteImage);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.deleteImage:
                DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
                try {
                    Dao<ListItems, String> ListItemsDao = dbHelper.getListItemsDao();
                    DeleteBuilder<ListItems, String> deleteBuilder = ListItemsDao.deleteBuilder();
                    deleteBuilder.where().eq("name", clickedItem);
                    deleteBuilder.delete();
                    /////////////////
                    File file = new File(Environment.getExternalStorageDirectory() + "/TestApp_CaptureSave/"+clickedItem);
                    file.delete();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
