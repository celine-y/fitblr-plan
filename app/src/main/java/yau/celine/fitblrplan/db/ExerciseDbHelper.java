package yau.celine.fitblrplan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import yau.celine.fitblrplan.Movements;

//import static android.content.ContentValues.TAG;

/**
 * Created by Celine on 2017-05-18.
 */

public class ExerciseDbHelper extends SQLiteOpenHelper {
    private  static final String TAG = "ExerciseDbHelper";

    public ExerciseDbHelper(Context context){
        super(context, ExerciseContract.DB_NAME, null, ExerciseContract.DB_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
//        creating exercise table
        String createTable =
                "CREATE TABLE "+ExerciseContract.ExerciseEntry.TABLE_NAME +
                        " ("+
                        ExerciseContract.ExerciseEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        ExerciseContract.ExerciseEntry.EXERCISE_TITLE+" TEXT NOT NULL, "+
                        ExerciseContract.ExerciseEntry.WORKOUT_ID+" INTEGER, "+
                        ExerciseContract.ExerciseEntry.LIST_NUM+" INTEGER, "+
                        ExerciseContract.ExerciseEntry.WEEK_NUM+" INTEGER, "+
                        "FOREIGN KEY ("+ ExerciseContract.ExerciseEntry.WORKOUT_ID+") REFERENCES "+
                        WorkoutContract.WorkoutEntry.TABLE_NAME+" ("+WorkoutContract.WorkoutEntry._ID+")"+
                        ");";
        Log.d(TAG, "strStmt="+createTable);

        db.execSQL(createTable);
//        creating setitions table
        createTable =
                "CREATE TABLE "+ ExerciseContract.SetitionEntry.TABLE_NAME+
                        " ("+
                        ExerciseContract.SetitionEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        ExerciseContract.SetitionEntry.EXERCISE_ID +" INTEGER NOT NULL, "+
                        ExerciseContract.SetitionEntry.WEEK_NUM+" INTEGER DEFAULT 1, "+
                        ExerciseContract.SetitionEntry.SETS+" INTEGER DEFAULT 1, "+
                        ExerciseContract.SetitionEntry.REPS+" INTEGER DEFAULT 1, "+
                        "FOREIGN KEY ("+ ExerciseContract.SetitionEntry.EXERCISE_ID+") REFERENCES "+
                        ExerciseContract.ExerciseEntry.TABLE_NAME+" ("+ ExerciseContract.ExerciseEntry._ID+")"+
                        ");";
        Log.d(TAG, "strStmt="+createTable);
        db.execSQL(createTable);
    }

//    insert exercise into table
    public void insertExercise(String exName, String workoutList, int listNum, int weekNum){
        SQLiteDatabase db = this.getReadableDatabase();

        String insertExercise =
                "INSERT INTO "+ ExerciseContract.ExerciseEntry.TABLE_NAME +
                        "("+ ExerciseContract.ExerciseEntry.EXERCISE_TITLE+", "+
                        ExerciseContract.ExerciseEntry.WORKOUT_ID+", "+
                        ExerciseContract.ExerciseEntry.LIST_NUM+", "+
                        ExerciseContract.ExerciseEntry.WEEK_NUM+
                        ") VALUES ( "+
                        exName+", "+
                        "(SELECT "+ WorkoutContract.WorkoutEntry._ID+" FROM "+
                        WorkoutContract.WorkoutEntry.TABLE_NAME+" WHERE "+
                        WorkoutContract.WorkoutEntry.WORKOUT_NAME+" = '"+workoutList+"'), "+
                        listNum+", "+
                        weekNum+", "+
                        ");";

        db.execSQL(insertExercise);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ExerciseContract.ExerciseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ExerciseContract.SetitionEntry.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Movements> getAllDatas(int workoutId, int weekNum){
        ArrayList<Movements> moveList = new ArrayList<Movements>();

        String selectStmt = "SELECT * "+
                "FROM "+ ExerciseContract.ExerciseEntry.TABLE_NAME+" AS E" +
                " INNER JOIN "+ExerciseContract.SetitionEntry.TABLE_NAME+" AS S"
                +" ON E."+ ExerciseContract.ExerciseEntry._ID+" = S."+ ExerciseContract.SetitionEntry.EXERCISE_ID+
                " WHERE "+ ExerciseContract.ExerciseEntry.WORKOUT_ID+" = "+workoutId +
                " AND S." + ExerciseContract.SetitionEntry.WEEK_NUM+"="+weekNum+
                ";";

        Log.d(TAG, "strStmt=/"+selectStmt+"/");

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectStmt, null);

        if (cursor. moveToFirst()){
            do {
                String eName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_TITLE));
                int wId = cursor.getInt(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.WORKOUT_ID));
                int listNum = cursor.getInt(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.LIST_NUM));
                int setNum = cursor.getInt(cursor.getColumnIndex(ExerciseContract.SetitionEntry.SETS));
                int repNum = cursor.getInt(cursor.getColumnIndex(ExerciseContract.SetitionEntry.REPS));

                Log.d(TAG, "elements: "+eName+";"+wId+":"+listNum+";"+setNum+";"+repNum);
                Movements movement = new Movements(eName, wId, listNum, setNum, repNum);
                moveList.add(movement);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, moveList.toString());
        return  moveList;
    }
}
