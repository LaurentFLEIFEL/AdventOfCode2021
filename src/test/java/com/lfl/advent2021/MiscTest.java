package com.lfl.advent2021;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.BitSet;

@Slf4j
public class MiscTest {

    @Test
    void bitSetTest() {
        BitSet bitSet = new BitSet();
        bitSet.set(8, true);
        System.out.println("bitSet = " + Arrays.toString(bitSet.toLongArray()));
    }
}
