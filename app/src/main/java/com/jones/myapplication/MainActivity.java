package com.jones.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView im = findViewById(R.id.imageView);
        im.setImageResource(R.drawable.qr);
        Button b1 = findViewById(R.id.button);
        b1.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button)
            finishAffinity();
    }

    public void doPDF() {
        try {
            File file = new File("/storahe ");
            Uri uri = FileProvider.getUriForFile(this,  "com.jones.myapplication.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);


   //         Intent intent = new Intent(Intent.ACTION_VIEW);
   //         intent.setDataAndType(Uri.parse("/sdcard/Documents/Jones.pdf"), "application/pdf");
   //         intent = Intent.createChooser(intent, "Open File");
   //         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   //         startActivity(intent);

        } catch (Exception e )
        {
            Log.e("tag", "doPDF: "+ e.getLocalizedMessage() );
        }
    }

}