package yau.celine.fitblrplan;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import yau.celine.fitblrplan.db.WorkoutContract;
import yau.celine.fitblrplan.db.WorkoutDbHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private WorkoutDbHelper mHelper;
    private ListView mWorkoutListView;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new WorkoutDbHelper(this);
        mWorkoutListView = (ListView) findViewById(R.id.list_workout);
        updateUI();

        //allow clicking
        mWorkoutListView.setClickable(true);
        mWorkoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3){
                Log.d(TAG, "OnItemClick");
                Object oItem = mWorkoutListView.getItemAtPosition(position);
                Log.d(TAG, "Object: "+String.valueOf(position));

            }
        });
    }

    //updates the main screen with the new workout name
    private void updateUI(){
        ArrayList<String> workoutList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(WorkoutContract.WorkoutEntry.TABLE,
                new String []{
                        WorkoutContract.WorkoutEntry._ID,
                        WorkoutContract.WorkoutEntry.COL_WORKOUT_TITLE},
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()){
            int idx = cursor.getColumnIndex(WorkoutContract.WorkoutEntry.COL_WORKOUT_TITLE);
            workoutList.add(cursor.getString(idx));
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
            case R.id.action_add_workout:
//                creates an alertDialog for entering new workout
                final EditText workoutText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add new workout plan")
                        .setView(workoutText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick (DialogInterface dialog, int which){
                                String workoutName = String.valueOf(workoutText.getText());
                                if (!mHelper.isExistingEntry(WorkoutContract.WorkoutEntry.TABLE,
                                        WorkoutContract.WorkoutEntry.COL_WORKOUT_TITLE,
                                        workoutName)) {
                                    SQLiteDatabase db = mHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put(WorkoutContract.WorkoutEntry.COL_WORKOUT_TITLE, workoutName);
                                    db.insertWithOnConflict(WorkoutContract.WorkoutEntry.TABLE,
                                            null,
                                            values,
                                            SQLiteDatabase.CONFLICT_REPLACE);
                                    db.close();
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
        db.delete(WorkoutContract.WorkoutEntry.TABLE,
                WorkoutContract.WorkoutEntry.COL_WORKOUT_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }
}
