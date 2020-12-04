package com.example.botanicallibrary.en.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultGbifMedia implements Serializable {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("rightsHolder")
    @Expose
    private String rightsHolder;
    @SerializedName("taxonKey")
    @Expose
    private Integer taxonKey;
    @SerializedName("sourceTaxonKey")
    @Expose
    private Integer sourceTaxonKey;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getRightsHolder() {
        return rightsHolder;
    }

    public void setRightsHolder(String rightsHolder) {
        this.rightsHolder = rightsHolder;
    }

    public Integer getTaxonKey() {
        return taxonKey;
    }

    public void setTaxonKey(Integer taxonKey) {
        this.taxonKey = taxonKey;
    }

    public Integer getSourceTaxonKey() {
        return sourceTaxonKey;
    }

    public void setSourceTaxonKey(Integer sourceTaxonKey) {
        this.sourceTaxonKey = sourceTaxonKey;
    }
}
