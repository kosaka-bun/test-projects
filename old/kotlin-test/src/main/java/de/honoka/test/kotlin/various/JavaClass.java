package de.honoka.test.kotlin.various;

import org.junit.Test;

public class JavaClass {
	
	@Test
	public void test1() {
		Class1 obj = new Class1();
		Class1.Companion.say();
		Class1.jvmStaticMethod();
		obj.dynamicMethod("hey!");
		System.out.println(Class1.staticField);
		obj.inlineMethod(integer -> integer);
		new Thread().interrupt();
		//obj.bindedMethodInClass("");
		//float f = 1.0F;
	}
}