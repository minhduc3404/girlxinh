package com.max.app.girlxinh.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mac on 12/22/15.
 */
public class SQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GXdb";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,phone text,email text, street text,place text)"
        );
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
