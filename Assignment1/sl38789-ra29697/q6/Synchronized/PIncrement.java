package q6.Synchronized;
import java.util.ArrayList;

public class PIncrement implements Runnable{
    
    static int C;
    int incAmount;
    PIncrement(int c, int incAmount) {
        this.C= c;
        this.incAmount = incAmount;
    }
    
    public static int parallelIncrement(int c, int numThreads){
            C = c;
            int totalOps = 1200000;
            int inc = totalOps / numThreads;
            int incExtra = totalOps % numThreads;
        // your implementation goes here.
            Thread[] threads = new Thread[numThreads];

            for(int i = 0; i < numThreads; i++) {
                if(i==numThreads-1) {
                    inc+= incExtra;
                    Thread t = new Thread(new PIncrement(c, inc));
                    threads[i] = t;
                }
                else {
                    Thread t = new Thread(new PIncrement(c, inc));
                    threads[i] = t;
                }
            }
            
            for(Thread thread: threads) {
                thread.start();
            }
            
            for(Thread thread: threads) {
                try {
                    thread.join();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return C;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        for (int i = 0; i < incAmount; i++) {
            increment();
        }
    }
    
    public synchronized static void increment() {
        C += 1;
    }
}
