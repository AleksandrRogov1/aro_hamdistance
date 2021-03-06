package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.document;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TRTokenFilter extends TokenFilter {
    private final CharArraySet stopWords;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private int skippedPositions;


    public TRTokenFilter(TokenStream in, CharArraySet stopWords) {
        super(in);
        this.stopWords = stopWords;
    }

    @Override
    public final boolean incrementToken() throws IOException {
        skippedPositions = 0;
        while (input.incrementToken()) {
            if (accept()) {
                if (skippedPositions != 0) {
                    posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
                }
                return true;
            }
            //skippedPositions += posIncrAtt.getPositionIncrement(); //- ARogov I commented out this line to exclude stopwords from the shingles

        }

        // reached EOS -- return false
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        skippedPositions = 0;
    }

    @Override
    public void end() throws IOException {
        super.end();
        posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
    }


    /**
     * Builds a Set from an array of stop words,
     * appropriate for passing into the StopFilter constructor.
     * This permits this stopWords construction to be cached once when
     * an Analyzer is constructed.
     *
     * @param stopWords An array of stopwords
     * @see #makeStopSet(java.lang.String[], boolean) passing false to ignoreCase
     */
    public static CharArraySet makeStopSet(String... stopWords) {
        return makeStopSet(stopWords, false);
    }

    /**
     * Builds a Set from an array of stop words,
     * appropriate for passing into the StopFilter constructor.
     * This permits this stopWords construction to be cached once when
     * an Analyzer is constructed.
     *
     * @param stopWords A List of Strings or char[] or any other toString()-able list representing the stopwords
     * @return A Set ({@link CharArraySet}) containing the words
     * @see #makeStopSet(java.lang.String[], boolean) passing false to ignoreCase
     */
    public static CharArraySet makeStopSet(List<?> stopWords) {
        return makeStopSet(stopWords, false);
    }

    /**
     * Creates a stopword set from the given stopword array.
     *
     * @param stopWords An array of stopwords
     * @param ignoreCase If true, all words are lower cased first.
     * @return a Set containing the words
     */
    public static CharArraySet makeStopSet(String[] stopWords, boolean ignoreCase) {
        CharArraySet stopSet = new CharArraySet(stopWords.length, ignoreCase);
        stopSet.addAll(Arrays.asList(stopWords));
        return stopSet;
    }

    /**
     * Creates a stopword set from the given stopword list.
     * @param stopWords A List of Strings or char[] or any other toString()-able list representing the stopwords
     * @param ignoreCase if true, all words are lower cased first
     * @return A Set ({@link CharArraySet}) containing the words
     */
    public static CharArraySet makeStopSet(List<?> stopWords, boolean ignoreCase){
        CharArraySet stopSet = new CharArraySet(stopWords.size(), ignoreCase);
        stopSet.addAll(stopWords);
        return stopSet;
    }

    private boolean accept() {
        return !stopWords.contains(termAtt.buffer(), 0, termAtt.length());
    }

}
