package com.frantonlin.scavengerhunt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by franton on 10/29/15.
 */
public class FinishPageFragment extends Fragment {

    Button restart;
    MainActivity activity;

    public FinishPageFragment(){

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.finish_page, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        restart = (Button)view.findViewById(R.id.button);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.resetClueNum();
                activity.onInstructions();
            }
        });
    }
}
