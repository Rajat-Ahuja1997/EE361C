//*** Some of Fischer's implementation was excepted from the class reading "f10-chapter2.pdf"

package q2.a;


public class PIncrement {
	private static final int M = 120000;
	private static final int Delta = 10000; // 10us
	private static int C; // value c for increment
	private static int Turn; // Turn for the thread
	private static Thread[] Threads;
	
	// thread for increment C
	static private class Increment implements Runnable{
		int Pid;
		int COUNT;

		Increment(int pid, int count) {
			Pid = pid;
			COUNT = count;
		}
		
		// Fischer requestCS
		void requestCS(int i) {
			while (true){
				while (Turn != -1) {}; // wait for the door to open
				Turn = i; // write my id on turn
				// Assume that delta is bigger than time to update turn
				long waitUntil = System.nanoTime() + Delta; // 10us wait
			    while(waitUntil > System.nanoTime()) {}
				if (Turn == i) return;
			}
		}
		// Fischer releaseCS
		void releaseCS(int i) {
			Turn = -1;
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
    	C = c;
    	Turn = -1;
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
