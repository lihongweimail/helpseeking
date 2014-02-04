package cn.edu.fudan.se.helpseeking.services;

import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.lda.Topic;


public interface LDAService
{
	public void setup();

	public List<Topic> getRelatedTopic(String query);
	public List<Topic> getTopicList();
	public void analysisTwords();
	public  int getCurrentfilenumber();

	public List<String> getTopicRelatedPosts(int topicIndex);
	public List<String> getTopicTopAllPostsAndSort(int topicIndex);

	public void doLDAAnalysis(List<String> fileList, String projectName);
	public void doLDAAnalysis(String analysizedProject);

	public void loadDataFromDB();
}



