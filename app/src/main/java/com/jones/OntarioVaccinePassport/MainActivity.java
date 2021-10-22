package com.jones.OntarioVaccinePassport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICKFILE_RESULT_CODE = 2;
    private static String url = "";
    private static boolean firstime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadProps();
        if (!"".equals(url))
            loadQR();
        else
            doFilePrompt();

        Button b1 = findViewById(R.id.button);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       // if(view.getId() == R.id.button)
        //    finishAffinity();
        doFilePrompt();
    }

    private void loadQR() {
        ImageView im = findViewById(R.id.imageView);
        FileInputStream in = null;
        try {
            URI uri = new URI(url);
            in = new FileInputStream (new File(uri.getPath()));
        } catch (Exception e) {
            Log.e("loadQR", "loadQR: ",e );
        }
        im.setImageBitmap(BitmapFactory.decodeStream(in));
    }


    private void doFilePrompt () {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a png file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICKFILE_RESULT_CODE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                url = uri.getPath();
                if(!"".equals(url))
                    saveProps();
            }
        }
    }

    public void loadProps()  {
        SharedPreferences settings = getSharedPreferences("com.jones.OntarioVaccinePassport", Activity.MODE_PRIVATE);
        url =  settings.getString("com.jones.OntarioVaccinePassport.pdf", "");
    }


    public void saveProps()  {

        try {
            SharedPreferences settings = getSharedPreferences("com.jones.OntarioVaccinePassport", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("com.jones.OntarioVaccinePassport.pdf", url);
            editor.commit();
            loadQR();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}