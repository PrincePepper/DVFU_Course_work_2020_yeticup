package com.praisethedevil.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_CODE_GET_PHOTO = 101;
    private static ArrayList<SimpleClass> elems = new ArrayList<>();
    private static Integer cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (cnt == 0) {
            elems.add(new SimpleClass("bfdbfd","2019", ContextCompat.getDrawable(this, R.drawable.a)));
            elems.add(new SimpleClass("grbdf","2020", ContextCompat.getDrawable(this, R.drawable.b)));
            elems.add(new SimpleClass("grgr","2021", ContextCompat.getDrawable(this, R.drawable.c)));
            cnt++;
        }

        for (SimpleClass elem : elems) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerFrag, SimpleFrag.newInstance(elem)).commit();
        }
    }

    public void Add(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_CODE_GET_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GET_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            Uri photo = data.getData();
            elems.add(new SimpleClass("fds", "2664", photo));
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}