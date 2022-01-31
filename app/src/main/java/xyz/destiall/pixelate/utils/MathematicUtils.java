package xyz.destiall.pixelate.utils;

import java.util.TreeMap;

/**
 * Written By Yong Hong
 */

public class MathematicUtils {
    private static final TreeMap<Integer, String> romanMap = new TreeMap<>();
    static {
        romanMap.put(10, "X");
        romanMap.put(9, "IX");
        romanMap.put(5, "V");
        romanMap.put(4, "IV");
        romanMap.put(1, "I");
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
        if (roman.startsWith("X")) return 10 + toNumber(roman.substring(1));
        if (roman.startsWith("IX")) return 9 + toNumber(roman.substring(2));
        if (roman.startsWith("V")) return 5 + toNumber(roman.substring(1));
        if (roman.startsWith("IV")) return 4 + toNumber(roman.substring(2));
        if (roman.startsWith("I")) return 1 + toNumber(roman.substring(1));
        throw new IllegalArgumentException("Out Of Range");
    }
}
