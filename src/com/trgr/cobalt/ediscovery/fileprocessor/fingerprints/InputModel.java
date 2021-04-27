
package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputModel {
	private List<DocumentModel> documents;
	private int nearDupsMinGrams;
	private int nearDupsMaxGrams;
	private boolean nearDupsUseStemming;
	private boolean nearDupsUseStopwords;

	@JsonProperty("MinGrams")
	public int getNearDupsMinGrams() {
		return nearDupsMinGrams;
	}

	@JsonProperty("MinGrams")
	public void setNearDupsMinGrams(final int nearDupsMinGrams) {
		this.nearDupsMinGrams = nearDupsMinGrams;
	}

	@JsonProperty("MaxGrams")
	public int getNearDupsMaxGrams() {
		return nearDupsMaxGrams;
	}

	@JsonProperty("MaxGrams")
	public void setNearDupsMaxGrams(final int nearDupsMaxGrams) {
		this.nearDupsMaxGrams = nearDupsMaxGrams;
	}

	@JsonProperty("UseStemming")
	public boolean getNearDupsUseStemming() {
		return nearDupsUseStemming;
	}

	@JsonProperty("UseStemming")
	public void setNearDupsUseStemming(final boolean nearDupsUseStemming) {
		this.nearDupsUseStemming = nearDupsUseStemming;
	}

	@JsonProperty("UseStopwords")
	public boolean getNearDupsUseStopwords() {
		return nearDupsUseStopwords;
	}

	@JsonProperty("UseStopwords")
	public void setNearDupsUseStopwords(final boolean nearDupsUseStopwords) {
		this.nearDupsUseStopwords = nearDupsUseStopwords;
	}

	@JsonProperty("Documents")
	public void setDocuments(List<DocumentModel> docs) {
		this.documents = docs;
	}
	
	@JsonProperty("Documents")
	public List<DocumentModel> getDocuments() {
		return documents;
	}

}
