package com.example.contactlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUser extends Activity {
    private FirebaseAuth mAuth;
    private EditText usernameInput, passwordInput;
    private Button completeRegButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        completeRegButton = (Button) findViewById(R.id.complete_registration);

        completeRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = (EditText) findViewById(R.id.email_input);
                passwordInput = (EditText) findViewById(R.id.password_input);

                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                RegisterUserInFirebase(username,password);
            }

            private void RegisterUserInFirebase(String username, String password) {
                mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            ;
                        }
                    }
                });
            }
        });
    }





}
