package com.luns.neuro.mlkn.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luns.neuro.mlkn.DataAdapter.Notifs;

import java.util.ArrayList;

public class SQLiteManagerNotifications extends SQLiteOpenHelper {

    public SQLiteManagerNotifications(Context context) {
        super(context, "android_cache_notifications", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS mynotifs(" +
                "strNotifId TEXT NOT NULL,strNotifTitle TEXT NOT NULL, strNotifBody TEXT NOT NULL,strNotifTime TEXT NOT NULL," +
                "strNotifPostId TEXT NOT NULL,strNotifColor TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE mynotifs");
        onCreate(db);
    }

    public void addData(Notifs notifsModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("strNotifId", notifsModel.getStrNotifId());
        contentValues.put("strNotifTitle", notifsModel.getStrNotifTitle());
        contentValues.put("strNotifBody", notifsModel.getStrNotifBody());
        contentValues.put("strNotifTime", notifsModel.getStrNotifTime());
        contentValues.put("strNotifPostId", notifsModel.getStrNotifPostId());
        contentValues.put("strNotifColor", notifsModel.getStrNotifColor());

        sqLiteDatabase.insert("mynotifs", null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteOldCache() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM mynotifs");
        sqLiteDatabase.close();
    }

    public ArrayList getData() {
        ArrayList data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM mynotifs", null);

        if (cursor.moveToFirst()) {
            do {
                Notifs notifsModel = new Notifs();
                notifsModel.setStrNotifId(cursor.getString(0));
                notifsModel.setStrNotifTitle(cursor.getString(1));
                notifsModel.setStrNotifBody(cursor.getString(2));
                notifsModel.setStrNotifTime(cursor.getString(3));
                notifsModel.setStrNotifPostId(cursor.getString(4));
                notifsModel.setStrNotifColor(cursor.getString(5));

                data.add(notifsModel);
            } while (cursor.moveToNext());
        }

        return data;
    }
}