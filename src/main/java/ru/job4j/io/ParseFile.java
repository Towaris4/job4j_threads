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


    public String getContent(Predicate<Integer> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            input.lines().flatMap((line) -> line.chars().boxed()).filter(filter).forEach((x) -> output.append((char) x.intValue()));
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

    public void saveContent(String content) throws IOException {
        try (PrintWriter output = new PrintWriter(
                new BufferedOutputStream(
                        new FileOutputStream(file)
                ))) {
            output.write(content);
        }
    }
}