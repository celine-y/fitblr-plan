package yau.celine.fitblrplan;

/**
 * Created by Celine on 2017-05-18.
 */

public class Movements {

    String exerciseName;
    int workoutId;
    int listNum;
    int setNum;
    int repNum;

    public Movements(){

    }

    public Movements(String name, int wId, int num, int setNum, int repNum){
        this.exerciseName = name;
        this.workoutId = wId;
        this.listNum = num;
        this.setNum = setNum;
        this.repNum = repNum;
    }

    public int getID(){
        return this.workoutId;
    }

    public String getName(){
        return  this.exerciseName;
    }

    public int getSets(){
        return this.setNum;
    }

    public int getReps(){
        return this.repNum;
    }

    public void setListNum(int num){
        this.listNum = num;
    }

    public void setExerciseName(String name){
        this.exerciseName = name;
    }

}
