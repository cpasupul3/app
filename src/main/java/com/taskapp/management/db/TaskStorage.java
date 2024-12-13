
package com.taskapp.management.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskStorage extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task_manager.db";
    public static final int DATABASE_VERSION = 1;

    public TaskStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TaskSchema.TaskTable.TABLE_NAME + " (" +
                TaskSchema.TaskTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskSchema.TaskTable.COLUMN_NAME + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskSchema.TaskTable.TABLE_NAME);
        onCreate(db);
    }
}
