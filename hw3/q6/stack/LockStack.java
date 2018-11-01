package stack;

import java.util.concurrent.locks.ReentrantLock;

public class LockStack implements MyStack {
	// you are free to add members
	Node top;
	ReentrantLock pushLock;
	ReentrantLock popLock;
	
	public LockStack() {
	 	// implement your constructor here
		top = null;
		pushLock = new ReentrantLock();
		popLock = new ReentrantLock();
 	}
  
	public boolean push(Integer value) {
		// implement your push method here
		pushLock.lock();
		try {
			Node newNode = new Node(value);
			newNode.next = top;
			top = newNode;
		} finally {
			pushLock.unlock();
		}
		return true;
	}
  
	public Integer pop() throws EmptyStack {
		// implement your pop method here
		Integer result = null;
		popLock.lock();
		try {
			if (top == null) {
				throw new EmptyStack();
			}
			result = top.value;
			top = top.next;
		} finally {
			popLock.unlock();
		}
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
		Node curr = top;
		while (curr != null) {
			sb.append(curr.value).append(",");
			curr = curr.next;
		}
		return sb.toString();
  	}
}
