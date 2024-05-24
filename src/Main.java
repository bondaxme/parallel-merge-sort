import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int size = 1000000;
        int numberOfThreads = 2;

        Flight[] flights = new Flight[size];

        // Заповнення масиву рейсів випадковими даними від 100 до 20000 із заокругленням до 2 знаків після коми
        for (int i = 0; i < size; i++) {
            double price = Math.round((100 + Math.random() * 19900) * 100.0) / 100.0;
            flights[i] = new Flight("Flight" + i, price);
        }

        Comparator<Flight> priceComparator = Comparator.comparingDouble(Flight::getPrice);

        long startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        ParallelMergeSort<Flight> task = new ParallelMergeSort<>(flights, 0, flights.length - 1, priceComparator);

        pool.invoke(task);
        long endTime = System.currentTimeMillis();

        System.out.println("Час виконання: " + (endTime - startTime) + " ms");

        // Послідовне сортування
        long startTime2 = System.currentTimeMillis();
        ParallelMergeSort.sequentialSort(flights, 0, flights.length - 1, priceComparator);
        long endTime2 = System.currentTimeMillis();

        System.out.println("Час виконання послідовного сортування: " + (endTime2 - startTime2) + " ms");

        // Перевірка чи масив відсортований
        for (int i = 1; i < size; i++) {
            if (flights[i - 1].getPrice() > flights[i].getPrice()) {
                System.out.println("Масив не відсортований");
                break;
            }
        }
        System.out.println("Масив відсортований");

        // Speedup
        System.out.println("Speedup: " + (double)(endTime2 - startTime2) / (endTime - startTime));

        // Вивід початку та кінця відсортованого масиву
        System.out.println("Початок відсортованого масиву:");
        for (int i = 0; i < 10; i++) {
            System.out.println(flights[i].getFlightNumber() + " " + flights[i].getPrice());
        }

        System.out.println("Кінець відсортованого масиву:");
        for (int i = size - 10; i < size; i++) {
            System.out.println(flights[i].getFlightNumber() + " " + flights[i].getPrice());
        }
    }
}
