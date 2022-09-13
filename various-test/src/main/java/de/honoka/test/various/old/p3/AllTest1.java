package de.honoka.test.various.old.p3;

import com.sobte.cqp.jcq.message.ActionCode;
import com.sobte.cqp.jcq.message.CoolQCode;
import de.honoka.test.various.util.EmojiHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sobte.cqp.jcq.event.JcqApp.CC;

public class AllTest1 {
	
	//@Test
	public void coolQCodeTest() {
		String msg = "文本1" + CC.at(123) + "文本2" +
				CC.image("~`!@#$%^&*()_+-={};';'\\:\"|,./<>?") + "文本3";
		System.out.println(msg + "\n");
		List<String> multiPart = new ArrayList<>();
		while (msg.contains("[CQ:")) {
			int cqCodeStart = msg.indexOf("[CQ:");
			int cqCodeEnd = msg.indexOf("]", cqCodeStart) + 1;
			multiPart.add(msg.substring(0, cqCodeStart));
			multiPart.add(msg.substring(cqCodeStart, cqCodeEnd));
			msg = msg.substring(cqCodeEnd);
		}
		multiPart.add(msg);
		multiPart.forEach(System.out::println);
		System.out.println();
		for(String part : multiPart) {
			if(!part.contains("[CQ:")) continue;
			CoolQCode cqCode = CC.analysis(part);
			ActionCode ac = cqCode.get(0);
			System.out.println(ac.getAction());
			System.out.println(ac.get("file"));
		}
	}
	
	//@Test
	public void cityWheatherQueryTest() throws Exception {
		String url = "http://www.weather.com.cn/weather/101010100.shtml";
		Document doc = Jsoup.connect(url).get();
		Element weatherTag = doc.selectFirst("#7d > ul > li")
				.selectFirst("p.wea");
		System.out.println(weatherTag.text());
	}
	
	//@Test
	public void getWheatherUrlTest() throws Exception {
		String[] urls = {
				"http://www.weather.com.cn/textFC/hb.shtml",
				"http://www.weather.com.cn/textFC/db.shtml",
				"http://www.weather.com.cn/textFC/hd.shtml",
				"http://www.weather.com.cn/textFC/hz.shtml",
				"http://www.weather.com.cn/textFC/hn.shtml",
				"http://www.weather.com.cn/textFC/xb.shtml",
				"http://www.weather.com.cn/textFC/xn.shtml",
				"http://www.weather.com.cn/textFC/gat.shtml"
		};
		String[] location = {"xx", "xx"};
		for(String url : urls) {
			Document doc = Jsoup.connect(url).timeout(20 * 1000).get();
			Elements provinceTags = doc.selectFirst("div.hanml > div.conMidtab")
					.select("div > table > tbody");
			for(Element provinceTag : provinceTags) {
				Elements cityTags = provinceTag.select("tr");
				String province = cityTags.get(2).selectFirst("td")
						.select("a").text();
				if(!province.equals(location[0])) continue;
				for(int i = 2; i < cityTags.size(); i++) {
					Element cityTag = cityTags.get(i);
					String city;
					Elements cols = cityTag.select("td");
					int dataIndex = 0;
					if(i == 2) dataIndex++;
					city = cols.get(dataIndex).selectFirst("a").text();
					if(!city.equals(location[1])) continue;
					System.out.println(cols.get(dataIndex).selectFirst("a").attr("href"));
					return;
				}
			}
		}
	}
	
	//@Test
	public void wheatherQueryTest() throws Exception {
		String url = "http://www.weather.com.cn/textFC/hb.shtml";
		Document doc = Jsoup.connect(url).get();
		Elements provinceTags = doc.selectFirst("div.hanml > div.conMidtab")
				.select("div > table > tbody");
		for(Element provinceTag : provinceTags) {
			Elements cityTags = provinceTag.select("tr");
			String province = cityTags.get(2).selectFirst("td")
					.select("a").text();
			System.out.println(province);
			for(int i = 2; i < cityTags.size(); i++) {
				Element cityTag = cityTags.get(i);
				String city, wheather;
				Elements cols = cityTag.select("td");
				int dataIndex = 0;
				if(i == 2) dataIndex++;
				city = cols.get(dataIndex).selectFirst("a").text();
				wheather = cols.get(dataIndex + 4).text();
				System.out.println(city + "\t" + wheather);
			}
			System.out.println();
		}
	}
	
	public static String[] ipLocationQuery0(String ip) {
		String urlBase = "http://ip.tool.chinaz.com/%s";
		String url = String.format(urlBase, ip);
		try {
			Document doc = Jsoup.connect(url).get();
			Element locationTag = doc.select("div.WhoIpWrap.jspu > p").get(1)
					.select("span").last();
			String location = locationTag.text().split(" ")[0];
			if(location.contains("国") && location.contains("省")) {
				if(location.indexOf("国") < location.indexOf("省"))
					location = location.substring(location.indexOf("国") + 1);
			}
			return new String[] {
					location
			};
		} catch (Exception e) {
			return null;
		}
	}
	
	//@Test
	public void stringBuilderTest() {
		long start, end;
		String result;
		//初始化数据
		String[] args = new String[100_0000];
		for(int i = 0; i < args.length; i++) {
			args[i] = String.valueOf(i);
		}
		//通过Builder添加
		start = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		for(String s : args) {
			sb.append(s);
			sb.append(File.separator);
		}
		result = sb.toString();
		end = System.currentTimeMillis();
		//System.out.println(result);
		double timeOfBuilder = (end - start) / 1000.0;
		
		//直接添加
		start = System.currentTimeMillis();
		String str = "";
		int count = 0;
		double percent = 0;
		for(String s : args) {
			str += s;
			str += File.separator;
			count++;
			if(count >= args.length / 10000) {
				count = 0;
				percent += 0.01;
				System.out.printf("%.2f%%\n", percent);
			}
		}
		//result = str;
		end = System.currentTimeMillis();
		//System.out.println(result);
		
		//输出数据
		System.out.printf("通过Builder添加：%.2f秒\n", timeOfBuilder);
		System.out.printf("直接添加：%.2f秒\n", (end - start) / 1000.0);
	}
	
	//@Test
	public void autoCloseTest() throws Exception {
		class Util implements AutoCloseable {
			boolean open = true;
			
			@Override
			public void close() {
				open = false;
				System.out.println("已关闭：" + open);
				throw new NullPointerException();
			}
			
			Util() {
				System.out.println("已开启");
			}
		}
		
		try (Util u = new Util()) {
			System.out.println("Util对象开启状态：" + u.open);
			//测试异常时是否会关闭对象
			int i = 0 / 0;
		} catch (NullPointerException npe) {
			/* 两个方法均发生异常时，异常仅在捕获了try块异常的代码块中处理，不在捕获了close方法异
			   常的代码块中处理 */
			//此处仅在try未发生异常，close方法发生异常时被执行
			System.out.println("空指针异常");
		} catch (ArithmeticException ae) {
			//try块发生异常时，先关闭资源，再处理异常
			//try与close均发生异常时，将两个异常合成为一个异常后在此处理
			System.out.println("算数异常");
			Thread.sleep(2000);
			ae.printStackTrace();
		}
		
	}
	
	//@Test
	public void doubleColonTest() {
		String[] array = new String[] {
				"test1", "testA", "TESTB", "Test_symbol1"
		};
		List<String> list = Arrays.asList(array);
		//list.forEach(str -> System.out.println(str));
		list.forEach(System.out::println);
	}
	
	//@Test
	public void utf8ToGbk() throws Exception {
		byte[] bytes = "DLL加载成功！".getBytes(StandardCharsets.UTF_8);
		System.out.println(new String(bytes, "GBK"));
	}
	
	//读取指定URL的数据，另一种写法
	//@Test
	void test2_2() throws Exception {
		URL url = new URL("xxxxx");
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		StringBuilder strb = new StringBuilder();
		String line;
		while((line = br.readLine()) != null) {
			strb.append(line);
			strb.append("\n");
		}
		System.out.println(strb.toString());
	}
	
	//读取指定URL的数据
	//@Test
	void test2() throws Exception {
		URL url = new URL("xxxxxx");
		InputStream is = url.openStream();
		int size = is.available();
		byte[] bytes = new byte[size];
		for(int i = 0; i < size; i++) {
			bytes[i] = (byte) is.read();
		}
		String str = new String(bytes, StandardCharsets.UTF_8);
		System.out.println(str);
	}
	
	//@Test
	void test1() {
		URI uri = URI.create("xxxxxxx");
		System.out.println(uri.getQuery());
	}
	
	//@Test
	void emojiTest() {
		List<String> emojis = new ArrayList<>();
		emojis.add(EmojiHelper.unicodeToEmoji(127822));	//红苹果
		emojis.add(EmojiHelper.unicodeToEmoji(127823));	//绿苹果
		emojis.add(EmojiHelper.unicodeToEmoji(127815));	//葡萄
		emojis.add(EmojiHelper.unicodeToEmoji(127816));	//哈密瓜
		emojis.add(EmojiHelper.unicodeToEmoji(127825));	//桃子
		emojis.add(EmojiHelper.unicodeToEmoji(127834));	//米饭
		emojis.add(EmojiHelper.unicodeToEmoji(127818));	//橘子
		for(String s : emojis)
			System.out.println(s);
	}
}
