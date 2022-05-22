package com.gttime.android.util;

import com.google.common.base.CharMatcher;

public final class CreditParser {

    private CreditParser() {
        throw new UnsupportedOperationException();
    }

    public static int parse(String credit) {
        String[] toSeperator = credit.split("TO");

        if(toSeperator.length >= 2) {
            credit = toSeperator[1];
        }

        String creditOnly = CharMatcher.inRange('1', '9').retainFrom(credit);

        return IntegerUtil.parseInt(creditOnly);
    }
}
