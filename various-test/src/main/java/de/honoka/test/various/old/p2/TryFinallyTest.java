package de.honoka.test.various.old.p2;

import org.junit.Test;

//查看下面方法的执行步骤，理解没有catch的try-finally用法
public class TryFinallyTest {
	
	class Closable {
		boolean open = true;
		void close() {
			open = false;
		}
	}
	
	Closable c = null;
	
	@Test
	public void main() {
		try {
			test();		//1.调用方法
		} catch (Exception e) {		//4.捕获到方法产生的异常
			e.printStackTrace();
			System.out.println(c.open);		//5.检查finally块是否已执行
		}
	}
	
	void test() {
		try {
			c = new Closable();
			System.out.println(c.open);
			int exp = 2 / 0;	//2.此处出现异常
		} finally {
			//3.try块出现异常，但没有捕获异常，也会即刻执行finally块，然后将异常交给调用方处理
			//即便此异常，将会导致程序最终停止运行，也会在停止运行前，先将finally块执行完毕
			if(c != null) c.close();
		}
		//此后的语句不会执行
		//finally执行完成以后，即刻跳出方法，由调用方处理异常
		System.out.println("finally执行完成后的外侧");
	}
	
}
