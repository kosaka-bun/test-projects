package de.honoka.test.various.test;

import org.junit.Test;

/**
 * 本类不可移动，本地方法的路径已经固定在动态库中，如移动此类，该本地方法的路径将和动态库中
 * 所包含的本地方法名称与签名相同而路径不同，无法成功映射本地方法
 */
public class JniTest {
	
	static {
		//使用loadLibrary加载库时，不能加文件后缀，而使用load方法则需要添加
		System.loadLibrary("./target/classes/JniTest/jni-test64");
	}
	
	public void messageInJava(String arg) {
		System.out.printf("【Java printf()】%s\n", arg);
	}
	
	//javac -h . JniTest.java -encoding utf-8
	public native void messageInC(String arg);
	
	@Test
	public void main() {
		messageInC("hello");
	}
}
