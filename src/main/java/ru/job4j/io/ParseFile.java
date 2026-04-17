package ru.job4j.io;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent() throws IOException {
        StringBuilder output = new StringBuilder();
        int data;
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            output.append(input.lines());
        }
        return output.toString();
    }

    public String getContentWithoutUnicode() throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            input.lines().flatMapToInt(String::chars).filter((int x) -> x < 0x80).forEach((int x) -> output.append((char) x));
        }
        return output.toString();
    }

}