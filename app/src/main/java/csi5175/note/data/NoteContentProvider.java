/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import csi5175.note.utility.Constants;

/*
* provide services to the app for querying database instead of direct database access*/
public class NoteContentProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;
    private static final String BASE_PATH = "notes";
    private static final String AUTHORITY = "csi5175.note.databases";
    private static final int NOTE = 100;
    private static final int NOTES = 101;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTE);
    }

    private boolean checkColumn(String[] projection) {
        HashSet<String> request = new HashSet<>(Arrays.asList(projection));
        HashSet<String> require = new HashSet<>(Arrays.asList(Constants.COLUMNS));
        if (!require.containsAll(request)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Constants.NOTES_TABLE);
        checkColumn(projection);
        int type = uriMatcher.match(uri);
        switch (type) {
            case NOTE:
                break;
            case NOTES:
                break;
            default:
                throw new IllegalArgumentException("NoteContentProvider Unknown URI: " + uri);

        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int type = uriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Long id;
        switch (type) {
            case NOTES:
                id = db.insert(Constants.NOTES_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("NoteContentProvider Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int type = uriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int res;
        switch (type) {
            case NOTE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    res = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + " = " + id, null);
                } else {
                    res = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            case NOTES:
                res = db.delete(Constants.NOTES_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("NoteContentProvider Unknown URI: " + uri);
        }
        return res;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int type = uriMatcher.match(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int res;
        switch (type) {
            case NOTE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    res = db.update(Constants.NOTES_TABLE, values, Constants.COLUMN_ID + " = " + id, null);
                } else {
                    res = db.update(Constants.NOTES_TABLE, values, Constants.COLUMN_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            case NOTES:
                res = db.update(Constants.NOTES_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("NoteContentProvider Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return res;
    }

}
