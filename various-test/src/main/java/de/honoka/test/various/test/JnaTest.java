package de.honoka.test.various.test;

import com.sun.jna.Library;
import com.sun.jna.Native;
import org.junit.Test;

public class JnaTest {
	
	interface JnaLibrary extends Library {
		//加载resource目录下的dll文件
		//resource的相对路径为.（即程序运行的当前目录）
		//要加载的dll位数必须与JDK位数保持一致，64位JDK并不兼容32位dll
		JnaLibrary impl = Native.load("./JnaTest/math64.dll", JnaLibrary.class);
		int plus(int n1, int n2);
		int minus(int n1, int n2);
		int multiply(int n1, int n2);
		double divide(int n1, int n2);
		void message();
	}

	@Test
	public void test() {
		JnaLibrary jl = JnaLibrary.impl;
		jl.message();
		
		System.out.println(jl.plus(8, 3));
		System.out.println(jl.minus(8, 3));
		System.out.println(jl.multiply(8, 3));
		System.out.println(jl.divide(8, 3));
	}
}