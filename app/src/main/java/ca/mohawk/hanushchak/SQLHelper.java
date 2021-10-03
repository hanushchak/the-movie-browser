package ca.mohawk.hanushchak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String TAG = "@@ SQLHelper @@";

    public static final String DATABASE_FILE_NAME = "MyDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLENAME = "imdb_wishlist";
    public static final String ID = "_id";
    public static final String IMDBID = "imdb_id";
    public static final String TITLE = "title";
    public static final String YEAR = "year";
    // This is the SQL Statement that will be executed to create the table and columns
    // "_id" is recommended to be a standard first column and primary key
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLENAME + " ( " + ID + " INTEGER PRIMARY KEY, " +
                    IMDBID + " TEXT, " + TITLE + " TEXT, " + YEAR + " TEXT) ";
    public SQLHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Constructor");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate " + SQL_CREATE);
        db.execSQL(SQL_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This is only called if the DATABASE_VERSION changes
        // Possible actions - delete table (ie DROP TABLE IF EXISTS mytable), then call onCreate
    }


}
