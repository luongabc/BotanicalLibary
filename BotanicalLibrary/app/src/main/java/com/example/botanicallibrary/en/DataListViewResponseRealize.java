package com.example.botanicallibrary.en;

import java.io.Serializable;

public class DataListViewResponseRealize implements Serializable {
    private String url,name,score,gbif;

    public String getGbif() {
        return gbif;
    }

    public void setGbif(String gbif) {
        this.gbif = gbif;
    }

    public DataListViewResponseRealize(String url, String name, String score, String gbif) {
        this.url = url;
        this.name = name;
        this.score = score;
        this.gbif = gbif;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
