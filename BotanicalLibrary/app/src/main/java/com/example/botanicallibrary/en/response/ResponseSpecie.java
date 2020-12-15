package com.example.botanicallibrary.en.response;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSpecie {

    @SerializedName("key")
    @Expose
    private Integer key;
    @SerializedName("nameKey")
    @Expose
    private Integer nameKey;
    @SerializedName("taxonID")
    @Expose
    private String taxonID;
    @SerializedName("sourceTaxonKey")
    @Expose
    private Integer sourceTaxonKey;
    @SerializedName("kingdom")
    @Expose
    private String kingdom;
    @SerializedName("phylum")
    @Expose
    private String phylum;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("genus")
    @Expose
    private String genus;
    @SerializedName("species")
    @Expose
    private String species;
    @SerializedName("kingdomKey")
    @Expose
    private Integer kingdomKey;
    @SerializedName("phylumKey")
    @Expose
    private Integer phylumKey;
    @SerializedName("classKey")
    @Expose
    private Integer classKey;
    @SerializedName("orderKey")
    @Expose
    private Integer orderKey;
    @SerializedName("familyKey")
    @Expose
    private Integer familyKey;
    @SerializedName("genusKey")
    @Expose
    private Integer genusKey;
    @SerializedName("speciesKey")
    @Expose
    private Integer speciesKey;
    @SerializedName("datasetKey")
    @Expose
    private String datasetKey;
    @SerializedName("constituentKey")
    @Expose
    private String constituentKey;
    @SerializedName("parentKey")
    @Expose
    private Integer parentKey;
    @SerializedName("parent")
    @Expose
    private String parent;
    @SerializedName("scientificName")
    @Expose
    private String scientificName;
    @SerializedName("canonicalName")
    @Expose
    private String canonicalName;
    @SerializedName("authorship")
    @Expose
    private String authorship;
    @SerializedName("nameType")
    @Expose
    private String nameType;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("taxonomicStatus")
    @Expose
    private String taxonomicStatus;
    @SerializedName("nomenclaturalStatus")
    @Expose
    private List<Object> nomenclaturalStatus = null;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("publishedIn")
    @Expose
    private String publishedIn;
    @SerializedName("numDescendants")
    @Expose
    private Integer numDescendants;
    @SerializedName("lastCrawled")
    @Expose
    private String lastCrawled;
    @SerializedName("lastInterpreted")
    @Expose
    private String lastInterpreted;
    @SerializedName("issues")
    @Expose
    private List<Object> issues = null;
    @SerializedName("synonym")
    @Expose
    private Boolean synonym;
    @SerializedName("class")
    @Expose
    private String _class;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getNameKey() {
        return nameKey;
    }

    public void setNameKey(Integer nameKey) {
        this.nameKey = nameKey;
    }

    public String getTaxonID() {
        return taxonID;
    }

    public void setTaxonID(String taxonID) {
        this.taxonID = taxonID;
    }

    public Integer getSourceTaxonKey() {
        return sourceTaxonKey;
    }

    public void setSourceTaxonKey(Integer sourceTaxonKey) {
        this.sourceTaxonKey = sourceTaxonKey;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getPhylum() {
        return phylum;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Integer getKingdomKey() {
        return kingdomKey;
    }

    public void setKingdomKey(Integer kingdomKey) {
        this.kingdomKey = kingdomKey;
    }

    public Integer getPhylumKey() {
        return phylumKey;
    }

    public void setPhylumKey(Integer phylumKey) {
        this.phylumKey = phylumKey;
    }

    public Integer getClassKey() {
        return classKey;
    }

    public void setClassKey(Integer classKey) {
        this.classKey = classKey;
    }

    public Integer getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Integer orderKey) {
        this.orderKey = orderKey;
    }

    public Integer getFamilyKey() {
        return familyKey;
    }

    public void setFamilyKey(Integer familyKey) {
        this.familyKey = familyKey;
    }

    public Integer getGenusKey() {
        return genusKey;
    }

    public void setGenusKey(Integer genusKey) {
        this.genusKey = genusKey;
    }

    public Integer getSpeciesKey() {
        return speciesKey;
    }

    public void setSpeciesKey(Integer speciesKey) {
        this.speciesKey = speciesKey;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getConstituentKey() {
        return constituentKey;
    }

    public void setConstituentKey(String constituentKey) {
        this.constituentKey = constituentKey;
    }

    public Integer getParentKey() {
        return parentKey;
    }

    public void setParentKey(Integer parentKey) {
        this.parentKey = parentKey;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getAuthorship() {
        return authorship;
    }

    public void setAuthorship(String authorship) {
        this.authorship = authorship;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTaxonomicStatus() {
        return taxonomicStatus;
    }

    public void setTaxonomicStatus(String taxonomicStatus) {
        this.taxonomicStatus = taxonomicStatus;
    }

    public List<Object> getNomenclaturalStatus() {
        return nomenclaturalStatus;
    }

    public void setNomenclaturalStatus(List<Object> nomenclaturalStatus) {
        this.nomenclaturalStatus = nomenclaturalStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPublishedIn() {
        return publishedIn;
    }

    public void setPublishedIn(String publishedIn) {
        this.publishedIn = publishedIn;
    }

    public Integer getNumDescendants() {
        return numDescendants;
    }

    public void setNumDescendants(Integer numDescendants) {
        this.numDescendants = numDescendants;
    }

    public String getLastCrawled() {
        return lastCrawled;
    }

    public void setLastCrawled(String lastCrawled) {
        this.lastCrawled = lastCrawled;
    }

    public String getLastInterpreted() {
        return lastInterpreted;
    }

    public void setLastInterpreted(String lastInterpreted) {
        this.lastInterpreted = lastInterpreted;
    }

    public List<Object> getIssues() {
        return issues;
    }

    public void setIssues(List<Object> issues) {
        this.issues = issues;
    }

    public Boolean getSynonym() {
        return synonym;
    }

    public void setSynonym(Boolean synonym) {
        this.synonym = synonym;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

}