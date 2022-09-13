package de.honoka.test.various.old.p4;

import de.honoka.sdk.util.code.ColorfulText;
import de.honoka.sdk.util.various.ReflectUtils;
import lombok.SneakyThrows;
import org.junit.Test;

public class ReflectTest {
	
	@SuppressWarnings("ALL")
	public static class ClassA {
		
		private String test = "test";
		
		private final String finalTest = "finalTest";
		
		private static String staticTest = "staticTest";
		
		private static final String finalStaticTest = "finalStaticTest";
		
		private int testM(int i1, int i2) {
			System.out.println("testM");
			return i1 + i2;
		}
		
		private void testM2() {
			System.out.println("testM2");
		}
		
		private static int staticTestM(int i1, int i2) {
			System.out.println("staticTestM");
			return i1 - i2;
		}
	}
	
	public static class ClassB extends ClassA {}
	
	//@Test
	@SneakyThrows
	public void test1() {
		ClassA obj = new ClassA();
		Class<?> clazz = ClassA.class;
		//de.honoka.test.various.test
		ColorfulText.of().white("de.honoka.test.various.test: ")
				.white(ReflectUtils.getFieldValue(obj, "test"))
				.println();
		ReflectUtils.setFieldValue(obj, "test", "changed");
		ColorfulText.of().white("de.honoka.test.various.test: ")
				.white(ReflectUtils.getFieldValue(obj, "test"))
				.println();
		//finalTest
		ColorfulText.of().white("finalTest: ")
				.white(ReflectUtils.getFieldValue(obj, "finalTest"))
				.println();
		ReflectUtils.setFieldValue(obj,
				"finalTest", "changed");
		ColorfulText.of().white("finalTest: ")
				.white(ReflectUtils.getFieldValue(obj, "finalTest"))
				.println();
		//staticTest
		ColorfulText.of().white("staticTest: ")
				.white(ReflectUtils.getFieldValue(clazz, "staticTest"))
				.println();
		ReflectUtils.setFieldValue(clazz, "staticTest", "changed");
		ColorfulText.of().white("staticTest: ")
				.white(ReflectUtils.getFieldValue(clazz, "staticTest"))
				.println();
		//finalStaticTest
		//只要get过了static final变量的值，那么就不能再set了，去掉final标识符也不行
		//必须在get之前就移除final修饰符，才能使get不影响之后的set操作
		ColorfulText.of().white("finalStaticTest: ")
				.white(ReflectUtils.getFieldValue(
						clazz, "finalStaticTest"))
				.println();
		ReflectUtils.setFieldValue(clazz,
				"finalStaticTest", "changed");
		ColorfulText.of().white("finalStaticTest: ")
				.white(ReflectUtils.getFieldValue(
						clazz, "finalStaticTest"))
				.println();
		//testM
		System.out.println("testM: " + ReflectUtils.invokeMethod(
				obj, "testM",
				new Class<?>[] { int.class, int.class },
				7, 1));
		//testM2
		ReflectUtils.invokeMethod(obj, "testM2");
		System.out.println("hashCode: " + ReflectUtils.invokeMethod(
				obj, "hashCode"));
		//staticTestM
		System.out.println("staticTestM: " + ReflectUtils.invokeMethod(
				clazz, "staticTestM",
				new Class<?>[] { int.class, int.class },
				7, 1));
	}
	
	@Test
	public void test2() {
		ClassB obj = new ClassB();
		System.out.println(ReflectUtils.getFieldValue(obj, "finalTest"));
		ReflectUtils.setFieldValue(obj, "finalTest", "changed");
		System.out.println(ReflectUtils.getFieldValue(obj, "finalTest"));
		System.out.println(ReflectUtils.getFieldValue(obj, "staticTest"));
		ReflectUtils.invokeMethod(obj, "testM2");
	}
}
