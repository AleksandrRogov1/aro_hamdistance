package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash;

/**
 *
 * Apply a hash function to a String.
 */
public class BitHash {
	protected static final int LONGSIZE = 64;

	public static int[] calculateBinaryFingerPrint(final HashFunction hashFunction, final byte[] bytes) {
		return calcBitNumber(hashFunction.hash(bytes));
	}

	public static int[] calculateBinaryFingerPrint(final HashFunction hashFunction, final char[] chars) {
		return calcBitNumber(hashFunction.hash(chars));
	}

	private static int[] calcBitNumber(final long hash) {
		final int[] bitNumber = new int[LONGSIZE];
		final byte[] hashBytes = Long.toBinaryString(hash).getBytes();
		final int bitLen = hashBytes.length;
		final int startFrom = LONGSIZE - bitLen;
		for (int i = 0; i < LONGSIZE; i++) {
			if (i < startFrom) {
				bitNumber[i] = 0;
			} else {
				bitNumber[i] = Character.getNumericValue(hashBytes[i-startFrom]);
			}
		}
		return bitNumber;
	}
}

