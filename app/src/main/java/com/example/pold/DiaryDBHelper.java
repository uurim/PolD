package com.example.pold;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DiaryDBHelper extends SQLiteOpenHelper {
    public DiaryDBHelper(Context context) {
        super(context, "Diary", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE diary (" +
                "'code' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'title' VARCHAR(20)," +
                "'year' INTEGER," +
                "'month' INTEGER," +
                "'day' INTEGER," +
                "'contents' TEXT," +
                "'imgName' VARCHAR(20)," +
                "'mood' INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS diary");
        onCreate(db);
    }
}
