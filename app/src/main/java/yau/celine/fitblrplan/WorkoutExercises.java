package yau.celine.fitblrplan;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import yau.celine.fitblrplan.db.ExerciseDbHelper;


/**
 * Created by Celine on 2017-05-03.
 */

public class WorkoutExercises extends AppCompatActivity {
    private static final String TAG = "WorkoutExercises";
    private Menu exerciseMenu;
    private Integer WORKOUT_ID;
    private String WORKOUTLIST_NAME;
    private ExerciseDbHelper exDbHelper;
    private ListView exListView;
    private MyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_exercises);

//        get the id of the workoutlist
        WORKOUT_ID = getIntent().getIntExtra("WORKOUT_ID", 0);
        Log.d(TAG, "WORKOUT_ID:"+Integer.toString(WORKOUT_ID));

//      Initiate ExerciseDbHelper
        exDbHelper = new ExerciseDbHelper(this);


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
            case R.id.action_add_item:
//             adding an exercise
//               Popup box
                FragmentManager fm = getSupportFragmentManager();
                ExerciseDialogue inputNew = new ExerciseDialogue();
                inputNew.show(fm, "popup_exercise");
//                End popup box
//                final EditText excersizeText = new EditText(this);
//                AlertDialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("Add new exercise")
//                        .setView(excersizeText)
//                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Log.d(TAG, "Add Clicked");
//                                String excersizeName = String.valueOf(excersizeText.getText());
////                                TODO: insert proper week# (last value)
////                                TODO: update proper listNum
//                                exDbHelper.insertExercise(excersizeName, WORKOUTLIST_NAME, 0, 0);
//                                updateUI();
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create();
//                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){

        // 1. pass context and data to the custom adapter
        Movements testMove = new Movements("Ex#1", 0, 0, 3,10);
        ArrayList<Movements> testArry = new ArrayList<Movements>();
        testArry.add(testMove);
        adapter = new MyAdapter(this, testArry);


//        TODO: change to proper week#
//        adapter = new MyAdapter(this, exDbHelper.getAllDatas(WORKOUT_ID, 1));
        Log.d(TAG, "adapter="+adapter.toString());

        if (adapter == null) {
            Log.d(TAG, "adapter  is null");
            //TODO:when adapter is empty
        }
        else{
            Log.d(TAG, "Adapter is not null");
            // 3. setListAdapter
            exListView.setAdapter(adapter);
        }

    }
}
