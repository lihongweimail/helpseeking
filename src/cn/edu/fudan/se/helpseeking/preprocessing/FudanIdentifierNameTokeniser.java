package cn.edu.fudan.se.helpseeking.preprocessing;

import java.util.ArrayList;
import java.util.List;

import uk.ac.open.crc.intt.IdentifierNameTokeniser;
import cn.edu.fudan.se.helpseeking.utils.CommUtil;
import cn.edu.fudan.se.helpseeking.utils.FileHelper;
import cn.edu.fudan.se.helpseeking.utils.INIHelper;


public class FudanIdentifierNameTokeniser 
{
	public static final String JAVA_STOP_LIST_FILENAME;
	private static final String stop_list_path ;
	public static final String SPLIT_STRING = "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
			//"[$#_.\\();,/\"\'{}+-=><!*\t ]";   
			// "[&#$_.(){}!*+-=><\\:;,?/\"\'\t ]"
	//添加齐全一些不可见字符   "[&#$_.(){}!*+-=><\\:;,?/\"\'\t\b\r\n\0 ]"
	
	
	static {
		INIHelper iniHelper = new INIHelper("conf.ini");
		stop_list_path = iniHelper.getValue("IDENTIFIEREXTRACTOR", "path", "StopResource");
		JAVA_STOP_LIST_FILENAME = iniHelper.getValue("IDENTIFIEREXTRACTOR", "javaStopList", "javaStopList.txt");
		
	}
	private IdentifierNameTokeniser identifierNametokeniser;
	private int minLength =2;
	private String[] keyWords;
	private boolean isIgnoreCase;
	
	
	public void isIgnoreCase(boolean isIgnoreCase)
	{
	
		this.isIgnoreCase = isIgnoreCase;
	}
	
	public void addFilterList(List<String> fielterlist)
	{
		
	}
	
	public FudanIdentifierNameTokeniser(IdentifierNameTokeniser tokeniser)
	{
		identifierNametokeniser = tokeniser;
		minLength = 1;	
		isIgnoreCase = true;
		constructDefaultFilterString(JAVA_STOP_LIST_FILENAME);
		
	}
	public static void main(String...strings )
	{
			
		
	}
	
	private void constructDefaultFilterString(String stopfileName)
	{	
		stopfileName = CommUtil.getCurrentProjectPath() + "\\" + stop_list_path +
				"\\" + stopfileName;
		keyWords = FileHelper.getContent(stopfileName).split(SPLIT_STRING);		
	}

	
	public void setFilterString(ArrayList<String> fileStrings)
	{
		for(String keyword : keyWords)
		{
			fileStrings.add(keyword);
		}
		keyWords = (String[]) fileStrings.toArray();
	}
	
	public void setMinTokenLength(int minLength)
	{
		this.minLength = minLength;
	}
	
	public List<String> tokenise(String line)
	{
		String[] tokens = identifierNametokeniser.tokenise(line);                    
		ArrayList<String> result = new ArrayList<String>();		
		for(String token : tokens)
		{
			//TODO 需要调试 对token为空串和空值NULL的判断
			//另外也可以考虑在这个地方设置词根化处理！！通过调用wordnet的API来做词根化。并考虑写入数据库（放到tokenextractor中实现数据库处理）
			if (token!=null || token!="") {
							
			token = token.trim();
			if(token.length() >= minLength && (int) token.charAt(0)< 255)
			{
				if( this.isIgnoreCase)
				{
					token = token.toLowerCase();
				}
				result.add(token);
			}
			}
		}				
		for(String keyword : keyWords)
		{
			result.remove(keyword);
		}
			
		return result;		
	}
}
