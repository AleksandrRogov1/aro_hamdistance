package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash;

import java.math.BigInteger;

/**
 * The class is taken from https://github.com/nstickney/fnv-java
 * Licence - Unlicense
 * We apply a HashRabin 64 bits to a token.
 */
public class Fnv1a64Function implements HashFunction {


    @Override
    public long hash(final byte[] bytes) {
        return fnv1a(bytes, 64);
    }

    @Override
    public long hash(final char[] chars) {
        return 0;
    }

    // FNV Primes
    private static final BigInteger FNV_32_PRIME = new BigInteger("16777619");
    private static final BigInteger FNV_64_PRIME = new BigInteger("1099511628211");
    private static final BigInteger FNV_128_PRIME
            = new BigInteger("309485009821345068724781371");
    private static final BigInteger FNV_256_PRIME
            = new BigInteger("374144419156711147060143317175368453031918731002211");
    private static final BigInteger FNV_512_PRIME
            = new BigInteger("35835915874844867368919076489095108449946327955754392558399825"
            + "615420669938882575126094039892345713852759");
    private static final BigInteger FNV_1024_PRIME
            = new BigInteger("50164565101131186554345988110352789550307653454047907443030175"
            + "23831112055108147451509157692220295382716162651878526895249385292291816524375"
            + "083746691371804094271873160484737966720260389217684476157468082573");

    // FNV Basis
    private static final BigInteger FNV_32_BASIS = new BigInteger("2166136261");
    private static final BigInteger FNV_64_BASIS = new BigInteger("14695981039346656037");
    private static final BigInteger FNV_128_BASIS
            = new BigInteger("144066263297769815596495629667062367629");
    private static final BigInteger FNV_256_BASIS
            = new BigInteger("10002925795805258090707096862062570483709279601424119394522528"
            + "4501741471925557");
    private static final BigInteger FNV_512_BASIS
            = new BigInteger("96593031294966694980094354007163104660904187456726378961083743"
            + "29434462657994582932197716438449813051892206539805784495328239340083876191928"
            + "701583869517785");
    private static final BigInteger FNV_1024_BASIS
            = new BigInteger("14197795064947621068722070641403218320880622795441933960878474"
            + "91461758272325229673230371772215086409652120235554936562817466910857181476047"
            + "10150761480297559698040773201576924585630032153049571501574036444603635505054"
            + "12711285966361610267868082893823963790439336411086884584107735010676915");

    // Modulo Values
    private static final BigInteger FNV_32_MOD = new BigInteger("2").pow(32);
    private static final BigInteger FNV_64_MOD = new BigInteger("2").pow(64);
    private static final BigInteger FNV_128_MOD = new BigInteger("2").pow(128);
    private static final BigInteger FNV_256_MOD = new BigInteger("2").pow(256);
    private static final BigInteger FNV_512_MOD = new BigInteger("2").pow(512);
    private static final BigInteger FNV_1024_MOD = new BigInteger("2").pow(1024);

    // XOR Int
    private static final int FNV_XOR = 255;

    /**
     * Calculates the FNV-1 hash, then XOR folds to achieve the desired length if the length
     * parameter is not one of {32, 64, 128, 256, 512, 1024}. Hash lengths longer than 1024
     * bits are not supported. Note that hash lengths which are not a multiple of 8 will
     * result in a byte array with some number (8 -(length mod 8)) of leading zeros.
     *
     * @param inp the byte array to be hashed
     * @param length the desired length (in bits) of the hash
     * @return the hash result
     * @throws UnsupportedOperationException length is less than 16 or more than 1024
     */
    public static byte[] fnv1(byte[] inp, int length) throws UnsupportedOperationException {

        if (length < 16 || length > 1024) { // Check the length is supported
            throw new UnsupportedOperationException(
                    "length must be between 16 and 1024, inclusive; received " + length);
        }

        BigInteger hash;

        if (length <= 32) { // 32 bits and below

            // Calculate the 32-bit hash
            hash = fnv1_noXor(inp, FNV_32_BASIS, FNV_32_PRIME, FNV_32_MOD);

            // XOR fold as needed
            if (length < 32) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 64) { // 33-64 bits

            // Calculate the 64-bit hash
            hash = fnv1_noXor(inp, FNV_64_BASIS, FNV_64_PRIME, FNV_64_MOD);

            // XOR fold as needed
            if (length < 64) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 128) { // 65-128 bits

            // Calculate the 128-bit hash
            hash = fnv1_noXor(inp, FNV_128_BASIS, FNV_128_PRIME, FNV_128_MOD);

            // XOR fold as needed
            if (length < 128) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 256) { // 129-256 bits

            // Calculate the 256-bit hash
            hash = fnv1_noXor(inp, FNV_256_BASIS, FNV_256_PRIME, FNV_256_MOD);

            // XOR fold as needed
            if (length < 256) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 512) { // 257-512 bits

            // Calculate the 512-bit hash
            hash = fnv1_noXor(inp, FNV_512_BASIS, FNV_512_PRIME, FNV_512_MOD);

            // XOR fold as needed
            if (length < 512) {
                hash = xorFold(hash, length);
            }

        } else { // 513-1024 bits

            // Calculate the 1024-bit hash
            hash = fnv1_noXor(inp, FNV_1024_BASIS, FNV_1024_PRIME, FNV_1024_MOD);

            // XOR fold as needed
            if (length < 1024) {
                hash = xorFold(hash, length);
            }
        }

        // Return the byte array
        return hash.toByteArray();

    }

    /**
     * Calculates the FNV-1a hash, then XOR folds to achieve the desired length if the length
     * parameter is not one of {32, 64, 128, 256, 512, 1024}. Hash lengths longer than 1024
     * bits are not supported. Note that hash lengths which are not a multiple of 8 will
     * result in a byte array with some number (8 - (length mod 8)) of leading zeros.
     *
     * @param inp the byte array to be hashed
     * @param length the desired length (in bits) of the hash
     * @return the hash result
     * @throws UnsupportedOperationException length is less than 16 or more than 1024
     */
    public static long fnv1a(byte[] inp, int length) throws UnsupportedOperationException {

        if (length < 16 || length > 1024) { // Check the length is supported
            throw new UnsupportedOperationException(
                    "length must be between 16 and 1024, inclusive; received " + length);
        }

        BigInteger hash;

        if (length <= 32) { // 32 bits and below

            // Calculate the 32-bit hash
            hash = fnv1a_noXor(inp, FNV_32_BASIS, FNV_32_PRIME, FNV_32_MOD);

            // XOR fold as needed
            if (length < 32) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 64) { // 33-64 bits

            // Calculate the 64-bit hash
            hash = fnv1a_noXor(inp, FNV_64_BASIS, FNV_64_PRIME, FNV_64_MOD);

            // XOR fold as needed
            if (length < 64) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 128) { // 65-128 bits

            // Calculate the 128-bit hash
            hash = fnv1a_noXor(inp, FNV_128_BASIS, FNV_128_PRIME, FNV_128_MOD);

            // XOR fold as needed
            if (length < 128) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 256) { // 129-256 bits

            // Calculate the 256-bit hash
            hash = fnv1a_noXor(inp, FNV_256_BASIS, FNV_256_PRIME, FNV_256_MOD);

            // XOR fold as needed
            if (length < 256) {
                hash = xorFold(hash, length);
            }

        } else if (length <= 512) { // 257-512 bits

            // Calculate the 512-bit hash
            hash = fnv1a_noXor(inp, FNV_512_BASIS, FNV_512_PRIME, FNV_512_MOD);

            // XOR fold as needed
            if (length < 512) {
                hash = xorFold(hash, length);
            }

        } else { // 513-1024 bits

            // Calculate the 1024-bit hash
            hash = fnv1a_noXor(inp, FNV_1024_BASIS, FNV_1024_PRIME, FNV_1024_MOD);

            // XOR fold as needed
            if (length < 1024) {
                hash = xorFold(hash, length);
            }
        }

        // Return the byte array
        return hash.longValue();

    }

    /**
     * Calculates the FNV-1 hash with no XOR folding.
     *
     * @param inp the byte array to be hashed
     * @param basis the FNV basis to use
     * @param prime the FNV prime to use
     * @param mod the FNV modulo to use
     * @return the hash result
     */
    private static BigInteger fnv1_noXor(byte[] inp, BigInteger basis, BigInteger prime,  BigInteger mod) {

        BigInteger digest = basis;

        for (byte b : inp) {
            digest = digest.multiply(prime).mod(mod);
            digest = digest.xor(BigInteger.valueOf((int) b & FNV_XOR));
        }
        return digest;
    }

    /**
     * Calculates the FNV-1a hash with no XOR folding.
     *
     * @param inp the byte array to be hashed
     * @param basis the FNV basis to use
     * @param prime the FNV prime to use
     * @param mod the FNV modulo to use
     * @return the hash result
     */
    private static BigInteger fnv1a_noXor(byte[] inp, BigInteger basis, BigInteger prime,
                                          BigInteger mod) {

        BigInteger digest = basis;

        for (byte b : inp) {
            digest = digest.xor(BigInteger.valueOf((int) b & FNV_XOR));
            digest = digest.multiply(prime).mod(mod);
        }
        return digest;
    }

    /**
     * <a href="https://tools.ietf.org/html/draft-eastlake-fnv-12#section-3">XOR-folds</a> a
     * BigInteger from one length down to another.
     *
     * @param inp the BigInteger to fold
     * @param k the required length (in bits) of the returned value
     * @return the result of the xor fold
     */
    private static BigInteger xorFold(BigInteger inp, int k) {
        BigInteger andme = new BigInteger("2").pow(k).add(new BigInteger("-1"));
        return (inp.xor(inp.shiftRight(k))).and(andme);
    }

}
