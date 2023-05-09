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

     //private static final int BASECASE1 = 10; // Threshold for insertion sort
     private static final int BASECASE2 = 500; // Threshold for Arrays.sort

     public static void parallelSort(float[] data) {
         ForkJoinPool pool = new ForkJoinPool().commonPool();  //hard capped number of threads created due to cost of thread creation overhead
         pool.invoke(new SortTask(data, 0, data.length - 1));
         pool.shutdown();
         //System.out.println("Final output is " + Arrays.toString(data));
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
             //System.out.println("Compute called with left: " + left + ", right: " + right);

             //calculate length of subarray
             int diff=right-left;

             if (diff < BASECASE2) {
                /* //checks whether array is very small, if so runs insertion sort
                 if(diff<BASECASE1){
                    
                    //iterates along the entire subarray
                    for (int i = left; i < right+1; ++i) {
                        //stores the value of the element
                        float compare = data[i];
                        int j = i - 1;

                    //if the element at j is larger than compare, moves it to the right and decrement the j pointer
                     while (j >= left && data[j] > compare) {
                        data[j + 1] = data[j];
                        j = j - 1;
                    }
                    //moves the element at index i to the right of j
                    data[j + 1] = compare;
                }   
                 return; //completes once subarray is sorted
                 }*/

                 Arrays.sort(data, left, right+1);  //sorts large subarrays
                 return; //completes once subarray is sorted
             }

                //identifies pivot point according to the Hoare partition scheme
                int pivotIndex=partition(data,left,right);
        
                 // Create sub-tasks for the left and right partitions
                 SortTask leftTask = new SortTask(data, left, pivotIndex);
                SortTask rightTask = new SortTask(data, pivotIndex + 1, right);
                 //System.out.println(Arrays.toString(Arrays.copyOfRange(data, left, right)));
                 // Fork the sub-tasks and wait for them to complete
                 leftTask.fork();
                 rightTask.fork();
                 leftTask.join();
                 rightTask.join();
             }
         }

    //implements the Hoare partition scheme for pivot selection
    //we use this partition scheme since it executes fewer swaps on average than Lomuto's
    //partition scheme, which is most commonly used
    private static int partition(float[] data, int low, int high) {
        
        //stores the indices of the start and end of the subarray
        float pivot = data[low];
        int i = low - 1;
        int j = high + 1;

        while (true)
        {  
            //increments the lower index until an element smaller than the pivot is found
            do {
                i++;
            } while (data[i] < pivot);
            
            //decrements the upper index until an element larger than the pivot is found
            do {
                j--;
            } while (data[j] > pivot);

            //returns the upper pointer if the pointers cross
            if (i >= j) {
                return j;
            }
            
            //swaps the elements before and after the pointer (at indices i and j respectively)
            swap(data, i, j);
        }
     }
     

    //swaps the elements at indices i and j of array data
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
