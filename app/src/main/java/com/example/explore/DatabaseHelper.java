package com.example.explore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "register.db";
    private static final String TABLE_NAME = "registeration";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "Name";
    private static final String COL_3 = "Password";

    SQLiteDatabase db;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID TEXT PRIMARY KEY ,Name TEXT,Password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //Drop older table if exists
        onCreate(db);
    }

    void addUser(USER user)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1,user.getID());
        values.put(COL_2,user.getName());
        values.put(COL_3,user.getPassword());

        db.insert(TABLE_NAME,null,values);
        db.close();
    }
/*    public String searchpassword(String uid)
    {

        db = this.getReadableDatabase();
        String Query = "select ID, password from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(Query,null);

        String a,b;
        b="not found";
        if(cursor.moveToFirst())
        {
            do{
                a=cursor.getString(0);


                if(a.equals(uid))
                {
                    b=cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return b;
    }*/
    public Cursor getData()
    {
        db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}
