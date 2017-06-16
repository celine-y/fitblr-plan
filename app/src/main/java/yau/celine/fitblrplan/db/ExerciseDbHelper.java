package yau.celine.fitblrplan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
                "CREATE TABLE IF NOT EXISTS "+ExerciseContract.ExerciseEntry.TABLE_NAME +
                        " ("+
                        ExerciseContract.ExerciseEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        ExerciseContract.ExerciseEntry.EXERCISE_TITLE+" TEXT NOT NULL, "+
                        ExerciseContract.ExerciseEntry.WORKOUT_ID+" INTEGER, "+
                        ExerciseContract.ExerciseEntry.LIST_NUM+" INTEGER, "+
                        ExerciseContract.ExerciseEntry.WEEK_NUM+" INTEGER, "+
                        "FOREIGN KEY ("+ ExerciseContract.ExerciseEntry.WORKOUT_ID+") REFERENCES "+
                        ExerciseContract.WorkoutEntry.TABLE_NAME+" ("+ExerciseContract.WorkoutEntry._ID+")"+
                        ");";
        Log.d(TAG, "strStmt="+createTable);

        db.execSQL(createTable);
//        creating setitions table
        createTable =
                "CREATE TABLE IF NOT EXISTS "+ ExerciseContract.SetitionEntry.TABLE_NAME+
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

        createTable =
                "CREATE TABLE "+ ExerciseContract.WorkoutEntry.TABLE_NAME +
                        " (" + ExerciseContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ExerciseContract.WorkoutEntry.WORKOUT_NAME + " TEXT NOT NULL);";
        Log.d(TAG, "strStmt="+createTable);
        db.execSQL(createTable);
    }

//    insert exercise into table
//    TODO: have it so that inserting the exercises also inserts the reps/sets
    public int insertExercise(String exName, int workoutListId, int listNum, int weekNum){
        SQLiteDatabase db = this.getReadableDatabase();

        String insertExercise =
                "INSERT INTO "+ ExerciseContract.ExerciseEntry.TABLE_NAME +
                        "("+ ExerciseContract.ExerciseEntry.EXERCISE_TITLE+", "+
                        ExerciseContract.ExerciseEntry.WORKOUT_ID+", "+
                        ExerciseContract.ExerciseEntry.LIST_NUM+", "+
                        ExerciseContract.ExerciseEntry.WEEK_NUM+
                        ") VALUES ( "+
                        "'"+exName+"', "+
                        "(SELECT "+ ExerciseContract.WorkoutEntry._ID+" FROM "+
                        ExerciseContract.WorkoutEntry.TABLE_NAME+" WHERE "+
                        ExerciseContract.WorkoutEntry._ID+" = "+workoutListId+"), "+
                        listNum+", "+
                        weekNum+
                        ");";

        ContentValues values = new ContentValues();

        values.put(ExerciseContract.ExerciseEntry.EXERCISE_TITLE, exName);
        values.put(ExerciseContract.ExerciseEntry.WORKOUT_ID, workoutListId);
        values.put(ExerciseContract.ExerciseEntry.LIST_NUM, listNum);
        values.put(ExerciseContract.ExerciseEntry.WEEK_NUM, weekNum);

        Long i = db.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, values);

        Log.d(TAG, "Returned ID="+i.toString());

        return i.intValue();

//        int returnId;
//        db.execSQL(insertExercise);
    }
//    TODO: update exercise

//    TODO: insert sets and reps into table
    public void insertSetsReps (int exId, int weekNum, int setNum, int repNum){
        SQLiteDatabase db = this.getReadableDatabase();

        String insertSetsReps =
                "INSERT INTO "+ExerciseContract.SetitionEntry.TABLE_NAME+
                        "("+ ExerciseContract.SetitionEntry.EXERCISE_ID+", "+
                        ExerciseContract.SetitionEntry.WEEK_NUM+", "+
                        ExerciseContract.SetitionEntry.SETS+", "+
                        ExerciseContract.SetitionEntry.REPS+
                        ") VALUES ( "+
                        "(SELECT "+ ExerciseContract.ExerciseEntry._ID+" FROM "+
                        ExerciseContract.ExerciseEntry.TABLE_NAME+" WHERE "+
                        ExerciseContract.ExerciseEntry._ID+" = "+exId+"), "+
                        weekNum+", "+
                        setNum+", "+
                        repNum+
                        ");";
        Log.d(TAG, "strStmt="+insertSetsReps);
        db.execSQL(insertSetsReps);
    }

//    TODO: update sets and reps

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ExerciseContract.ExerciseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ExerciseContract.SetitionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseContract.WorkoutEntry.TABLE_NAME);
        onCreate(db);
    }

//    Get the exercises (w/sets and reps) in arraylist
    public ArrayList<Movements> getAllDatas(int workoutId, int weekNum){
        ArrayList<Movements> moveList = new ArrayList<Movements>();

        String selectStmt = "SELECT * "+
                "FROM "+ ExerciseContract.ExerciseEntry.TABLE_NAME+" AS E" +
                " INNER JOIN "+ExerciseContract.SetitionEntry.TABLE_NAME+" AS S"
                +" ON E."+ ExerciseContract.ExerciseEntry._ID+" = S."+ ExerciseContract.SetitionEntry.EXERCISE_ID+
                " WHERE "+ ExerciseContract.ExerciseEntry.WORKOUT_ID+" = "+workoutId +
                " AND S." + ExerciseContract.SetitionEntry.WEEK_NUM+"="+weekNum+
                " ORDER BY "+ ExerciseContract.ExerciseEntry.LIST_NUM+
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

//    Check if workout name is already used
    public boolean isExistingEntry(String TableName,
                                   String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = getWritableDatabase();
        String Query = "SELECT * FROM " + TableName + " WHERE " + dbfield + " = '" + fieldValue+"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

//    insert the workout name
    public void insertWorkout (String workoutName){
        SQLiteDatabase db = getReadableDatabase();

        String insertWorkout =
                "INSERT INTO "+ ExerciseContract.WorkoutEntry.TABLE_NAME +
                        "("+ ExerciseContract.WorkoutEntry.WORKOUT_NAME+
                        ") VALUES ("+
                        "'"+workoutName+"');";

        Log.d(TAG, "strStmt="+insertWorkout);

        db.execSQL(insertWorkout);
    }

//    Array list for the weeks in spinner
    public List<String> getSpinnerWeeks (int workoutId){
        List<String> titles = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();

        String sqlMaxWeek = "SELECT MAX("+ ExerciseContract.ExerciseEntry.WEEK_NUM+") AS Weeknum " +
                "FROM "+ ExerciseContract.ExerciseEntry.TABLE_NAME+" "+
                "WHERE "+ ExerciseContract.ExerciseEntry.WORKOUT_ID+" = "+workoutId+
                ";";

        Cursor cursor = db.rawQuery(sqlMaxWeek, null);
        cursor.moveToFirst();
//        retrieve the max weekNum
        String maxWeek = cursor.getString(cursor.getColumnIndex("Weeknum"));

        if (maxWeek != null) {
            int i;
            for (i = 0; i <= Integer.parseInt(maxWeek); i++) {
                String weekTitle = "Week " + Integer.toString(i + 1);
                titles.add(i, weekTitle);
            }
//      Add the last title
            titles.add(i, "Start new week");
        }
        else{
            titles.add(0, "Start new week");
        }

        return titles;
    }
}
