package yau.celine.fitblrplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Celine on 2017-05-03.
 */

public class WorkoutExercises extends AppCompatActivity {
    private static final String TAG = "WorkoutExercises";
    private long WORKOUT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_exercises);

        WORKOUT_ID = getIntent().getLongExtra("WORKOUT_ID", 0L);
        Log.d(TAG, "WORKOUT_ID:"+Long.toString(WORKOUT_ID));

        
    }
}
