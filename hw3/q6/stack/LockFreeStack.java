package stack;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
	// you are free to add members
	AtomicReference<Node> top;
	
	
	public LockFreeStack() {
		// implement your constructor here
		top = new AtomicReference<Node>();
	}
	
	public boolean push(Integer value) {
		// implement your push method here
		Node newNode = new Node(value);
		while (true) {
			Node oldTop = top.get();
			newNode.next = oldTop;
			if (top.compareAndSet(oldTop, newNode)) { break; }
			else Thread.yield();
		}
		return true;
	}
  
	public Integer pop() throws EmptyStack {
		// implement your pop method here
		Integer result = null;
		while (true) {
			Node oldTop = top.get();
			if (oldTop == null) {
				throw new EmptyStack();
			}
			result = oldTop.value;
			Node newTop = oldTop.next;
			if (top.compareAndSet(oldTop, newTop)) { return result; }
			else Thread.yield();
		}
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
		Node curr = top.get();
		while (curr != null) {
			sb.append(curr.value).append(",");
			curr = curr.next;
		}
		return sb.toString();
  	}
}
