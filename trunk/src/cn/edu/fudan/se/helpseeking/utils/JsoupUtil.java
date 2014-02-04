package cn.edu.fudan.se.helpseeking.utils;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class JsoupUtil {
	public static void main(String[] args) throws IOException {

		//		testXMLDoc("D:/lihongwei/research/research topic/help seeking/newIdeas/tool/HelpSeeking/testResource/JavaMismatchOrNeverEndingProgram.htm","http://stackoverflow.com/questions/10238644/java-mismatch-or-never-ending-program");
		testXMLURL1("http://stackoverflow.com/questions/10238644/java-mismatch-or-never-ending-program");


	}

	public static void testXMLDoc(String fileName,String baseURL) throws IOException {
		//		D:\lihongwei\research\research topic\help seeking\newIdeas\tool\HelpSeeking\testResource\JavaMismatchOrNeverEndingProgram.htm
		//		./testResource/JavaMismatchOrNeverEndingProgram.htm
		//		http://stackoverflow.com/questions/10238644/java-mismatch-or-never-ending-program
		Document doc=parserXMLdoc(fileName,baseURL);
		Element content = doc.getElementById("content"); 
		Elements links = content.getElementsByTag("a"); 
		int i=0;
		for (Element link : links) { 
			String linkHref = link.attr("href"); 
			String linkText = link.text();
			System.out.println("\nResult: "+(i++)+"\n");
			System.out.println(link.toString());
		}
	}

	public static void testXMLURL1(String URL) throws IOException {

		//		http://stackoverflow.com/questions/10238644/java-mismatch-or-never-ending-program

		//		1通过URL获取文档的基本方法
		Document doc=parserXMLURL1(URL);

		//		2.1基本数据访问方式
		Element content = doc.getElementById("content"); 
		Elements links = content.getElementsByTag("a"); 

		int i=0;
		for (Element link : links) { 
			String linkHref = link.attr("href"); 
			String linkText = link.text();
			System.out.println("\nResult: "+(i++)+"\n");
			System.out.println(link.toString());
		}


		//		 2.2查询数据
		// 对解析的文档采用选择查询的方式获得文档元素，这点非常像jQuery， 非常方便

		Elements linkss = doc.select("a[href]"); // 具有 href 属性的链接
		Elements pngs = doc.select("img[src$=.png]");// 所有引用 png 图片的元素
		Element masthead = doc.select("div.masthead").first(); 
		// 找出定义了 class=masthead 的元素
		Elements resultLinks = doc.select("h3.r > a"); // direct a after h3 


		//		 2.3修改数据

		doc.select("div.comments a").attr("rel", "nofollow"); 
		// 为所有链接增加 rel=nofollow 属性
		doc.select("div.comments a").addClass("mylinkclass"); 
		// 为所有链接增加 class=mylinkclass 属性
		doc.select("img").removeAttr("onclick"); // 删除所有图片的 onclick 属性
		doc.select("input[type=text]").val(""); // 清空所有文本输入框中的文本

		//   2.4文档清理
//		在做网站的时候，经常会提供用户评论的功能。有些用户比较淘气，会搞一些脚本到评论内容中，
//		而这些脚本可能会破坏整个页面的行为，更严重的是获取一些机要信息，例如 XSS 跨站点攻击之类。
//		可以清理文档中的一些指定的元素
		 String unsafe = "<p><a href='http://www.oschina.net/' onclick='stealCookies()'> 开源中国社区 </a></p>"; 
				 String safe = Jsoup.clean(unsafe, Whitelist.basic()); 
				 // 输出 : 
				 // <p><a href="http://www.oschina.net/" rel="nofollow"> 开源中国社区 </a></p> 
//				 jsoup 使用一个 Whitelist 类用来对 HTML 文档进行过滤，该类提供几个常用方法：
//
//				 表 1. 常用方法：
//				 方法名	简介
//				 none()	 只允许包含文本信息
//				 basic()	 允许的标签包括：a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, strike, strong, sub, sup, u, ul, 以及合适的属性
//				 simpleText()	 只允许 b, em, i, strong, u 这些标签
//				 basicWithImages()	 在 basic() 的基础上增加了图片
//				 relaxed()	 这个过滤器允许的标签最多，包括：a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul
//
//				 如果这五个过滤器都无法满足你的要求呢，例如你允许用户插入 flash 动画，没关系，Whitelist 提供扩展功能，例如 whitelist.addTags("embed","object","param","span","div"); 也可调用 addAttributes 为某些元素增加属性。
//				 jsoup 的过人之处——选择器
//				 前面我们已经简单的介绍了 jsoup 是如何使用选择器来对元素进行检索的。本节我们把重点放在选择器本身强大的语法上。下表是 jsoup 选择器的所有语法详细列表。
//
//				 表 2. 基本用法：
//				 tagname	 使用标签名来定位，例如 a
//				 ns|tag	 使用命名空间的标签定位，例如 fb:name 来查找 <fb:name> 元素
//				 #id	 使用元素 id 定位，例如 #logo
//				 .class	 使用元素的 class 属性定位，例如 .head
//				 [attribute]	 使用元素的属性进行定位，例如 [href] 表示检索具有 href 属性的所有元素
//				 [^attr]	 使用元素的属性名前缀进行定位，例如 [^data-] 用来查找 HTML5 的 dataset 属性
//				 [attr=value]	 使用属性值进行定位，例如 [width=500] 定位所有 width 属性值为 500 的元素
//				 [attr^=value], [attr$=value], [attr*=value]	 这三个语法分别代表，属性以 value 开头、结尾以及包含
//				 [attr~=regex]	 使用正则表达式进行属性值的过滤，例如 img[src~=(?i)\.(png|jpe?g)]
//				 *	 定位所有元素
//
//				 以上是最基本的选择器语法，这些语法也可以组合起来使用，下面是 jsoup 支持的组合用法：
//
//				 表 3：组合用法：
//				 el#id	 定位 id 值某个元素，例如 a#logo -> <a id=logo href= … >
//				 el.class	 定位 class 为指定值的元素，例如 div.head -> <div class=head>xxxx</div>
//				 el[attr]	 定位所有定义了某属性的元素，例如 a[href]
//				 以上三个任意组合	 例如 a[href]#logo 、a[name].outerlink
//				 ancestor child	 这五种都是元素之间组合关系的选择器语法，其中包括父子关系、合并关系和层次关系。
//				 parent > child
//				 siblingA + siblingB
//				 siblingA ~ siblingX
//				 el, el, el
//
//				 除了一些基本的语法以及这些语法进行组合外，jsoup 还支持使用表达式进行元素过滤选择。下面是 jsoup 支持的所有表达式一览表：
//
//				 表 4. 表达式：
//				 :lt(n)	 例如 td:lt(3) 表示 小于三列
//				 :gt(n)	 div p:gt(2) 表示 div 中包含 2 个以上的 p
//				 :eq(n)	 form input:eq(1) 表示只包含一个 input 的表单
//				 :has(seletor)	 div:has(p) 表示包含了 p 元素的 div
//				 :not(selector)	 div:not(.logo) 表示不包含 class=logo 元素的所有 div 列表
//				 :contains(text)	 包含某文本的元素，不区分大小写，例如 p:contains(oschina)
//				 :containsOwn(text)	 文本信息完全等于指定条件的过滤
//				 :matches(regex)	 使用正则表达式进行文本过滤：div:matches((?i)login)
//				 :matchesOwn(regex)	 使用正则表达式找到自身的文本
	}


	public  static Document parserXMLString(String xmlString) {
		// 直接从字符串中输入 HTML 文档
		String html =xmlString;
		//		 String html = "<html><head><title> 开源中国社区 </title></head>"
		//		  + "<body><p> 这里是 jsoup 项目的相关文章 </p></body></html>"; 
		Document doc = Jsoup.parse(html); 

		return doc;


	}

	public static Document parserXMLURL1(String urlString ) throws IOException {
		// 从 URL 直接加载 HTML 文档
		//		 Document doc = Jsoup.connect("http://www.oschina.net/").get(); 
		Document doc = Jsoup.connect(urlString).get(); 

		String title = doc.title(); 

		return doc;

	}
	public static Document parserXMLURL2(String urlString) throws IOException {
		// 从 URL 直接加载 HTML 文档
		//		 Document doc = Jsoup.connect("http://www.oschina.net/") 


		Document doc = Jsoup.connect(urlString) 
				.data("query", "Java")   // 请求参数
				.userAgent("I ’ m jsoup") // 设置 User-Agent 
				.cookie("auth", "token") // 设置 cookie 
				.timeout(3000)           // 设置连接超时时间
				.post();                 // 使用 POST 方法访问 URL 

		return doc;

	}
	public static Document parserXMLdoc(String fileName, String baseURL) throws IOException {
		// 从文件中加载 HTML 文档
		//		 File input = new File("D:/test.html"); 
		File input = new File(fileName); 

		//		 Document doc = Jsoup.parse(input,"UTF-8","http://www.oschina.net/");
		Document doc = Jsoup.parse(input,"UTF-8",baseURL);

		return doc;

	}

}
