package com.mankind.petidea.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mankind.petidea.R;
import com.mankind.petidea.databinding.ActivitySignUpBinding;
import com.mankind.petidea.viewmodel.ViewModel;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signUpEmail.getText().toString().trim();
                String password = binding.signUpPassword.getText().toString().trim();
                String confirmPassword = binding.signUpConfirmPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
                    if (password.equals(confirmPassword)) {
                        viewModel.signUpUser(email, password, SignUp.this);
                        finish();
                        binding.signUpEmail.getText().clear();
                        binding.signUpPassword.getText().clear();
                        binding.signUpConfirmPassword.getText().clear();
                    }else{
                        Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SignUp.this, "Please fill in all the fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.loginPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.signInAnonymously(getApplicationContext());
            }
        });

    }

}