package org.jsoup.examples;

import java.io.File;
import java.io.IOException;

import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

public class TestforGetHTML {

	public static void main(String[] args) throws IOException {
//		File input=new File("C:/Users/Grand/workspace/jsoup/resources/htmltests/ipod-GoogleSearch.html");
//		Document doc = Jsoup.parse(input, "UTF-8", "https://www.google.com.hk/search?newwindow=1&safe=strict&espv=210&es_sm=122&q=ipod&oq=ipod&gs_l=serp.3...3127859.3130032.0.3130323.6.5.1.0.0.0.319.564.2-1j1.2.0....0...1c.1.29.serp..3.3.578.mJheYyNKMp8");

//		connect(String url) 方法创建一个新的 Connection, 和 get() 取得和解析一个HTML文件。如果从该URL获取HTML时发生错误，便会抛出 IOException，应适当处理。
//		Connection 接口还提供一个方法链来解决特殊请求，具体如下：
//
//		Document doc = Jsoup.connect("http://example.com")
//		  .data("query", "Java")
//		  .userAgent("Mozilla")
//		  .cookie("auth", "token")
//		  .timeout(3000)
//		  .post();
		
		Document doc = Jsoup.connect("http://stackoverflow.com/questions/16153450/github-windows-client-behind-proxy")
				  .userAgent("Mozilla")
				  .timeout(3000)
				  .post();
		
	       HtmlToPlainText formatter = new HtmlToPlainText();
	        String plainText = formatter.getPlainText(doc);
	        System.out.println(plainText);
	        System.out.println("============================");
	        
	        Element tempString =doc.getElementById("content");
	        Elements linksElements=tempString.getElementsByTag("div");
	        String  temp1=tempString.attr("answer");
	        String temp2=tempString.text();
	        System.out.println(temp2);
	        
	        
		
	}
	
}
