package Main;

import java.util.Arrays;
import java.util.Random;

public class MinimumElementFinder {
    private static int[] array; // Великий масив

    private static int[] minimumValues;
    private static int numThreads = 10000; // Кількість потоків

    public static void main(String[] args) {
        // Ініціалізуємо великий масив з 10 000 000 елементів
        array = generateArray(10_000_000);

        // Замінюємо довільний елемент на випадкове від'ємне число
        replaceRandomElementWithNegative();

        minimumValues = new int[numThreads];

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int startIndex = i * (array.length / numThreads);
            final int endIndex = (i == numThreads - 1) ? array.length : (i + 1) * (array.length / numThreads);
            final int threadIndex = i;

            threads[i] = new Thread(() -> {
                int min = array[startIndex];
                int minIndex = startIndex;

                for (int j = startIndex + 1; j < endIndex; j++) {
                    if (array[j] < min) {
                        min = array[j];
                        minIndex = j;
                    }
                }

                minimumValues[threadIndex] = min;

                // Синхронізовано виводимо значення мінімального елементу та його індекс
                synchronized (MinimumElementFinder.class) {
                    System.out.println("Thread " + threadIndex + ": Minimum element: " + min + ", Index: " + (minIndex));
                }
            });

            threads[i].start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int overallMin = Arrays.stream(minimumValues).min().getAsInt();
        int overallMinIndex = findOverallMinIndex(overallMin);
        System.out.println("Overall Minimum element: " + overallMin + ", Index: " + overallMinIndex);
    }

    private static int[] generateArray(int length) {
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        return arr;
    }

    private static void replaceRandomElementWithNegative() {
        Random random = new Random();
        int randomIndex = random.nextInt(array.length);
        int randomNegativeNumber = -random.nextInt(1000); // Випадкове від'ємне число
        array[randomIndex] = randomNegativeNumber;
    }

    private static int findOverallMinIndex(int overallMin) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == overallMin) {
                return i;
            }
        }
        return -1;
    }
}






