#include "test_JNITest.h"
#include <stdio.h>
#include <windows.h>
#include "../CHeader/utf8_to_gbk.h"

/*
 * gcc -fexec-charset=UTF-8 -finput-charset=UTF-8 -m64 -shared -o jni-test64.dll ^
 * jni-test.c -I "%JAVA_HOME%\include" -I "%JAVA_HOME%\include\win32"
 */

void callJavaMethod(JNIEnv *);

//jobj表示本地方法所属的对象，其后的其他参数表示本地方法的参数
JNIEXPORT void JNICALL Java_de_honoka_test_various_test_JNITest_messageInC(JNIEnv *env,
        jobject jobj, jstring jstr) {
    MessageBoxA(0, utf8ToGbk("DLL加载成功！"), utf8ToGbk("JNI测试"), MB_OK);
    char *str = (char *)(*env)->GetStringUTFChars(env, jstr, 0);
    printf("【C语言 printf()】传入的字符串为：%s\n", str);
    callJavaMethod(env);
}

//调用Java动态方法
void callJavaMethod(JNIEnv *env) {
	//加载指定包的类
	jclass c_JNITest = (*env)->FindClass(env, "de/honoka/test/various/test/JNITest");
	if(c_JNITest == NULL) {
		printf("Error: c_JNITest");
		return;
	}
	//构造方法
	jmethodID m_construct = (*env)->GetMethodID(env, c_JNITest, "<init>", "()V");
	if(m_construct == NULL) {
		printf("Error: m_construct");
		return;
	}
	//获取要调用的方法
	/* 获取名为messageInJava，参数为(String)，返回值为void的方法
	   此时，void messageInJava(String arg)的签名应写作：(Ljava/lang/String;)V
	   括号内填方法参数，参数为Java类的，用L加类的完全限定名加分号表示，括号右侧填写返回值
	   类型 */
	jmethodID m_messageInJava = (*env)->GetMethodID(env, c_JNITest, "messageInJava", "(Ljava/lang/String;)V");
	if(m_messageInJava == NULL) {
		printf("Error: m_messageInJava");
		return;
	}
	//使用构造方法实例化
	jobject o_JNITest = (*env)->NewObject(env, c_JNITest, m_construct);
	if(o_JNITest == NULL) {
		printf("Error: o_JNITest");
		return;
	}
	//调用方法
	jstring arg = (*env)->NewStringUTF(env, "尝试用C语言调用Java中的方法，这是C语言生成的jstring字符串，作为参数传入，hello！");
	(*env)->CallVoidMethod(env, o_JNITest, m_messageInJava, arg);
	//释放资源
	(*env)->DeleteLocalRef(env, o_JNITest);
    (*env)->DeleteLocalRef(env, c_JNITest);
}