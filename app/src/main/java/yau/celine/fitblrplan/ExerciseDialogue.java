package yau.celine.fitblrplan;

//import android.app.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import yau.celine.fitblrplan.db.ExerciseDbHelper;

//import android.app.DialogFragment;

/**
 * Created by Celine on 2017-06-06.
 */

public class ExerciseDialogue extends DialogFragment {
    EditText exName;
    EditText setNum;
    EditText repNum;
    static String dialogTitle;
    public int sets;
    public int reps;
    private ExerciseDbHelper exDbHelper;
    private ListView exListView;

    private  static final String TAG = "ExerciseDialogue";

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public ExerciseDialogue(){

    }

    public static ExerciseDialogue newInstance(int workoutId, int weekNum) {
        ExerciseDialogue frag = new ExerciseDialogue();
        Bundle args = new Bundle();
        args.putInt("workoutId", workoutId);
        args.putInt("weekNum", weekNum);
        frag.setArguments(args);
        return frag;
    }

    public void setDialogTitle(String title){
        dialogTitle = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final int workoutId = getArguments().getInt("workoutId");
        final int weekNum = getArguments().getInt("weekNum");

        Log.d(TAG, "onCreateView start: args="+workoutId+", "+weekNum);

        exDbHelper = new ExerciseDbHelper(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.popup_exercise, null);

//        Get button views
        exName = (EditText) view.findViewById(R.id.exercise_name);
        setNum = (EditText) view.findViewById(R.id.set_num);
        repNum = (EditText) view.findViewById(R.id.rep_num);

//      set default sets & reps
        this.sets = 0;
        this.reps = 0;

        builder.setView(view);
        Log.d(TAG, builder.toString());

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int which){
                String newExName = exName.getText().toString();
//              Get sets and reps from EditText
                if (tryParseInt(setNum.getText().toString())){
                    sets = Integer.parseInt(setNum.getText().toString());
                }
                if (tryParseInt(repNum.getText().toString())){
                    reps = Integer.parseInt(repNum.getText().toString());
                }

//                Validate all info is entered and valid
                if (newExName.length() > 0 && sets > 0 && reps > 0) {
//                TODO: insert proper listNum
                    Integer exId = exDbHelper.insertExercise(newExName, workoutId, 0, weekNum);

                    exDbHelper.insertSetsReps(exId, weekNum, sets, reps);

//                TODO: update UI in WorkoutExercises

                    // Send the positive button event back to the host activity
                    mListener.onDialogPositiveClick(ExerciseDialogue.this);

                }
                else{
                    showToast("Please double check you have entered everything correctly");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public  void onClick(DialogInterface dialogInterface, int which){
                //TODO: return back to main screen
                Log.d(TAG, "Cancel clicked");
                mListener.onDialogNegativeClick(ExerciseDialogue.this);
            }
        });

        return builder.create();
    }

    public void increaseSets(View view){
        this.sets++;
        setNum.setText(sets);
    }

    public void decreaseSets(View view){
        if (this.sets <= 0){
            return;
        }
        this.sets--;
        setNum.setText(sets);
    }

    public void increaseReps(View view){
        this.reps++;
        repNum.setText(reps);
    }

    public void decreaseReps(View view){
        if (this.reps <= 0){
            return;
        }
        this.reps--;
        repNum.setText(reps);
    }

    private void showToast(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
