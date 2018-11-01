package stack;

import org.junit.Assert; 
import org.junit.Test;

public class SimpleTest {

    @Test
    public void testLockFreeStack() {
        LockFreeStack stack = new LockFreeStack();
        makeThread(stack);
        checkNode(0, 0, stack);
    }

    private void makeThread(MyStack stack) {
        Thread[] threads = new Thread[3];
        threads[0] = new Thread(new MyThread(0, 10000, stack));
        threads[1] = new Thread(new MyThread(0, 10000, stack));
        threads[2] = new Thread(new MyThread(0, 10000, stack));
        
        threads[1].start(); threads[0].start(); threads[2].start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkNode(int num, int count, MyStack stack) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(num).append(",");
        }
        System.out.println(stack.toString());
        System.out.println(sb.toString());
        Assert.assertEquals(stack.toString(), sb.toString());
    }

    private class MyThread implements Runnable {
        int num;
        int count;
        MyStack stack;

        MyThread(int num, int count, MyStack stack) {
            this.num = num;
            this.count = count;
            this.stack = stack;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                stack.push(num);
            }
            for (int i = 0; i < count; i++) {
                try {
					int s = stack.pop();
				} catch (EmptyStack e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
}
