package q6.ReentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class PIncrement implements Runnable{
	private static final int M = 1200000;
	private static int COUNT;
	private static int C;
	private static int threadFinshedNumber;
	private static ReentrantLock ReentLockC;
	private static ReentrantLock ReentLockThread;

	PIncrement(int count) {
		COUNT = count;
	}
	
	private void incrementC() {
		ReentLockC.lock(); 
        try
        { 
        	C++;
        } 
        catch(Exception e) 
        { 
            e.printStackTrace(); 
        } 
        finally
        { 
        	ReentLockC.unlock(); 
        }
    }
	
	private void incrementThreadFinishedNumber() {
		ReentLockThread.lock(); 
        try
        { 
        	threadFinshedNumber++;
        } 
        catch(Exception e) 
        { 
            e.printStackTrace(); 
        } 
        finally
        { 
        	ReentLockThread.unlock(); 
        }
    }
	
	public void run() {
		for (int i = 0; i < COUNT; i++) {
			incrementC();
		}
		incrementThreadFinishedNumber();
	}
	
    public static int parallelIncrement(int c, int numThreads){
        // your implementation goes here.
    	ReentLockC = new ReentrantLock();
    	ReentLockThread = new ReentrantLock();
    	
    	C = c;
    	threadFinshedNumber = 0;
    	int count = M / numThreads;
    	
    	for (int i = 0; i < numThreads; i++) {
    		if (i == numThreads - 1) { count += M % numThreads; }
   
    		Thread t = new Thread(new PIncrement(count));
    		t.start();
    	}
    	
    	while (threadFinshedNumber != numThreads) {} // wait until all threads are finished
    	return C;
    	
    }
}
