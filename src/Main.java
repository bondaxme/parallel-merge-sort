import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int size = 1000000;
        int numberOfThreads = 6;


        Building[] buildings = new Building[size];

        for (int i = 0; i < size; i++) {
            buildings[i] = new Building("Building" + i, 3000 + Math.random() * 997000);
        }

        Building[] buildings2 = buildings.clone();

        Comparator<Building> priceComparator = Comparator.comparingDouble(Building::getPrice);

//        long startTime = System.currentTimeMillis();
//        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
//        ParallelMergeSort<Building> task = new ParallelMergeSort<>(buildings, 0, buildings.length - 1, priceComparator);
//
//        pool.invoke(task);
//        long endTime = System.currentTimeMillis();

//        System.out.println("Час виконання: " + (endTime - startTime) + " ms");

        long startTime2 = System.currentTimeMillis();
        ParallelMergeSort.sequentialSort(buildings2, 0, buildings2.length - 1, priceComparator);
        long endTime2 = System.currentTimeMillis();

        System.out.println("Час виконання послідовного сортування: " + (endTime2 - startTime2) + " ms");

        for (int i = 1; i < size; i++) {
            if (buildings2[i - 1].getPrice() > buildings2[i].getPrice()) {
                System.out.println("Масив не відсортований");
                break;
            }
        }
        System.out.println("Масив відсортований");

//        // Speedup
//        System.out.println("Speedup: " + (double)(endTime2 - startTime2) / (endTime - startTime));
    }
}
