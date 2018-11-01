package q5;

import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedListSet implements ListSet {
	// you are free to add members
	Node head;
	Node tail;

	public FineGrainedListSet() {
		// implement your constructor here	
		head = new Node(Integer.MIN_VALUE);
		tail = new Node(Integer.MAX_VALUE);
		head.next = tail;
	}
	  
	public boolean add(int value) {
		// implement your add method here
		Node prev = head;
		Node succ = prev.next;
		boolean valueExisted = false;
		
		prev.lock();
		succ.lock();
		try {
			while (prev != tail) {
				if (prev.isDeleted == false && succ.isDeleted == false) { // (condition 1) neither pred, nor succ should have been delete
					if (prev.next == succ) { // (condition 2) the next pointer of pred should be pointing to succ
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
							prev.unlock();
							prev = succ;
							succ = succ.next;
							succ.lock();
						}
					} else {
						succ = prev.next;
					}
				} else {
					prev = head;
					succ = prev.next;
				}
			}
		} finally {
			prev.unlock();
			succ.unlock();
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
		
		prev.lock();
		curr.lock();
		try {
			while (prev != tail) {
				if (prev.isDeleted == false && curr.isDeleted == false) { // (condition 1) neither pred, nor succ should have been delete
					if (prev.next == curr) { // (condition 2) the next pointer of pred should be pointing to succ
						if (curr.value == value) {
							curr.isDeleted = true;
							prev.next = curr.next;
							valueExisted = true;
							break;
						} 
						else if (curr.value > value) {
							break;
						} else { // move next
							prev.unlock();
							prev = curr;
							curr = curr.next;
							curr.lock();
						}
					} else {
						curr = prev.next;
					}
				} else {
					prev = head;
					curr = prev.next;
				}
			}
		} finally {
			prev.unlock();
			curr.unlock();
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
			if (target.value == value) {
				if (target.isDeleted == false) return true;
				else return false;
			}
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
		public boolean isDeleted;
		ReentrantLock lockNode;
			    
		public Node(Integer x) {
			value = x;
			next = null;
			isDeleted = false;
			lockNode = new ReentrantLock();
		}
		
		public void lock() {
			lockNode.lock();
		}
		
		public void unlock() {
			lockNode.unlock();
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
