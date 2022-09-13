package de.honoka.test.various.old.p2;

import org.junit.Test;

import java.util.*;

public class GenericsTest {
	
	@Test
	public void main() {
		List<Integer> list = new MyLinkedList<>();
		Integer[] arr = new Integer[] {
				1, 3, 5, 7, 9, 2, 4, 6, 8, null, 10
		};
		Collections.addAll(list, arr);
		System.out.println("init: " + list);
		String[] results = new String[] {
				"size: " + list.size(),
				"isEmpty(new): " + new MyLinkedList<>().isEmpty(),
				"isEmpty: " + list.isEmpty(),
				"contains: " + list.contains(5),
				"contains: " + list.contains(0),
				"toArray: " + list.toArray().length,
				"add: " + list.add(12) + "\n" + list,
				"remove: " + list.remove((Object) 10) + "\n" + list
		};
		for(String s : results) {
			System.out.println(s);
		}
	}
}

@SuppressWarnings("unchecked")
class MyLinkedList<E> implements List<E> {
	
	private Node head = null;
	
	@Override
	public int size() {
		Node n = head;
		int i = 0;
		while (n != null) {
			i++;
			n = n.next;
		}
		return i;
	}
	
	@Override
	public boolean isEmpty() {
		return head == null;
	}
	
	@Override
	public boolean contains(Object o) {
		Node n = head;
		while (n != null) {
			if(n.value == o) return true;
			n = n.next;
		}
		return false;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new MyIterator<>();
	}
	
	@Override
	public Object[] toArray() {
		Object[] arr = new Object[size()];
		Node n = head;
		for(int i = 0; i < size(); i++) {
			arr[i] = n.value;
			n = n.next;
		}
		return arr;
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		Node n = head;
		for(int i = 0; i < a.length; i++) {
			a[i] = (T) n.value;
			n = n.next;
		}
		return a;
	}
	
	@Override
	public boolean add(E e) {
		try {
			Node n = head, last = null;
			while (n != null) {
				last = n;
				n = n.next;
			}
			Node newNode = new Node();
			newNode.value = e;
			newNode.prev = last;
			newNode.next = null;
			if(last != null) last.next = newNode;
			else head = newNode;
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	@Override
	public boolean remove(Object o) {
		try {
			Node n = head;
			while (n != null) {
				if(n.value == o) {
					if(n.prev != null)
						n.prev.next = n.next;
					if(n.next != null)
						n.next.prev = n.prev;
					//break;
				}
				n = n.next;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c) {
			if(!contains(o)) return false;
		}
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		try {
			for(E e: c) {
				add(e);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}
	
	@Override
	public void clear() {
	
	}
	
	@Override
	public E get(int index) {
		return null;
	}
	
	@Override
	public E set(int index, E element) {
		return null;
	}
	
	@Override
	public void add(int index, E element) {
	
	}
	
	@Override
	public E remove(int index) {
		return null;
	}
	
	@Override
	public int indexOf(Object o) {
		return 0;
	}
	
	@Override
	public int lastIndexOf(Object o) {
		return 0;
	}
	
	@Override
	public ListIterator<E> listIterator() {
		return null;
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return null;
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return null;
	}
	
	private class Node {
		private E value;
		private Node prev, next;
	}
	
	private class MyIterator<T> implements Iterator<T> {
		
		private Node now = null;
		
		@Override
		public boolean hasNext() {
			if(now == null) return head != null;
			return now.next != null;
		}
		
		@Override
		public T next() {
			if(now == null) now = head;
			else now = now.next;
			return (T) now.value;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(E e : this) {
			sb.append(e).append(" ");
		}
		return sb.toString();
	}
}