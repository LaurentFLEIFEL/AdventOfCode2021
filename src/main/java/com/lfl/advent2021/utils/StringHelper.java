package com.lfl.advent2021.utils;

import org.eclipse.collections.api.set.primitive.ImmutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntSets;

public class StringHelper {
    private static final ImmutableIntSet NUMERIC_CHARS = IntSets.immutable.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final ImmutableIntSet HEXADECIMAL_CHARS = IntSets.immutable.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');

    public static boolean isNumeric(String input) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(input)) {
            return false;
        }
        return input.chars().allMatch(NUMERIC_CHARS::contains);
    }

    public static boolean isHexadecimalNumeric(String input) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(input)) {
            return false;
        }
        return input.chars().allMatch(HEXADECIMAL_CHARS::contains);
    }
}
