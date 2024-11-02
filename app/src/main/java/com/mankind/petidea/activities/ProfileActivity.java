package com.mankind.petidea.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mankind.petidea.R;
import com.mankind.petidea.databinding.ActivityProfileBinding;
import com.mankind.petidea.model.ProfileModel;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ActivityProfileBinding binding;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;
    ActivityResultLauncher profilePhoto;
    private StorageReference storageReference;
    Uri profilePictureUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        profilePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            binding.profilePicture.setImageURI(o);
                            profilePictureUri = o;
                        }
                    }
                });
        binding.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePhoto.launch("image/*");
            }
        });
        firestore = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() != null) {
            collectionReference = firestore.collection(mAuth.getCurrentUser().getUid());
            documentReference = firestore.collection(mAuth.getCurrentUser().getUid()).document();
        }

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.usernameEditText.getText().toString().trim();
                String bio = binding.bioEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(bio)){
                    if(profilePictureUri != null){
                        StorageReference filePath = storageReference.child("user_profile_pictures")
                                .child("image"+ Timestamp.now().getSeconds());
                        filePath.putFile(profilePictureUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String profilePicturePath = uri.toString();
                                        ProfileModel profileModel = new ProfileModel(username, profilePictureUri, bio, profilePicturePath);
                                        documentReference.set(profileModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ProfileActivity.this, "Information added successfully", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        Toast.makeText(ProfileActivity.this, "Please select an image", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ProfileActivity.this, "Please fill the above fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}