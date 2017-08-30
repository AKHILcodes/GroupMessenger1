package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    DatabaseHelper mOpenHelper;
    SQLiteDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mOpenHelper.getWritableDatabase();

        //Log.v("key",key);
        //Log.v("value",values.getAsString(Integer.toString(counter)));
        //mOpenHelper.insertData("key"+Integer.toString(counter),values.toString());
        //values.get("key").toString()
        //mOpenHelper.insertData(values.get("key"));
        //mOpenHelper.insertData(values.get("value"));
        db.insert(mOpenHelper.TABLE_NAME,null,values);
        Log.v("message",values.get("value").toString());
        //mOpenHelper.insertData(values.get("key").toString(),values.get("value").toString());
        /*
         * TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * every content provider is specific to one avd
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */
        Log.v("insert", values.toString());
        return uri;
    }

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.
        //mOpenHelper = new DatabaseHelper(getContext());
        mOpenHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */
        db = mOpenHelper.getReadableDatabase();
        String[] myprojection = {mOpenHelper.COL_1,mOpenHelper.COL_2};
        String myselection = mOpenHelper.COL_1 + " = ?";
        String[] mySelectionArgs = {selection};
        //Log.v("row index",Integer.toString(mOpenHelper.getData(selection).getColumnIndex("key")));
        //Log.v("column index",Integer.toString(mOpenHelper.getData(selection).getColumnIndex("value")));
        return db.query(mOpenHelper.TABLE_NAME,myprojection,myselection,mySelectionArgs,null,null,null);
        //Log.v("query", selection);
        //Log.v("cursorquery",Integer.toString(mOpenHelper.getData(selection).getCount()));
        //return mOpenHelper.getData(selection);
        //return null;
    }
}
