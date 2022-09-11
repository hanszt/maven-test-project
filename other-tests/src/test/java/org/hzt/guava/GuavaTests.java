package org.hzt.guava;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.hzt.utils.io.FileX;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuavaTests {

    @Test
    void testNewArrayListFromListsFactory() {
        final var integers = Lists.newArrayList(3, 4, 5, 3, 2);
        assertEquals(List.of(3, 4, 5, 3, 2), integers);
    }

    @Test
    void testArrayFromIterable() {
        Iterable<String> strings = () -> new Iterator<>() {
            int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < 100;
            }

            @Override
            public String next() {
                return "hi" + counter++;
            }
        };
        final var stringsArray = Iterables.toArray(strings, String.class);

        assertEquals(100, stringsArray.length);
    }

    /**
     * A Bloom filter is a space-efficient probabilistic data structure,
     * conceived by Burton Howard Bloom in 1970, that is used to test whether an element is a member of a set.
     * False positive matches are possible, but false negatives are not – in other words,
     * a query returns either "possibly in set" or "definitely not in set".
     *
     * @see <a href="https://en.wikipedia.org/wiki/Bloom_filter">Bloom filter</a>
     * @see <a href="https://www.baeldung.com/guava-bloom-filter">Bloom Filter in Java using Guava</a>
     */
    @SuppressWarnings("UnstableApiUsage")
    @Nested
    class BloomFilterTests {

        @Test
        void testBloomFilterUsedProperly() {
            final double falsePositiveProbability = .01;

            final List<String> strings = FileX.of("input/random_words.txt").useLines(seq -> seq.map(StringX::toString).toList());

            BloomFilter<String> bloomFilter = BloomFilter
                    .create(Funnels.stringFunnel(Charset.defaultCharset()), strings.size(), falsePositiveProbability);

            strings.forEach(bloomFilter::put);

            assertAll(
                    () -> assertTrue(bloomFilter.mightContain(strings.get(0))),
                    () -> assertTrue(bloomFilter.mightContain(strings.get(1))),
                    () -> assertFalse(bloomFilter.mightContain("test"))
            );
        }

        @Test
        void testBloomFilterUsedWithToSmallExpectedInsertions() {
            final double falsePositiveProbability = .01;

            final List<String> strings = FileX.of("input/random_words.txt").useLines(seq -> seq.map(StringX::toString).toList());

            BloomFilter<String> bloomFilter = BloomFilter
                    .create(Funnels.stringFunnel(Charset.defaultCharset()), strings.size() / 10, falsePositiveProbability);

            strings.forEach(bloomFilter::put);

            assertAll(
                    () -> assertTrue(bloomFilter.mightContain(strings.get(0))),
                    () -> assertTrue(bloomFilter.mightContain(strings.get(1))),
                    () -> assertTrue(bloomFilter.mightContain("test")),
                    () -> assertTrue(bloomFilter.mightContain("this is a very long sentence unlikely to be present. " +
                            "How can it still be present? The bloom filter is clearly over saturated"))
            );
        }

        @Test
        void testCollectToBloomFilter() {
            final var bloomFilter = FileX.of("input/random_words.txt").useLines(seq -> seq
                    .map(StringX::toString)
                    .collect(BloomFilter.toBloomFilter(Funnels.stringFunnel(Charset.defaultCharset()), 500)));

            assertAll(
                    () -> assertEquals(199, bloomFilter.approximateElementCount()),
                    () -> assertTrue(bloomFilter.mightContain("count")),
                    () -> assertTrue(bloomFilter.mightContain("fly")),
                    () -> assertFalse(bloomFilter.mightContain("test"))
            );
        }
    }
}
