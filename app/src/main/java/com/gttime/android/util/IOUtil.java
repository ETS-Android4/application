package com.gttime.android.util;

public final class IOUtil {
    private IOUtil() {
        throw new UnsupportedOperationException();
    }

    public static String getFileName(String filename) {
        return filename + ".json";
    }
}
