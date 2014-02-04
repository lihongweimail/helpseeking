package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class TokensInFileDataType
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
}
