package com.chtl.spike;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by User on 1/1/2017.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "easyApp";
    private static final int DATABASE_VERSION = 1;

    private Dao<ListItems, String> ListItemsDao = null;
    private Dao<GroupReceipt, String> GroupReceiptDao = null;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ListItems.class);
            TableUtils.createTable(connectionSource, GroupReceipt.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ListItems.class, true);
            TableUtils.dropTable(connectionSource, GroupReceipt.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<ListItems, String> getListItemsDao() throws SQLException {
        if (ListItemsDao == null)
        {
            ListItemsDao = getDao(ListItems.class);
        }
        return ListItemsDao;
    }

    public Dao<GroupReceipt, String> getGroupReceiptDao() throws SQLException {
        if (GroupReceiptDao == null)
        {
            GroupReceiptDao = getDao(GroupReceipt.class);
        }
        return GroupReceiptDao;
    }
}
