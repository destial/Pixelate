package xyz.destiall.pixelate.utils;

import java.util.TreeMap;

public class MathematicUtils {

    public static String toRoman(int num) {
        TreeMap<Integer, String> map = new TreeMap<Integer, String>();
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
        int l =  map.floorKey(num);
        if (num == l) {
            return map.get(num);
        }
        return map.get(l) + toRoman(num - l);
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
