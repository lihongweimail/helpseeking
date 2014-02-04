package cn.edu.fudan.se.helpseeking.test.googleAPI;

	 
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.Reader;
	import java.net.URL;
	import java.net.URLEncoder;
	import java.util.List;
	import com.google.gson.Gson;
	 
public class BetterLoopGoogleAPICall {
	public static void main(String[] args) throws IOException {
		
		int j=0; //count for the results
	 
			for (int i = 0; i < 9; i = i + 4) {
				String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+i+"&q=";
			 
				String query = "Programcreek";
				String charset = "UTF-8";
			 
				URL url = new URL(address + URLEncoder.encode(query, charset));
				Reader reader = new InputStreamReader(url.openStream(), charset);
				BetterGoogleAPICallResults results = new Gson().fromJson(reader, BetterGoogleAPICallResults.class);
			 
				// Show title and URL of each results
				for (int m = 0; m <= 3; m++) {
					System.out.println("Result "+(++j)+" :");
					System.out.println("Title: " + results.getResponseData().getResults().get(m).getTitle());
					System.out.println("URL: " + results.getResponseData().getResults().get(m).getUrl() + "\n");
				}
			}
		}
	}
	 
	 


