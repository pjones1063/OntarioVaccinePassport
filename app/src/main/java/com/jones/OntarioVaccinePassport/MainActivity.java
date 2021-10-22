package com.jones.OntarioVaccinePassport;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.PathUtils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;



public class MainActivity extends AppCompatActivity  {

    private static final int PICKFILE_RESULT_CODE = 2;
    private static String pfd_uri = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadProps();
        if (!loadQR()) doFilePrompt();
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private boolean loadQR() {
        verifyStoragePermissions(this);
        ImageView im = findViewById(R.id.imageView);
        FileInputStream in = null;
        try {
            in = new FileInputStream (new File(pfd_uri));
            im.setImageBitmap(BitmapFactory.decodeStream(in));
            return true;
        } catch (Exception e) {
            Log.e("loadQR", "loadQR: ",e );
            return false;
        }
    }


    private void doFilePrompt () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Image File Location");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.setText(pfd_uri);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pfd_uri = input.getText().toString();
                saveProps();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void loadProps()  {
        SharedPreferences settings = getSharedPreferences("com.jones.OntarioVaccinePassport", Activity.MODE_PRIVATE);
         pfd_uri =  settings.getString("com.jones.OntarioVaccinePassport.pdf", "");
    }


    public void saveProps()  {
        try {
            SharedPreferences settings = getSharedPreferences("com.jones.OntarioVaccinePassport", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("com.jones.OntarioVaccinePassport.pdf", pfd_uri);
            editor.commit();
            loadQR();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
}