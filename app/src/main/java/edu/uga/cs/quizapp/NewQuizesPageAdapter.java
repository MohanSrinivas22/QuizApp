package edu.uga.cs.quizapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;



public class NewQuizesPageAdapter extends FragmentStateAdapter {

    private List<StateCapitalPOJO> list = new ArrayList<>();
    int quizId;// Define a field to store the extra value

    public NewQuizesPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<StateCapitalPOJO> list) {
        super(fragmentManager, lifecycle);
        this.list = list;  // Store the extra value in the adapter
    }

    /**
     * Creating new instance to NewQuizFragment to display new/next question upon swipe
     * @param position current swipe position
     * @return NewQuizFragment
     */
    @Override
    public Fragment createFragment(int position){
        int temp = position > 5 ? 1 : position;
        return NewQuizFragment
                .newInstance( String.valueOf(list.get(temp)), position, list);
    }

    /**
     * callback to define number of swipes
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size()+1;
    }

}
