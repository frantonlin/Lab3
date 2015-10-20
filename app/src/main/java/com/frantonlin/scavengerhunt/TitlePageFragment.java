package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by keenan on 10/13/15.
 */
public class TitlePageFragment extends Fragment {

    Button letsGo;
    onInstructionsListener mInstructionsListener;

    public TitlePageFragment(){

    }

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        mInstructionsListener = (onInstructionsListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.title_page, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        letsGo = (Button)view.findViewById(R.id.button);

        letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInstructionsListener.onInstructions();
            }
        });

    }

    public interface onInstructionsListener {
        public void onInstructions();
    }
}
