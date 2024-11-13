package de.honoka.test.various;

import cn.hutool.core.map.MapUtil;
import de.honoka.sdk.util.code.CodeUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@SuppressWarnings({ "unused", "unchecked" })
public class AllTest {

    @Test
    public void test27() {
        Object map = MapUtil.of(new Object[][] {
            { 1, 1 },
            { 2, 2 }
        });
        Map<Integer, Integer> map2 = (Map<Integer, Integer>) map;
        for(Map.Entry<Integer, Integer> entry : map2.entrySet()) {
            System.out.println((entry.getKey() + 1) + "\t" + (entry.getValue() + 1));
        }
    }

    @Test
    public void test26() {
        List<Integer> list = Stream.of('A', 'c', '1', 'p', '*').map(c -> (int) c).toList();
        list = new ArrayList<>(list);
        Collections.sort(list);
        List<Character> chars = list.stream().map(i -> (char) i.intValue()).toList();
        System.out.println(chars);
    }

    //@Test
    @SneakyThrows
    public void test25() {
        byte[] utf8 = "\ufefftest��".getBytes(StandardCharsets.UTF_8);
        System.out.println(new String(utf8, "GBK"));
    }

    //@Test
    public void test24() {
        URL resource = AllTest.class.getResource("/CHeader/utf8_to_gbk.h");
        System.out.println(resource);
        URL resource2 = CodeUtils.class.getResource("/text.html");
        System.out.println(resource2);
        assert resource2 != null;
        //System.out.println(de.honoka.sdk.util.file.FileUtils.urlToString(resource2));
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
    @SuppressWarnings("UnnecessaryUnicodeEscape")
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
