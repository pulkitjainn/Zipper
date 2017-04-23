package com.example.pulkit.zipper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MA";
    public static final int BUFFER = 2048;

    Button zip,unzip;
    EditText et1,et2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zip = (Button) findViewById(R.id.zip);
        unzip = (Button) findViewById(R.id.unzip);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);

        zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String files = et1.getText().toString();
                final String zipName = et2.getText().toString();
                zip(files,zipName);
                Toast.makeText(MainActivity.this, "Zip File Created!", Toast.LENGTH_SHORT).show();
            }
        });

        unzip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String location = et1.getText().toString();
                final String zipName = et2.getText().toString();
                unzip(zipName,location);
                Toast.makeText(MainActivity.this, "File Unzipped!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void zip(String file, String zipFileName) {
        try {
            Toast.makeText(this, "Creating Zip...", Toast.LENGTH_LONG).show();
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                  out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unzip(String zipFile, String location){
        try{
            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry zipEntry = null;
            while ((zipEntry = zin.getNextEntry())!=null){
                Log.d(TAG, "Unzipping " + zipEntry.getName());
                if(zipEntry.isDirectory()){
                    File f = new File(location+zipEntry.getName());
                    if(!f.isDirectory()){
                        f.mkdirs();
                    }
                }else{
                    FileOutputStream fout = new FileOutputStream(location + zipEntry.getName());
                    for (int c = zin.read(); c != -1; zin.read()) {
                        fout.write(c);
                    }
                    zin.closeEntry();
                    fout.close();
                }
            }
            zin.close();
        }catch (Exception e){
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }
}

