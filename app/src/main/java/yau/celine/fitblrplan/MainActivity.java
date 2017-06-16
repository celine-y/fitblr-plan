package yau.celine.fitblrplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import yau.celine.fitblrplan.db.ExerciseContract;
import yau.celine.fitblrplan.db.ExerciseDbHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ExerciseDbHelper mHelper;
    private ListView mWorkoutListView;
    private ArrayList<Integer> workoutId;
    private ArrayList<String> workoutList;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new ExerciseDbHelper(this);
        mWorkoutListView = (ListView) findViewById(R.id.list_workout);
        updateUI();
    }

    protected void OnListItemClick(ListView l, View v, int position, long id){
        Log.d(TAG, "onListItemClick");
    }

    //updates the main screen with the new workout name
    private void updateUI(){
        workoutList = new ArrayList<>();
        workoutId = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ExerciseContract.WorkoutEntry.TABLE_NAME,
                new String []{
                        ExerciseContract.WorkoutEntry._ID,
                        ExerciseContract.WorkoutEntry.WORKOUT_NAME},
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()){
            int title = cursor.getColumnIndex(ExerciseContract.WorkoutEntry.WORKOUT_NAME);
            int id = cursor.getInt(cursor.getColumnIndex(ExerciseContract.WorkoutEntry._ID));
            Log.d(TAG, Long.toString(id));
            workoutId.add(id);
            workoutList.add(cursor.getString(title));
        }

        if (mAdapter == null){
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_exercise,
                    R.id.workout_title,
                    workoutList
            );

            mWorkoutListView.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(workoutList);
            mAdapter.notifyDataSetChanged();
        }


        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Clicking the top right + button
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_item:
//                creates an alertDialog for entering new workout
                final EditText workoutText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add new workout plan")
                        .setView(workoutText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick (DialogInterface dialog, int which){
                                Log.d(TAG, "Add Clicked");
                                String workoutName = String.valueOf(workoutText.getText());
                                if (!mHelper.isExistingEntry(ExerciseContract.WorkoutEntry.TABLE_NAME,
                                        ExerciseContract.WorkoutEntry.WORKOUT_NAME,
                                        workoutName)) {
                                    mHelper.insertWorkout(workoutName);
                                    updateUI();
                                }
                                else{
                                    Context context = getApplicationContext();
                                    CharSequence text = "Sorry, '"+workoutName+"' is already an entry";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //deleting the workout
    public void deleteWorkout(View view) {
        View parent = (View) view.getParent();
        TextView workoutTextView = (TextView) parent.findViewById(R.id.workout_title);
        String task = String.valueOf(workoutTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ExerciseContract.WorkoutEntry.TABLE_NAME,
                ExerciseContract.WorkoutEntry.WORKOUT_NAME + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    public void openWorkout(View view){
        Log.d(TAG, "Start openWorkout");
        final int position = mWorkoutListView.getPositionForView((View) view.getParent());
        Log.d(TAG, Integer.toString(position));

        Intent i = new Intent(MainActivity.this, WorkoutExercises.class);
        i.putExtra("WORKOUT_ID", workoutId.get(position));

        Log.d(TAG, "WORKOUT_ID="+Integer.toString(workoutId.get(position)));
        i.putExtra("WORKOUT_TITLE", workoutList.get(position));
        startActivity(i);
    }
}
