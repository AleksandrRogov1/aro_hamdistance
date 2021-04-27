package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.document;

import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.InputModel;
import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.NearDupModel;
import org.apache.lucene.analysis.Analyzer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

abstract public class FingerprintDocument {

	protected Map<String, Word> tokensTop; // Most important tokens
	protected Map<String, Word> singleTerms; // Store 1gram tokens, basically store words.
	protected int totalTokens = 0;
	protected int totalnGramTokens = 0;
	protected Map<String, Word> nGrams; // Store ngrams tokens
	protected final int TOPTOKENS = 60; // Number of significant tokens.
	protected double idf_tall = 0.80; // This idf value limits the value of which a word is considered significant
	private String fingerprint;


	// Only accept words between 4 -30 characters size
	protected static final int MAXLENGTHWORDS = 30;
	protected static final int MINLENGHTWORDS = 3;
	private boolean useStopWords = true;
	private boolean useStemming = true;
	protected Analyzer analyzer = null;
	//Min token generated
	protected int mingrams = 3;
	//Max token generated
	protected int maxgrams = 3;


	public FingerprintDocument(final InputModel inputModel) {
		this.singleTerms = new HashMap<>(500);
		this.nGrams = new HashMap<>(500);
		this.tokensTop = new HashMap<>(300); // Top 1 gram
		this.useStemming = inputModel.getNearDupsUseStemming();
		this.useStopWords = inputModel.getNearDupsUseStopwords();
		this.mingrams = inputModel.getNearDupsMinGrams();
		this.maxgrams = inputModel.getNearDupsMaxGrams();
	}

    abstract NearDupModel calculateFingerprint(final String documentLocation, final String languageCode, final Charset charset) throws IOException;

	/*
	 * Add a token to the Map, if the token exists he have to increase the frequency
	 * in 1. We store the offset value and the position of the token.
	 * 
	 * @param term: token
	 * 
	 * @param m: Where we store the term analyzed
	 */
	protected void add(String term, Map<String, Word> map) {
		Word word = map.get(term);
		Integer freq = 1;
		int offset = 0;

		if (word != null) {
			freq = word.getFrequency();
			offset = word.getOffset() + (this.totalTokens - word.getLastPos());
			freq++;
		} else {
			word = new Word();
			word.setWord(term);
		}

		word.setFrequency(freq);
		word.setOffset(offset);
		word.setLastPos(this.totalTokens);

		map.put(term, word);
	}

	/*
	 * Show the valid tokens found in the collection.
	 */
	protected void singleTermAnalysis() {
		System.out.println(this.analisis(this.singleTerms));
	}

	/*
	 * This is only for test information.
	 */
	protected String analisis(Map<String, Word> hashMap) {
		StringBuffer output = new StringBuffer();

		for (String item : hashMap.keySet()) {
			Integer frequency = hashMap.get(item).getFrequency();
			Integer offset = hashMap.get(item).getOffset();
			double idf = this.idf_norm(frequency, this.totalTokens);
			if (idf < this.idf_tall) {
				output.append(item + " -> " + frequency + " (" + offset + ") idf = " + idf + " \n");
			}
		}

		return output.toString();
	}

	/*
	 * Calculate idf normalized. Term frequency-inverse document frequency
	 */
	protected double idf_norm(int frequency, int total) {
		double sup = (total + 0.5) / frequency;
		double inf = total + 1;

		double idfnorm = Math.log(sup) / Math.log(inf);
		return idfnorm;
	}

	public String toString() {
		StringBuffer output = new StringBuffer();

		for (String singleTerm : singleTerms.keySet()) {
			Integer frequency = singleTerms.get(singleTerm).getFrequency();
			output.append(singleTerm + " -> " + frequency + "\n");
		}

		return output.toString();
	}

	/*
	 * Add term to the top terms analyzing main map tokens
	 */
	protected void createTopTerms(Map<String, Word> singleTerms, Map<String, Word> bestTokens, int total) {
		for (String singleTerm : singleTerms.keySet()) {
			double valor = idf_norm(singleTerms.get(singleTerm).getFrequency(), total);
			if (valor < this.idf_tall) {
				this.addTop(singleTerm, singleTerms.get(singleTerm), bestTokens);
			}
		}
	}

	/*
	 * Add term to the top terms.
	 * 
	 * @param term: Token in string format
	 * 
	 * @param freq: numer of times that the term appears in the text
	 */
	private void addTop(String term, Word w, Map<String, Word> bestTokens) {
		String terme_min_valor = "";
		int terme_min_freq = w.getFrequency();

		if (bestTokens.size() < this.TOPTOKENS) {
			bestTokens.put(term, w);
		} else {
			for (String token : bestTokens.keySet()) {
				Integer valor = bestTokens.get(token).getFrequency();
				if (valor > terme_min_freq) {
					terme_min_freq = valor;
					terme_min_valor = token;
				}
			}
			if (!terme_min_valor.equals("")) {
				bestTokens.remove(terme_min_valor);
				bestTokens.put(term, w);
			}
		}
	}

	public int getTotalTokens() {
		return totalTokens;
	}

	public int getDistinctTokens() {
		return this.singleTerms.size();
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public boolean useStopWords() {
		return useStopWords;
	}

	public void setUseStopWords(boolean useStopWords) {
		this.useStopWords = useStopWords;
	}

	public boolean useStemming() {
		return useStemming;
	}

	public void setUseStemming(boolean useStemming) {
		this.useStemming = useStemming;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Map<String, Word> getSingleTerms() {
		return singleTerms;
	}

	public Map<String, Word> getShingles() {
		return this.nGrams;
	}

	public Map<String, Word> getTokensTop() {
		return tokensTop;
	}

}
