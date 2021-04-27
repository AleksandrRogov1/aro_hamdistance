package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash;

/**
 *
 *
 * <p>
 * For each token that we add, we regenerate de vector fingerprint. Analizyng
 * the sentence detecting web: tokens = (detecting,web)
 * </p>
 * <p>
 * Sample
 * </p>
 * <p>
 * token: <i>detecting</i> with <b>Rabin hash 64</b>
 * </p>
 * <table>
 * <tr>
 * <td>Source bithash1</td>
 * <td>0</td>
 * <td>1</td>
 * <td>1</td>
 * <td>0</td>
 * <td>1</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>0</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>Vector</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>Final vector</td>
 * <td>-1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>-1</td>
 * <td>1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>-1</td>
 * <td>1</td>
 * </tr>
 * </table>
 * 
 */
public class Simhash {

	/** The size that we use for generate a fingerprint vector */
	public final static int LONGSIZE = 64;

	private double[] vector;
	private int[] fingerprint;

	public Simhash() {
		this.vector = new double[LONGSIZE];
		this.fingerprint = new int[LONGSIZE];
	}

	/**
	 * 
	 * @param bitArray
	 */
	public void add(final int[] bitArray, final double weight) {
		/*
		 * System.out.println(bh.shingle + " HasValue:" + bh.baseNumber);
		 * System.out.println("Bits  :" + bh.toString());
		 * System.out.println("Vector:"+this.vectorToString());
		 */
		for (int i = 0; i < LONGSIZE; i++) {
			// if bh ith bit is 1 we increase the vector else we decrease the vector
			if (bitArray[i] == 1) {
				this.vector[i] += weight;
			} else {
				this.vector[i] -= weight;
			}
			
			if (this.vector[i] > 0) {
				this.fingerprint[i] = 1;
			} else {
				this.fingerprint[i] = 0;
			}
		}
	}

	/*
	 * We can see the bithash and the fingerprint.
	 */
	public String toString() {
		String vectorBits = "";
		String fingerprint = "";

		for (int i = 0; i < LONGSIZE; i++) {
			if (this.vector[i] < 0) {
				vectorBits = " " + this.vector[i] + vectorBits;
			} else {
				vectorBits = "  " + this.vector[i] + vectorBits;
			}
			fingerprint = " " + this.getFingerprint()[i] + fingerprint;
		}

		return "Vector:" + vectorBits + "\nFingerprint:" + fingerprint;
	}

/**
	 * Gets the fingerprint.
	 *
	 * @return the fingerprint
	 */
	public int[] getFingerprint() {
		return fingerprint;
	}
}
