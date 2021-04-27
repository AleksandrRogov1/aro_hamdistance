package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash;

import com.planetj.math.rabinhash.RabinHashFunction64;

/**
 *
 * We apply a HashRabin 64 bits to a token.
 */
public class RabinFunction implements HashFunction {

    private static final long REFERENCE_VALUE_HASH = RabinHashFunction64.DEFAULT_HASH_FUNCTION.getP();
    /**
     * 
     * This is the hash function that we apply, Hashrabin 64.
     */
    public long hash (final byte[] bytes) {
        return  new RabinHashFunction64(REFERENCE_VALUE_HASH).hash(bytes);
    }

    public long hash (final char[] chars) {
        return  new RabinHashFunction64(REFERENCE_VALUE_HASH).hash(chars);
    }

}
