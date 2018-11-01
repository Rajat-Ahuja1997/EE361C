package q5;

import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedListSet implements ListSet {
	// you are free to add members
	Node head;
	Node tail;
	ReentrantLock lock;
	
	public CoarseGrainedListSet() {
		// implement your constructor here	
		head = new Node(Integer.MIN_VALUE);
		tail = new Node(Integer.MAX_VALUE);
		head.next = tail;
		lock = new ReentrantLock();
	}
  
	public boolean add(int value) {
		// implement your add method here
		Node prev = head;
		Node succ = prev.next;
		boolean valueExisted = false;
		
		lock.lock();
		try {
			while (prev != tail) {
				if (prev.value < value && value < succ.value) {
					Node x = new Node(value);
					x.next = succ;
					prev.next = x;
					break;
				} 
				else if (prev.value == value) {
					valueExisted = true;
					break;
				}
				else { // move next
					prev = succ;
					succ = succ.next;
				}
			}
		} finally {
			lock.unlock();
		}
		
		if (valueExisted) 
			return false;
		else
			return true;
	}
  
	public boolean remove(int value) {
		// implement your remove method here	
		Node prev = head;
		Node curr = prev.next;
		boolean valueExisted = false;
		
		lock.lock();
		try {
			while (prev != tail) {
				if (curr.value == value) {
					prev.next = curr.next;
					valueExisted = true;
					break;
				} 
				else if (curr.value > value) {
					break;
				} else { // move next
					prev = curr;
					curr = curr.next;
				}
			}
		} finally {
			lock.unlock();
		}
		
		if (valueExisted) 
			return true;
		else
			return false;
	}
  
	public boolean contains(int value) {
		// implement your contains method here	
		Node target = head.next;
		while (target != tail) {
			if (target.value == value) 
				return true;
			else if (target.value > value) 
				return false;
			else
				target = target.next;
		}
		// not reached
		return false;
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
		while (curr != tail) {
			sb.append(curr.value).append(",");
			curr = curr.next;
		}
		return sb.toString();
  	}
}
