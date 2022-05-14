package com.gttime.android.util;

public final class IntegerUtil {
    private IntegerUtil() {
        throw new UnsupportedOperationException();
    }

    public static int parseInt(String strNum) {
        int returnVal = 0;
        if (strNum == null) {
            return returnVal;
        }

        try {
            returnVal = (int) Double.parseDouble(strNum.trim());
        } catch (NumberFormatException nfe) {
            returnVal = 0;
        }

        return returnVal;
    }

    public static Integer[] parseIntegerArr(int[] num) {
        Integer[] IntegerArr = new Integer[num.length];
        for(int i=0; i<num.length; i++) {
            IntegerArr[i] = new Integer(num[i]);
        }
        return IntegerArr;
    }
}
