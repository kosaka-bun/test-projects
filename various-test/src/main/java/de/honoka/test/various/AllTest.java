package de.honoka.test.various;

import de.honoka.sdk.util.code.CodeUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class AllTest {

    @Test
    public void test24() {
        URL resource = AllTest.class.getResource("/CHeader/utf8_to_gbk.h");
        System.out.println(resource);
        URL resource2 = CodeUtils.class.getResource("/text.html");
        System.out.println(resource2);
        assert resource2 != null;
        System.out.println(de.honoka.sdk.util.file.FileUtils.urlToString(resource2));
    }

    @SneakyThrows
    public void test23() {
        try(RandomAccessFile file = new RandomAccessFile(
                "xxxxxxxxxxxxx", "r")) {
            FileChannel channel = file.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = channel.read(byteBuffer);
            while(read != -1) {
                byteBuffer.flip();
                System.out.println(read);
                byteBuffer.clear();
                read = channel.read(byteBuffer);
            }
            System.out.println("end");
        }
    }

    //@Test
    @SneakyThrows
    public void test22() {
        try(FileOutputStream os = FileUtils.openOutputStream(
                new File("xxxxx"))) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("test");
            writer.close();
        }
    }

    //@Test
    public void test21() {
        System.out.println("\u6d4b\u8bd5");
        Pattern pattern = Pattern.compile("\\u6d4b\\u8bd5");
        Matcher matcher = pattern.matcher("测试文本123");
        if(matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }

    //@Test
    public void test20() {
        String[] arr = { "a", "b", "c", "d", "e" };
        for(String s : ArrayUtils.subarray(arr, 2, arr.length)) {
            System.out.println(s);
        }
    }
}
