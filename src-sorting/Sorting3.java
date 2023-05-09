package optimized_sorting;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class Sorting3 {
    // replace with your 
    public static final String TEAM_NAME = "baseline";
    
    /**
     * Sorts an array of doubles in increasing order. This method is a
     * single-threaded baseline implementation.
     *
     * @param data   the array of doubles to be sorted
     */
    public static void baselineSort (float[] data) {
	Arrays.sort(data, 0, data.length);
    }

    /**
     * Sorts an array of doubles in increasing order. This method is a
     * multi-threaded optimized sorting algorithm. For large arrays (e.g., arrays of size at least 1 million) it should be significantly faster than baselineSort.
     *
     * @param data   the array of doubles to be sorted
     */
    public static void parallelSort (float[] data) {

	// replace this with your method!

    //calls the quicksort algorithm
	quicksort(data,0,data.length-1);
	
    }

    public static void quicksort(float [] data, int i, int j){
        
    if (i<j){
            int pivot = findPivot(data,i,j);

            quicksort(data,i,pivot-1);
            quicksort(data,pivot+1,j);
        }
    }

    private static int findPivot(float[] data, int i, int j){
        float pivot = data[j];      //chooses the final elemet of the array to be the pivot (can be further optimized by choosing elements near the middle)
        int small= i-1;     //creates a pointer to the smallest element of the array

        for(int curr=i;curr<j;curr++){      //loops through all elements of the (sub)array being studied
            if(data[curr]<=pivot){       //if an element that is smaller than the pivot is found 
                small++;            //increment the left/small pointer 
                swap(data,small,curr);   //swap the current element and the small element
            }
        }

        swap(data,small+1,j);        //swaps the final element of the (sub)array and the small pointer: the subarray is now ordered
        return small+1;   //returns the index of small+1 to serve as the pivot next time quicksort is called
    }

    //swaps the position of two elements within the data array
    public static void swap(float[] data, int swp_1, int swp_2){
        float temp = data[swp_1];         //saves the value of element at index swp_1
        data[swp_1]=data[swp_2];         //moves the element at index swp_2 to swp_1
        data[swp_2]=temp;       //saves the temp element at index swp_2
    }

    /**
     * Determines if an array of doubles is sorted in increasing order.
     *
     * @param   data  the array to check for sortedness
     * @return        `true` if the array is sorted, and `false` otherwise
     */
    public static boolean isSorted (float[] data) {
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
