package com.gttime.android.mapping;

public class KeyValPair {
    public KeyValPair() { throw new UnsupportedOperationException(); }
    public static String[] mapTerm(int[] val) {
        String[] termText = new String[val.length];

        for(int i=0; i<val.length; i++) {
            int value = val[i];
            int year = value/100; // first four digit
            int month = value%100; // last two digit

            String term;
            StringBuilder builder = new StringBuilder();
            switch(month) {
                case 8:
                    builder.append("Fall ");
                    break;
                case 5:
                    builder.append("Summer ");
                    break;
                case 2:
                    builder.append("Spring ");
                    break;
            }

            String yearStr = Integer.toString(year);
            builder.append(yearStr);
            term = builder.toString();

            termText[i] = term;
        }
        return termText;
    }
}
