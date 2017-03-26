package com.chtl.spike;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by User on 1/14/2017.
 */


@DatabaseTable(tableName = "GroupTable")
public class GroupReceipt {

    @DatabaseField()
    private String groupId;

    @DatabaseField()
    private String name;

    @DatabaseField()
    private String date;

    public GroupReceipt(String groupId, String name, String date) {
        this.groupId = groupId;
        this.name = name;
        this.date = date;
    }

    public GroupReceipt() {
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
