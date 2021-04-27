package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.document;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AnalyzerCache {
    // prevent instantiation of this class
	private AnalyzerCache() {
	}

	private static Map<String, Analyzer> cache = new HashMap<>();

	/**
	 * Flush all cached from memory, emptying the cache.
	 */
	public static synchronized void flushAll() {
		cache.clear();
	}

	/**
	 * Flush a specific cached analyzer from memory.
	 */
	public static synchronized void flush(String language) {
		cache.remove(language);
	}
	
	/**
	 * Obtain a new Analyzer instance for the specified language. A new entry will
	 * be added to the cache if this is the first request for the specified file
	 * name.
	 *
	 * @param language language.
	 * @return Analyzer.
	 */
	public static synchronized Analyzer newAnalyzer(final String language, final boolean useStopwords) {
		Analyzer entry = cache.get(language);
		if (entry == null) {
            if (useStopwords) {
                try {
                    CharArraySet stopwords = getStopwords("neardups/stopwords/stop-" + language + ".txt");
                    if (stopwords != null) {
                        entry = new TRAnalyzer(stopwords);
                    } else {
                        entry = new TRAnalyzer();
                    }
                } catch (IOException e) {
                	e.printStackTrace();
                    entry = new TRAnalyzer();
                }
            } else {
                entry = new TRAnalyzer();
            }
			cache.put(language, entry);
		}
		return entry;
	}
	
	/**
	 * Gets the stopwords.
	 *
	 * @param filename the filename
	 * @return the stopwords
	 */
	private static CharArraySet getStopwords(final String filename) throws IOException {
		final CharArraySet stopwords = new CharArraySet(1, true);

		try (
                final InputStream inputStream = AnalyzerCache.class.getClassLoader().getResourceAsStream(filename);
        ) {
			if (inputStream != null) {
				try (
					final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
				) {
					while (br.ready()) {
						stopwords.add(br.readLine());
					}
					return stopwords;
				}
			}
		} catch (final IOException e) {
		}
		return null;
	}
}
