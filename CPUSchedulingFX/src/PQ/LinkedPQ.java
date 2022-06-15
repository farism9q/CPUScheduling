package PQ;

public class LinkedPQ<T> {
	private int size;
	private PQNode<T> head;
	
	public LinkedPQ() {
		head = null;
		size = 0;
	}
	
	public int length() {
		return size;
	}
	
	public boolean full() {
		return false;
	}
	
	public void enqueue(T e, int pri, String name) {
		PQNode<T> tmp = new PQNode<T>(e,pri, name); 
		
		
		if(size == 0 || tmp.priority < head.priority) {
			tmp.next = head;
			head = tmp;
		}
		else {
			PQNode<T> p = head;
			PQNode<T> q = null;
			
			while(p != null && pri >= p.priority) {
				q = p;
				p = p.next;
			}
			
			tmp.next = p;
			q.next = tmp;
		}
		size++;
	}
	
	public PQElement<T> serve(){
		PQNode<T> e = head;
		PQElement<T> temp = new PQElement<T>(e.data, e.priority, e.name);
		head = head.next;
		size--;
		return temp;
	}
	
}
