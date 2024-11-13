package de.honoka.test.various.old;

import de.honoka.sdk.util.code.ColorfulText;
import de.honoka.sdk.util.code.HonokaComparator;
import de.honoka.sdk.util.file.FileUtils;
import de.honoka.sdk.util.text.TextUtils;
import de.honoka.sdk.util.various.Retrier;
import lombok.SneakyThrows;

import java.net.URI;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class AllTestBackup {

    //@Test
    public void test19() {
        List<String> list = Arrays.asList("a", "b", "a", "b", "c", "a");
        List<Map.Entry<String, List<String>>> collect = list.stream().collect(
                Collectors.groupingBy(s -> s)).entrySet().stream()
                .sorted(((HonokaComparator<Map.Entry<String, List<String>>>)
                        (o1, o2) -> {
                            return o1.getValue().size() > o2.getValue().size() ?
                                    o1 : o2;
                        }).desc())
                .collect(Collectors.toList());
        for(Map.Entry<String, List<String>> entry : collect) {
            System.out.printf("%s\t%d\n", entry.getKey(),
                    entry.getValue().size());
        }
    }

    //@Test
    @SuppressWarnings("ALL")
    public void test18() {
        String str = new String("a") + new String("b");
        String str2 = "a" + "b";
        str.intern();
        System.out.println(str == "ab");
    }

    //@Test
    public void test17() {
        System.out.println(new RuntimeException("this is a de.honoka.test.various.test").toString());
    }

    //@Test
    public void test16() {
        List<String> list = Arrays.asList(
                "a", "b", "c", "d", "e"
        );
        Stream<String> stream = list.stream();
        System.out.println(stream.anyMatch(s -> s.equals("B")));
    }

    //@Test
    public void test15() {
        ColorfulText.of().purple("■ ").aqua("■ ").black("■ ").blue("■ ")
                .green("■ ").lightAqua("■ ").lightBlue("■ ").lightPurple("■ ")
                .yellow("■ ").pink("■ ").red("■ ").white("■ ").darkYellow("■ ")
                .println();
    }

    //@Test
    public void test14() {
        String[] names = {
                "de.honoka.de.honoka.test.various.test.A",
                "de.honoka.de.honoka.test.various.test.pa1.A",
                "de.honoka.de.honoka.test.various.test.a.ADao",
                "de.honoka.de.honoka.test.various.test.BDao",
                "de.honoka.de.honoka.test.various.test.a.BDaoA"
        };
        String pointcutStr = "de.honoka.de.honoka.test.various.test.*.*Dao";
        String regex = pointcutStr
                .replace(".", "\\.")
                .replace("*", "[A-Za-z0-9_]+");
        for(String s : names) {
            if(Pattern.matches(regex, s))
                System.out.println(s);
            else
                System.err.println(s);
        }
    }

    //@Test
    public void test13() {
        new Retrier(Exception.class).tryCode(() -> {
            Random ra = new Random();
            switch (ra.nextInt(5)) {
                //case 0:
                //	System.out.println("Throwable");
                //	throw new Throwable();
                case 1:
                    System.out.println("Exception");
                    throw new Exception();
                case 2:
                    System.out.println("RuntimeException");
                    throw new RuntimeException();
                case 3:
                    System.out.println("IllegalArgumentException");
                    throw new IllegalArgumentException();
                default:
                    System.out.println("Done!");
            }
            return null;
        });
    }

    //@Test
    public void test12() {
        Set<Integer> set = new HashSet<>(Arrays.asList(
                1, 2, 3, 1, 2, 3, 4, 5
        ));
        set.forEach(System.out::println);
    }

    //@Test
    public void test11() {
        System.out.println("中文测试ABCDE12345■");
        for(int i = 10; i <= 120; i++) {
            String template = "\u001B[%sm中文测试ABCDE12345■\u001B[0m";
            System.out.println(i + "\t" + String.format(template, i));
        }
    }

    //@Test
    public void test10() {
        List<String> list = Arrays.asList("中文", "123");
        System.out.println(list.contains("中文"));
        System.out.println(list.contains(new String("中文".getBytes())));
    }

    //@Test
    public void test9() {
        //System.out.println('\033' == '\u001b');  //always true
        String str = "\u001B[32m :: Spring Boot :: \u001B[39m      \u001B[2m (v2.3.5.RELEASE)\u001B[0;39m";
        System.out.println("---------------------------");
        String[] parts = str.split("\\u001B");
        List<String> list = Arrays.asList(parts);
        list.forEach(System.out::println);
        System.out.println("---------------------------");
        for(int i = 1; i < list.size(); i++) {
            list.set(i, "\u001B" + list.get(i));
        }
        list.forEach(System.out::print);
        System.out.println();
        str = str.replaceAll("\\u001B\\[[0-9;]+?m", "");
        System.out.println(str);
    }

    //@Test
    public void test8() {
        String relativePath = "xxxxx";
        String classpath = FileUtils.getMainClasspath();
        String path = Path.of(classpath, relativePath).toString();
        System.out.println(classpath);
        System.out.println(path);
    }

    //@Test
    @SneakyThrows
    public void test7() {
        List<Path> paths = List.of(
                Path.of(new URI("xxxxxxxxxx")),
                Path.of("D:\\dir", "one", "two.dat"),
                Path.of("D:\\dir\\", "one", "two.dat"),
                Path.of("usr", "local", "bin"),
                Path.of(FileUtils.getMainClasspath(), "one", "two")
        );
        paths.forEach(p -> System.out.println(p.toString()));
    }

    //@Test
    public void test6() {
        class MyClass<T> {
            public void print(T obj) {
                System.out.println(obj);
            }

            public T get() {
                return null;
            }
        }
        MyClass<?> obj = new MyClass<>();
        //obj.print(new Object());
        MyClass<? super RuntimeException> obj2 = new MyClass<>();
        //obj2.print(new Exception());
        Object o = obj2.get();
        //obj2.print(o);
    }

    //@Test
    public void test5() {
        List<Exception> list = new ArrayList<>();
        try {
            for(; ; ) {
                list.add(new Exception());
                if (list.size() % 10000 == 0)
                    System.out.println(list.size());
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.err.println(list.size());
        }
    }

    //@Test
    public void test4() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        int dom = cal.get(Calendar.DAY_OF_MONTH);
        int doy = cal.get(Calendar.DAY_OF_YEAR);

        System.out.println("当期时间: " + cal.getTime());
        System.out.println("日期: " + day);
        System.out.println("月份: " + month);
        System.out.println("年份: " + year);
        // 星期日为一周的第一天输出为 1，星期一输出为 2，以此类推
        System.out.println("一周的第几天: " + dow);
        System.out.println("一月中的第几天: " + dom);
        System.out.println("一年的第几天: " + doy);
    }

    //@Test
    public void test3() {
        Calendar cal = Calendar.getInstance();
        Date d = new Date();
        cal.setTime(d);
        //cal.setTimeZone();
        DateFormat df = TextUtils.getSimpleDateFormat("normal");
        System.out.println(df.format(d));
        System.out.println(df.format(cal.getTime()));
        System.out.println(cal.get(Calendar.MONTH));
        //System.out.println(String.format("%s-%s-%s %s:%s:%s",
        //		d.getYear(), d.getMonth(), d.getDate(),
        //		d.getHours(), d.getMinutes(), d.getSeconds()));
    }
}
