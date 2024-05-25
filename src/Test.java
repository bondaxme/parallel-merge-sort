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

        Building[] buildings = new Building[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            buildings[i] = new Building("Building" + i, 3000 + Math.random() * 997000);
        }

        Comparator<Building> priceComparator = Comparator.comparingDouble(Building::getPrice);

        // Вармап на 5 ітераціях
        System.out.println("Прогрівання запущено");
        for (int i = 0; i < warmupIterations; i++) {
            Building[] buildingsCopy = buildings.clone();
            ParallelMergeSort.sequentialSort(buildingsCopy, 0, buildingsCopy.length - 1, priceComparator);
        }
        System.out.println("Прогрівання завершено");

        // Запуск на 20 ітераціях
        System.out.println("Запуск тестування");
        long totalTime = 0;
        System.out.println("Час виконання: ");
        for (int i = 0; i < iterations; i++) {
            Building[] buildingsCopy = buildings.clone();
            long startTime = System.currentTimeMillis();
            ParallelMergeSort.sequentialSort(buildingsCopy, 0, buildingsCopy.length - 1, priceComparator);
            long endTime = System.currentTimeMillis();
            System.out.print(i + 1 + ") " + (endTime - startTime) + " ms");
            System.out.println("\t Масив відсортований: " + isSorted(buildingsCopy));
            totalTime += (endTime - startTime);
        }

        double averageTime = totalTime / (double) iterations;
        System.out.println("\nСередній час виконання: " + averageTime + " ms");
    }
    public static boolean isSorted(Building[] buildings) {
        for (int i = 1; i < buildings.length; i++) {
            if (buildings[i - 1].getPrice() > buildings[i].getPrice()) {
                return false;
            }
        }
        return true;
    }
}
