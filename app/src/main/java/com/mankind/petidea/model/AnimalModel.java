package com.mankind.petidea.model;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AnimalModel {
    private String animalName;
    private Uri animalProfileUri;
    private String animalProfilePictureUrl;
    private String animalBreed;
    private String animalInformation;

    public AnimalModel(String animalName,Uri animalProfileUri, String animalProfilePictureUrl, String animalBreed, String animalInformation) {
        this.animalName = animalName;
        this.animalProfileUri = animalProfileUri;
        this.animalProfilePictureUrl = animalProfilePictureUrl;
        this.animalBreed = animalBreed;
        this.animalInformation = animalInformation;
    }

    public Uri getAnimalProfileUri() {
        return animalProfileUri;
    }

    public void setAnimalProfileUri(Uri animalProfileUri) {
        this.animalProfileUri = animalProfileUri;
    }

    public String getAnimalProfilePictureUrl() {
        return animalProfilePictureUrl;
    }

    public void setAnimalProfilePictureUrl(String animalProfilePictureUrl) {
        this.animalProfilePictureUrl = animalProfilePictureUrl;
    }

    public String getAnimalBreed() {
        return animalBreed;
    }

    public void setAnimalBreed(String animalBreed) {
        this.animalBreed = animalBreed;
    }
    public String getAnimalInformation(){
        return animalInformation;
    }
    public void setAnimalInformation(String animalInformation){
        this.animalInformation = animalInformation;
    }

}
