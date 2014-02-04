package cn.edu.fudan.se.helpseeking.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.ResultSetMetaData;

import cn.edu.fudan.se.helpseeking.bean.Word;
import cn.edu.fudan.se.helpseeking.bean.lda.Post;
import cn.edu.fudan.se.helpseeking.bean.lda.Topic;
import cn.edu.fudan.se.helpseeking.bean.lda.TopicToPost;
import cn.edu.fudan.se.helpseeking.bean.lda.TopicToWord;
import cn.edu.fudan.se.helpseeking.utils.DBHelper;



public class LDADAL
{


	public static List<String> getTokensOfPosts( DBHelper db)

	{
		List<String> result=null;

		String sql= "SELECT poststokens.Id,    poststokens.PostId,   poststokens.Tokens FROM stackoverflow.poststokens";
		ResultSet rs=db.executeQuery(sql);
		try
		{
			while(rs.next())
			{
				result.add(rs.getString(3));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}


		return result;
	}



	public static List<TopicToWord> getTopWordsByTopic(DBHelper db , int topicID,
			int count)
			{
		List<TopicToWord> result = new ArrayList<TopicToWord>();
		String sql = " SELECT  topictoword.topicid, topictoword.wordid, topictoword.worddistributionvalue FROM stackoverflow.topictoword " +
				" where topicid = "
				+ topicID + " order by wordDistributionValue desc";

		ResultSet rs = db.executeQuery(sql);
		try
		{
			while (rs.next() && count > 0)
			{
				TopicToWord ttw = new TopicToWord();
				ttw.setTopicID(rs.getInt(1));
				ttw.setWordID(rs.getInt(2));
				ttw.setWordDistributionValue(rs.getFloat(3));
				result.add(ttw);
				count --;
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
			}

	public static int getTopicCount(DBHelper db )
	{
		int result = -1;
		String sql =" SELECT distinct count(topics.Id)  FROM stackoverflow.topics ";
		ResultSet rs = db.executeQuery(sql);
		try
		{

			result = rs.getInt(1) ;

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public static int getMaxTopicCount(DBHelper db )
	{
		int result = -1;
		String sql =" SELECT max(topics.Id)  FROM stackoverflow.topics ";
		ResultSet rs = db.executeQuery(sql);
		

		try {
			rs.last();
			if (rs.isFirst())
			result = rs.getInt(1) ;
			else {
				result=-1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static int getMaxPostsTokensCount(DBHelper db )
	{
		int result = -1;
		String sql =" SELECT max(poststokens.Id)  FROM stackoverflow.poststokens ";
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
	public static void inserTopicToWord(DBHelper db , List<TopicToWord> topicToWords)
	{

		PreparedStatement pst = null;
		String sql = "insert into  topictoword(topictoword.topicid,topictoword.wordid,topictoword.worddistributionvalue) values (?,?,?)";
		try
		{

			pst = db.getConn().prepareStatement(sql);

			for (TopicToWord topictoWord : topicToWords)
			{
				pst.setInt(1, topictoWord.getTopicID());
				pst.setInt(2, topictoWord.getWordID());
				pst.setDouble(3, topictoWord.getWordDistributionValue());
				pst.addBatch();
			}
			pst.executeBatch();

		}

		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void insertTopicToPost( DBHelper db,
			List<TopicToPost> topicToPosts)
	{
		PreparedStatement pst = null;
		String sql = "insert into  topictoposts(topictoposts.TopicId,   topictoposts.PostId,   topictoposts.postdistributionvalue)  values (?,?,?)";
		try
		{
			pst = db.getConn().prepareStatement(sql);
			for (TopicToPost topictoDocument : topicToPosts)
			{
				pst.setInt(1, topictoDocument.getTopicID());
				pst.setInt(2, topictoDocument.getPostID());
				pst.setDouble(3, topictoDocument.getPostDistributionValue());
				pst.addBatch();
			}
			pst.executeBatch();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void insertWords(DBHelper db, List<Word> words)
	{
		PreparedStatement pst = null;
		String sql = "insert into word(id,name) values (?,?)";
		try
		{
			pst = db.getConn().prepareStatement(sql);
			for (Word word : words)
			{
				if (word.getName()!=null) {
					
			
				pst.setInt(1, word.getId());
				pst.setString(2, word.getName());
				pst.addBatch();
				}
			}
			pst.executeBatch();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void insertTopics(DBHelper db, List<Topic> topics)
	{
		PreparedStatement pst = null;
		String sql = "insert into topics(topics.Id,topics.TopicName, topics.Topic) values (?,?,?)";
		try
		{
			pst = db.getConn().prepareStatement(sql);
			for (Topic topic : topics)
			{
				pst.setInt(1, topic.getIndex());
				//初始采用topic words的前20个词
//				String topicName="";
				
				String topWords="";
				List<String> tList=topic.getTopWords();
				for (String  t : tList) {
					topWords=topWords+" "+t;
				}
				pst.setString(2, topWords);
				pst.setString(3, topWords);
				pst.addBatch();
			}
			pst.executeBatch();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}


	public static  List<String> getPostsIDs(DBHelper db)
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT posts.Id,  posts.Title FROM stackoverflow.posts ";
		ResultSet rs = db.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				result.add(rs.getString(1));
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}

	public static List<Word> getWords(DBHelper db)
	{
		List<Word> result = new ArrayList<Word>();
		String sql = "SELECT id,name  FROM word";
		ResultSet rs = db.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				Word word = new Word();
				word.setId(rs.getInt(1));
				word.setName(rs.getString(2));
				result.add(word);
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}

	public static List<TopicToPost> getTopicToPosts(DBHelper db)
	{
		List<TopicToPost> result = new ArrayList<TopicToPost>();
		String sql = "SELECT  topictoposts.TopicId,   topictoposts.PostId,   topictoposts.postdistributionvalue FROM stackoverflow.topictoposts";

		ResultSet rs = db.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				TopicToPost topicToDocument = new TopicToPost();
				topicToDocument.setTopicID(rs.getInt(1));
				topicToDocument.setPostID(rs.getInt(2));
				topicToDocument.setPostDistributionValue(rs.getFloat(3));
				result.add(topicToDocument);
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}


	/*
	 * create table if not exists topicToWord ( topicID int not null, wordID
	 * int, wordDistributionValue double, projectPath varchar(1000) );
	 */
	public static List<TopicToWord> getTopicToWord(DBHelper db)
	{

		List<TopicToWord> result = new ArrayList<TopicToWord>();
		String sql = "SELECT topicID,wordID, wordDistributionValue FROM topictoword";
		ResultSet rs = db.executeQuery(sql);
		try
		{
			while (rs.next())
			{
				TopicToWord topicToWord = new TopicToWord();
				topicToWord.setTopicID(rs.getInt(1));
				topicToWord.setWordID(rs.getInt(2));
				topicToWord.setWordDistributionValue(rs.getFloat(3));
				result.add(topicToWord);
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}


	//清理数据库中的这些表格：（poststokens）stem , topics, topictoposts, topictoword , word 
	public static void  clearWord( DBHelper db)
	{
		String sql= "delete  from stackoverflow.Word";
		PreparedStatement pst = null;

		try
		{
			pst = db.getConn().prepareStatement(sql);
			pst.execute();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
	public static void  clearTopicToWord( DBHelper db)
	{
		String sql= "delete  from stackoverflow.topictoword";
		PreparedStatement pst = null;

		try
		{
			pst = db.getConn().prepareStatement(sql);
			pst.execute();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void  clearTopicToPosts( DBHelper db)
	{
		String sql= "delete  from stackoverflow.topictoposts";
		PreparedStatement pst = null;

		try
		{
			pst = db.getConn().prepareStatement(sql);
			pst.execute();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void  clearTopics( DBHelper db)
	{
		String sql= "delete  from stackoverflow.topics";
		PreparedStatement pst = null;

		try
		{
			pst = db.getConn().prepareStatement(sql);
			pst.execute();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void  clearPostsTokens( DBHelper db)
	{
		String sql= "delete  from stackoverflow.poststokens";
		PreparedStatement pst = null;

		try
		{
			pst = db.getConn().prepareStatement(sql);
			pst.execute();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void  clearStem( DBHelper db)
	{
		String sql= "delete  from stackoverflow.stem";
		PreparedStatement pst = null;

		try
		{
			pst = db.getConn().prepareStatement(sql);
			pst.execute();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

}
