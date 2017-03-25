package com.chtl.spike;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by User on 1/1/2017.
 */

@DatabaseTable(tableName = "ListofPicture")
public class ListItems {

    @DatabaseField()
    private String groupId;

    @DatabaseField()
    private String name;

    @DatabaseField()
    private String address;

    @DatabaseField()
    private String date;

    //private List<GroupReceipt> receiptList;

    public ListItems(String groupId, String name, String address, String date) {
        this.groupId = groupId;
        this.name = name;
        this.address = address;
        this.date = date;
    }

    public ListItems() {
    }

    //public void setReceipt(List<GroupReceipt> receiptList) {
    //    this.receiptList = receiptList;
    //}

    public String getGroupId(){
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

//    public List<GroupReceipt> getReceipt() {
//        return receiptList;
//    }
}
