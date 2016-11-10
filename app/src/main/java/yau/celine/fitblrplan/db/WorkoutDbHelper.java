package yau.celine.fitblrplan.db;

import android.content.Context;
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
//        creating the string for the table
        String createTable = "CREATE TABLE "+ WorkoutContract.WorkoutEntry.TABLE +
                "(" + WorkoutContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkoutContract.WorkoutEntry.COL_WORKOUT_TITLE + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + WorkoutContract.WorkoutEntry.TABLE);
        onCreate(db);
    }

}
