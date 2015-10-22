package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements TitlePageFragment.onInstructionsListener, InstructionsFragment.onClueListener, VideoClueFragment.onTakePhotoListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, CameraActivity.class);
//        startActivity(intent);

        Fragment titlePage = new TitlePageFragment();
        Fragment instructions = new InstructionsFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, titlePage);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onInstructions(){
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new InstructionsFragment())
                .commit();
    }

    public void onClue(){
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new VideoClueFragment())
                .commit();
    }

    public void takePhoto(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}
