import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int size = 10000;
        int numberOfThreads = 8;


        Building[] buildings = new Building[size];

        for (int i = 0; i < size; i++) {
            buildings[i] = new Building("Building" + i, 3000 + Math.random() * 997000);
        }

        Building[] buildings2 = buildings.clone();

        Comparator<Building> priceComparator = Comparator.comparingDouble(Building::getPrice);


        long startTime2 = System.currentTimeMillis();
        ParallelMergeSort.sequentialSort(buildings2, 0, buildings2.length - 1, priceComparator);
        long endTime2 = System.currentTimeMillis();

        System.out.println("Час виконання послідовного сортування: " + (endTime2 - startTime2) + " ms");


        long startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        ParallelMergeSort<Building> task = new ParallelMergeSort<>(buildings, 0, buildings.length - 1, priceComparator);

        pool.invoke(task);
        long endTime = System.currentTimeMillis();

        System.out.println("Час виконання паралельного сортування: " + (endTime - startTime) + " ms");

        System.out.println("Масив відсортований: " + Test.isSorted(buildings));

        System.out.println("Масиви рівні: " + Test.areEqual(buildings, buildings2));

//        // Speedup
//        System.out.println("Speedup: " + (double)(endTime2 - startTime2) / (endTime - startTime));
    }
}
