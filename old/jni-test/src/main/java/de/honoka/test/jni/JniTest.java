package de.honoka.test.jni;

public class JniTest {
	
	static {
		System.loadLibrary("./target/classes/JniDll");
	}
	
	public native void start();
}
