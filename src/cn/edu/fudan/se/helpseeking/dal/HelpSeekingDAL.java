package cn.edu.fudan.se.helpseeking.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.edu.fudan.se.helpseeking.Activator;
import cn.edu.fudan.se.helpseeking.bean.CommentsDataType;
import cn.edu.fudan.se.helpseeking.bean.PostHistoryDataType;
import cn.edu.fudan.se.helpseeking.bean.PostsDataType;
import cn.edu.fudan.se.helpseeking.bean.PostsTokensDataType;
import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.preprocessing.TokenExtractor;
import cn.edu.fudan.se.helpseeking.utils.CommUtil;
import cn.edu.fudan.se.helpseeking.utils.DBHelper;


public class HelpSeekingDAL {
	
	public static int getMaxQueryId(DBHelper db )
	{
		int result = -1;
		String sql =" SELECT max(query.QueryId)  FROM stackoverflow.query ";
		ResultSet rs = db.executeQuery(sql);
      
		try {
			rs.last();
			if (rs.isFirst())
			result = rs.getInt(1) ;
			else {
				result=0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static ResultSet queryStem(DBHelper db) throws SQLException
	{
		String query = "SELECT stem.id,  stem.word, stem.stem FROM stackoverflow.stem";

		ResultSet rSet = db.executeQuery(query);
		return rSet;
	}

	public static ResultSet queryPosts(DBHelper db) throws SQLException
	{
		String query = "SELECT posts.Id,    posts.PostTypeId,    posts.AcceptedAnswerId,    posts.ParentID,    posts.CreationDate,    posts.Score,    posts.ViewCount,    posts.Body,    posts.OwnerUserId,    posts.OwnerDisplayName,    posts.LastEditorUserId,    posts.LastEditorDisplayName,    posts.LastEditDate,    posts.LastActivityDate,    posts.Title,     posts.Tags,    posts.AnswerCount,    posts.CommentCount,    posts.FavoriteCount,    posts.ClosedDate,    posts.CommunityOwnedDate FROM stackoverflow.posts ";

		ResultSet rSet = db.executeQuery(query);
		return rSet;
	}

	public static ResultSet queryTopics(DBHelper db) throws SQLException
	{
		String query = "SELECT topics.Id,   topics.TopicName,   topics.Topic FROM stackoverflow.topics";

		ResultSet rSet = db.executeQuery(query);
		return rSet;
	}
	public static ResultSet queryTopicsToPosts(DBHelper db) throws SQLException
	{
		String query = "SELECT  topictoposts.TopicId,   topictoposts.PostId,   topictoposts.postdistributionvalue FROM stackoverflow.topictoposts";

		ResultSet rSet = db.executeQuery(query);
		return rSet;
	}
	public static ResultSet queryTopicsToWord(DBHelper db) throws SQLException
	{
		String query = "SELECT topictoword.id,    topictoword.topicid,    topictoword.wordid,    topictoword.worddistributionvalue FROM stackoverflow.topictoword";

		ResultSet rSet = db.executeQuery(query);
		return rSet;
	} 	  	  

	public static ResultSet queryWord(DBHelper db) throws SQLException
	{
		String query = "SELECT word.id,  word.name FROM stackoverflow.word";

		ResultSet rSet = db.executeQuery(query);
		return rSet;
	} 	  	  

	public static ResultSet queryPostsHistoryWithPostId(DBHelper db, int postId)
	{

		String queryComments="select posthistory.Id, posthistory.PostId, posthistory.Text from stackoverflow.posthistory where posthistory.PostId= " +postId +"  ";

		ResultSet rSet = db.executeQuery(queryComments);
		return rSet;

	}

	public static ResultSet queryPostsCommentsWithPostId(DBHelper db, int postId)
	{

		String queryComments="select comments.Id, comments.PostId, comments.Text from stackoverflow.comments where comments.PostId= " +postId +"  ";

		ResultSet rSet = db.executeQuery(queryComments);
		return rSet;

	}



	public static ResultSet queryPostsContents(DBHelper db)
	{
		//默认将body 和 post的标题title作为全部内容
		//TODO ID=11317
		String queryPosts="select posts.Id, posts.Body, posts.Title from stackoverflow.posts ";

		ResultSet rSet = db.executeQuery(queryPosts);
		return rSet;

	}
	
	public static void insertPostsTokensLine(DBHelper db, PostsTokensDataType row)
	{
		PreparedStatement pst = null;
		String sql = "insert into poststokens(Id, PostId, Tokens) values (?,?,?)";

		try
		{
			pst = db.getConn().prepareStatement(sql);
			
				pst.setInt(1, Integer.valueOf(row.getId()));
				pst.setInt(2, Integer.valueOf(row.getPostId()));
				String tokensString=row.getTokens();
				pst.setString(3, tokensString);
				pst.addBatch();
			
			pst.executeBatch();			

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void insertPostsTokens(DBHelper db, List <PostsTokensDataType> postsTokensList )
	{
		PreparedStatement pst = null;
		String sql = "insert into poststokens(Id, PostId, Tokens) values (?,?,?)";

		try
		{
			pst = db.getConn().prepareStatement(sql);
			for (PostsTokensDataType postwithTokens : postsTokensList)
			{
				pst.setInt(1, Integer.valueOf(postwithTokens.getId()));
				pst.setInt(2, Integer.valueOf(postwithTokens.getPostId()));
				String tokensString=postwithTokens.getTokens();
				pst.setString(3, tokensString);
				pst.addBatch();
			}
			pst.executeBatch();			

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	//从数据库中读出poststokens，写入临时文件中，准备topic提取

	public static ResultSet queryPostsTokens(DBHelper db) throws SQLException
	{
		String query = "SELECT poststokens.Id, poststokens.PostId, poststokens.Tokens FROM stackoverflow.poststokens";

		ResultSet rSet = db.executeQuery(query);
		return rSet;
	}
	
	

	public static void doQueryandSearchResult(DBHelper db,List<WEBResult> searchResults, int queryId) {
		
		
		int maxSearchResultId=getMaxSearchResultId(db);
		List<Integer> relatedRSID=insertSearchResultForTemp(db, searchResults, maxSearchResultId+1);
		int maxQueryAndSearchResultListid=getQueryAndSearchResultList(db);
			insertQueryAndSearchResultList(db, relatedRSID,maxQueryAndSearchResultListid+1, queryId);
				
		
	}
	
	public static int getQueryAndSearchResultList(DBHelper db )
	{
		int result = -1;
		String sql =" SELECT max(queryandsearchresultlist.id)  FROM stackoverflow.queryandsearchresultlist ";
		ResultSet rs = db.executeQuery(sql);
      
		try {
			rs.last();
			if (rs.isFirst())
			result = rs.getInt(1) ;
			else {
				result=0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	public static void insertQueryAndSearchResultList(DBHelper db,
			List<Integer> relatedRSID, int maxId, int queryId) {
		PreparedStatement pst = null;
		String sql = "insert into queryandsearchresultlist(id,SearchResultId,QueryId) values (?,?,?)";

		try
		{
			pst = db.getConn().prepareStatement(sql);
		int maxCount=maxId;
			for (Integer result : relatedRSID)
			{
				
	    	    pst.setInt(1, maxCount);
				pst.setInt(2,result);
				pst.setInt(3, queryId);
				maxCount=maxCount+1;
				pst.addBatch();
			}
			pst.executeBatch();			

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
	}

	public static List <Integer>  insertSearchResultForTemp(DBHelper db, List<WEBResult> searchResults, int startSearchResultId )
	{
		
		 List <Integer> relatedSRID= new ArrayList<Integer>();
		PreparedStatement pst = null;
		String sql = "insert into searchresult(SearchResultId,Title,Summary,Similarity,Popularity,Tag,URL) values (?,?,?,?,?,?,?)";

		try
		{
			pst = db.getConn().prepareStatement(sql);
			int tempCount=startSearchResultId;
			for (WEBResult result : searchResults)
			{
				
				       relatedSRID.add(tempCount);
			 pst.setInt(1, Integer.valueOf(tempCount));
						tempCount=tempCount+1;
				
				pst.setString(2, result.getTitleNoFormatting());
				pst.setString(3, result.getContent());
				
				Random random=new Random();
				int s=random.nextInt(8)%(8)+1;
				float tempValue=(float) (1.0-0.1*s);
				pst.setFloat(4, tempValue);
				pst.setFloat(5, tempValue);
				
				pst.setString(6, "UnTag");
				pst.setString(7, result.getUnescapedUrl());
			
				pst.addBatch();
			}
			pst.executeBatch();			

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return relatedSRID;

	}

	public static int getMaxSearchResultId(DBHelper db )
	{
		int result = -1;
		String sql =" SELECT max(searchresult.SearchResultId)  FROM stackoverflow.searchresult ";
		ResultSet rs = db.executeQuery(sql);
      
		try {
			rs.last();
			if (rs.isFirst())
			result = rs.getInt(1) ;
			else {
				result=0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	public static void insertQuery(DBHelper db, int queryId,
			String queryTextString) {
		PreparedStatement pst = null;
		String sql = "insert into query(QueryId, QueryText) values (?,?)";

		try
		{
			pst = db.getConn().prepareStatement(sql);
				pst.setInt(1, queryId);
				pst.setString(2, queryTextString);
				pst.addBatch();
			
			pst.executeBatch();			

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}

	
	//调用ldaoperator中的方法完成LDA命令的组织、调用LDA、分析结果文件、写入数据库中

	//在LDADAL中完成对topic，post，word的读写方法
	
	
	public static int queryPostsCount(DBHelper db) {

		int result = 0;
		String sql = " select count(id) from stackoverflow.posts  ";
		ResultSet rs = db.executeQuery(sql);

		try {
			rs.last();
			if (rs.isFirst())
				result = rs.getInt(1);
			else {
				result = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}



	public static ResultSet queryPostsContents(DBHelper db, int start, int count) {
		// 默认将body 和 post的标题title作为全部内容
		// TODO ID=11317
		// 另外，对tags字段中的java 和eclipse 进行选择，缩小post的范围
		// 2013-12-27日提出，希望后续更改代码时加上这个选择！
		// 选择设置可以放到helpseekingdal.querypostscontents中处理
		// String
		// queryPosts="select posts.Id, posts.Body, posts.Title from stackoverflow.posts where Tags='"+"java"+"' or Tags='"+"eclipse"+"' "
		// +" limit "+start+" , " +count ;

		String queryPosts = "select posts.Id, posts.Body, posts.Title from stackoverflow.posts "
				+ " limit " + start + " , " + count;

		ResultSet rSet = db.executeQuery(queryPosts);
		return rSet;

	}




}
