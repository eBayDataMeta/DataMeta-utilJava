package org.ebay.datameta.util.jdk;

import java.util.HashMap;

/**
 * @author Michael Bergens
 * @see #MERSENNE_2
 * @see #MERSENNE_3
 * @see #MERSENNE_5
 * @see #MERSENNE_7
 * @see #MERSENNE_13
 * @see #MERSENNE_17
 * @see #MERSENNE_19
 * @see #MERSENNE_31
 * @see #MERSENNE_61
 */
public class MathUtil {
    
    /**
     * Binary Kilo.
     */
    public static final int K = 1024;
    
    /**
     * Binary Mega;
     */
    public static final int M = K * K;
    
    /**
     * Binary Giga.
     */
    public static final int G = M * K;
    
    /**
     * Prime of {@code 2 &uarr; 2}.
     */
    public static final int MERSENNE_2 = 3;

    /**
     * Prime of {@code 2 &uarr; 3}.
     */
    public static final int MERSENNE_3 = 7;
    /**
     * Prime of {@code 2 &uarr; 5 - 1 == 31}, the most popular Mersenne prime of all.
     */
    public static final int MERSENNE_5 = 0x1F;
    /**
     * Prime of {@code 2 &uarr; 7 - 1 == 127}.
     */
    public static final int MERSENNE_7 = 0x7F;
    /**
     * Prime of {@code 2 &uarr; 13 - 1 = 8191}.
     */
    public static final int MERSENNE_13 = 0x1FFF;
    /**
     * Prime of {@code 2 &uarr; 17 == 131,071}.
     */
    public static final int MERSENNE_17 = 0x1FFFF;
    /**
     * Prime of {@code 2 &uarr; 19 == 524,287}.
     */
    public static final int MERSENNE_19 = 0x7FFFF;

    /**
     * Prime of {@code 2 &uarr; 31 == 2,147,483,647}.
     */
    public static final int MERSENNE_31 = Integer.MAX_VALUE;

    /**
     * Prime of {@code 2 &uarr; 31 == 2,305,843,009,213,693,951}.
     */
    public static final long MERSENNE_61 = 0x1FFFFFFFFFFFFFFFL;

    private static final long FNV_SEED_64 = 0xCBF29CE484222325L;
    private static final long FNV_PRIME_64 = 0x100000001B3L;

    private static final long FNV_SEED_32 = 0x811C9DC5L;
    private static final long FNV_PRIME_32 = 16777619;

    /**
     * Fowler–Noll–Vo hash, 32 bit version, multiplication first; a bit better than, for example,
     * {@link String#hashCode()} but the avalange is not spectacular.
     */
    public static long fnvHashMul32(final String source) {
      long rv = FNV_SEED_32;
      for (int i = 0; i < source.length(); i++) {
        rv *= FNV_PRIME_32;
        rv ^= source.charAt(i);
      }
      return rv & 0xFFFFFFFFL;
    }

    /**
     * Fowler–Noll–Vo hash, 32 bit version, char XOR first, best avalanche effect.
     */
    public static long fnvHashXor32(final String source) {
      long rv = FNV_SEED_32;
      for (int i = 0; i < source.length(); i++) {
        rv ^= source.charAt(i);
        rv *= FNV_PRIME_32;
      }
      return rv & 0xFFFFFFFFL;
    }

    /**
     * Fowler–Noll–Vo hash, 64 bit version, mul first; a bit better than, for example,
     * {@link String#hashCode()} but the avalange is not spectacular.
     */
    public static long fnvHashMul64(final String source) {
        long rv = FNV_SEED_64;
        for (int i = 0; i < source.length(); i++) {
          rv *= FNV_PRIME_64;
          rv ^= source.charAt(i);
        }
        return rv & 0xFFFFFFFFL;
    }
    /**
     * Fowler–Noll–Vo hash, 64 bit version, XOR first; a bit better than, for example,
     * {@link String#hashCode()} but the avalange is not spectacular.
     */
    public static long fnvHashXor64(final String source) {
        long rv = FNV_SEED_64;
        for (int i = 0; i < source.length(); i++) {
          rv ^= source.charAt(i);
          rv *= FNV_PRIME_64;
        }
        return rv & 0xFFFFFFFFL;
    }
    /**
     * Improve quality of the hash code by shuffling the bits around, borrowed from {@link HashMap#hash(int)}
     * which is package local.
     */
    public static int hashSmear(int hashCode) {
      hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
      return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }
}
