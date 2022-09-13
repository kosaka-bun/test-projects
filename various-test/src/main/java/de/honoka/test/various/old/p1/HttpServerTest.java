package de.honoka.test.various.old.p1;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仅仅用作实现，许多类与方法的用途尚不明确
 */
public class HttpServerTest {

	/*static {
		try {
			//建立9394端口HTTP服务器
			HttpServer server = HttpServer.create(new InetSocketAddress(9394), 0);
			//在该服务器下设定资源路径，访问此URI时执行代码，获取响应
			server.createContext("/getUsageLog", new de.honoka.test.various.old.TestHandler());
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public static void main(String[] args) throws Exception {
		//while (true) Thread.sleep(10 * 1000);
		int page;   //页码
		String query = URI.create("/a/b?id=2&qw=er").getQuery();
		Map<String, String> paras = TestHandler.queryStringToMap(query);
		try {
			page = Integer.parseInt(paras.getOrDefault("page", "1"));
		} catch (NumberFormatException e) {
			page = 1;
		}
	}
}

//处理类，访问上面的URI时，由本类来处理，写出响应
class TestHandler implements HttpHandler {
	/*//HttpExchange类中可能包含请求信息，发出请求的客户端信息等，一个对象对应一次请求-响应操作
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		//响应内容（html内容体或文件内容）
		//String response = "<p style='color: purple;'>Hello World</p>";
		String response = "{ \"text\":\"Hello World\" }";
		//响应码与响应长度
		exchange.sendResponseHeaders(200, 0);
		//响应头
		Headers h = exchange.getResponseHeaders();
		h.set("Content-Type", "application/json");
		//for(String s : h.keySet()) System.out.println(s);
		//获得响应体的输出流，通过输出流输出为html
		OutputStream os = exchange.getResponseBody();
		//写出字节信息
		os.write(response.getBytes());
		os.close();
		exchange.close();
	}*/
	//本次运行使用记录信息表，程序关闭后丢失
	private static List<UsageLog> list = new ArrayList<>();
	//HttpExchange类中可能包含请求信息，发出请求的客户端信息等，一个对象对应一次请求-响应操作
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		int page;   //页码
		String query = exchange.getRequestURI().getQuery();
		Map<String, String> paras = queryStringToMap(query);
		try {
			page = Integer.parseInt(paras.getOrDefault("page", "1"));
		} catch (NumberFormatException e) {
			page = 1;
		}
		//响应内容（html内容体或文件内容）
		String response = "123";//toJsonByPage(list, page);
		//响应码与响应长度
		exchange.sendResponseHeaders(200, 0);
		//响应头
		Headers h = exchange.getResponseHeaders();
		h.set("Content-Type", "application/json; charset=UTF-8");
		//获得响应体的输出流，通过输出流输出为html
		OutputStream os = exchange.getResponseBody();
		//写出字节信息
		os.write(response.getBytes());
		//关闭信息交换对象，同时关闭其中的输入输出流
		os.close();
		//exchange.close();
	}

	//在机器人处理信息后调用，记录使用记录
	static void logUsage(Timestamp time, String groupName, String username, long qq, String msg, String reply) {
		UsageLog u = new UsageLog();
		u.time = time;
		u.groupName = groupName;
		u.username = username;
		u.qq = qq;
		u.msg = msg;
		u.reply = reply;
		list.add(0, u); //添加到首条，整个表按时间顺序降序排列
	}

	//将List表转换为json字符串
	private static String toJsonByPage(List list, int page) {
		final int PAGE_SIZE = 20;   //web端每页显示的信息条数
		//计算最大页数
		int maxPage = list.size() / PAGE_SIZE;
		if(list.size() % PAGE_SIZE > 0) maxPage++;
		//修正不正确的页数
		if(page > maxPage) page = maxPage;
		//计算页面起始位置，截取列表内容
		int index = (page - 1) * PAGE_SIZE;
		List pageList = new ArrayList();
		for(int i = index; i < ((page < maxPage)?(index + PAGE_SIZE):list.size()); i++)
			pageList.add(list.get(i));
		//生成便于查看的json文件的gson操作对象
		//Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//返回本页信息列表
		//return gson.toJson(pageList);
		return null;
	}

	//使用记录信息类
	private static class UsageLog {
		//时间戳
		Timestamp time;
		//群名，群名片或昵称，执行的操作，处理的信息，回复的信息
		String groupName, username, msg, reply;
		//QQ号码
		long qq;
	}

	public static Map<String, String> queryStringToMap(String query) {
		Map<String, String> paraMap = new HashMap<String, String>();
		String[] parameters = query.split("&");
		for(String s : parameters) {
			String key = s.substring(0, s.indexOf("="));
			String value = s.substring(s.indexOf("=") + 1);
			paraMap.put(key, value);
		}
		return paraMap;
	}
}