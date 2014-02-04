package cn.edu.fudan.se.helpseeking.preprocessing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.DateTime;

import cn.edu.fudan.se.helpseeking.Activator;
import cn.edu.fudan.se.helpseeking.bean.CommentsDataType;
import cn.edu.fudan.se.helpseeking.bean.PostHistoryDataType;
import cn.edu.fudan.se.helpseeking.bean.PostsDataType;
import cn.edu.fudan.se.helpseeking.bean.PostsTokensDataType;
import cn.edu.fudan.se.helpseeking.bean.TokenInFile;
import cn.edu.fudan.se.helpseeking.dal.HelpSeekingDAL;
import cn.edu.fudan.se.helpseeking.dal.LDADAL;
import cn.edu.fudan.se.helpseeking.lda.LDA;
import cn.edu.fudan.se.helpseeking.utils.ChangeCharset;
import cn.edu.fudan.se.helpseeking.utils.CommUtil;
import cn.edu.fudan.se.helpseeking.utils.DBHelper;
import cn.edu.fudan.se.helpseeking.utils.FileHelper;
import cn.edu.fudan.se.helpseeking.utils.INIHelper;

public class DozPreprocessing {

	private static   DBHelper db=new DBHelper();



	public  DBHelper getDb() {
		return db;
	}


	public  void setDb(DBHelper mydb) {
		db = mydb;
	}


	// 弃置的 getPostsFromMYSQL
	public  List<PostsDataType> getPostsFromMYSQL(boolean  plusComments )
	{
		List <PostsDataType> postsResult = new ArrayList<PostsDataType>();
		try {			
			ResultSet postRS=HelpSeekingDAL.queryPostsContents(db);
			//				ResultSetMetaData postsRSMD = postRS.getMetaData();
			while (postRS.next())
			{		
				PostsDataType row=new PostsDataType();
				row.setIdString(postRS.getString(1));  //id

				String content=postRS.getString(3) +" "+ postRS.getString(2);  //title + body
				String postId=row.getIdString();
				//如果需要将comments添加到post的文字字体中   
				String commentsString="";
				//并加上posthistory相关的文字信息
				String postHistoryString="";
				if (plusComments) {
					commentsString = getCommentsWithPostId(postId);
					postHistoryString = getPosthistoryWithPostId(postId);
					content=content+" "+commentsString+ " "+postHistoryString;
				}
				row.setBodyString(content);
				postsResult.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		return postsResult;
	}


	//将post相关的comment和posthistory取出，并将词token化后，写入数据库的表poststokens中

	public void generatePostsTokens(boolean  plusComments )
	{
		
		int id=0;
//		id=LDADAL.getMaxPostsTokensCount(db); //这个预处理程序开始就先清理了这个表。所以注释掉
		int postsCount=HelpSeekingDAL.queryPostsCount(db);
		//为了不出现使用内存太大造成溢出，采用分批读取和处理的方法
		//另外，对tags字段中的java 和eclipse 进行选择，缩小post的范围 2013-12-27日提出，希望后续更改代码时加上这个选择！
		//选择设置可以放到helpseekingdal.querypostscontents中处理
		int limitCount=100000;
		int startCount=0; //表中记录是以0开始对应第一条记录
		int roundCount=(postsCount / limitCount);
		int remainder=postsCount %limitCount;
		if (remainder>0) {
			roundCount=roundCount+1;
		}
		
		
		try {			

			for(int i=0;i<roundCount;i++)
			{	
				//重新开始记录数据
				List<PostsTokensDataType> postsTokens=new ArrayList<PostsTokensDataType>();
				
				System.out.println("开始处理第 "+i+" 批（100000）post");
				long totalMilliseconds = System.currentTimeMillis();
				long totalSeconds = totalMilliseconds/1000;
				int currentSecond = (int)(totalSeconds );
				long totalMinutes = totalSeconds/60;
				int currentMinute = (int)(totalSeconds );
				long totalHours = totalMinutes/60;
				int curentHour = (int)(totalMinutes );
				String out = "Curent GMT_time is:  "+curentHour+":"+currentMinute+":"+currentSecond;
				System.out.println(out);
				
				
				System.out.println("processing number: "+ id);
				
				ResultSet postRS=HelpSeekingDAL.queryPostsContents(db, startCount+i*limitCount, limitCount);
//System.out.println(startCount+i*limitCount+" : "+limitCount);

				//dbhelper是一个实例，其中只维护一个resultset 所以 对resultset不能嵌套使用
				while (postRS.next())
				{		
					PostsTokensDataType  row=new PostsTokensDataType();
					id=id+1;
					
					
					row.setId(String.valueOf(id));
					row.setPostId(postRS.getString(1));//==> postid
					//				System.out.println("title: "+postRS.getString(3));
					//				System.out.println("body: "+postRS.getString(2));		
					String title=postRS.getString(3);
					if (title==null)
						title=" ";
					String body=postRS.getString(2);
					if (body==null)
						body=" ";
					//				postRS.getString(3) +" "+ postRS.getString(2) //title + body

					row.setTokens(title+" "+body); 
					
//					System.out.println("id="+row.getId());
//					System.out.println("postid="+row.getPostId());
//					
					postsTokens.add(row);
				}

				for (PostsTokensDataType irow : postsTokens) {


					String content=irow.getTokens(); //title + body
					if (content==null) {
						content=" ";
					}

					String postId=irow.getPostId();   //postid
					//					System.out.println(id);	
					//					System.out.println("postID: "+postId);

					//如果需要将comments添加到post的文字字体中   
					String commentsString="";
					//并加上posthistory相关的文字信息
					String postHistoryString="";

					if (plusComments) {
						commentsString = getCommentsWithPostId(postId);
						if (commentsString==null)
							commentsString=" ";


						postHistoryString = getPosthistoryWithPostId(postId);
						if (postHistoryString==null)
							postHistoryString=" ";

					}

					content=content+" "+commentsString+ " "+postHistoryString;
					//				content="<p>Besides Stack Overflow of course, here are mine.</p>&#xA;&#xA;<ul>&#xA;<li>Many have already mentioned <a href= http://www.hanselminutes.com/hanselminutes%5Fmp3Direct.xml  rel= nofollow >Hanselminutes</a>.</li>&#xA;<li>Some have already mentioned <a href= http://feeds.feedburner.com/netRocksFullMp3Downloads  rel= nofollow >.NET Rocks!</a> </li>&#xA;<li>Not quite as many have mentioned <a href= http://feeds.feedburner.com/RunasRadio  rel= nofollow >RunAs Radio</a>.</li>&#xA;</ul>&#xA;&#xA;<p>I can't believe the size of some of these lists. With podcasts, I like to keep the list short and the quality high. As such, I tend to skip the aggregates like ITConversations et. al.</p>&#xA;";
					//切词工具似乎有点问题，所以先用尽可能处理掉特殊符号再放到切词里面切词
					content=pureTokens(content);
					//TODO: 2014-01-06  java.sql.BatchUpdateException: Data truncation: Data too long for column 'Tokens' at row 1	 
//					mysql text data type  size <2^16 =65536 
					int size=content.length();
					
					if (size>65530) {
						size=65530;
					}
					content=content.substring(0, size);

					if (content==null)
						content="";

					//token化处理
					TokenExtractor tokensExtractor=new TokenExtractor();
					List<String> tokenList=tokensExtractor.analysisPostsString(content);
					String mytokens=CommUtil.tokenListToString(tokenList);
					//				System.out.println(mytokens);
					
					irow.setTokens(mytokens);

					//n行，每行为一个post的(行id, postid, tokens)
					
				

					//如果有必要，list太大内存不过考虑逐行写入
					// HelpSeekingDAL.insertPostsTokensLine(db, row);
					
					
//					System.out.println("id="+irow.getId());
//					System.out.println("postid="+irow.getPostId());

				}

				//成批写入数据库poststokens！！？  如果这个list太大，考虑逐行写入(见前面 while循环内)
				HelpSeekingDAL.insertPostsTokens(db, postsTokens);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}			

	}

	public static String pureTokens(String content) {
		String[] myList=content.split("[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]");

		List<String> list=new ArrayList<String>();
		for (int i = 0; i < myList.length; i++) {
			if(!myList[i].isEmpty())
				list.add(myList[i]);
		}
		content=CommUtil.tokenListToString(list);

		return content;
	}
	public  String getPosthistoryWithPostId(String postId) throws SQLException {
		String postHistoryString;
		//add posthistory text  
		ResultSet postHistoryRS=HelpSeekingDAL.queryPostsHistoryWithPostId(db,  Integer.valueOf(postId));		
		postHistoryString="";
		while (postHistoryRS.next()) {
			PostHistoryDataType postHistoryRow=new PostHistoryDataType();
			postHistoryRow.setIdString(postHistoryRS.getString(1));
			postHistoryRow.setPostIdString(postHistoryRS.getString(2));
			postHistoryRow.setTextString(postHistoryRS.getString(3));
			if(postHistoryRow.getTextString()!=null)
				postHistoryString=postHistoryString+ " "+ postHistoryRow.getTextString();
		}
		
		
		return postHistoryString;
	}


	public  String getCommentsWithPostId(String postId) throws SQLException {
		String commentsString;
		//comments
		ResultSet commentsRS=HelpSeekingDAL.queryPostsCommentsWithPostId(db,  Integer.valueOf(postId));		


		commentsString="";
		while (commentsRS.next()) {
			CommentsDataType commentRow=new CommentsDataType();
			commentRow.setIdString(commentsRS.getString(1));
			commentRow.setPostIdString(commentsRS.getString(2));
			commentRow.setTextString(commentsRS.getString(3));


			if(commentRow.getTextString()!=null)
				commentsString=commentsString+ " "+ commentRow.getTextString();
		}



		return commentsString;
	}

	//从数据库中读出poststokens，写入临时文件中，准备topic提取

	public void generateTempFileForLDA(String tempFileName) throws SQLException {

		//		String tempFileName="templda.txt";

		ResultSet postsTokensRS=HelpSeekingDAL.queryPostsTokens(db);
		List<PostsTokensDataType> tokenList=new ArrayList<PostsTokensDataType>();
		while (postsTokensRS.next()) {
			PostsTokensDataType postsTokensRow=new PostsTokensDataType();
			postsTokensRow.setId(postsTokensRS.getString(1));
			postsTokensRow.setPostId(postsTokensRS.getString(2));
			postsTokensRow.setTokens(postsTokensRS.getString(3));
			tokenList.add(postsTokensRow);
		}


		writeToFile(tokenList, tempFileName);


	}

	public void writeToFile(List<PostsTokensDataType> tokenList, String fileName)
	{
		FileHelper.createFile(fileName);
		PrintWriter outputStream = null;
		try
		{
			outputStream = new PrintWriter(fileName);
			outputStream.println(tokenList.size());
			for (PostsTokensDataType tokens : tokenList)
			{
				outputStream.println(tokens.getTokens());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			outputStream.close();
		}
	}

	//调用ldaoperator中的方法完成LDA命令的组织、调用LDA、分析结果文件、写入数据库中
	//LDAOperator.doLDAAnalysis();


	public void preMYSQLParameter() {
		//加载数据库驱动 在官网www.mysql.org下载驱动mysql-connector-java-3.0-ga-bin.jar包
		//String url="jdbc:mysql://10.131.252.224:3309/stackoverflow";         //指定连接数据库的URL
		//		String url="jdbc:mysql://localhost:3306/stackoverflow";         //指定笔记本电脑连接数据库的URL
		String url="jdbc:mysql://localhost:3309/stackoverflow";         //指定本机224服务器连接数据库的URL
		String user="root";                                          	//指定连接数据库的用户名
		String passWord="root";											//指定连接数据库的密码

		db.openConnMySQL(url, user, passWord);
	}

	public void exitMYSQLConn(){
		db.closeMYSQLConn();
	}

	public void clearDBTables() {
		//清理数据库中的这些表格：（poststokens）stem , topics, topictoposts, topictoword , word 
		// you must know this is dangerous behavior!
		LDADAL.clearPostsTokens(db);
		LDADAL.clearStem(db);
		LDADAL.clearTopics(db);
		LDADAL.clearTopicToPosts(db);
		LDADAL.clearTopicToWord(db);
		LDADAL.clearWord(db);
	}

	//====================

	public static  void main(String[] args) {

		DozPreprocessing doz=new DozPreprocessing();

		//连接数据库
		System.out.println("连接数据库");
		doz.preMYSQLParameter();

		//清理数据库中的这些表格：（poststokens）stem , topics, topictoposts, topictoword , word 
		System.out.println("清理数据库中的这些表格");
		doz.clearDBTables();

		//预处理posts的tokens    （plusComments =true 将comment和posthistory的token加入；   =false  仅保留发帖posts的标题和内容）
		//处理结束后写入数据库
		System.out.println("预处理posts的tokens,处理结束后写入数据库，生成表格poststokens");
		doz.generatePostsTokens(true);  

		//从数据库中读取tokens到临时文件，为LDA准备
		System.out.println("从数据库中读取tokens到临时文件，为LDA准备;");
		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath()
				+ "\\conf.ini");
		String tempFileName = iniHelper.getValue("IDENTIFIEREXTRACTOR",
				"postsForLDAFileName", "postsTokens.txt");
		tempFileName=CommUtil.getCurrentProjectPath()+"\\LDAResult\\"+tempFileName;

		System.out.println("文件名："+tempFileName);

		try {

			doz.generateTempFileForLDA(tempFileName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//调用ldaoperator中的方法完成LDA命令的组织、调用LDA、分析结果文件、写入数据库中
		System.out.println("调用LDA、分析结果文件、写入数据库中");
		LDAOperator ldaOperator=new LDAOperator();
		ldaOperator.getInstance();
		ldaOperator.setDB(db);

		//处理
		System.out.println("LDA");
		ldaOperator.doLDAAnalysis(db);


		//关闭数据库
		System.out.println("关闭数据库");
		doz.exitMYSQLConn();


	}


}
