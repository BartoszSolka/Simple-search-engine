package main.java.com.searchengine;

import com.searchengine.TFIDFRanking;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TFIDFRankingTest {

    @Test
    public void thatCanGetTFIDF() {
        //given
        double termFrequency = 0.2;
        double inverseDocumentFrequency = 0.6;

        //when
        double result = TFIDFRanking.getTFIDF(termFrequency, inverseDocumentFrequency);

        //then

        assertThat(result).isEqualTo(0.12);
    }

    @Test
    public void thatCanGetTermFrequency() {
        //given
        String term = "test1";

        String document = "test1 test2 test1";

        //when
        double result = TFIDFRanking.getTermFrequency(term, document);

        //then
        assertThat(result).isEqualTo((double)2/3);
    }

    @Test
    public void thatCanGetInverseDocumentFrequency() {
        //given
        int appearsIn = 100;
        int total = 250;

        //when
        double result = TFIDFRanking.getInverseDocumentFrequency(appearsIn, total);

        //then
        assertThat(result).isEqualTo(Math.log(2.5));
        assertThat(result).isBetween(0.9, 1.0);

    }
}
