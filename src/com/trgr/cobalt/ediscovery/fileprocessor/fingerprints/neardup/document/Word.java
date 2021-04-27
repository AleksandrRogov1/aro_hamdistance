package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.document;

public class Word {

	private String word = "";
	private int frequency = 0;
	private int offset = 0;
	private int lastPos = 0;

	public Word(String word, int frequency, int offset) {
		this.word = word;
		this.frequency = frequency;
		this.offset = offset;
	}

	public Word() {
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLastPos() {
		return lastPos;
	}

	public void setLastPos(int lastPos) {
		this.lastPos = lastPos;
	}
}
