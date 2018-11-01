package q5;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeListSet implements ListSet {
	// you are free to add members
	Node head;
	Node tail;
	
	public LockFreeListSet() {
		// implement your constructor here	
		head = new Node(Integer.MIN_VALUE);
		tail = new Node(Integer.MAX_VALUE);
		head.next = tail;
	}
	  
	public boolean add(int value) {
		// implement your add method here			
		Node prev = head;
		Node curr = prev.next;
		Node x = new Node(value);		
		boolean valueExisted = false;
		
		while (prev != tail) {
			if (prev.value < value && value < curr.value) {
				x.next = curr;
				
				if (prev.isMarked() == false) {
					if (prev.next == curr) {
						if (prev.next.compareAndSet(curr, x, false, false)) {
							prev.next = x;
							break;
						} else {
							prev = head;
							curr = prev.next;
						}
					} else {
						prev = head;
						curr = prev.next;
					}
				} else {
					prev = head;
					curr = prev.next;
				}
			} 
			else if (prev.value == value || curr.value == value) {
				valueExisted = true;
				break;
			}
			else { // move next
				prev = curr;
				curr = curr.next;
			}
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
		
		while (prev != tail) {
			if (curr.value == value) {
				if (prev.isMarked() == false) {
					if (prev.next.compareAndSet(curr, curr.next, false, false)) {
						prev.next = curr.next;
						curr.set(true);
						valueExisted = true;
						break;
					} else {
						prev = head;
						curr = prev.next;
					}
				} else {
					prev = head;
					curr = prev.next;
				}
			} 
			else if (curr.value > value) {
				break;
			} else { // move next
				prev = curr;
				curr = curr.next;
			}
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
				if (target.isMarked() == false) return true;
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
		AtomicMarkableReference<Node> amrNode;
		
		public Node(Integer x) {
			value = x;
			next = null;
			amrNode = new AtomicMarkableReference<Node>(this, false);
		}
		
		public boolean isMarked() {
			return amrNode.isMarked();
		}
		
		public boolean compareAndSet(Node expectedReference, Node newReference, boolean expectedMark, boolean newMark) {
			boolean result = amrNode.compareAndSet(expectedReference, newReference, expectedMark, newMark);
			if (result) { amrNode.set(expectedReference, expectedMark); }
			return result;
		}
		
		public void set(boolean newMark) {
			amrNode.set(this, newMark);
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
