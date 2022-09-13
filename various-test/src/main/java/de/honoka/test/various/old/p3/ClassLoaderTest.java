package de.honoka.test.various.old.p3;

import org.junit.Test;

public class ClassLoaderTest {
	
	private static class ClassA {
		
		public static void say() {
			System.out.println("ClassA");
		}
	}
	
	private static class ClassB {
		
		public static void say() {
			System.out.println("ClassB");
		}
	}
	
	@Test
	public void test() {
		ClassA.say();
		ClassLoader classLoader = ClassA.class.getClassLoader();
		System.out.println(classLoader.getName());
	}
}
