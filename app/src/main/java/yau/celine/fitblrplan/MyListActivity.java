//package yau.celine.fitblrplan;
//
//import android.app.ListActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ListAdapter;
//import android.widget.SimpleAdapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import yau.celine.fitblrplan.db.ExerciseDbHelper;
//
///**
// * Created by Celine on 2017-05-18.
// */
//
//public class MyListActivity extends ListActivity {
//    private int parentId;
//    private static final String TAG = "MyListActivity";
//
//    public MyListActivity(int itemNum){
//        this.parentId = itemNum;
//    }
//
//    @Override
//    public void onCreate (Bundle icicle){
//        Log.d(TAG, "onCreate:"+this.parentId);
//        final ExerciseDbHelper db = new ExerciseDbHelper(this);
//
//        ArrayList<HashMap<String, String>> Items = new ArrayList<HashMap<String, String>>();
//
//        super.onCreate(icicle);
//
//        setContentView(R.layout.workout_exercises);
//
//        List<Movements> data = db.getAllDatas(this.parentId, 1);
//        for (Movements move : data){
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("Exercise", move.getName());
//            map.put("Sets", Integer.toString(move.getSets()));
//            map.put("Reps", Integer.toString(move.getReps()));
//
//            Items.add(map);
//        }
//
////        Adding Items to ListView
//        ListAdapter adapter = new SimpleAdapter(this, Items,
//                R.layout.workout_exercises,
//                new String[]{"Exercise", "Sets", "Reps"},
//                new int[] {R.id.workout_name, R.id.sets, R.id.reps});
//
//        setListAdapter(adapter);
//    }
//}
