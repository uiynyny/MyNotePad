/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import csi5175.note.utility.Constants;

/*help to initiate table and execute query formed by user*/
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = Constants.DB_NAME;
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = Constants.CREATE_TABLE;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.NOTES_TABLE);
        onCreate(db);
    }
}
