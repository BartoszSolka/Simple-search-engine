package com.searchengine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TFIDFRanking {

    public static double getTFIDF(
            double termFrequency,
            double inverseDocumentFrequency
    ) {
        return termFrequency * inverseDocumentFrequency;
    }

    public static double getTermFrequency(
            String term,
            String document
    ) {
        int wordCount = document.split(" ").length;

        int count = getTermCount(term, document);

        return (double)count / wordCount;
    }

    public static double getInverseDocumentFrequency(
            int appearsIn,
            int total
    ) {
        //inverse document frequency smooth
        return Math.log((double)total /appearsIn);
    }

    private static int getTermCount(String term, String document) {
        Pattern p = Pattern.compile(term, Pattern.LITERAL);

        Matcher m = p.matcher(document);
        int count = 0;
        while (m.find()){
            count += 1;
        }
        return count;
    }
}
