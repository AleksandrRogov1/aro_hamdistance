package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash;

public interface HashFunction {
    long hash(byte[] bytes);
    long hash(char[] chars);
}
