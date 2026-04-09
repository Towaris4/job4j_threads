package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp.xml");
        String url = this.url;
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[512];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                System.out.println("Read 512 bytes : " + (System.nanoTime() - downloadAt) + " nano.");
            }
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        if (url.isEmpty()) {
            System.out.println("Отсутствует ссылка.");
        }
        int speed = Integer.parseInt(args[1]);
        if (args[1].isEmpty() || (speed == 0)) {
            System.out.println("Неверное ограничение по скорости.");
        }
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}