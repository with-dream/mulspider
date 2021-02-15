package com.example.core.db.bloomfilter;

import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiangtiteng
 */
public enum BloomFilterStrategies implements BloomFilter.Strategy {
    MURMUR128_MITZ_32() {
        @Override
        public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bitArray) {
            long bitSize = bitArray.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >> 32);

            List<Long> indexes = new LinkedList<>();

            boolean bitsChanged = false;
            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = hash1 + (i * hash2);
                // Flip all the bits if it's negative (guaranteed positive number)
                if (combinedHash < 0) {
                    combinedHash = ~combinedHash;
                }
                indexes.add(combinedHash % bitSize);
            }
            List<Boolean> result = bitArray.batchSet(indexes);
            return result.stream().filter(aBoolean -> !aBoolean).count() > 0;
        }

        @Override
        public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);

            List<Long> indexes = new LinkedList<>();

            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = hash1 + (i * hash2);
                // Flip all the bits if it's negative (guaranteed positive number)
                if (combinedHash < 0) {
                    combinedHash = ~combinedHash;
                }

                indexes.add(combinedHash % bitSize);
            }

            List<Boolean> result = bits.batchGet(indexes);

            long falseCount = result.stream().filter(aBoolean -> !aBoolean).count();
            return falseCount == 0;
        }
    }

}
