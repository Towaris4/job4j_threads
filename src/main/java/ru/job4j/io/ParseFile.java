package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;


public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent(Predicate<Character> predicate) throws IOException {
        StringBuilder output = new StringBuilder();
        char data;
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            data = (char) input.read();
            if (predicate.test(data)) {
                output.append(data);
            }
        }
        return output.toString();
    }
}