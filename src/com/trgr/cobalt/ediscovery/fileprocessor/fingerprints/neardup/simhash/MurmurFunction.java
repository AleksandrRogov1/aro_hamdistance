package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash;

import org.apache.commons.codec.digest.MurmurHash3;

public class MurmurFunction implements HashFunction {
    @Override
    public long hash(final byte[] bytes) {
        return MurmurHash3.hash64(bytes);
    }

    @Override
    public long hash(final char[] chars) {
        return 0;
    }
}
