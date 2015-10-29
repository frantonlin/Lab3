package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Main Activity of the Scavenger Hunt app
 *
 */
public class MainActivity extends AppCompatActivity implements TitlePageFragment.onInstructionsListener, InstructionsFragment.onClueListener{

    static final int REQUEST_TAKE_PHOTO = 1;
    static final String PREFS_NAME = "OlinScavengePrefs";
    private String mCurrentPhotoPath;
    private int clueNum;
    private int numClues;
    private SharedPreferences prefs;
    private HTTPHandler httpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, 0);
        clueNum = prefs.getInt("clueNum", 0);
        Log.d("TESTING", String.valueOf(clueNum));
        if(clueNum == 0) {
            incrementClueNum();
            Fragment titlePage = new TitlePageFragment();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.container, titlePage);
            transaction.commit();
        } else if(clueNum == 1) {
            onInstructions();
        } else {
            onClue();
        }

        httpHandler = new HTTPHandler(this);
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

    /**
     * Switches views from the title page to the instructions page using fragment manager
     */
    public void onInstructions(){
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new InstructionsFragment())
                .commit();
    }

    /**
     * Switches views from the instructions page to the video clue page using fragment manager
     */
    public void onClue(){
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new VideoClueFragment())
                .commit();
    }

    public int getClueNum() {
        return clueNum;
    }


    public void setNumClues(int numClues) {
        this.numClues = numClues;
    }


    /**
     * Increments the clue number and saves the changes to Shared Preferences
     */
    public void incrementClueNum() {
        clueNum++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("clueNum", clueNum);
        editor.apply();
    }

    public void nextClue() {
        if(clueNum < numClues) {
            incrementClueNum();
            Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
            getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }
        else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new FinishPageFragment())
                    .commit();
        }
    }

    public void resetClueNum() {
        clueNum = 0;
        incrementClueNum();
    }

    public HTTPHandler getHttpHandler() {
        return httpHandler;
    }

    /**
     * Creates an intent to the camera, so we don't need a seperate activity
     */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("PHOTO FILE", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Make sure request is the same one that was requested and the result code is ok! Also override
     * the callback in httphandler
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            AmazonS3 s3Client = new AmazonS3Client();
            TransferUtility transferUtility = new TransferUtility(s3Client, getApplicationContext());

            s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
            File fileToUpload = new File(mCurrentPhotoPath);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageName = "Olin_Scavenge_" + timeStamp + ".jpg";

            TransferObserver observer = transferUtility.upload(
                    "olin-mobile-proto",     /* The bucket to upload to */
                    imageName,               /* The key for the uploaded object */
                    fileToUpload             /* The file where the data to upload exists */
            );

            httpHandler.postInfo(new PostCallback() {
                @Override
                public void callback(boolean success) {
                    if (success) {
                        Log.d("Success", Boolean.toString(success));
                    } else {
                        Log.d("Failure", Boolean.toString(success));
                    }
                }
            }, imageName, String.valueOf(clueNum));

//            (Replace "MY-BUCKET" with your S3 bucket name, and "MY-OBJECT-KEY" with whatever you would like to name the file in S3)
//            PutObjectRequest putRequest = new PutObjectRequest("olin-mobile-proto", imageName, fileToUpload).withCannedAcl(CannedAccessControlList.PublicRead);
//            PutObjectResult putResponse = s3Client.putObject(putRequest);
            Log.d("PHOTO URL", "https://olin-mobile-proto.s3.amazonaws.com/" + imageName);

            nextClue();
        }
    }

    /**
     * Creates the jpg image from the camera intent
     * @return A File type of an jpg
     * @throws IOException Signals a general, I/O-related error
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Olin_Scavenge_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        ); // DOES NOT WORK IN EMULATOR

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
