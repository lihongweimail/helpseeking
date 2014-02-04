package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

public class SortDataType<T>
{
	private List<T> contentList;
	private List<Integer> fitCountList;

	public SortDataType()
	{
		contentList = new ArrayList<T>();
		fitCountList = new ArrayList<Integer>();
	}

	public void addContent(T content)
	{
		int index = -1;
		if (contentList.contains(content))
		{
			index = contentList.indexOf(content);
			fitCountList.set(index, fitCountList.get(index) + 1);
		}
		else
		{
			contentList.add(content);
			fitCountList.add(1);
			
		}
	}

	public List<T> getContentList()
	{
		return contentList;
	}

	public void setContentList(List<T> contentList)
	{
		this.contentList = contentList;
	}

	public List<Integer> getFitCountList()
	{
		return fitCountList;
	}

	public void setFitCountList(List<Integer> fitCountList)
	{
		this.fitCountList = fitCountList;
	}

	public void sort()
	{
		 List<T> tempContentList = new ArrayList<T>();
		 List<Integer> tempFitCountList = new ArrayList<Integer>();
		 while(fitCountList.size()>0)
		 {
			int maxValue = fitCountList.get(0);
			for(int i=1; i<fitCountList.size(); i++)
			{
				if(fitCountList.get(i) >= maxValue)
				{
					maxValue = fitCountList.get(i);
				}
			}
			int indexOfMaxValue = fitCountList.indexOf(maxValue);
			tempContentList.add(contentList.get(indexOfMaxValue));
			tempFitCountList.add(fitCountList.get(indexOfMaxValue));
			fitCountList.remove(indexOfMaxValue);	
			contentList.remove(indexOfMaxValue);
		 }
		 contentList = tempContentList;
		 fitCountList = tempFitCountList;

	}

}
