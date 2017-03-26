package com.dd7.lurkerhn.util;

public class StringUtils {

    public static boolean isEmptyOrNull(String str) {
        if (str.isEmpty() || str == null) {
            return true;
        }
        return false;
    }
}