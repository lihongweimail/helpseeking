package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class TokenInFile
{
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}
	public List<String> getTokens()
	{
		return tokens;
	}
	public void setTokens(List<String> tokens)
	{
		this.tokens = tokens;
	}
	String fileName;
	String projectName;
	List<String> tokens;
	String content;
	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}
}
