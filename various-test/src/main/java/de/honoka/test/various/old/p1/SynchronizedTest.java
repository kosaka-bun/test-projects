package de.honoka.test.various.old.p1;

public class SynchronizedTest {
	
	public static void main(String[] args) throws Exception {
		Test t = new Test();
		Thread t1, t2;
		t1 = new Thread(() -> {
			System.out.println("线程1开始运行……");
			//Test.method1("线程1");
			t.method2("线程1");
		});
		t2 = new Thread(() -> {
			System.out.println("线程2开始运行……");
			//Test.method1("线程2");
			t.method2("线程2");
		});
		//线程1先调用加锁方法，线程2后调用
		t1.start();
		Thread.sleep(1000);
		t2.start();
		//线程1先调用了加锁方法，线程2调用加锁方法时，必须一直等待到线程1调用的方法返回，才能继续执行
	}
	
}

class Test {
	
	static synchronized void method1(String s) {
		System.out.println(s + "调用了静态方法");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("静态方法已返回");
	}
	
	synchronized void method2(String s) {
		System.out.println(s + "调用了动态方法");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("动态方法已返回");
	}
}
