package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NearDupModel {
	@JsonProperty("documentLocation")
	@SerializedName(value = "documentLocation")
	private String documentLocation;
	
	@JsonProperty("fnRabin")
	private String fnRabin;

	@JsonProperty("longRabin")
	private long longRabin;

	@JsonProperty("fnFnv1a64")
	private String fnFnv1a64;

	@JsonProperty("longFnv1a64")
	private long longFnv1a64;

	@JsonProperty("fnMurmur")
	private String fnMurmur;

	@JsonProperty("longMurmur")
	private long longMurmur;

    @JsonProperty("language")
    @SerializedName(value = "language")
    private String language;

	@JsonProperty("distances")
	@SerializedName(value = "distances")
    private List<Distances> distances = new ArrayList<>();

	@JsonIgnore
	private int[] rabinSim;
	@JsonIgnore
	private int[] fnvSim;
	@JsonIgnore
	private int[] murmurSim;

	/**
	 * @return the documentLocation
	 */
	public String getDocumentLocation() {
		return documentLocation;
	}
	/**
	 * @param documentLocation the documentLocation to set
	 */
	public void setDocumentLocation(String documentLocation) {
		this.documentLocation = documentLocation;
	}
	/**
	 * @return the documentGuid
	 */
    /**
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

	public String getFnRabin() {
		return fnRabin;
	}

	public void setFnRabin(final String fnRabin) {
		this.fnRabin = fnRabin;
	}

	public long getLongRabin() {
		return longRabin;
	}

	public void setLongRabin(final long longRabin) {
		this.longRabin = longRabin;
	}

	public String getFnFnv1a64() {
		return fnFnv1a64;
	}

	public void setFnFnv1a64(final String fnFnv1a64) {
		this.fnFnv1a64 = fnFnv1a64;
	}

	public long getLongFnv1a64() {
		return longFnv1a64;
	}

	public void setLongFnv1a64(final long longFnv1a64) {
		this.longFnv1a64 = longFnv1a64;
	}

	public String getFnMurmur() {
		return fnMurmur;
	}

	public void setFnMurmur(final String fnMurmur) {
		this.fnMurmur = fnMurmur;
	}

	public long getLongMurmur() {
		return longMurmur;
	}

	public void setLongMurmur(final long longMurmur) {
		this.longMurmur = longMurmur;
	}

	public int[] getRabinSim() {
		return rabinSim;
	}

	public void setRabinSim(final int[] rabinSim) {
		this.rabinSim = rabinSim;
	}

	public int[] getFnvSim() {
		return fnvSim;
	}

	public void setFnvSim(final int[] fnvSim) {
		this.fnvSim = fnvSim;
	}

	public int[] getMurmurSim() {
		return murmurSim;
	}

	public void setMurmurSim(final int[] murmurSim) {
		this.murmurSim = murmurSim;
	}

	public List<Distances> getDistances() {
		return distances;
	}

	public void setDistances(final List<Distances> distances) {
		this.distances = distances;
	}
}
