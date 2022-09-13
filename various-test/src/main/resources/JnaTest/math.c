#include <stdio.h>
#include <windows.h>
#include <string.h>
#include "../CHeader/utf8_to_gbk.h"

//为了防止UTF-8编码的c文件在Windows运行时出现乱码，需使用下面的编译命令，指定文件编码与运行编码
//gcc -fexec-charset=GBK -finput-charset=UTF-8 -m64 -shared -o math64.dll math.c

int plus(int n1, int n2) {
	return n1 + n2;
}

int minus(int n1, int n2) {
    return n1 - n2;
}

int multiply(int n1, int n2) {
    return n1 * n2;
}

double divide(int n1, int n2) {
    return n1 / (n2 * 1.0);
}

void message() {
	MessageBoxA(0, utf8ToGbk("DLL加载成功！"), utf8ToGbk("math链接库"), MB_OK);
	printf("DLL加载成功！");
}

int main() {
	message();
	printf("请按任意键继续...");
	getchar();
	return 0;
}