package com.luns.neuro.mlkn.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luns.neuro.mlkn.DataAdapter.FundiTypes;

import java.util.ArrayList;

public class SQLiteManagerMainMenu extends SQLiteOpenHelper {

    public SQLiteManagerMainMenu(Context context) {
        super(context, "android_cache_mainmenudb1", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS mainmenu(" +
                "strFtId TEXT NOT NULL,strFtTitle TEXT NOT NULL, strFtDescription TEXT NOT NULL,strFtUrl TEXT NOT NULL, strServerResponseResultData TEXT NOT Null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE mainmenu");
        onCreate(db);
    }

    public void addData(FundiTypes funditypesModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("strFtId", funditypesModel.getStrFtId());
        contentValues.put("strFtTitle", funditypesModel.getStrFtTitle());
        contentValues.put("strFtDescription", funditypesModel.getStrFtDescription());
        contentValues.put("strFtUrl", funditypesModel.getStrFtUrl());
        contentValues.put("strServerResponseResultData", funditypesModel.getStrServerResponseResultData());


        sqLiteDatabase.insert("mainmenu", null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteOldCache() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM mainmenu");
        sqLiteDatabase.close();
    }

    public ArrayList getData() {
        ArrayList data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM mainmenu", null);

        if (cursor.moveToFirst()) {
            do {
                FundiTypes funditypesModel = new FundiTypes();
                funditypesModel.setStrFtId(cursor.getString(0));
                funditypesModel.setStrFtTitle(cursor.getString(1));
                funditypesModel.setStrFtDescription(cursor.getString(2));
                funditypesModel.setStrFtUrl(cursor.getString(3));
                funditypesModel.setStrServerResponseResultData(cursor.getString(4));
                data.add(funditypesModel);
            } while (cursor.moveToNext());
        }

        return data;
    }
}