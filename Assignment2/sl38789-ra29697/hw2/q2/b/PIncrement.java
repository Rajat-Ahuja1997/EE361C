//*** Some of Lamport's Fast Mutex implementation was excepted from the class reading "f10-chapter2.pdf"

package q2.b;


public class PIncrement {
	private static final int M = 120000;
	private static int N;
	private static int X; // last
	private static int Y; // door
	private static boolean[] Flag; // 0: down; 1: up
	private static int C; // value c for increment
	private static Thread[] Threads;
	
	// Lamport requestCS
	static void requestCS(int i) {
		while(true) {
			Flag[i] = true;
			X = i;
			if (Y != -1) { // splitter's left
				Flag[i] = false;
				while (Y != -1) {} // wait until Y == -1
				continue;
			} else {
				Y = i;
				if (X == i) // success with splitter
					return; // fast path
				else { // splitter's right
					Flag[i] = false;
					for (int j = 0; j < N; j++) {
						while (Flag[j] == true) {} // wait until Flag[j] == down
					}
							
					if (Y == i) // slow path
						return;
					else {
						while (Y != -1) {} // wait until Y == -1
						continue;
					}
				}
			}
		}
	}
	
	// Lamport releaseCS
	static void releaseCS(int i) {
		Y = -1;
		Flag[i] = false;
	}
	
	// thread for increment C
	static private class Increment implements Runnable{
		int Pid;
		int COUNT;

		Increment(int pid, int count) {
			Pid = pid;
			COUNT = count;
		}
		
		public void run() {
			requestCS(Pid);
			for (int i = 0; i < COUNT; i++) { // Critical Section
				C++;
			}
			releaseCS(Pid);
		}
	}
	
    public static int parallelIncrement(int c, int numThreads) {
    	// numThreads is 1, 2, 4, or 8
    	// initialization
    	N = numThreads;
    	C = c;
    	X = -1;
    	Y = -1;
    	Flag = new boolean[numThreads];
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
