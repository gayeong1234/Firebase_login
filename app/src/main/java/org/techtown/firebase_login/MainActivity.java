package org.techtown.firebase_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.operation.ListenComplete;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    EditText et_user_name,et_user_age,et_pet_name,et_pet_age,et_pet_species;
    Button buttonSignup;
    TextView textviewSingin;
    TextView textviewMessage;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        readUser();

        if(firebaseAuth.getCurrentUser() != null){
            //?????? ????????? ???????????? ??? ??????????????? ?????????
            finish();
            //????????? profile ??????????????? ??????.
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class)); //????????? ??? ProfileActivity
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_user_age = (EditText) findViewById(R.id.et_user_age);
        et_pet_name = (EditText) findViewById(R.id.et_pet_name);
        et_pet_age = (EditText) findViewById(R.id.et_pet_age);
        et_pet_species = (EditText) findViewById(R.id.et_pet_species);
        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        //button click event
        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
    }

    //Firebse creating a new user
    private void registerUser(){
        //???????????? ???????????? email, password??? ????????????.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String userName = et_user_name.getText().toString().trim();
        String userAge = et_user_age.getText().toString().trim();
        String petName = et_pet_name.getText().toString().trim();
        String petAge = et_pet_age.getText().toString().trim();
        String petSpecies = et_pet_species.getText().toString().trim();

        //email??? password??? ???????????? ???????????? ?????? ??????.
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
        }

        //email??? password??? ????????? ???????????? ????????? ?????? ????????????.
        progressDialog.setMessage("??????????????????. ????????? ?????????...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            HashMap result = new HashMap<>();
                            result.put("email",email);
                            result.put("password",password);
                            result.put("userName",userName);
                            result.put("userAge",userAge);
                            result.put("petName",petName);
                            result.put("petAge",petAge);
                            result.put("petSpecies",petSpecies);

                            writeNewUser("id",email,password,userName,userAge,petName,petAge,petSpecies);

                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            //???????????????
                            textviewMessage.setText("????????????\n - ?????? ????????? ?????????  \n -?????? ?????? 6?????? ?????? \n - ????????????");
                            Toast.makeText(MainActivity.this, "?????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    //button click event
    @Override
    public void onClick(View view) {
        if(view == buttonSignup) {
            //TODO
            registerUser();
        }

        if(view == textviewSingin) {
            //TODO
            startActivity(new Intent(this, LoginActivity.class)); //????????? ??? ????????? ????????????
        }
    }

    private void writeNewUser(String userId, String email, String password, String userName, String userAge, String petName, String petAge, String petSpecies) {
        User user = new User(email, password, userName, userAge, petName, petAge, petSpecies);

        mDatabase.child("users").child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(MainActivity.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(MainActivity.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void readUser(){
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(MainActivity.this, "????????? ??????...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}