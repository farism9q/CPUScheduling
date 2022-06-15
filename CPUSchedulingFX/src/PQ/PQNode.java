package PQ;

public class PQNode<T> {
	public T data;
	public int priority;
	public String name;
	public PQNode<T> next;
	
	public PQNode() {
		next = null;
	}
	
	public PQNode(T e, int pri, String name) {
		data = e;
		priority = pri;
		this.name = name;
		
	}

}
