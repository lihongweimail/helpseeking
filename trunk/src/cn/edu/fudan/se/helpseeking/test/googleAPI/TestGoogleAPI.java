package cn.edu.fudan.se.helpseeking.test.googleAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class TestGoogleAPI {
	
	public static void testWithGson() throws Exception{
		String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String search = "stackoverflow";
		String charset = "UTF-8";

		URL url = new URL(google + URLEncoder.encode(search, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		TestGoogleResults results = new Gson().fromJson(reader, TestGoogleResults.class);
		results.getResponseData().setSize();
		results.setSize();

		// Show title and URL of 1st result.
		for (int i = 0; i < results.getSize(); i++) {
			System.out.println(results.getResponseData().getResults().get(i).getTitle());
			System.out.println(results.getResponseData().getResults().get(i).getUrl());
			System.out.println(results.getResponseData().getResults().get(i).getContents());
		}
		
	}
public static void testManURL() {
    try {
        URL url = new URL("http://www.google.com/search?q=cell phone");
        URLConnection conn =  url.openConnection();
        conn.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        String str;
        String snippet="";

        while ((str = in.readLine()) != null) {
                System.out.println(str);
                snippet=snippet+str;
                
        }
        
        // add jsoup to parse those strings with XML tags  
        Document doc = Jsoup.parse(snippet);
        //travel over the document 
        Element content=doc.getElementById("content");
        Elements links=content.getElementsByTag("a");
        
        System.out.println(content.getAllElements().toString());
        
//    for(Element link: links){
//    	String linkHref=link.attr("href");
//    	String linkText=link.text();
//    	System.out.println("\n the link and content are :"+linkHref+linkText);
//    	
//    }

        in.close();
}
catch (MalformedURLException e) {}
catch (IOException e) {}
}

	
	public static void testManuallyConnectURL() {
		String searchText = "android cell phones";
        searchText = searchText.replaceAll(" ", "+");
        String key="AIzaSyAKKoduBGTuuvQ1OG35JebLuNtICy6zBaE";
        String cx = "005635559766885752621:va1etsiak-a";
        URL url;
        try {
            url = new URL(
                    "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx="+ cx +"&q="+ searchText + "&alt=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        String output;
        while ((output = br.readLine()) != null) {
            if(output.contains("\"snippet\": \"")){               
                String snippet = output.substring(output.indexOf("\"snippet\": \"")+("\"snippet\": \"").length(), output.indexOf("\","));
                System.out.println(snippet+ "\n\n\n");

                
           }    
        }
        conn.disconnect();
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
	
	public static void main(String[] args)  {
		try {
			testManURL();
			testWithGson();
//			testManuallyConnectURL();  // 100 query request times to google in each day 
		} catch (Exception e) {
			// TODO Auto-generated catch block
	
			e.printStackTrace();
			
		}
		
		

	}


}
