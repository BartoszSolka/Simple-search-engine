package com.searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class SearchEngine {

    private static final List<String> EXAMPLE_DOCUMENTS = Arrays.asList(
            "the lazy brown dog sat in the corner",
            "the brown fox jumped over the brown dog",
            "the red fox bit the lazy dog"
    );

    public static void main(String[] args) throws IOException {

        Index index = new InvertedIndex();

        if (args.length == 0) {
            index.add(EXAMPLE_DOCUMENTS);
        } else {
            String directory = args[0];
            List<String> documents = Files.readAllLines(Paths.get(directory));
            index.add(documents);
        }

        printIndex(index);

        Scanner reader = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a string: ");

            String query = reader.nextLine();

            List<String> results = index.retrieve(query);

            printResults(results);
        }
    }

    private static void printIndex(Index index) {
        Collection<String> indexedDocuments = index.retrieveAll();
        System.out.println("Index size: " + indexedDocuments.size());
        System.out.println();
        indexedDocuments.forEach(System.out::println);
        System.out.println();
    }

    private static void printResults(List<String> results) {
        System.out.println("Result: ");
        results.forEach(System.out::println);
        System.out.println();
    }
}
