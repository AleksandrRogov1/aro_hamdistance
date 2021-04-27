package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Distances {

    @JsonProperty("doc")
    @SerializedName(value = "doc")
    private String doc;

    @JsonProperty("rabinDistance")
    @SerializedName(value = "rabinDistance")
    private int rabinDistance;

    @JsonProperty("fnv1a64Distance")
    @SerializedName(value = "fnv1a64Distance")
    private int fnv1a64Distance;

    @JsonProperty("murmurDistance")
    @SerializedName(value = "murmurDistance")
    private int murmurDistance;

    public String getDoc() {
        return doc;
    }

    public void setDoc(final String doc) {
        this.doc = doc;
    }

    public int getRabinDistance() {
        return rabinDistance;
    }

    public void setRabinDistance(final int rabinDistance) {
        this.rabinDistance = rabinDistance;
    }

    public int getFnv1a64Distance() {
        return fnv1a64Distance;
    }

    public void setFnv1a64Distance(final int fnv1a64Distance) {
        this.fnv1a64Distance = fnv1a64Distance;
    }

    public int getMurmurDistance() {
        return murmurDistance;
    }

    public void setMurmurDistance(final int murmurDistance) {
        this.murmurDistance = murmurDistance;
    }
}
