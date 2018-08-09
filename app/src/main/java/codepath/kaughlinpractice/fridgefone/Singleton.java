package codepath.kaughlinpractice.fridgefone;

import java.util.HashMap;
import java.util.HashSet;

public class Singleton {
    //static variable of single_instance of type Singleton
    private static Singleton sSingleInstance = null;

    public final static String[] recipe_traits = {"vegetarian", "vegan", "glutenFree", "dairyFree", "veryHealthy", "veryPopular", "cheap"};

    //shared data variables
    private HashSet<String> mSelectedNamesSet;
    private HashSet<String> mAllItemNamesSet;
    private String mSelectedItemsString;
    private String mAllFridgeItemsString;
    private boolean mAllSelected;
    private boolean mSelectItemsBoolean;
    private boolean mNoneSelected;
    private HashMap<String, Boolean> mUserDict;

    // private constructor restricted to class itself
    private Singleton(){
        mSelectedNamesSet = new HashSet<>();
        mAllItemNamesSet = new HashSet<>();
        mAllSelected = false;
        mSelectItemsBoolean = false;
        mNoneSelected = true;
        mUserDict = new HashMap<>();
        for (int i = 0; i < recipe_traits.length; i++) {
            mUserDict.put(recipe_traits[i], false);
        }
    }

    // static method to create instance of singleton class
    public static Singleton getSingletonInstance(){
        //To ensure only one instance is created
        if(sSingleInstance == null){
            sSingleInstance = new Singleton();
        }
        return sSingleInstance;
    }


    //getters
    public HashSet<String> getmSelectedNamesSet() {
        return mSelectedNamesSet;
    }

    public HashSet<String> getmAllItemNamesSet() {
        return mAllItemNamesSet;
    }

    public String getmSelectedItemsString() { return mSelectedItemsString; }

    public boolean ismAllSelected() {
        return mAllSelected;
    }

    public boolean ismSelectItemsBoolean() {
        return mSelectItemsBoolean;
    }

    public boolean ismNoneSelected() {
        return mNoneSelected;
    }

    public HashMap<String, Boolean> getmUserDict() {
        return mUserDict;
    }

    public String getmAllFridgeItemsString() {
        return mAllFridgeItemsString;
    }

    // setters
    public void setmAllSelected(boolean mAllSelected) {
        this.mAllSelected = mAllSelected;
    }

    public void setmSelectItemsBoolean(boolean mSelectItemsBoolean) {
        this.mSelectItemsBoolean = mSelectItemsBoolean;
    }

    public void setmSelectedItemsString(String mSelectedItemsString) {
        this.mSelectedItemsString = mSelectedItemsString;
    }

    public void setmNoneSelected(boolean mNoneSelected) {
        this.mNoneSelected = mNoneSelected;
    }

    public void setmAllFridgeItemsString(String mAllFridgeItemsString) {
        this.mAllFridgeItemsString = mAllFridgeItemsString;
    }

    public void resetFridge(){
        mSelectedNamesSet.clear();
        mAllSelected = false;
        mSelectItemsBoolean = false;
        mNoneSelected = true;
    }
}
