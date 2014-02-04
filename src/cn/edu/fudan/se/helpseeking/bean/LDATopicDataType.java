package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class LDATopicDataType
{
	List<String> relatedPosts ;
	List<String> topWords;
	int index;
	
	public LDATopicDataType()
	{
		relatedPosts = new ArrayList<String>();
		topWords = new ArrayList<String>();
		
	}
	public List<String> getRelatedDocs()
	{
		return relatedPosts;
	}
	public void setRelatedDocs(List<String> relatedDocs)
	{
		this.relatedPosts = relatedDocs;
	}
	public List<String> getTopWords()
	{
		return topWords;
	}
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		this.index = index;
	}
	public void setTopWords(List<String> topWords)
	{
		this.topWords = topWords;
	}
	
	public void addTopWord(String word)
	{
		topWords.add(word);
	}
	
	
}
