package cn.edu.fudan.se.helpseeking.test.googleAPI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class BrowseLikeGoogleAPICall {

	public static URL url;
	public static HttpURLConnection http;
	public static java.io.InputStream urlstream;

	public static void main(String[] args)  {
		search();
		
	}
	
	public static void search() {
		
	
		String totalstring ="";
		String query="stackoverflow";
		int start=0;
		int linecount=0;
		
	 for(int i=0;  i < 9; i=i+4)
	 {
		 
		 
	try {
		start=i;
	url = new URL("http://www.google.com/search?q="+query+"&hl=zh-CN&start="+start+"&sa=N&ie=UTF-8");
//	url = new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q="+query+"&hl=zh-CN&start="+start+"&sa=N&ie=UTF-8");
	
	// 
	}catch(Exception ef){};
	
	try {
		http = (HttpURLConnection) url.openConnection();
		http.setRequestProperty("User-Agent", "Mozilla/4.0");
		http.connect();
		urlstream = http.getInputStream();
		}catch(Exception ef){};
		
		java.io.BufferedReader l_reader = new java.io.
	BufferedReader(new java.io.InputStreamReader(urlstream));
	try {
	String currentLine="";
	
	while (( currentLine = l_reader.readLine()) != null) {
	totalstring +=currentLine;
	System.out.println("\n Result : "+String.valueOf(++linecount) +"\n"+currentLine);
	}
	
	} catch (IOException ex3) {} ;
	
	}
	
	 System.out.println("\n the all results \n");
	 System.out.println(totalstring);
	//本次搜索的结果已经放到totalstring中了，是一些HTML代码，需要下一步进行分析了。
	}
}