package de.honoka.test.various.old.p1;

public class InterfaceTest {
	
	public static void main(String[] args) {
		//将一个已实现了接口的类的实例传给接口变量，这个接口变量下的成员方法，就对应了实例的这两个方法
		MyInterface mi = new MyInterfaceImpl();
		mi.method1();
		mi.method2();
	}
}

interface MyInterface {
	void method1();
	void method2();
}

class MyInterfaceImpl implements MyInterface {
	@Override
	public void method1() {
		System.out.println("method1");
	}
	@Override
	public void method2() {
		System.out.println("method2");
	}
}