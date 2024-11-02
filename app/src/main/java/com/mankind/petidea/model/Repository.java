package com.mankind.petidea.model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mankind.petidea.activities.HomeActivity;
import com.mankind.petidea.activities.ProfileActivity;
import com.mankind.petidea.activities.SignIn;
import com.mankind.petidea.spinkit.SpinKitLoader;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;

    public Repository(){
        this.mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            documentReference = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document();
        }
    }

    public void signInUser(String email, String password, Context context){
        SpinKitLoader spinKitLoader = new SpinKitLoader(context);
        spinKitLoader.showDialog();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            spinKitLoader.dismissDialog();
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }else{
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        spinKitLoader.dismissDialog();
                                        Toast.makeText(context, "Email not verified, please check your email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    spinKitLoader.dismissDialog();
                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    spinKitLoader.dismissDialog();
                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void signUpUser(String email, String password, Context context){
        SpinKitLoader spinKitLoader = new SpinKitLoader(context);
        spinKitLoader.showDialog();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    spinKitLoader.dismissDialog();
                                    context.startActivity(new Intent(context.getApplicationContext(), SignIn.class).addFlags(
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    ));
                                    Toast.makeText(context, "Please check your email for verification", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                spinKitLoader.dismissDialog();
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    spinKitLoader.dismissDialog();
                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void forgotPassword(String email, Context context){
        if(!TextUtils.isEmpty(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            context.startActivity(new Intent(context, SignIn.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            Toast.makeText(context, "Check your email for password reset link", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }
    }
    public void signInAnonymously(Context context){
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    context.startActivity(new Intent(context, ProfileActivity.class).addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    ));
                    Toast.makeText(context, "Guest account created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void signOut(){
        mAuth.signOut();
    }
    public LiveData<List<ProfileModel>> getUserInformation(Context context){
        MutableLiveData mutableLiveData = new MutableLiveData();
        ArrayList<ProfileModel> profileModelArrayList = new ArrayList<>();
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProfileModel profileModel = documentSnapshot.toObject(ProfileModel.class);
                profileModelArrayList.add(profileModel);
                mutableLiveData.postValue(profileModelArrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return mutableLiveData;
    }
}