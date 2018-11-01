//*** Some of Anderson's Spin Lock implementation was excepted from the class reading "f10-chapter2.pdf"

package q2.c;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {
	private static final int M = 120000;
	private static AtomicInteger tailSlot;
	private static boolean[] Available;
	private static ThreadLocal<Integer> mySlot;
	private static int N;
	private static int C; // value c for increment
	private static Thread[] Threads;
	
	// thread for increment C
	static private class Increment implements Runnable{
		int Pid;
		int COUNT;

		Increment(int pid, int count) {
			Pid = pid;
			COUNT = count;
		}
		
		// Anderson requestCS
		void requestCS(int i) {
			mySlot.set(tailSlot.getAndIncrement() % N);
			while (!Available[mySlot.get()]) {}
		}
		// Anderson releaseCS
		void releaseCS(int i) {
			Available[mySlot.get()] = false;
			Available[(mySlot.get() + 1) % N] = true;
		}
		
		public void run() {
			for (int i = 0; i < COUNT; i++) {
				requestCS(Pid);
				C++; // Critical Section
				releaseCS(Pid);
			}
		}
	}
	
    public static int parallelIncrement(int c, int numThreads) {
    	// numThreads is 1, 2, 4, or 8
    	// initialization
    	N = numThreads;
    	C = c;
    	tailSlot = new AtomicInteger(0);
    	mySlot = new ThreadLocal<Integer>();
    	mySlot.set(0);
    	Available = new boolean[N];
    	Available[0] = true;
    	Threads = new Thread[numThreads];
    	
    	int count = M / numThreads;    	
    	for (int i = 0; i < numThreads; i++) {
    		if (i == numThreads - 1) { count += M % numThreads; } // add remainder
   
    		Thread t = new Thread(new Increment(i, count));
    		Threads[i] = t;
    		t.start();
    	}
    	
    	// wait all threads to finish
    	for (int i = 0; i < numThreads; i++) {
    		while (Threads[i].isAlive()) {} // wait
    	}
    	
        return C;
    }
}
