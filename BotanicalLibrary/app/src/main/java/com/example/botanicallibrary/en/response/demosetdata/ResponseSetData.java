
package com.example.botanicallibrary.en.response.demosetdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSetData {

    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("endOfRecords")
    @Expose
    private Boolean endOfRecords;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("facets")
    @Expose
    private List<Object> facets = null;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public List<Object> getFacets() {
        return facets;
    }

    public void setFacets(List<Object> facets) {
        this.facets = facets;
    }

}
