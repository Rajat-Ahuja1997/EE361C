package q6.AtomicInteger;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement implements Runnable{
	private static final int M = 1200000;
	private static AtomicInteger atomicInteger;
	private static int COUNT;
	private static AtomicInteger threadFinshedNumber;

	PIncrement(int count) {
		COUNT = count;
	}
	
	public void run() {
		for (int i = 0; i < COUNT; i++) {
			atomicInteger.incrementAndGet();
		}
		threadFinshedNumber.incrementAndGet();
	}
	
    public static int parallelIncrement(int c, int numThreads){
        // your implementation goes here.
    	atomicInteger = new AtomicInteger(c);
    	threadFinshedNumber = new AtomicInteger(0);
    	int count = M / numThreads;
    	
    	for (int i = 0; i < numThreads; i++) {
    		if (i == numThreads - 1) { count += M % numThreads; }
   
    		Thread t = new Thread(new PIncrement(count));
    		t.start();
    	}
    	
    	while (threadFinshedNumber.get() != numThreads) {} // wait until all threads are finished
    	return atomicInteger.get();
    }
}