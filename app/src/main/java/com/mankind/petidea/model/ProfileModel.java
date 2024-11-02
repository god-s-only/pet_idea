package com.mankind.petidea.model;

import android.net.Uri;

public class ProfileModel {
    private String username;
    private Uri profilePictureUri;
    private String bio;
    private String profilePictureUrl;


    public ProfileModel(String username, Uri profilePictureUri, String bio, String profilePictureUrl){
        this.username = username;
        this.profilePictureUri = profilePictureUri;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    public ProfileModel(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Uri getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(Uri profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
