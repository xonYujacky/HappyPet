package com.adj.happypet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase fireDatabase;
    private DatabaseReference databaseReference;

    private TextView bannerHP;
    private EditText edt_fullName;
    private EditText edt_age;
    private EditText email_regis;
    private EditText pass_regis;
    private ProgressBar progressBar;
    private Button regis_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findId();

        regis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        bannerHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(move);
            }
        });
    }

    private void createUser() {
        // validate string variable from input data
        final String email = email_regis.getText().toString().trim();
        final String password = pass_regis.getText().toString().trim();
        final String fullname = edt_fullName.getText().toString().trim();
        final String age = edt_age.getText().toString().trim();

        // all field must required

        if (fullname.isEmpty()) {
            edt_fullName.setError("Full Name must be Required!");
            edt_fullName.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            edt_age.setError("Age must be Required!");
            edt_age.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            email_regis.setError("Email must be Required!");
            email_regis.requestFocus();
            return;
        }
        //check email syntax
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_regis.setError("Please check your email format");
            email_regis.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pass_regis.setError("Password must be Required");
            pass_regis.requestFocus();
            return;
        }

        //password must be at least 6
        if (password.length() < 6) {
            pass_regis.setError("Password must be at least 6");
            pass_regis.requestFocus();
            return;
        }

        //set ProgressBar visibility
        progressBar.setVisibility(View.VISIBLE);

        //firebase create account by email and password
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                fireDatabase = FirebaseDatabase.getInstance();
                databaseReference = fireDatabase.getReference("users");
                User user = new User(fullname,age,email,password);

                databaseReference.child(fullname).setValue(user);
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Berhasil mendaftarkan akun anda!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Intent keLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(keLogin);
                }else{
                    Toast.makeText(RegisterActivity.this,"Failed to register , Try Again !",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



    }


    private void findId() {
        bannerHP = findViewById(R.id.happy_pet_banner);
        edt_fullName = findViewById(R.id.fName_edt_regis);
        edt_age = findViewById(R.id.age_edt_regis);
        email_regis = findViewById(R.id.email_edt_regis);
        pass_regis = findViewById(R.id.pass_edt_regis);
        progressBar = findViewById(R.id.progressBar_regis);
        regis_btn = findViewById(R.id.btn_regis);



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }
}
