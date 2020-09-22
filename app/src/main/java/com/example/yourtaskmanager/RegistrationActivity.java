package com.example.yourtaskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

//    Variables
    private EditText email,pass;
    private Button btnReg;
    private TextView login_txt;

//    Firebase variables
    private FirebaseAuth mAuth;

//    ProgressDialog
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        email=findViewById(R.id.email_reg);
        pass=findViewById(R.id.password_reg);
        btnReg=findViewById(R.id.reg_btn);
        login_txt=findViewById(R.id.login_text);

//      onClick listeners
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail=email.getText().toString().trim();
                String mPass=pass.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    email.setError("Required Field..");
                    return;
                }
                mDialog.setMessage("Processing..");
                mDialog.show();
                mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"SuccessFull",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            mDialog.dismiss();

                        }else {
                            Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });
    }
}