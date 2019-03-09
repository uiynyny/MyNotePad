/*
 * Yan Zhang
 * 300052103*/
package csi5175.note.utility;

//all constants that is used in the application
public class Constants {

    public static final String NOTES_TABLE = "notes";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_MODIFIED_TIME = "modified_time";
    public static final String COLUMN_CREATED_TIME = "created_time";
    public static final String COLUMNS_IMAGES = "images";

    public static final String[] COLUMNS = {
            Constants.COLUMN_ID,
            Constants.COLUMN_TITLE,
            Constants.COLUMN_CONTENT,
            Constants.COLUMN_CREATED_TIME,
            Constants.COLUMN_MODIFIED_TIME
    };

    public static final String DB_NAME = "notePad.db";

    public static final String CREATE_TABLE = "create table "
            + NOTES_TABLE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_CONTENT + " text not null, "
            + COLUMN_MODIFIED_TIME + " integer not null, "
            + COLUMN_CREATED_TIME + " integer not null "
            + ")";

}
