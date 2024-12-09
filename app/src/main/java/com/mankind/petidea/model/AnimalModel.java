package com.mankind.petidea.model;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AnimalModel {
    private Uri animalProfileUri;
    private String animalProfilePictureUrl;
    private String animalBreed;

    public AnimalModel(Uri animalProfileUri, String animalProfilePictureUrl, String animalBreed) {
        this.animalProfileUri = animalProfileUri;
        this.animalProfilePictureUrl = animalProfilePictureUrl;
        this.animalBreed = animalBreed;
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

}
