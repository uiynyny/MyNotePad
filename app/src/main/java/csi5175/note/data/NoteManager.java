/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import csi5175.note.model.Note;
import csi5175.note.utility.Constants;


/*provide notes and give app a way to interact with content provider or CRUD operation on database*/
public class NoteManager {
    private Context mContext;
    private static NoteManager noteManagerInstance;


    public static NoteManager newInstance(Context context) {
        if (noteManagerInstance == null) {
            noteManagerInstance = new NoteManager(context.getApplicationContext());
        }
        return noteManagerInstance;
    }

    private NoteManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public long create(Note note) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());
        Uri res = mContext.getContentResolver().insert(NoteContentProvider.CONTENT_URI, values);
        long id = Long.parseLong(res.getLastPathSegment());
        return id;
    }

    public long delete(Note note) {
        String whereClause = Constants.COLUMN_ID + "=" + note.getId();
        String[] whereArgs = {note.getId().toString()};
        int res = mContext.getContentResolver().delete(NoteContentProvider.CONTENT_URI, whereClause, null);
        return res;
    }

    public long update(Note note) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());
        String whereClause = Constants.COLUMN_ID + "=" + note.getId();
        String[] whereArgs = {note.getId().toString()};
        long id = mContext.getContentResolver().update(NoteContentProvider.CONTENT_URI, values, whereClause, null);
        return id;
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI, Constants.COLUMNS, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                notes.add(Note.getNotefromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notes;
    }

    public Note getNote(long id) {
        String selection = Constants.COLUMN_ID + "=" + id;
        String[] selectionArgs = {Long.toString(id)};
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI, Constants.COLUMNS, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return Note.getNotefromCursor(cursor);
        }
        return null;
    }
}
