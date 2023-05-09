import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class Sorting {
    // replace with your
    public static final String TEAM_NAME = "baseline";

    /**
     * Sorts an array of doubles in increasing order. This method is a
     * single-threaded baseline implementation.
     *
     * @param data the array of doubles to be sorted
     */
    public static void baselineSort(float[] data) {
        Arrays.sort(data, 0, data.length);
    }

    /**
     * Sorts an array of doubles in increasing order. This method is a
     * multi-threaded optimized sorting algorithm. For large arrays (e.g., arrays of size at least 1 million) it should be significantly faster than baselineSort.
     *
     * @param data   the array of doubles to be sorted
     */

    private static final int BASECASE = 10000; // Threshold for normal sorting

    public static void parallelSort(float[] data) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new SortTask(data, 0, data.length - 1));
        pool.shutdown();
    }

    public static class SortTask extends RecursiveAction {
        private float[] data;
        private int left;
        private int right;

        public SortTask(float[] data, int left, int right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            // Base case:
            if (right - left < BASECASE) {
                Arrays.sort(data, left, right + 1);
                return;
            }

            // Median-of-three approach:
            int mid = (left + right) >>> 1;
            if (data[left] > data[mid]) {
                swap(data, left, mid);
            }
            if (data[mid] > data[right]) {
                swap(data, mid, right);
                if (data[left] > data[mid]) {
                    swap(data, left, mid);
                }
            }
            float pivot1 = data[left];
            float pivot2 = data[right];

            // Dual pivot quicksort:
            int lt = left + 1;
            int gt = right - 1;
            int i = lt;
            while (i <= gt) {
                if (data[i] < pivot1) {
                    swap(data, i, lt);
                    lt++;
                } else if (data[i] >= pivot2) {
                    while (data[gt] > pivot2 && i < gt) {
                        gt--;
                    }
                    swap(data, i, gt);
                    gt--;
                    if (data[i] < pivot1) {
                        swap(data, i, lt);
                        lt++;
                    }
                }
                i++;
            }
            lt--;
            gt++;
            swap(data, left, lt);
            swap(data, right, gt);

            // Create sub-tasks for the left and right partitions
            SortTask leftTask = new SortTask(data, left, lt - 1);
            SortTask middleTask = new SortTask(data, lt + 1, gt - 1);
            SortTask rightTask = new SortTask(data, gt + 1, right);

            // Fork the sub-tasks and wait for them to complete
            leftTask.fork();
            middleTask.fork();
            rightTask.fork();
            leftTask.join();
            middleTask.join();
            rightTask.join();
        }
    }

    private static void swap(float[] data, int i, int j) {
        float temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Determines if an array of doubles is sorted in increasing order.
     *
     * @param data the array to check for sortedness
     * @return `true` if the array is sorted, and `false` otherwise
     */
    public static boolean isSorted(float[] data) {
        double prev = data[0];

        for (int i = 1; i < data.length; ++i) {
            if (data[i] < prev) {
                return false;
            }

            prev = data[i];
        }

        return true;
    }
}