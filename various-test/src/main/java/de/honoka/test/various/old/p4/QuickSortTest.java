package de.honoka.test.various.old.p4;

import de.honoka.sdk.util.basic.ColorfulText;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuickSortTest {
	
	@Test
	public void main() {
		Random ra = new Random();
		for(int i = 0; i < 10; i++) {
			List<Integer> list = new ArrayList<>();
			for(int i1 = 0; i1 < 20; i1++) {
				list.add(ra.nextInt(101));
			}
			System.out.println(list);
			quickSort(list);
			ColorfulText.of().green(list).println();
		}
	}
	
	void quickSort(List<Integer> ints) {
		quickSort(ints, 0, ints.size() - 1);
	}
	
	void quickSort(List<Integer> ints, int start, int end) {
		if(end - start <= 0) return;
		int start0 = start, end0 = end;
		boolean rightToLeft = true;
		while(end > start) {
			if(ints.get(end) < ints.get(start)) {
				int temp = ints.get(start);
				ints.set(start, ints.get(end));
				ints.set(end, temp);
				rightToLeft = !rightToLeft;
				continue;
			}
			if(rightToLeft) end--;
			else start++;
		}
		if(start > start0) quickSort(ints, start0, start - 1);
		if(end < end0) quickSort(ints, end + 1, end0);
	}
}
