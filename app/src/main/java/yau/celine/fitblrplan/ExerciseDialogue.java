package yau.celine.fitblrplan;

//import android.app.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
    private  static final String TAG = "ExerciseDialogue";


//    public interface ExerciseDialogue {
//        void onFinishInputDialog(String inputText);
//    }

    public ExerciseDialogue(){

    }

    public void setDialogTitle(String title){
        dialogTitle = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Log.d(TAG, "onCreateView start");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.popup_exercise, null);

//        Get button views
        exName = (EditText) view.findViewById(R.id.exercise_name);
        setNum = (EditText) view.findViewById(R.id.set_num);
        repNum = (EditText) view.findViewById(R.id.rep_num);

//        setDefault Number of sets/reps
        setNum.setText("3");
        repNum.setText("5");

        this.sets = Integer.parseInt(this.setNum.getText().toString());
        this.reps = Integer.parseInt(this.repNum.getText().toString());

        builder.setView(view);
        Log.d(TAG, builder.toString());

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int which){
                //TODO: add exercise
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public  void onClick(DialogInterface dialogInterface, int which){
                //TODO: return back to main screen
            }
        });

        return builder.create();
    }

    public void increaseSets(View view){
        this.sets++;
        setNum.setText(sets);
    }

    public void decreaseSets(View view){
        this.sets--;
        setNum.setText(sets);
    }

    public void increaseReps(View view){
        this.reps--;
        repNum.setText(reps);
    }

    public void decreaseReps(View view){
        this.reps--;
        repNum.setText(reps);
    }



}
