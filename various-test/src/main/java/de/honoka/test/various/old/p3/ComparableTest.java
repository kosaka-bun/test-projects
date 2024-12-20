package de.honoka.test.various.old.p3;

import de.honoka.sdk.util.basic.HonokaComparator;

import java.util.ArrayList;
import java.util.List;

public class ComparableTest {
	
	static class MyData {
		
		int data;
		
		static List<MyData> of(int... values) {
			List<MyData> list = new ArrayList<>();
			for(int i : values) {
				MyData myData = new MyData();
				myData.data = i;
				list.add(myData);
			}
			return list;
		}
		
		@Override
		public String toString() {
			return String.valueOf(data);
		}
	}
	
	public static void main(String[] args) {
		List<MyData> list = MyData.of(9, 7, 3, 8, 6, 1, 2, 8);
		System.out.println(list);
		HonokaComparator<MyData> comparator = (o1, o2) -> {
			return o1.data == o2.data ? null :
					o1.data > o2.data ? o1 : o2;
		};
		//Sorter.asc(list, comparator);
		list.sort(comparator);
		System.out.println(list);
		//Sorter.desc(list, comparator);
		list.sort(comparator.desc());
		System.out.println(list);
		//Sorter.asc(list.stream(), comparator)
		//		.forEach(d -> System.err.print(d.data + " "));
		list.stream().sorted(comparator)
				.forEach(d -> System.err.print(d + " "));
		System.err.println();
		//Sorter.desc(list.stream(), comparator)
		//		.forEach(d -> System.err.print(d.data + " "));
		list.stream().sorted(comparator.desc())
				.forEach(d -> System.err.print(d + " "));
		System.err.println();
		System.out.println(
				list.stream().sorted(comparator)
						.filter(i -> i.data >= 5)
						.findFirst()
						.orElse(null)
		);
		
	}
}
