package com.luns.neuro.mlkn.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luns.neuro.mlkn.DataAdapter.HelpItems;

import java.util.ArrayList;

public class SQLiteManagerHelp extends SQLiteOpenHelper {

    public SQLiteManagerHelp(Context context) {
        super(context, "android_cache_helpitemsMainApp", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS helpitems(" +
                "hlpid TEXT NOT NULL,hlpcategory TEXT NOT NULL, hlpissue TEXT NOT NULL,hlpdescription TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE helpitems");
        onCreate(db);
    }

    public void addData(HelpItems helpitemsModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("hlpid", helpitemsModel.getStrHlpId());
        contentValues.put("hlpcategory", helpitemsModel.getStrHlpCategory());
        contentValues.put("hlpissue", helpitemsModel.getStrHlpIssue());
        contentValues.put("hlpdescription", helpitemsModel.getStrHlpDescription());

        sqLiteDatabase.insert("helpitems", null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteOldCache() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM helpitems");
        sqLiteDatabase.close();
    }

    public ArrayList getData() {
        ArrayList data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM helpitems", null);

        if (cursor.moveToFirst()) {
            do {
                HelpItems helpitemsModel = new HelpItems();
                helpitemsModel.setStrHlpId(cursor.getString(0));
                helpitemsModel.setStrHlpCategory(cursor.getString(1));
                helpitemsModel.setStrHlpIssue(cursor.getString(2));
                helpitemsModel.setStrHlpDescription(cursor.getString(3));

                data.add(helpitemsModel);
            } while (cursor.moveToNext());
        }

        return data;
    }
}