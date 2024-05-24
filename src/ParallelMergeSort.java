import java.util.Comparator;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort<T> extends RecursiveAction {
    private T[] array;
    private int left;
    private int right;
    private Comparator<T> comparator;
    private int threshold = 1000;

    public ParallelMergeSort(T[] array, int left, int right, Comparator<T> comparator) {
        this.array = array;
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }

    @Override
    protected void compute() {
        if (right - left < threshold) {
            sequentialSort(array, left, right, comparator);
        } else {
            int mid = (left + right) / 2;
            invokeAll(new ParallelMergeSort<>(array, left, mid, comparator),
                    new ParallelMergeSort<>(array, mid + 1, right, comparator));

            merge(array, left, mid, right, comparator);
        }
    }

    public static <T> void sequentialSort(T[] array, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int mid = (left + right) / 2;
            sequentialSort(array, left, mid, comparator);
            sequentialSort(array, mid + 1, right, comparator);
            merge(array, left, mid, right, comparator);
        }
    }

    private static <T> void merge(T[] array, int left, int mid, int right, Comparator<T> comparator) {
        T[] temp = (T[]) new Object[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (comparator.compare(array[i], array[j]) <= 0) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, left, temp.length);
    }
}
