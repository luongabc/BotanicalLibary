package com.example.botanicallibrary.en.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseDataPost implements Serializable {
    @Override
    public String toString() {
        return "ResponseDataPost{" +
                "query=" + query +
                ", language='" + language + '\'' +
                ", preferedReferential='" + preferedReferential + '\'' +
                ", results=" + resultRealizePlants +
                ", remainingIdentificationRequests=" + remainingIdentificationRequests +
                '}';
    }

    @SerializedName("query")
@Expose
private Query query;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("preferedReferential")
    @Expose
    private String preferedReferential;
    @SerializedName("results")
    @Expose
    private List<ResultRealizePlant> resultRealizePlants = null;
    @SerializedName("remainingIdentificationRequests")
    @Expose
    private Integer remainingIdentificationRequests;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreferedReferential() {
        return preferedReferential;
    }

    public void setPreferedReferential(String preferedReferential) {
        this.preferedReferential = preferedReferential;
    }

    public List<ResultRealizePlant> getResultRealizePlants() {
        return resultRealizePlants;
    }

    public void setResultRealizePlants(List<ResultRealizePlant> resultRealizePlants) {
        this.resultRealizePlants = resultRealizePlants;
    }

    public Integer getRemainingIdentificationRequests() {
        return remainingIdentificationRequests;
    }

    public void setRemainingIdentificationRequests(Integer remainingIdentificationRequests) {
        this.remainingIdentificationRequests = remainingIdentificationRequests;
    }
}
