package cn.edu.fudan.se.helpseeking.test.googleAPI;

import java.util.List;



public class TestGoogleResults {

    private ResponseData responseData;
    private int size;
    public int getSize() {return size;	}
	public void setSize(int size) {	this.size = size;	}
	public void setSize(){ this.size=responseData.getSize();}
	
	public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; this.setSize(responseData.getSize()); }
    public String toString() { return "ResponseData[" + responseData + "]"; }
    
    
    static class Result {
        private String url;
        private String title;
        private String contents;
        public String getContents() {	return contents;	}
	
		public String getUrl() { return url; }
        public String getTitle() { return title; }
        
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public void setContents(String contents) {this.contents = contents;	}
        
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }
    
    static class ResponseData {
        private List<Result> results;
        private int size;
        public int getSize() {return size;	}
    	public void setSize(int size) {	this.size = size;	}
    	public void setSize(){this.size=results.size();}
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; setSize(results.size());}
        public String toString() { return "Results[" + results + "]"; }
    }



}