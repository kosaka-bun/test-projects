#include <iostream>
#include <windows.h>
#include "de_honoka_test_jni_JniTest.h"

#using "TestDll.dll"
using namespace TestDll;

void showWindow() {
	MessageBoxA(0, "DLL加载成功！", "JNI测试", MB_OK);
    Test ^test = gcnew Test();
    test->start();
}

JNIEXPORT void JNICALL Java_de_honoka_test_jni_JniTest_start
(JNIEnv* env, jobject jo) {
    showWindow();
}