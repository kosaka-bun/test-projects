package de.honoka.test.various.old.p2;

import org.junit.Test;

public class InterfaceTest {
	
	@Test
	public void main() {
		TestClass tc = new TestClass();
		System.out.println(TestClass.str);
		tc.say();
	}
	
	static class TestClass implements TestInterfaceA, TestInterfaceB {
		//类中定义的静态成员可以覆盖实现的接口中定义的静态成员
		static String str = "static string in class";
		
		void say() {
			/* 在类中可以直接引用实现的接口中定义的成员，但如果类中与接口中有重名的静态成员，
			不指明类名调用同名静态成员时，仅调用类中的静态成员。
			实现多个接口中有同名成员时，类不再继承任何一个同名的成员，调用这些成员必须显式指
			明类名。 */
			System.out.println(TestInterfaceA.str);
		}
		
		//实现的两个接口中若有方法名相同而返回值不同的方法定义，则这两个接口不可同时实现
	}
	
	interface TestInterfaceA {
		//接口中定义的成员一定是静态的，类实现此接口后，自动继承此静态成员
		String str = "static string A";
		//void interSay();
	}
	
	interface TestInterfaceB {
		String str = "static string B";
		//String interSay();
	}
}
