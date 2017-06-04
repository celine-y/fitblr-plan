package yau.celine.fitblrplan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Celine on 2017-06-04.
 */

public class MyAdapter extends ArrayAdapter<Movements> {
    private static  final  String TAG = "MyAdapter";
    private final Context context;
    private final ArrayList<Movements> itemsArrayList;


    public MyAdapter(Context context, ArrayList<Movements> movementsArrayList) {
        super(context, R.layout.movement, movementsArrayList);
        this.context = context;
        this.itemsArrayList = movementsArrayList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "position"+position);
//        Log.d(TAG, "getView("+position+", "+convertView.toString()+", "+parent.toString()+")");

        // 1. Create inflater
         LayoutInflater inflater = (LayoutInflater) this.getContext()
         .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get movement from inflater
         View movementView = inflater.inflate(R.layout.movement, parent, false);
        // 3. Get the text views from the movementView
        TextView workoutView = (TextView) movementView.findViewById(R.id.workout_name);
        TextView setsView = (TextView) movementView.findViewById(R.id.sets);
        TextView repsView = (TextView) movementView.findViewById(R.id.reps);

        // 4. Set the text for textViews
        workoutView.setText(itemsArrayList.get(position).getName());
        setsView.setText(Integer.toString(itemsArrayList.get(position).getSets()));
        repsView.setText(Integer.toString(itemsArrayList.get(position).getReps()));

        // 5. return movementView
        return movementView;
    }
}

