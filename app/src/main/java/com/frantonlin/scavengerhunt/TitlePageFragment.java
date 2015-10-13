package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * Created by keenan on 10/13/15.
 */
public class TitlePageFragment extends Fragment {

    public TitlePageFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.titlePage, container, false);
    }
}
