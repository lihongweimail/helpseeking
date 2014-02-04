package cn.edu.fudan.se.helpseeking.preprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.Activator;
import cn.edu.fudan.se.helpseeking.bean.CommentsDataType;
import cn.edu.fudan.se.helpseeking.bean.PostHistoryDataType;
import cn.edu.fudan.se.helpseeking.bean.PostsDataType;
import cn.edu.fudan.se.helpseeking.bean.PostsTokensDataType;
import cn.edu.fudan.se.helpseeking.dal.HelpSeekingDAL;
import cn.edu.fudan.se.helpseeking.utils.CommUtil;

public class TokenExtractor
{

	List<PostsTokensDataType > tokenList;







	public List<PostsTokensDataType> analysis(List<PostsDataType> postsRow)
	{
		List<PostsTokensDataType> posts =  new ArrayList<PostsTokensDataType>();
		//		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath() + "\\conf.ini");

		TokenExtractor tokenExtractor = new TokenExtractor();	
		tokenExtractor.isAcceptAlphabet(true);
		tokenExtractor.isAcceptDigit(false);

		for(PostsDataType row : postsRow)
		{

			PostsTokensDataType post=new PostsTokensDataType();

			String tokensString=row.getBodyString();

			post.setPostId(row.getIdString());
			post.setTokens(CommUtil.tokenListToString(tokenExtractor.getIdentifierOccurenceOfString(tokensString)));

			posts.add(post);


		}

		return posts;

	}	



	private boolean isAcceptReduplication = false;
	public void isAcceptReduplication(boolean isAcceptReduplication )
	{
		this.isAcceptReduplication = isAcceptReduplication;
	}


	boolean isAcceptAlphabet = true;
	boolean isAcceptDigit = false;
	public void isAcceptAlphabet(boolean isAcceptAlphabet)
	{
		this.isAcceptAlphabet = isAcceptAlphabet;
	}

	public void isAcceptDigit(boolean isAcceptDigit)
	{
		this.isAcceptDigit = isAcceptDigit;
	}

	private boolean isStringAccepted(List<String> content, String str)
	{
		str = str.trim();
		boolean accpeted = false;
		if(isAcceptAlphabet)
			return str.charAt(0)>='a' && str.charAt(0) <= 'z' || str.charAt(0) >= 'A' && str.charAt(0) <= 'Z';
			if(isAcceptDigit)
				return str.charAt(0) >= '0' && str.charAt(0) <='9';
				if(isAcceptReduplication == false)
				{
					return content.indexOf(str) == -1 ; 
				}
				return accpeted;
	}

	public List<String> analysisPostsString(String str)
	{
		TokenExtractor tokenExtractor = new TokenExtractor();	
		tokenExtractor.isAcceptAlphabet(true);
		tokenExtractor.isAcceptDigit(false);

		List<String> tokens =tokenExtractor.getIdentifierOccurenceOfString(str);
		return tokens;

	}	


	public List<String> getIdentifierOccurenceOfString(String tokensString)
	{
		
		List<String> content = new ArrayList<String>();
		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
				factory.create());
		tokeniser.setMinTokenLength(2);
			List<String>  tokens=tokeniser.tokenise(tokensString);
			for (String token : tokens)
			{
				if(isStringAccepted(content,token))
					content.add(token);
			}				

		return content;
	}	



	public List<String> getIdentifierOccurenceOfDocument(
			String fileName)
			{
		List<String> content = new ArrayList<String>();
		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
				factory.create());
		tokeniser.setMinTokenLength(2);
		try
		{
			BufferedReader inputFile = new BufferedReader(new FileReader(
					fileName));
			String line;
			List<String> tokens;
			while (true)
			{
				line = inputFile.readLine();
				if (line == null)
				{
					break;
				}
				tokens = tokeniser.tokenise(line);				
				for (String token : tokens)
				{
					if(isStringAccepted(content,token))
						content.add(token);
				}				
			}
			inputFile.close();
		}
		catch (IOException ioEx)
		{
			System.err.println(ioEx.getMessage());
		}
		return content;
			}

}
