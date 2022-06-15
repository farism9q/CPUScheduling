package PQ;

public class PQElement<T> {
	public T data;
	public int priority;
	public String name;
	
	public PQElement(T val, int pri, String name) {
		data = val;
		priority = pri;
		this.name = name;
	}
	

}
