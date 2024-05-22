import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort extends RecursiveAction {
    private Flight[] array;
    private int left;
    private int right;
    private int threshold = 100000;

    public ParallelMergeSort(Flight[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (right - left < threshold) {
            sequentialSort(array, left, right);
        } else {
            int mid = (left + right) / 2;
            ParallelMergeSort leftTask = new ParallelMergeSort(array, left, mid);
            ParallelMergeSort rightTask = new ParallelMergeSort(array, mid + 1, right);

            invokeAll(leftTask, rightTask);

            merge(array, left, mid, right);
        }
    }

    public static void sequentialSort(Flight[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sequentialSort(array, left, mid);
            sequentialSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private static void merge(Flight[] array, int left, int mid, int right) {
        Flight[] temp = new Flight[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (array[i].getPrice() <= array[j].getPrice()) {
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
