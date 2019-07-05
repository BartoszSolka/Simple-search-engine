package com.searchengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.searchengine.TFIDFRanking.getInverseDocumentFrequency;
import static com.searchengine.TFIDFRanking.getTFIDF;
import static com.searchengine.TFIDFRanking.getTermFrequency;

public class InvertedIndex implements Index {

    private static final String DELIMITER = " ";

    private Map<String, List<Integer>> index = new HashMap<>();
    private Map<Integer, String> documents = new HashMap<>();

    public InvertedIndex() {
    }

    public InvertedIndex(
            Map<String, List<Integer>> index,
            Map<Integer, String> documents
    ) {
        this.index = index;
        this.documents = documents;
    }

    public Map<String, List<Integer>> getIndex() {
        return index;
    }

    public Map<Integer, String> getDocuments() {
        return documents;
    }

    @Override
    public void add(List<String> documents) {
        documents.stream()
                .filter(s -> !s.isEmpty())
                .forEach(this::addDocument);

        rankDocuments();
    }

    @Override
    public List<String> retrieve(String query) {
        List<Integer> documentIds = index.get(query);

        return Optional.ofNullable(documentIds)
                .map(ids -> ids.stream()
                        .map(documents::get)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public Collection<String> retrieveAll() {
        return documents.values();
    }

    private void rankDocuments() {
        Set<String> terms = index.keySet();
        int total = documents.size();
        terms.forEach(term -> {
            Map<Integer, Double> ranking = calculateRanking(total, term);
            List<Integer> sortedDocumentIds = ranking.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            index.put(term, sortedDocumentIds);
        });
    }

    private Map<Integer, Double> calculateRanking(int total, String term) {
        Map<Integer, Double> ranking = new HashMap<>();
        int appearsIn = index.get(term).size();
        double inverseDocumentFrequency = getInverseDocumentFrequency(appearsIn, total);
        List<Integer> documentIds = index.get(term);
        documentIds.forEach(id -> {
            double rankValue = getRankValue(term, inverseDocumentFrequency, id);
            ranking.put(id, rankValue);
        });

        return ranking;
    }

    private double getRankValue(String term, double inverseDocumentFrequency, Integer id) {
        String document = documents.get(id);
        double termFrequency = getTermFrequency(term, document);
        return getTFIDF(termFrequency, inverseDocumentFrequency);
    }

    private void addDocument(String document) {
        List<String> tokens = tokenize(document);
        int documentId = documents.size();
        documents.put(documentId, document);

        tokens.forEach(token -> {
            index.computeIfAbsent(token, k -> new ArrayList<>());
            index.computeIfPresent(token, (k, documentIds) -> addIdIfNotPresent(documentId, documentIds));
        });
    }

    private List<Integer> addIdIfNotPresent(int documentId, List<Integer> documentIds) {
        if (!documentIds.contains(documentId)) {
            documentIds.add(documentId);
        }

        return documentIds;
    }

    private List<String> tokenize(String document) {
        return Arrays.asList(document.split(DELIMITER));
    }
}
