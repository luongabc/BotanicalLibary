package com.example.botanicallibrary.en.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseGbifMedia implements Serializable {
    @Expose
    private Integer offset;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("endOfRecords")
    @Expose
    private Boolean endOfRecords;
    @SerializedName("results")
    @Expose
    private List<ResultGbifMedia> results = null;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getEndOfRecords() {
        return endOfRecords;
    }

    public void setEndOfRecords(Boolean endOfRecords) {
        this.endOfRecords = endOfRecords;
    }

    public List<ResultGbifMedia> getResults() {
        return results;
    }

    public void setResults(List<ResultGbifMedia> results) {
        this.results = results;
    }
}
