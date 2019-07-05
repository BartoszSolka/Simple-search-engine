package com.searchengine;

import java.util.Collection;
import java.util.List;

public interface Index {

    void add(List<String> documents);
    List<String> retrieve(String query);
    Collection<String> retrieveAll();
}
