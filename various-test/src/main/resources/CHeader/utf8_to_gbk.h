#ifndef UTF8_TO_GBK_H
#define UTF8_TO_GBK_H

#include <windows.h>

//包含windows.h，可以将UTF-8编码的字符串转换为GBK编码的字符串
//以解决Windows下UTF-8字符串显示乱码的问题
char *utf8ToGbk(const char *utf8String) {
	wchar_t *unicodeStr = NULL;
	int nRetLen = 0;
	//求需求的宽字符数大小
	nRetLen = MultiByteToWideChar(CP_UTF8, 0, utf8String, -1, NULL, 0);
	//将utf-8编码转换成unicode编码
	unicodeStr = (wchar_t *)malloc(nRetLen * sizeof(wchar_t));
	nRetLen = MultiByteToWideChar(CP_UTF8, 0, utf8String, -1, unicodeStr, nRetLen);
	//求unicode转换到gbk所需字符数
	nRetLen = WideCharToMultiByte(CP_ACP, 0, unicodeStr, -1, NULL, 0, NULL, 0);
	char *gbkString = (char *)malloc(nRetLen * sizeof(char));
	//unicode编码转换成gbk编码
	nRetLen = WideCharToMultiByte(CP_ACP, 0, unicodeStr, -1, gbkString, nRetLen, NULL, 0);
	//释放内存
	free(unicodeStr);
	return gbkString;
}

#endif