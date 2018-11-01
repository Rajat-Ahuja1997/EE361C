package queue;

import java.util.concurrent.Semaphore; 
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
	// you are free to add members
	Node head;
	Node tail;
	ReentrantLock enqLock;
	Semaphore deqLock;

	public LockQueue() {
		// implement your constructor here
		head = new Node(Integer.MIN_VALUE);
		tail = head;
		enqLock = new ReentrantLock();
		deqLock = new Semaphore(1); // only one thread is permitted to CS
	}
  
	public boolean enq(Integer value) {
		// implement your enq method here
		enqLock.lock();
		try {
			Node newNode = new Node(value);
			tail.next = newNode;
			tail = newNode;
		} finally {
			enqLock.unlock();
		}
		return true;
	}
  
	public Integer deq() {
		// implement your deq method here
		Integer result = null;
		try {
			deqLock.acquire();
			if (head.next != null) {
				result = head.next.value;
				head = head.next;
			} 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		deqLock.release();
		return result;
	}
  
  	protected class Node {
  		public Integer value;
  		public Node next;
		    
  		public Node(Integer x) {
  			value = x;
  			next = null;
  		}
  	}
  	
  	/*
  	return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
  	check simpleTest for more info
  	 */
  	public String toString() {
  		StringBuilder sb = new StringBuilder();
		Node curr = head.next;
		while (curr != null) {
			sb.append(curr.value).append(",");
			curr = curr.next;
		}
		return sb.toString();
  	}
}
