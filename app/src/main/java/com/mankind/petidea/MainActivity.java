package com.mankind.petidea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mankind.petidea.activities.HomeActivity;
import com.mankind.petidea.activities.ProfileActivity;
import com.mankind.petidea.activities.SignIn;
import com.mankind.petidea.activities.SignUp;
import com.mankind.petidea.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collectionReference = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid());
        if(mAuth != null && mAuth.getCurrentUser() != null){
            if (collectionReference != null) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }else{
                startActivity(new Intent(this, ProfileActivity.class));
            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });
    }
}