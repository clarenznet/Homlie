package com.luns.neuro.mlkn.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luns.neuro.mlkn.DataAdapter.ServiceItems;

import java.util.ArrayList;

public class SQLiteManagerServiceCalculator extends SQLiteOpenHelper {

    public SQLiteManagerServiceCalculator(Context context) {
        super(context, "android_cache_servicecalculator", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS servicecalculator(" +
                "strId TEXT NOT NULL,strArticle TEXT NOT NULL, strDemographic TEXT NOT NULL,strPrice TEXT NOT NULL," +
                "strArticleIconUrl TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE servicecalculator");
        onCreate(db);
    }

    public void addData(ServiceItems serviceItemsModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("strId", serviceItemsModel.getStrId());
        contentValues.put("strArticle", serviceItemsModel.getStrArticle());
        contentValues.put("strDemographic", serviceItemsModel.getStrDemographic());
        contentValues.put("strPrice", serviceItemsModel.getStrPrice());
        contentValues.put("strArticleIconUrl", serviceItemsModel.getStrArticleIconUrl());

        sqLiteDatabase.insert("servicecalculator", null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteOldCache() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM servicecalculator");
        sqLiteDatabase.close();
    }

    public ArrayList getData() {
        ArrayList data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM servicecalculator", null);

        if (cursor.moveToFirst()) {
            do {
                ServiceItems serviceItemsModel = new ServiceItems();
                serviceItemsModel.setStrId(cursor.getString(0));
                serviceItemsModel.setStrArticle(cursor.getString(1));
                serviceItemsModel.setStrDemographic(cursor.getString(2));
                serviceItemsModel.setStrPrice(cursor.getString(3));
                serviceItemsModel.setStrArticleIconUrl(cursor.getString(4));
                data.add(serviceItemsModel);
            } while (cursor.moveToNext());
        }

        return data;
    }
}