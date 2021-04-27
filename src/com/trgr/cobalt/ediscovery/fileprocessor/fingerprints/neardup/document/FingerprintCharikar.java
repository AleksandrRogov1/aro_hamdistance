package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.document;

import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.InputModel;
import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.NearDupModel;
import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash.*;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 *  
 * Calculate the fingerprint of a document using
 * Charikar simhash.
 * First Split the text in tokens and generate a fingerprint with this tokens usign HashRbing
 * and Charikar's algorithm.
 * https://github.com/albertjuhe/charikars_algorithm
 */
public class FingerprintCharikar extends FingerprintDocument {
    private Simhash simHash;
    private Simhash simHashFnv1a64;
    private Simhash simHashMurmur;

    public FingerprintCharikar(final InputModel inputModel) {
        super(inputModel);
        this.simHash = new Simhash();
    }

    /**
     * Calculate the fingerprint.
     * Splt the text in shingles and with each token generate a hashrabin, with the result
     * we generate a final fingerprint vector.
     * @return fingerprint in a string
     * @throws IOException
     */

    public NearDupModel calculateFingerprint(final String documentLocation, final String languageCode, final Charset charset) throws IOException {
        final NearDupModel dupModel = new NearDupModel();
        dupModel.setDocumentLocation(documentLocation);
        dupModel.setLanguage(languageCode);

        totalTokens = 0;
        totalnGramTokens = 0;
        TokenStream tokenStream = null;
        final Charset chset = charset != null ? charset : StandardCharsets.UTF_8;
        final Path docPath = Paths.get(documentLocation);

        this.simHashFnv1a64 = new Simhash();
        this.simHashMurmur = new Simhash();

        try (InputStreamReader inputFile = new InputStreamReader(Files.newInputStream(docPath), chset)) {
            if (this.useStemming()) {
                this.analyzer = AnalyzerCache.newAnalyzer(languageCode, this.useStopWords());
                tokenStream = this.analyzer.tokenStream("fingerprint", inputFile);
            } else {
                final StandardTokenizer standardTokenizer = new StandardTokenizer();
                standardTokenizer.setReader(inputFile);
                tokenStream = standardTokenizer;
            }

            final ShingleFilter tokens = new ShingleFilter(tokenStream, 2, this.maxgrams);
            final CharTermAttribute charTermAtt = tokens.addAttribute(CharTermAttribute.class);
            tokenStream.reset();

            //Put the tokens in a map and select the most important terms.
            while (tokens.incrementToken()) {
                final String token = charTermAtt.toString();
                if (token == null || token.isEmpty()) {
                    break;
                }
                int numtokens = calcWords(token);
                if (numtokens == 1) {
                    this.add(token, this.singleTerms); //Add a token to the list of frequencies tokens
                    //System.out.println(token.term());
                    totalTokens++;
                } else if (numtokens >= this.mingrams) {
                    //System.out.println(token.term());
                    this.add(token, this.nGrams);
                    totalnGramTokens++; //Count the ngram tokens
                }
            }
            tokens.close();
            this.createTopTerms(this.singleTerms, this.getTokensTop(), this.totalTokens);
            //Calculate the fingerprint vector
            this.calculateVectorFingerprint(this.nGrams, this.totalnGramTokens, chset);
        } finally {
            if (tokenStream != null) {
                tokenStream.close();
            }
        }

        dupModel.setFnRabin(fingerprint2String(this.simHash.getFingerprint()));
        dupModel.setFnFnv1a64(fingerprint2String(this.simHashFnv1a64.getFingerprint()));
        dupModel.setFnMurmur(fingerprint2String(this.simHashMurmur.getFingerprint()));
        dupModel.setLongRabin(Long.parseUnsignedLong(dupModel.getFnRabin(), 2));
        dupModel.setLongFnv1a64(Long.parseUnsignedLong(dupModel.getFnFnv1a64(), 2));
        dupModel.setLongMurmur(Long.parseUnsignedLong(dupModel.getFnMurmur(), 2));

        dupModel.setRabinSim(this.simHash.getFingerprint());
        dupModel.setFnvSim(this.simHashFnv1a64.getFingerprint());
        dupModel.setMurmurSim(this.simHashMurmur.getFingerprint());

        return dupModel;
    }

    private int calcWords(final String data) {
        final int len = data.length();
        int spaceCount = 1;
        for (int i = 0; i < len; i++) {
            if (data.charAt(i) == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }

    private void calculateVectorFingerprint(final Map<String, Word> shingles, final int total_tokens, final Charset charset) {
        for (String shingle : shingles.keySet()) {
            Integer frequency = shingles.get(shingle).getFrequency();
            double idf = this.idf_norm(frequency, total_tokens);
            // ApplyHashrabin function
            //Add the fingerprint of the term to the final fingerprint vector.
            this.simHash.add(BitHash.calculateBinaryFingerPrint(new RabinFunction(), shingle.toCharArray()), idf);

            byte[] bytes = shingle.getBytes(charset);
            this.simHashFnv1a64.add(BitHash.calculateBinaryFingerPrint(new Fnv1a64Function(), bytes), idf);
            this.simHashMurmur.add(BitHash.calculateBinaryFingerPrint(new MurmurFunction(), bytes), idf);
        }
    }

    private String fingerprint2String(int[] vFpr) {
        final StringBuilder sb = new StringBuilder(64);
        for (int i = 0; i < vFpr.length; i++) {
            sb.append(vFpr[i]);
        }
        return sb.toString();
    }
}
