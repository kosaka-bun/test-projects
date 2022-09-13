package de.honoka.test.various.old.p2;

import org.junit.Test;

import java.lang.reflect.Method;

public class AnnotationTest {

	@Command("命令1")
	private void method1() {
		System.out.println("命令1");
	}
	
	@Command("命令2")
	private void method2(String arg) {
		System.out.println("命令2：" + arg);
	}
	
	//反射测试，获取方法上的注解
	@Test
	void main() throws Exception {
		//获取用于反射操作本类的对象
		Class thisClass = Class.forName("de.honoka.test.various.old.p2.AnnotationTest");
		//获取实例
		AnnotationTest instance = (AnnotationTest) thisClass.newInstance();
		//getMethod获取本类的方法，必须是public的，否则找不到此方法
		/*Method m1 = thisClass.getMethod("method1");
		Method m2 = thisClass.getMethod("method2", String.class);*/
		//getDeclaredMethod获取方法，可以获取到任何权限的方法
		Method m1 = thisClass.getDeclaredMethod("method1");
		Method m2 = thisClass.getDeclaredMethod("method2", String.class);
		//获取私有权限，若在本类中使用本类的实例，可以不用获取
		/*m1.setAccessible(true);
		m2.setAccessible(true);*/
		//调用本类某个实例的某个方法
		m1.invoke(instance);	//调用instance实例的m1对应的方法
		m2.invoke(instance, "进入方法");	//调用方法并传递参数
		
		Command c1 = m1.getAnnotation(Command.class);
		if(c1 != null) System.out.println("注解：" + c1.value());
		Command c2 = m2.getAnnotation(Command.class);
		if(c2 != null) System.out.println("注解：" + c2.value());
	}
	
}

