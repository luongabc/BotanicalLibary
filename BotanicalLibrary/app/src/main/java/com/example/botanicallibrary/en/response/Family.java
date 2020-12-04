package com.example.botanicallibrary.en.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Family implements Serializable {
    @SerializedName("scientificNameWithoutAuthor")
    @Expose
    private String scientificNameWithoutAuthor;
    @SerializedName("scientificNameAuthorship")
    @Expose
    private String scientificNameAuthorship;

    public String getScientificNameWithoutAuthor() {
        return scientificNameWithoutAuthor;
    }

    public void setScientificNameWithoutAuthor(String scientificNameWithoutAuthor) {
        this.scientificNameWithoutAuthor = scientificNameWithoutAuthor;
    }

    public Family(String scientificNameWithoutAuthor, String scientificNameAuthorship) {
        this.scientificNameWithoutAuthor = scientificNameWithoutAuthor;
        this.scientificNameAuthorship = scientificNameAuthorship;
    }

    @Override
    public String toString() {
        return "Family{" +
                "scientificNameWithoutAuthor='" + scientificNameWithoutAuthor + '\'' +
                ", scientificNameAuthorship='" + scientificNameAuthorship + '\'' +
                '}';
    }

    public String getScientificNameAuthorship() {
        return scientificNameAuthorship;
    }

    public void setScientificNameAuthorship(String scientificNameAuthorship) {
        this.scientificNameAuthorship = scientificNameAuthorship;
    }
}
