package yau.celine.fitblrplan;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import yau.celine.fitblrplan.db.ExerciseDbHelper;


/**
 * Created by Celine on 2017-05-03.
 */

public class WorkoutExercises extends AppCompatActivity
        implements ExerciseDialogue.NoticeDialogListener {
    private static final String TAG = "WorkoutExercises";
    private Menu exerciseMenu;
    private Integer WORKOUT_ID;
    private Integer CURR_WEEK;
    private ExerciseDbHelper exDbHelper;
    private ListView exListView;
    private MyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_exercises);
//        show menu

//        get the id of the workoutlist
        WORKOUT_ID = getIntent().getIntExtra("WORKOUT_ID", 0);
        Log.d(TAG, "WORKOUT_ID:"+Integer.toString(WORKOUT_ID));

//      Initiate ExerciseDbHelper
        exDbHelper = new ExerciseDbHelper(this);

        updateSpinner();
//        Set current week as week 1
        CURR_WEEK = 0;

        // Get listView from workout_exercises.xml
        exListView = (ListView) findViewById(R.id.list_exercise);

        updateUI();

//       TODO: Change the title on the menu
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        Log.d(TAG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercises_menu, menu);
        this.exerciseMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
//            Adding an exercise
            case R.id.action_add_item:
//               Popup box
                FragmentManager fm = getSupportFragmentManager();
                ExerciseDialogue inputNew = ExerciseDialogue.newInstance(WORKOUT_ID, CURR_WEEK);
                inputNew.show(fm, "popup_exercise");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        updateUI();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    private void updateUI(){
        Log.d(TAG, "updateUI");
        // pass context and data to the custom adapter
        adapter = new MyAdapter(this, exDbHelper.getAllDatas(WORKOUT_ID, CURR_WEEK));

        if (adapter == null) {
            Log.d(TAG, "adapter  is null");
            //TODO:when adapter is empty
        }
        else{
            Log.d(TAG, "Adapter is not null");
            // setListAdapter
            exListView.setAdapter(adapter);
        }

    }

    private  void updateSpinner(){

//        Set the week dropdown
        final Spinner weekSpinner = (Spinner) findViewById(R.id.weeks_spinner);
        List<String> spinTitle = exDbHelper.getSpinnerWeeks(WORKOUT_ID);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinTitle);


        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                check if the last item on weeks is clicked (aka add new week)
//                if (position == weekSpinner.getCount()){
//
//                }
                CURR_WEEK = position;
                Log.d(TAG, "CURR_WEEK="+CURR_WEEK);
                updateUI();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // attaching data adapter to spinner
        weekSpinner.setAdapter(adapter);
    }
}
