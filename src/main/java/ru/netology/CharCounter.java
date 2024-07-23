package ru.netology;

import java.util.concurrent.BlockingQueue;

class CharCounter implements Runnable {
    private BlockingQueue<String> queue;
    private char character;
    private String maxString = "";
    private int maxCount = 0;

    public CharCounter(BlockingQueue<String> queue, char character) {
        this.queue = queue;
        this.character = character;
    }

    @Override
    public void run() {
        String text;
        try {
            while (!(text = queue.take()).equals("END")) {
                int count = countChar(text, character);
                if (count > maxCount) {
                    maxCount = count;
                    maxString = text;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Max count of '" + character + "': " + maxCount);
    }

    private int countChar(String text, char character) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }
}