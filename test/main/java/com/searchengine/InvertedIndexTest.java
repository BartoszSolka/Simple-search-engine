package main.java.com.searchengine;

import com.searchengine.InvertedIndex;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class InvertedIndexTest {

    @Test
    public void thatCanAddListOfDocuments() {
        //given
        InvertedIndex index = new InvertedIndex();
        List<String> documents = Arrays.asList("test1", "test2");

        //when
        index.add(documents);

        //then
        assertThat(index.getDocuments()).hasSize(2);
        assertThat(index.getIndex()).hasSize(2);
    }

    @Test
    public void thatCanAddOneDocument() {
        //given
        InvertedIndex index = new InvertedIndex();
        List<String> documents = Collections.singletonList("test1");

        //when
        index.add(documents);

        //then
        assertThat(index.getDocuments()).hasSize(1);
        assertThat(index.getIndex()).hasSize(1);
    }

    @Test
    public void thatCanAddEmptyListOfDocuments() {
        //given
        InvertedIndex index = new InvertedIndex();
        List<String> documents = Collections.emptyList();

        //when
        index.add(documents);

        //then
        assertThat(index.getDocuments()).hasSize(0);
        assertThat(index.getIndex()).hasSize(0);
    }

    @Test
    public void thatCanRetrieveDocument() {
        //given
        Map<String, List<Integer>> index = new HashMap<>();
        Map<Integer, String> documents = new HashMap<>();

        index.put("test", Collections.singletonList(1));
        documents.put(1, "test document");

        InvertedIndex invertedIndex = new InvertedIndex(index, documents);


        //when
        List<String> result = invertedIndex.retrieve("test");

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("test");
        assertThat(result.get(0)).isEqualTo("test document");
    }

    @Test
    public void thatCanRetrieveDocuments() {
        //given
        Map<String, List<Integer>> index = new HashMap<>();
        Map<Integer, String> documents = new HashMap<>();

        index.put("test", Arrays.asList(1, 2));
        documents.put(1, "test document first");
        documents.put(2, "test document second");

        InvertedIndex invertedIndex = new InvertedIndex(index, documents);


        //when
        List<String> result = invertedIndex.retrieve("test");

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(document -> document.contains("test"));
        assertThat(result).anyMatch(document -> document.contains("first"));
        assertThat(result).anyMatch(document -> document.contains("second"));
    }

    @Test
    public void thatCanRetrieveDocumentsWithCorrectOrder() {
        //given
        List<String> documents = Arrays.asList(
                "test document first",
                "test document second test test test test",
                "third document"
        );

        InvertedIndex invertedIndex = new InvertedIndex();

        invertedIndex.add(documents);


        //when
        List<String> result = invertedIndex.retrieve("test");

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).contains("second");
        assertThat(result.get(1)).contains("first");
    }

    @Test
    public void thatCanRetrieveAllDocuments() {
        //given
        List<String> documents = Arrays.asList(
                "test document first",
                "test document second test test test test",
                "third document"
        );

        InvertedIndex invertedIndex = new InvertedIndex();

        invertedIndex.add(documents);


        //when
        Collection<String> result = invertedIndex.retrieveAll();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result).anyMatch(document -> document.contains("first"));
        assertThat(result).anyMatch(document -> document.contains("second"));
        assertThat(result).anyMatch(document -> document.contains("third"));
    }

    @Test
    public void thatCanRetrieveAllDocumentsWhenIndexEmpty() {
        //given
        List<String> documents = Collections.emptyList();

        InvertedIndex invertedIndex = new InvertedIndex();

        invertedIndex.add(documents);


        //when
        Collection<String> result = invertedIndex.retrieveAll();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
