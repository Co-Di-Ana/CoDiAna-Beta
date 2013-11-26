
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Sorting {

    private int[] numbers;


    StringTokenizer st = new StringTokenizer("");

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));



    String nextToken() throws Exception {
        while (!st.hasMoreTokens())
            st = new StringTokenizer(input.readLine());
        return st.nextToken();
    }



    final int nextInt() throws Exception {
        return Integer.parseInt(nextToken());
    }

    public static void main(String[] args) throws Exception {
        new Sorting();
    }



    public Sorting() throws Exception {
        int i, l = nextInt();
        while (l != -1) {
            numbers = new int[l];
            for (i = 0; i < l; i++)
                numbers[i] = nextInt();

            if (l < 100)
                shellSort(numbers);
            else
                quicksort(numbers, 0, l-1);

            for (i = 0; i < l; i++)
                System.out.println(numbers[i]);

            System.out.println();
            l = nextInt();
        }
    }



    public void shellSort(int[] a) {
        for (int increment = a.length / 2; increment > 0;
                increment = (increment == 2 ? 1 : (int) Math.round(increment / 2.2)))
            for (int i = increment; i < a.length; i++) {
                int temp = a[i];
                for (int j = i; j >= increment && a[j - increment] > temp; j -= increment) {
                    a[j] = a[j - increment];
                    a[j - increment] = temp;
                }
            }
    }



    private void quicksort(int[] numbers, int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        int pivot = numbers[low + (high - low) / 2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (numbers[i] < pivot)
                i++;
            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (numbers[j] > pivot)
                j--;

            // If we have found a values in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                int temp = numbers[i];
                numbers[i] = numbers[j];
                numbers[j] = temp;
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(numbers, low, j);
        if (i < high)
            quicksort(numbers, i, high);
    }



}
