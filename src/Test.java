import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть кількість будинків: ");
        int size = scanner.nextInt();

        int iterations = 20;
        int warmupIterations = 5;
        int numberOfThreads = 6;
        boolean GENERATE_NEW_ARRAY = false;

        Building[] buildings = new Building[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            buildings[i] = new Building("Building" + i, 3000 + Math.random() * 997000);
        }

        Comparator<Building> priceComparator = Comparator.comparingDouble(Building::getPrice);

        System.out.println("Прогрівання запущено");
        for (int i = 0; i < warmupIterations; i++) {
            Building[] buildingsCopy = buildings.clone();
            Building[] buildingsCopyParallel = buildings.clone();

            ParallelMergeSort.sequentialSort(buildingsCopy, 0, buildingsCopy.length - 1, priceComparator);

            ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
            ParallelMergeSort<Building> task = new ParallelMergeSort<>(buildingsCopyParallel, 0, buildingsCopyParallel.length - 1, priceComparator);
            pool.invoke(task);
        }
        System.out.println("Прогрівання завершено");


//        System.out.println("\n\nЗапуск тестування послідовного алгоритму");
//        long totalTime = 0;
//        System.out.println("Час виконання: ");
//        for (int i = 0; i < iterations; i++) {
//            Building[] buildingsLocal = buildings.clone();
//            long startTime = System.currentTimeMillis();
//                ParallelMergeSort.sequentialSort(buildingsLocal, 0, buildingsLocal.length - 1, priceComparator);
//            long endTime = System.currentTimeMillis();
//            System.out.print(i + 1 + ") " + (endTime - startTime) + " ms");
//            System.out.println("\t Масив відсортований: " + isSorted(buildingsLocal));
//            totalTime += (endTime - startTime);
//        }
//        double averageTime = (double) totalTime / iterations;
//        System.out.println("\nСередній час виконання послідовного алгоритму: " + averageTime + " ms");


        System.out.println("\n\nЗапуск тестування паралельного алгоритму");
        long totalTimeParallel = 0;
        System.out.println("Час виконання: ");
        for (int i = 0; i < iterations; i++) {

            Building[] buildingsLocal = new Building[size];
            if (GENERATE_NEW_ARRAY) {
                for (int j = 0; j < size; j++) {
                    buildingsLocal[j] = new Building("Building" + j, 3000 + Math.random() * 997000);
                }
            } else {
                buildingsLocal = buildings.clone();
            }

            long startTime = System.currentTimeMillis();
            ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
            ParallelMergeSort<Building> task = new ParallelMergeSort<>(buildingsLocal, 0, buildingsLocal.length - 1, priceComparator);
            pool.invoke(task);
            long endTime = System.currentTimeMillis();

            System.out.print(i + 1 + ") " + (endTime - startTime) + " ms");
            System.out.println("\t Масив відсортований: " + isSorted(buildingsLocal));
            totalTimeParallel += (endTime - startTime);
        }
        double averageTimeParallel = (double) totalTimeParallel / iterations;
        System.out.println("\nСередній час виконання паралельного алгоритму: " + averageTimeParallel + " ms");

//        System.out.println("\n\nSpeedup: " + (double) totalTime / totalTimeParallel);
    }
    public static boolean isSorted(Building[] buildings) {
        for (int i = 1; i < buildings.length; i++) {
            if (buildings[i - 1].getPrice() > buildings[i].getPrice()) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(Building[] buildings1, Building[] buildings2) {
        if (buildings1.length != buildings2.length) {
            return false;
        }
        for (int i = 0; i < buildings1.length; i++) {
            if (buildings1[i].getPrice() != buildings2[i].getPrice()) {
                return false;
            }
        }
        return true;
    }

}
