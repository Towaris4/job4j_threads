package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private static final Pattern FILENAME_PATTERN = Pattern.compile(".*/([^/]+)$");

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        int loadBytes = 0;
        var startAt = System.currentTimeMillis();
        String fileName = null;
        Matcher matcher = FILENAME_PATTERN.matcher(url);
        if (matcher.find()) {
            fileName = matcher.group(1);
        }
        String filePath = "src/main/resources/" + fileName;
        var file = new File(filePath);
        String url = this.url;
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[512];
            int bytesRead;
            long sleepTime = 0;
            startAt = System.nanoTime();
            long realSpeed;
            long time;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                loadBytes += bytesRead;
                if (speed < loadBytes) {
                    time = (System.nanoTime() - startAt);
                    if (time < 1000000000) {
                        realSpeed = loadBytes * 1_000_000_000L / time;
                        sleepTime =  realSpeed  / speed;
                        Thread.sleep(sleepTime);
                        startAt = System.nanoTime();
                        loadBytes = 0;
                    }
                }
                output.write(dataBuffer, 0, bytesRead);
            }
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Pattern URL_PATTERN = Pattern.compile(
                "^(https?:\\/\\/)?([\\w\\-\\.]+)\\.([a-z]{2,6}\\.?)(\\/[\\w\\-\\.\\/]*)*\\/?$");
        if (!(args.length == 2)) {
            System.out.println("Отсутствует необходимое количество параметров.");
        }
        String url = null;
        int speed = 0;
        for (String arg : args) {
            if (arg != null && URL_PATTERN.matcher(arg).matches()) {
                url = arg;
            }
        }
        if (url.isEmpty()) {
            System.out.println("Отсутствует ссылка.");
        }
        Pattern NUMBER_PATTERN = Pattern.compile("^[-+]?\\d+([.,\\s]\\d+)?$");
        for (String arg : args) {
            if (arg != null && NUMBER_PATTERN.matcher(arg).matches()) {
                speed = Integer.parseInt(arg);
            }
        }
        if (speed == 0) {
            System.out.println("Неверное ограничение по скорости.");
        }
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}