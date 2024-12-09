package com.mankind.petidea.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mankind.petidea.activities.HomeActivity;
import com.mankind.petidea.activities.ProfileActivity;
import com.mankind.petidea.activities.SignIn;
import com.mankind.petidea.activities.SignUp;
import com.mankind.petidea.spinkit.SpinKitLoader;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private DocumentReference animalReference;

    public Repository(){
        this.mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            documentReference = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document();
            collectionReference = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid());
            animalReference = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document(mAuth.getCurrentUser().getUid()).collection(mAuth.getCurrentUser().getUid()).document(mAuth.getCurrentUser().getUid());
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
                                        if (context instanceof Activity) {
                                            ((Activity) context).finish();
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
                                    if (context instanceof Activity) {
                                        ((Activity) context).finish();
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
    public void updateUserInformation(String username,
                                      Uri profilePictureUri,
                                      String bio,
                                      String profilePictureUrl,
                                      Context context){
        documentReference.update("username", username);
        documentReference.update("profilePictureUri", profilePictureUri).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        documentReference.update("bio", bio);
        documentReference.update("profilePictureUrl", profilePictureUrl);
    }
    public LiveData<List<ProfileModel>> adminPanelGetAllUsersInfo(Context context){
        ArrayList<ProfileModel> profileModelArrayList = new ArrayList<>();
        MutableLiveData<List<ProfileModel>> mutableLiveData = new MutableLiveData<>();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                    ProfileModel profileModel = snapshot.toObject(ProfileModel.class);
                    profileModelArrayList.add(profileModel);
                    mutableLiveData.postValue(profileModelArrayList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Sorry master, we could not get all users information, the problem is because of "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return mutableLiveData;
    }
    public void addUserInformation(String username, Uri profilePictureUri, String bio, String profilePictureUrl, Context context) {
        if (context == null) {
            Log.e("addUserInformation", "Context is null");
            return;
        }

        // Initialize SpinKitLoader and show loading dialog
        SpinKitLoader spinKitLoader = new SpinKitLoader(context);
        spinKitLoader.showDialog();
        if (documentReference == null) {
            Log.e("addUserInformation", "Document reference is null. Initialization required.");
            spinKitLoader.dismissDialog();
            Toast.makeText(context, "Failed to add information: database reference error.", Toast.LENGTH_SHORT).show();
            return;
        }
        ProfileModel profileModel = new ProfileModel(username, profilePictureUri, bio, profilePictureUrl);
        documentReference.set(profileModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        spinKitLoader.dismissDialog();
                        Toast.makeText(context, "Information added successfully", Toast.LENGTH_SHORT).show();
                        if (context instanceof Activity) {
                            context.startActivity(new Intent(context, HomeActivity.class));
                            ((Activity) context).finish();
                        } else {
                            Log.e("addUserInformation", "Invalid context type for starting HomeActivity");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        spinKitLoader.dismissDialog();
                        Log.e("addUserInformation", "Failed to add user information: " + e.getMessage(), e);
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteUser(Context context){
        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    documentReference.delete();
                    context.startActivity(new Intent(context, SignUp.class));
                    Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addAnimalInformation(Uri animalProfileUri, String animalProfilePictureUrl, String animalBreed, Context context){
        AnimalModel animalModel = new AnimalModel(animalProfileUri, animalProfilePictureUrl, animalBreed);
        animalReference.set(animalModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Information added successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public LiveData<List<AnimalModel>> getAnimalInformation(Context context){
        ArrayList<AnimalModel> animalModelArrayList = new ArrayList<>();
        MutableLiveData<List<AnimalModel>> listMutableLiveData = new MutableLiveData<>();
        animalReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AnimalModel animalModel = documentSnapshot.toObject(AnimalModel.class);
                animalModelArrayList.add(animalModel);
                listMutableLiveData.postValue(animalModelArrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return listMutableLiveData;
    }
}