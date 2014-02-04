package cn.edu.fudan.se.helpseeking.test.googleAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class NaiveGoogleAPICall {
	public static void main(String[] args) throws IOException {
		 
		NaiveCall();
		
		
	}

	public static void NaiveCall() throws MalformedURLException,
			UnsupportedEncodingException, IOException {
		String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String query = "java tutorial";
		String charset = "UTF-8";
	 
		URL url = new URL(address + URLEncoder.encode(query, charset));
	 
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String str;
	 
		while ((str = in.readLine()) != null) {
			System.out.println(str);
		}
	 
		in.close();
	}
	
	
	
}
