package de.honoka.test.various.old.p2;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HtmlUnitTest {

    @Test
    public void test1() throws InterruptedException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setCssEnabled(false);
        //很重要，启用JS
        webClient.getOptions().setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        Map<String, String> headers = new HashMap<>(); //请求头参数
        //headers.put("Accept", "text/html,application/xhtml+xml," +
        //"application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");

        for(Map.Entry<String, String> entry : headers.entrySet()) {
            webClient.addRequestHeader(entry.getKey(), entry.getValue());
        }

        HtmlPage page = null;
        try {
            //尝试加载上面图片例子给出的网页
            page = webClient.getPage("xxxxxxxxxxxxx");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }
        //String html = page.getWebResponse().getContentAsString(StandardCharsets.UTF_8);
        String html = page.asXml();

        Document htmlDoc = Jsoup.parse(html);
        Elements lis = htmlDoc.select("td.fcatcol > ul > li");
        for(Element li : lis) {
            //System.out.println(li + "\n\n");
            try {
                System.out.println(li.select("a").get(0).text() + "\t" +
                        li.select("div").get(0).text());
            } catch (Exception e) {
                System.out.println(li.select("a").get(0).text());
            }
        }

        //Scanner sc = new Scanner(System.in);
        for(;;) {
            System.out.println("\n\n10秒后加载下一页");
            Thread.sleep(10000);
            //sc.nextLine();
            page.executeJavaScript("$('.categorypaginglink')[1].click()");

            html = page.asXml();
            htmlDoc = Jsoup.parse(html);

            lis = htmlDoc.select("td.fcatcol > ul > li");
            for(Element li : lis) {
                try {
                    System.out.println(li.select("a").get(0).text() + "\t" +
                            li.select("div").get(0).text());
                } catch (Exception e) {
                    System.out.println(li.select("a").get(0).text());
                }
            }
        }
    }
}
