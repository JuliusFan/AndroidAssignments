package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "MESSAGES";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "MESSAGE";
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + KEY_ID + " integer primary key autoincrement, " + KEY_MESSAGE + " text not null);";

    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 1;


    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db){
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        db.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion="+oldVer+" newVersion="+newVer);
        Log.w(ChatDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVer + " to "
                        + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
