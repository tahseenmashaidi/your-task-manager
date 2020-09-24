package com.example.yourtaskmanager;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourtaskmanager.Model.Data;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabBtn;
    private RecyclerView recyclerView;


    private DatabaseReference  mDatabase;
    private FirebaseAuth mAuth;

//    update input field
    private EditText titleUp,noteUp;
    private Button btnDeleteUp,btnUpdateUp;

//    variables
    private String title,note,post_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        recycler
        recyclerView=findViewById(R.id.recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


//        firebase
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uId);
        mDatabase.keepSynced(true);
//toolbar
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
                final AlertDialog dialog=myDialog.create();

                final EditText title=myview.findViewById(R.id.edit_title);
                final EditText note=myview.findViewById(R.id.edit_note);
                Button save_btn=myview.findViewById(R.id.btn_save);

                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mTitle=title.getText().toString().trim();
                        String mNote=note.getText().toString().trim();
                        if (TextUtils.isEmpty(mTitle)){
                            title.setError("Required Field..");
                            return;
                        }
                        if (TextUtils.isEmpty(mNote)){
                            note.setError("Required Field..");
                            return;
                        }

                        String id=mDatabase.push().getKey();
                        String date= DateFormat.getDateInstance().format(new Date());

                        Data data=new Data(mTitle,mNote,date,id);
                        mDatabase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Task Added",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,myViewHolder>adapter=new FirebaseRecyclerAdapter<Data, myViewHolder>(
                Data.class,R.layout.item_data,myViewHolder.class,mDatabase
        ) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, final Data model, final int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key=getRef(position).getKey();
                        title=model.getTitle();
                        note=model.getNote();
                        updateData();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{
        View myView;
        public myViewHolder( View itemView) {
            super(itemView);
            myView=itemView;
        }
        public void setTitle(String title){
            TextView mTitle=myView.findViewById(R.id.title);
            mTitle.setText(title);
        }
        public void setNote(String note){
            TextView mNote=myView.findViewById(R.id.note);
            mNote.setText(note);
        }
        public void setDate(String date){
            TextView mDate=myView.findViewById(R.id.date);
            mDate.setText(date);
        }

    }
    public void updateData(){
        AlertDialog.Builder myBuilder=new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);
        View myView=inflater.inflate(R.layout.updateinputfield,null);
        myBuilder.setView(myView);
        final AlertDialog dialog=myBuilder.create();
        titleUp=myView.findViewById(R.id.update_edit_title);
        noteUp=myView.findViewById(R.id.update_edit_note);
        btnDeleteUp=myView.findViewById(R.id.btn_delete_update);
        btnUpdateUp= myView.findViewById(R.id.btn_save_update);

        titleUp.setText(title);
        titleUp.setSelection(title.length());
        noteUp.setText(note);
        noteUp.setSelection(note.length());
        btnUpdateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=titleUp.getText().toString().trim();
                note=noteUp.getText().toString().trim();
                String mDate=DateFormat.getDateInstance().format(new Date());
                Data data=new Data(title,note,mDate,post_key);
                mDatabase.child(post_key).setValue(data);
                dialog.dismiss();
            }
        });
        btnDeleteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu ,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}