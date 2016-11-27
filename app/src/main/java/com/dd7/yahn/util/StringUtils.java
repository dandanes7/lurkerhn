package com.dd7.yahn.util;

public class StringUtils {

    public static boolean isEmptyOrNull(String str) {
        if (str.isEmpty() || str == null) {
            return true;
        }
        return false;
    }
}