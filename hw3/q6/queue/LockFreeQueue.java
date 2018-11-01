package queue;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue implements MyQueue {
	// you are free to add members
	Node head;
	Node tail;
	AtomicReference<Node> arHead;
	AtomicReference<Node> arTail;
	
	public LockFreeQueue() {
		// implement your constructor here
		head = new Node(Integer.MIN_VALUE);
		tail = head;
		arHead = new AtomicReference<Node>(head);
		arTail = new AtomicReference<Node>(tail);
	}

	public boolean enq(Integer value) {
		// implement your enq method here
		Node currTail;
		Node currNext;
		Node newNode = new Node(value);
		AtomicReference<Node> arCurrTailNext = new AtomicReference<Node>();
		
		while (true) {
			currTail = tail;
			currNext = currTail.next;
			arCurrTailNext.set(currNext);
			
			if (currTail == tail) {
				if (currNext == null) {
					if (arCurrTailNext.compareAndSet(currNext, newNode)) {
						currTail.next = newNode;
						break; 
					}
				}
				else {
					tail = currNext;
				}
			}
		}
		tail = newNode;
		return true;
	}
  
	public Integer deq() {
		// implement your deq method here
		Integer result = null;
		Node currHead;
		Node currTail;
		Node currNext;
		
		while (true) {
			currHead = head;
			currNext = currHead.next;
			currTail = tail;
			
			if (currHead == head) {
				if (currHead == currTail) {
					if (currNext == null) {
						return result;
					}
					tail = currNext;
					//arTail.compareAndSet(currTail, currNext);
					//tail = arTail.get();
				}
				else {
					result = currNext.value; // read value
					if (arHead.compareAndSet(currHead, currNext)) {
						head = currNext;
						break; 
					}
				}
			}
		}
		
		return result;
	}
  
	protected class Node {
		public Integer value;
		public Node next;
		public AtomicReference<Node> ar;
		
		public Node(Integer x) {
			value = x;
			next = null;
			ar = new AtomicReference<Node>(this);
		}
		
		public boolean compareAndSet(Node expect, Node update) {
			boolean result = ar.compareAndSet(expect, update);
			if (result) { ar.set(expect); }
			return result;
		}
		
		public Node get() {
			return ar.get();
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
