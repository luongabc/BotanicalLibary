package com.example.botanicallibrary.en.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultRealizePlant implements Serializable {
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("species")
    @Expose
    private Species species;
    @SerializedName("gbif")
    @Expose
    private Gbif gbif;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Gbif getGbif() {
        return gbif;
    }

    public void setGbif(Gbif gbif) {
        this.gbif = gbif;
    }

}
