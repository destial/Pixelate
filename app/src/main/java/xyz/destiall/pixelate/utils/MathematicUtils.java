package xyz.destiall.pixelate.utils;

import java.util.TreeMap;

/**
 * Written By Yong Hong
 */

public class MathematicUtils {
    private static final TreeMap<Integer, String> romanMap = new TreeMap<>();
    static {
        romanMap.put(10, StringUtils.ROMAN_X);
        romanMap.put(9, StringUtils.ROMAN_IX);
        romanMap.put(5, StringUtils.ROMAN_V);
        romanMap.put(4, StringUtils.ROMAN_IV);
        romanMap.put(1, StringUtils.ROMAN_I);
    }

    public static String toRoman(int num) {
        int l =  romanMap.floorKey(num);
        if (num == l) {
            return romanMap.get(num);
        }
        return romanMap.get(l) + toRoman(num - l);
    }

    public static Integer toNumber(String roman) {
        if (roman.isEmpty()) return 0;
        if (roman.startsWith(StringUtils.ROMAN_X)) return 10 + toNumber(roman.substring(1));
        if (roman.startsWith(StringUtils.ROMAN_IX)) return 9 + toNumber(roman.substring(2));
        if (roman.startsWith(StringUtils.ROMAN_V)) return 5 + toNumber(roman.substring(1));
        if (roman.startsWith(StringUtils.ROMAN_IV)) return 4 + toNumber(roman.substring(2));
        if (roman.startsWith(StringUtils.ROMAN_I)) return 1 + toNumber(roman.substring(1));
        return 0;
    }
}
