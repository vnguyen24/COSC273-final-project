import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

import jdk.incubator.vector.*;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorMask;

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
     * @param data the array of doubles to be sorted
     */
    public static void sort(float[] data) {
        if (data == null || data.length <= 1) {
            return;
        }
        sort(data, 0, data.length - 1);
    }

    private static void sort(float[] data, int low, int high) {
        if (low >= high) {
            return;
        }

        // Choose pivot as middle element
        int pivotIndex = (low + high) >>> 1;
        float pivot = data[pivotIndex];

        // Partition the array around the pivot
        int i = low - 1;
        int j = high + 1;
        while (i < j) {
            do {
                i++;
            } while (Float.compare(data[i], pivot) < 0);

            do {
                j--;
            } while (Float.compare(data[j], pivot) > 0);

            if (i < j) {
                swap(data, i, j);
            }
        }

        // Recursively sort the two partitions
        sort(data, low, j);
        sort(data, j + 1, high);

        // Use SIMD vectorization to speed up the sorting process
        int vectorSize = FloatVector.SPECIES_256.length();
        FloatVector pivotVector = FloatVector.broadcast(FloatVector.SPECIES_256, pivot);

        for (int k = low; k < high; k += vectorSize) {
            int remaining = Math.min(high - k + 1, vectorSize);
            FloatVector vec = FloatVector.fromArray(FloatVector.SPECIES_256, data, k, remaining);

            VectorMask mask = vec.compareGreaterThan(pivotVector);
            int numGreater = mask.reduceLanes(VectorOperators.ADD);

            if (numGreater > 0) {
                int p = k + numGreater - 1;
                for (int n = 0; n < remaining; n++) {
                    if (mask.test(n)) {
                        while (Float.compare(data[p], pivot) >= 0) {
                            p--;
                        }
                        swap(data, k + n, p);
                        p--;
                    }
                }
            }
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
