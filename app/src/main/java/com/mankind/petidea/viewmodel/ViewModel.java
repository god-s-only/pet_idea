package com.mankind.petidea.viewmodel;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mankind.petidea.model.AnimalModel;
import com.mankind.petidea.model.ProfileModel;
import com.mankind.petidea.model.Repository;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private Repository repository;
    public ViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository();
    }
    public void signInUser(String email, String password, Context c){
        repository.signInUser(email, password, c);
    }
    public void signUpUser(String email, String password, Context context){
        repository.signUpUser(email, password, context);
    }

    public void forgotPassword(String email){
        repository.forgotPassword(email, this.getApplication());
    }
    public void signInAnonymously(Context context){
        repository.signInAnonymously(context);
    }
    public void signOut(){
        repository.signOut();
    }
    public LiveData<List<ProfileModel>> getUserInformation(Context context){
        return repository.getUserInformation(context);
    }
    public void addUserInformation(String username, Uri profilePictureUri, String bio, String profilePictureUrl, Context context){
        repository.addUserInformation(username, profilePictureUri, bio, profilePictureUrl, context);
    }
    public void updateUserInformation(String username,
                                      Uri profilePictureUri,
                                      String bio,
                                      String profilePictureUrl,
                                      Context context){
        repository.updateUserInformation(username, profilePictureUri, bio, profilePictureUrl, context);
    }
    public void addAnimalInformation(String animalname,Uri animalProfileUri, String animalProfilePictureUrl, String animalBreed,String animalInformation ,Context context){
        repository.addAnimalInformation(animalname,animalProfileUri, animalProfilePictureUrl, animalBreed,animalInformation ,context);
    }
    public LiveData<List<AnimalModel>> getAnimalInformation(Context context){
        return repository.getAnimalInformation(context);
    }
}
