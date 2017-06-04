package yau.celine.fitblrplan.db;

import android.provider.BaseColumns;

/**
 * Created by Celine on 2016-11-02.
 */

public class WorkoutContract {
    public static final String DB_NAME = "fitblrplan.workoutlist.db";
    public static final int DB_VERSION = 1;

    public class WorkoutEntry implements BaseColumns{
        public static final String TABLE_NAME = "workouts";
        public static final String WORKOUT_NAME = "title";

//        Don't know if I need code bellow
//        B/c SQL should create unique ID for ea
//        workout that is stored
//        public static final String COL_WORKOUT_ID = "workout_id";
    }
}
