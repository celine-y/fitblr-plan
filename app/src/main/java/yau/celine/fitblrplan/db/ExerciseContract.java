package yau.celine.fitblrplan.db;

import android.provider.BaseColumns;

/**
 * Created by Celine on 2017-05-18.
 */

public class ExerciseContract {
    public static  final String DB_NAME = "fitblrplan.db";
    public static final int DB_VERSION = 7;

    public class WorkoutEntry implements BaseColumns {
        public static final String TABLE_NAME = "workouts";
        public static final String WORKOUT_NAME = "title";
    }

    public class ExerciseEntry implements BaseColumns{
        public  static final String TABLE_NAME = "exercises";
        public static final String EXERCISE_TITLE = "exerciseName";
        public static final String WORKOUT_ID = "workoutID";
        public static final String LIST_NUM = "listNum";
        public static final String WEEK_NUM = "weekNum";
    }

    public  class SetitionEntry implements  BaseColumns{
        public static final String TABLE_NAME = "setitions";
        public static  final String EXERCISE_ID = "eID";
        public static  final String WEEK_NUM = "weekNum";
        public static  final String SETS = "setNum";
        public static  final String REPS = "repNum";
    }

}
