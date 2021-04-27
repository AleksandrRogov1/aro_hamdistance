/*
 * Copyright 2019: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */

package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentModel {
	private String documentLocation;
	private String mimeType;
    private String languageCode;


    @JsonProperty("Path")
	public void setDocumentLocation(String documentLocation) {
		this.documentLocation = documentLocation;
	}
	
	@JsonProperty("Path")
	public String getDocumentLocation() {
		return documentLocation;
	}

	@JsonProperty("MimeType")
	public String getMimeType() {
		return mimeType;
	}

	@JsonProperty("MimeType")
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
    @JsonProperty("LanguageCode")
    public String getLanguageCode() {
        return languageCode;
    }

    @JsonProperty("LanguageCode")
    public void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }

    @JsonProperty("Language")
    public void setLanguage(final String language) {
        this.languageCode = language;
    }

}
