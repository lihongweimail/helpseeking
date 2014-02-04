package cn.edu.fudan.se.helpseeking.bean.lda;

import java.util.ArrayList;
import java.util.List;

public class Topic
{
	List<String> relatedPosts ;
	List<String> topWords;
	List<String> topic;
	int index;
	
	public List<String> getTopic() {
		return topic;
	}


	public void setTopic(List<String> topic) {
		this.topic = topic;
	}


	
	
	public Topic()
	{
		relatedPosts = new ArrayList<String>();
		topWords = new ArrayList<String>();
		
	}


	public List<String> getRelatedPosts() {
		return relatedPosts;
	}


	public void setRelatedPosts(List<String> relatedPosts) {
		this.relatedPosts = relatedPosts;
	}


	public List<String> getTopWords() {
		return topWords;
	}


	public void setTopWords(List<String> topWords) {
		this.topWords = topWords;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public void addTopWord(String word)
	{
		topWords.add(word);
	}
	
	
}
