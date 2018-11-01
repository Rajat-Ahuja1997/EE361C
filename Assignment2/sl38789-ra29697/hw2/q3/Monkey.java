package q3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {
	// declare the variables here
	static final int MaxNum = 3;
	static final int MinNum = 0;
	Queue<Integer> MonkeysOnRope; // should be 0 <= NumMonkeysOnRope <= 3; 1 if Kong is on the rope
	Lock LockForRope;
	Condition NotFull;
	Condition NotEmpty;
	Condition Empty;
	
	
    public Monkey() {
    	MonkeysOnRope = new LinkedList<Integer>();
    	LockForRope = new ReentrantLock();
    	NotFull = LockForRope.newCondition();
    	NotEmpty = LockForRope.newCondition();
    	Empty = LockForRope.newCondition();
    }

    // A monkey calls the method when it arrives at the river bank and wants to climb
    // the rope in the specified direction (0 or 1); Kong’s direction is -1.
    // The method blocks a monkey until it is allowed to climb the rope.
    public void ClimbRope(int direction) throws InterruptedException {
    	LockForRope.lock();
    	try {
    		while (MonkeysOnRope.size() == MaxNum) {
    			try {
					NotFull.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		while (MonkeysOnRope.peek() == -1) {
    			try {
    				Empty.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		int headMonkeyDir = MonkeysOnRope.peek();
    		if (direction == 0 || direction == 1) {
    			if (direction == headMonkeyDir) { // same direction monkey
    				MonkeysOnRope.add(direction);
    			} else { // opposite direction monkey
    				MonkeysOnRope.poll();
    			}
    		} else if (direction == -1) {
    			while (!MonkeysOnRope.isEmpty()) {
        			try {
    					Empty.await();
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        		}
    			
    			MonkeysOnRope.add(direction);
    		}
    		
    	    NotEmpty.signal();
    	} finally {
    		LockForRope.unlock();
    	}
    }

    // After crossing the river, every monkey calls this method which
    // allows other monkeys to climb the rope.
    public void LeaveRope() {
    	LockForRope.lock();
    	try {
    		while (MonkeysOnRope.size() == MinNum) {
    			try {
					NotEmpty.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		MonkeysOnRope.poll();
    		if (MonkeysOnRope.isEmpty()) { Empty.signal(); }
    		
    	    NotFull.signal();
    	} finally {
    		LockForRope.unlock();
    	}
    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
        return MonkeysOnRope.size();
    }

}
