package queue;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {

    @Test
    public void testLockFreeQueue() {
        LockFreeQueue queue = new LockFreeQueue();
        makeThread(queue);
        checkNode(0, 0, queue);
    }

    @Test
    public void testLockQueue() {
        LockQueue queue = new LockQueue();
        makeThread(queue);
        checkNode(0, 0, queue);
    }

    private void makeThread(MyQueue queue) {
        Thread[] threads = new Thread[3];
        threads[0] = new Thread(new MyThread(0, 1000, queue));
        threads[1] = new Thread(new MyThread(0, 1000, queue));
        threads[2] = new Thread(new MyThread(0, 1000, queue));
        
        threads[1].start(); threads[0].start(); threads[2].start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkNode(int num, int count, MyQueue queue) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(num).append(",");
        }
        System.out.println(queue.toString());
        //System.out.println(sb.toString());
        Assert.assertEquals(queue.toString(), sb.toString());
    }

    private class MyThread implements Runnable {
        int num;
        int count;
        MyQueue queue;

        MyThread(int num, int count, MyQueue queue) {
            this.num = num;
            this.count = count;
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                queue.enq(num);
            }
            for (int i = 0; i < count; i++) {
                queue.deq();
            }
        }
    }
}
