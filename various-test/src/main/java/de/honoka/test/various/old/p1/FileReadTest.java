package de.honoka.test.various.old.p1;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReadTest {
	
	public static void main(String[] args) throws Exception {
		String path = "xxxxxxxxxx";
		printFile(path);
	}
	
	static void printFile(String path) throws Exception {
		int flag = 0;
		//byte[] bytes = fis.readAllBytes();	//readAllBytes()仅在Java9以上支持
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		for(byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b).toUpperCase();
			if(hex.length() == 1) hex = "0" + hex;
			System.out.print(hex + "  ");
			flag++;
			if(flag >= 20) {
				flag = 0;
				System.out.println();
			}
		}
	}
}
