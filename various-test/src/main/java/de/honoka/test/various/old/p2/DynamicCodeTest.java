package de.honoka.test.various.old.p2;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DynamicCodeTest {
	
	@Test
	public void main() throws Exception {
		Tester t;
		//t = new Tester();
		//反射突破私有方法与私有构造器
		Class<Tester> cla = Tester.class;
		Constructor<Tester> con = cla.getDeclaredConstructor();
		con.setAccessible(true);	//突破权限
		t = con.newInstance();	//调用构造器
		//t.method1();
		Method m = cla.getDeclaredMethod("method1", int.class);
		m.setAccessible(true);	//突破权限
		m.invoke(t, 10);	//调用方法
		
		//获取、篡改私有属性
		Field f = cla.getDeclaredField("attr1");
		f.setAccessible(true);
		int fValue = (int) f.get(t);
		System.out.printf("attr1：%d\n", fValue);
		f.set(t, 20);
		fValue = (int) f.get(t);
		System.out.printf("attr1：%d\n", fValue);
	}
}

class Tester {
	
	private int attr1 = 10;
	
	static {
		System.out.println("静态代码块");
	}
	
	{
		System.out.println("动态代码块");
	}
	
	private Tester() {
		System.out.println("构造方法");
	}
	
	private void method1(int arg) {
		System.out.printf("方法1：%d\n", arg);
	}
}
