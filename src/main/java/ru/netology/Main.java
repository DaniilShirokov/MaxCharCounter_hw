package ru.netology;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final int NUMBER_OF_STRINGS = 10000;
    private static final int STRING_LENGTH = 100000;

    public static void main(String[] args) {
        BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

        Thread generator = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < NUMBER_OF_STRINGS; i++) {
                StringBuilder sb = new StringBuilder(STRING_LENGTH);
                for (int j = 0; j < STRING_LENGTH; j++) {
                    sb.append((char) ('a' + random.nextInt(3))); // 'a', 'b' or 'c'
                }
                String generatedString = sb.toString();
                try {
                    queueA.put(generatedString);
                    queueB.put(generatedString);
                    queueC.put(generatedString);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            try {
                queueA.put("END");
                queueB.put("END");
                queueC.put("END");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread counterA = new Thread(new CharCounter(queueA, 'a'));
        Thread counterB = new Thread(new CharCounter(queueB, 'b'));
        Thread counterC = new Thread(new CharCounter(queueC, 'c'));

        generator.start();
        counterA.start();
        counterB.start();
        counterC.start();

        try {
            generator.join();
            counterA.join();
            counterB.join();
            counterC.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}