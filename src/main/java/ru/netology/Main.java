package ru.netology;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int NUMBER_STRING = 10_000;
    private static final int TEXT_LENGTH = 100_000;
    private static final String CHARACTER_SET = "abc";
    private static final char SYMBOL_A = 'a';
    private static final char SYMBOL_B = 'b';
    private static final char SYMBOL_C = 'c';
    private static final int capacityQueue = 100;

    private static final BlockingQueue<String> queueLookupCharacterA = new ArrayBlockingQueue<>(capacityQueue);
    private static final BlockingQueue<String> queueLookupCharacterB = new ArrayBlockingQueue<>(capacityQueue);
    private static final BlockingQueue<String> queueLookupCharacterC = new ArrayBlockingQueue<>(capacityQueue);

    private static String textWithMaxNumberA;
    private static String textWithMaxNumberB;
    private static String textWithMaxNumberC;
    private static int maxNumberA;
    private static int maxNumberB;
    private static int maxNumberC;

    public static void main(String[] args) throws InterruptedException {
        // Поток заполнения очередей
        Thread threadFillingQueues = createThreadFillingQueues();
        threadFillingQueues.start();

        // Поток нахождения текста с максимальным количеством символа 'a'
        Thread threadFindMaxNumberA = createThreadFindMaxNumberA();
        threadFindMaxNumberA.start();

        // Поток нахождения текста с максимальным количеством символа 'b'
        Thread threadFindMaxNumberB = createThreadFindMaxNumberB();
        threadFindMaxNumberB.start();

        // Поток нахождения текста с максимальным количеством символа 'c'
        Thread threadFindMaxNumberC = createThreadFindMaxNumberC();
        threadFindMaxNumberC.start();

        // Ожидаем завершения всех потоков
        threadFillingQueues.join();
        threadFindMaxNumberA.join();
        threadFindMaxNumberB.join();
        threadFindMaxNumberC.join();

        // Выводим результаты
        System.out.println("\nМаксимальное количество символов 'a': " + maxNumberA +
                "\nТекст строки: " + textWithMaxNumberA.substring(0,100) + "...");
        System.out.println("\nМаксимальное количество символов 'b': " + maxNumberB +
                "\nТекст строки: " + textWithMaxNumberB.substring(0,100) + "...");
        System.out.println("\nМаксимальное количество символов 'c': " + maxNumberC +
                "\nТекст строки: " + textWithMaxNumberC.substring(0,100) + "...");
    }

    private static Thread createThreadFillingQueues() {
        Runnable taskFillingQueues = () -> {
            for (int i = 0; i < NUMBER_STRING; i++) {
                String text = TextGenerator.generateText(CHARACTER_SET, TEXT_LENGTH);
                try {
                    queueLookupCharacterA.put(text);
                    queueLookupCharacterB.put(text);
                    queueLookupCharacterC.put(text);
                    System.out.println("Добавили " + text.substring(0, 10) + "...");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
        return new Thread(taskFillingQueues);
    }

    private static Thread createThreadFindMaxNumberA() {
        Runnable taskFindMaxNumberA = () -> {
            for (int j = 0; j < NUMBER_STRING; j++) {
                int countSymbol = 0;
                try {
                    String text = queueLookupCharacterA.take();
                    System.out.println("ПОТОК A: Взяли " + text.substring(0, 10) + "...");
                    for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) == SYMBOL_A) {
                            countSymbol++;
                        }
                    }
                    if (countSymbol > maxNumberA) {
                        maxNumberA = countSymbol;
                        textWithMaxNumberA = text;
                    }
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
        return new Thread(taskFindMaxNumberA);
    }

    private static Thread createThreadFindMaxNumberB() {
        Runnable taskFindMaxNumberB = () -> {
            for (int j = 0; j < NUMBER_STRING; j++) {
                int countSymbol = 0;
                try {
                    String text = queueLookupCharacterB.take();
                    System.out.println("ПОТОК B: Взяли " + text.substring(0, 10) + "...");
                    for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) == SYMBOL_B) {
                            countSymbol++;
                        }
                    }
                    if (countSymbol > maxNumberB) {
                        maxNumberB = countSymbol;
                        textWithMaxNumberB = text;
                    }
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
        return new Thread(taskFindMaxNumberB);
    }

    private static Thread createThreadFindMaxNumberC() {
        Runnable taskFindMaxNumberC = () -> {
            for (int j = 0; j < NUMBER_STRING; j++) {
                int countSymbol = 0;
                try {
                    String text = queueLookupCharacterC.take();
                    System.out.println("ПОТОК C: Взяли " + text.substring(0, 10) + "...");
                    for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) == SYMBOL_C) {
                            countSymbol++;
                        }
                    }
                    if (countSymbol > maxNumberC) {
                        maxNumberC = countSymbol;
                        textWithMaxNumberC = text;
                    }
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    return;
                }

            }
        };
        return new Thread(taskFindMaxNumberC);
    }
}