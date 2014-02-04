package cn.edu.fudan.se.helpseeking.bean.lda;



public class TopicToPost
{
	private int topicID;
	private int postID;
	private float postDistributionValue;
	public int getTopicID() {
		return topicID;
	}
	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}
	public int getPostID() {
		return postID;
	}
	public void setPostID(int postID) {
		this.postID = postID;
	}
	public float getPostDistributionValue() {
		return postDistributionValue;
	}
	public void setPostDistributionValue(float postDistributionValue) {
		this.postDistributionValue = postDistributionValue;
	}
		
}
