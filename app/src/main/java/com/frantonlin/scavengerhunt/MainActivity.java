package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.UUID;
import java.util.HashMap;

/**
 * Main Activity of the Scavenger Hunt app
 *
 */
public class MainActivity extends AppCompatActivity implements TitlePageFragment.onInstructionsListener, InstructionsFragment.onClueListener{

    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private int clueNum;
    private HashMap<String, String> clue;
    private SharedPreferences prefs;
    private HTTPHandler httpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getPreferences(MODE_PRIVATE);
        clueNum = prefs.getInt("clueNum", 0);
        if(clueNum == 0) {
            clueNum = 1;
            prefs.edit().putInt("clueNum", 1);
            prefs.edit().apply();
        }

        Fragment titlePage = new TitlePageFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, titlePage);
        transaction.commit();

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

    /**
     * Increments the clue number and saves the changes to Shared Preferences
     */
    public void incrementClueNum() {
        clueNum++;
        prefs = this.getPreferences(MODE_PRIVATE);
        prefs.edit().putInt("clueNum", clueNum);
        prefs.edit().apply();
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
                photoFile = createImageFile(1); //TODO: get clue number and insert here
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            AmazonS3 s3Client = new AmazonS3Client();
            TransferUtility transferUtility = new TransferUtility(s3Client, getApplicationContext());

            s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
            File fileToUpload = new File(mCurrentPhotoPath);
            UUID uid = UUID.fromString("6f34f25e-0b0d-4426-8ece-a8b3f27f4b63");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageName = "Olin_Scavenge_" + timeStamp + ".jpg";

            TransferObserver observer = transferUtility.upload(
                    "olin-mobile-proto",     /* The bucket to upload to */
                    imageName,    /* The key for the uploaded object */
                    fileToUpload        /* The file where the data to upload exists */
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

            //(Replace "MY-BUCKET" with your S3 bucket name, and "MY-OBJECT-KEY" with whatever you would like to name the file in S3)
//            PutObjectRequest putRequest = new PutObjectRequest("olin-mobile-proto", imageName, fileToUpload).withCannedAcl(CannedAccessControlList.PublicRead);
//            PutObjectResult putResponse = s3Client.putObject(putRequest);
            Log.d("PHOTO URL", "https://olin-mobile-proto.s3.amazonaws.com/"+imageName);

            incrementClueNum();
            Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
            getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

    private File createImageFile(int number) throws IOException {
        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "clue_" + number + "_photo";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        ); // DOES NOT WORK IN EMULATOR

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
