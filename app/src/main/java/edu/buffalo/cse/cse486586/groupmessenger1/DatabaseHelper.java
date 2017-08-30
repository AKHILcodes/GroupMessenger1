package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by akhil on 2/19/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDB.db";
    public static final String TABLE_NAME = "MyTable";
    public static final String COL_1 = "key";
    public static final String COL_2 = "value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + COL_1 + " text, "+ COL_2 + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*public void insertData(String key, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, key);
        contentValues.put(COL_2, value);

        db.insert(TABLE_NAME,null,contentValues);
    }

    public Cursor getData(String keytest){
        SQLiteDatabase db = this.getReadableDatabase();
        String myselection = DatabaseHelper.COL_1 + " = ?";
        String[] myselectionArgs = {keytest};
        String[] myProjection = {DatabaseHelper.COL_2};
        //String query = "Select * from MyTable where key='" + keytest;
        //String[] columns = {DatabaseHelper.COL_2};
        //Cursor res = db.query(DatabaseHelper.TABLE_NAME,columns,DatabaseHelper.COL_1+" = '"+keytest+"'",null,null,null,null);
        Cursor res = db.query(DatabaseHelper.TABLE_NAME,myProjection,myselection,myselectionArgs,null,null,null);
        return res;
    }*/
}
