package cn.edu.fudan.se.helpseeking.bean.lda;


public class TopicToWord
{
	private int topicID;
	private int wordID;
	private float wordDistributionValue;
	public int getTopicID()
	{
		return topicID;
	}
	public void setTopicID(int topicID)
	{
		this.topicID = topicID;
	}
	public int getWordID()
	{
		return wordID;
	}
	public void setWordID(int wordID)
	{
		this.wordID = wordID;
	}
	public float getWordDistributionValue()
	{
		return wordDistributionValue;
	}
	public void setWordDistributionValue(float wordDistributionValue)
	{
		this.wordDistributionValue = wordDistributionValue;
	}
	
}
