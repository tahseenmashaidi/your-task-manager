package com.example.yourtaskmanager;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar= findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Task App");

        fabBtn=findViewById(R.id.fab_btn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog= new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);
                View myview=inflater.inflate(R.layout.custominputfield,null);
                myDialog.setView(myview);

                AlertDialog dialog=myDialog.create();
                dialog.show();

            }
        });
    }
}