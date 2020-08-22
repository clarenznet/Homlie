package com.luns.neuro.mlkn.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luns.neuro.mlkn.DataAdapter.MyRequests;

import java.util.ArrayList;

public class SQLiteManagerMyRequests extends SQLiteOpenHelper {

    public SQLiteManagerMyRequests(Context context) {
        super(context, "android_cache_myrequests", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS myrequests(" +
                "requestid TEXT NOT NULL,requesttcktcode TEXT NOT NULL, requesttitle TEXT NOT NULL,requestbody TEXT NOT NULL," +
                "requesttime TEXT NOT NULL,requestcolor TEXT NOT NULL,requeststatus TEXT NOT NULL,requestcreatedat TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE myrequests");
        onCreate(db);
    }

    public void addData(MyRequests myrequestsModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("requestid", myrequestsModel.getStrRequestTypeUrl());
        contentValues.put("requesttcktcode", myrequestsModel.getStrRequestTcktCode());
        contentValues.put("requesttitle", myrequestsModel.getStrRequestTitle());
        contentValues.put("requestbody", myrequestsModel.getStrRequestBody());
        contentValues.put("requesttime", myrequestsModel.getStrRequestTime());
        contentValues.put("requestcolor", myrequestsModel.getStrRequestColor());
        contentValues.put("requeststatus", myrequestsModel.getStrRequestStatus());
        contentValues.put("requestcreatedat", myrequestsModel.getStrCtreatedAt());

        sqLiteDatabase.insert("myrequests", null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteOldCache() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM myrequests");
        sqLiteDatabase.close();
    }

    public ArrayList getData() {
        ArrayList data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM myrequests", null);

        if (cursor.moveToFirst()) {
            do {
                MyRequests myrequestsModel = new MyRequests();
                myrequestsModel.setStrRequestTypeUrl(cursor.getString(0));
                myrequestsModel.setStrRequestTcktCode(cursor.getString(1));
                myrequestsModel.setStrRequestTitle(cursor.getString(2));
                myrequestsModel.setStrRequestBody(cursor.getString(3));
                myrequestsModel.setStrRequestTime(cursor.getString(4));
                myrequestsModel.setStrRequestColor(cursor.getString(5));
                myrequestsModel.setStrRequestStatus(cursor.getString(6));
                myrequestsModel.setStrCtreatedAt(cursor.getString(7));

                data.add(myrequestsModel);
            } while (cursor.moveToNext());
        }

        return data;
    }
}