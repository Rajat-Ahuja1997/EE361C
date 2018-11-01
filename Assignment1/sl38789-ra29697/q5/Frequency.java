package q5;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Frequency implements Callable<Integer> {
    int x;
    int[] A;
    
    Frequency() {}
    Frequency(int target, int[] array) {
        this.x = target;
        this.A = array;
    }
    
    public Integer call() throws Exception {
        int size = A.length;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (A[i] == x) { count++; }
        }
        return count;
    }
    
    public static int parallelFreq(int x, int[] A, int numThreads){
        // your implementation goes here, return -1 if the input is not valid.
        // invalid checks
        if (A == null) { return -1; }
        if (numThreads <= 0) { return -1; }
        
        ArrayList<Future<Integer>> tasks = new ArrayList<Future<Integer>>();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        
        int size = A.length;
        int cutIdx = 0; // start index of A for copying sub-array for each thread
        
        for (int i = 0; i < numThreads; i++) {
            // get subA and its size_subA for each thread
            int size_subA = size / numThreads;
            if (i == numThreads - 1) { size_subA += size % numThreads; } // count leftover
            int[] subA = new int[size_subA];
            int j = cutIdx;
            int k = 0;
            for (; j < cutIdx + size_subA; j++) {
                subA[k] = A[j];
                k++;
            }
            cutIdx = j;
            
            // create thread and send x, subA
            Frequency task = new Frequency(x, subA);
            Future<Integer> future = executorService.submit(task);
            tasks.add(future);
        }
        
        // calculate sum of freq
        int sum = 0;
        for (Future<Integer> task : tasks) {
            try {
                while (!task.isDone()) {} // wait until the thread is done
                sum += task.get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sum;
    }
}
