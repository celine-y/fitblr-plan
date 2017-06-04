package yau.celine.fitblrplan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Celine on 2016-11-10.
 */

public class WorkoutDbHelper extends SQLiteOpenHelper {

    public WorkoutDbHelper(Context context){
        super(context, WorkoutContract.DB_NAME, null, WorkoutContract.DB_VERSION);

    }

    @Override
    public void onCreate (SQLiteDatabase db){
//      string for creating the table
        String createTable =
                "CREATE TABLE "+ WorkoutContract.WorkoutEntry.TABLE_NAME +
                " (" + WorkoutContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkoutContract.WorkoutEntry.TABLE_NAME + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + WorkoutContract.WorkoutEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean isExistingEntry(String TableName,
                                            String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = getWritableDatabase();
        String Query = "SELECT * from " + TableName + " WHERE " + dbfield + " = '" + fieldValue+"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
