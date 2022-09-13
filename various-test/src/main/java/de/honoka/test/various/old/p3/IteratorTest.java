package de.honoka.test.various.old.p3;

import java.util.Iterator;
import java.util.List;

public class IteratorTest {
	
	static class MyList implements Iterable<String> {
		
		List<String> list;
		
		public MyList(List<String> list) {
			this.list = list;
		}
		
		@Override
		public Iterator<String> iterator() {
			return new Iterator<>() {
				int pointer = -1;
				
				@Override
				public boolean hasNext() {
					return pointer < list.size() - 1;
				}
				
				@Override
				public String next() {
					pointer++;
					return list.get(pointer);
				}
			};
		}
	}
	
	public static void main(String[] args) {
		MyList myList = new MyList(List.of("a", "b", "c", "d", "e"));
		for(String s : myList) {
			System.out.println(s);
		}
	}
}
