package cn.edu.fudan.se.helpseeking.test.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.cli.*;

import cn.edu.fudan.se.helpseeking.bean.PostsTokensDataType;
import cn.edu.fudan.se.helpseeking.bean.Word;
import cn.edu.fudan.se.helpseeking.dal.HelpSeekingDAL;
import cn.edu.fudan.se.helpseeking.dal.LDADAL;
import cn.edu.fudan.se.helpseeking.lda.LDA;
import cn.edu.fudan.se.helpseeking.preprocessing.DozPreprocessing;
import cn.edu.fudan.se.helpseeking.preprocessing.LDAOperator;
import cn.edu.fudan.se.helpseeking.preprocessing.TokenExtractor;
import cn.edu.fudan.se.helpseeking.utils.CommUtil;

public class TestAll {

	public static void main(String[] args) {

	TestAll ta=new TestAll();
	
	ta.generatePostsTokens();
	
		}
	
	public void generatePostsTokens()
	{
		List<PostsTokensDataType> postsTokens=new ArrayList<PostsTokensDataType>();
		int id=0;
      	int limitCount=10;
		int startCount=0; //表中记录是以0开始对应第一条记录
		int roundCount=15;
		
	
			for(int i=0;i<roundCount;i++)
			{		
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
				
	int j=0;
				//dbhelper是一个实例，其中只维护一个resultset 所以 对resultset不能嵌套使用
				while (j<10)
				{		
					PostsTokensDataType  row=new PostsTokensDataType();
					id=id+1;
					row.setId(String.valueOf(id));
					postsTokens.add(row);
					System.out.println("id="+id);
					j++;
				}

	


			}

		

	}
	
	public void testInsertTopic() {
		// TODO Auto-generated method stub
		DozPreprocessing  mydoz=new DozPreprocessing();
		
		mydoz.preMYSQLParameter();
		LDAOperator ldaOperator=new LDAOperator();
		ldaOperator.getInstance();
		ldaOperator.setDB(mydoz.getDb());
		ldaOperator.setup();
		ldaOperator.loadPostsID();
//       ldaOperator.loadWords();
		ldaOperator.analysisFiles();
		ldaOperator.generateTopicList();//由于改进需求，直接从数据库中获得post ；写入postIdList .(金水实现的代码中是将项目文件分析后写入数据库的，所以每个文件的tokens需要再次转载)
	    ldaOperator.insertTopics();
		
		mydoz.exitMYSQLConn();
	}
	
	public void testQueryPostID() {
		// TODO Auto-generated method stub
				DozPreprocessing  mydoz=new DozPreprocessing();
				
				mydoz.preMYSQLParameter();
				LDAOperator ldaOperator=new LDAOperator();
				ldaOperator.getInstance();
				ldaOperator.setDB(mydoz.getDb());
	
				ldaOperator.loadPostsID();//由于改进需求，直接从数据库中获得post ；写入postIdList .(金水实现的代码中是将项目文件分析后写入数据库的，所以每个文件的tokens需要再次转载)
			
				mydoz.exitMYSQLConn();
		
	}
	
	public void testSaveDB() {
		// TODO Auto-generated method stub
		DozPreprocessing  mydoz=new DozPreprocessing();
		
		mydoz.preMYSQLParameter();
		LDAOperator ldaOperator=new LDAOperator();
		ldaOperator.getInstance();
		ldaOperator.setDB(mydoz.getDb());
		ldaOperator.analysisFiles();
		
		ldaOperator.loadWords();//由于改进需求，直接从数据库中获得 words 写入wordMapHashMap
		ldaOperator.loadPostsID();//由于改进需求，直接从数据库中获得post ；写入postIdList .(金水实现的代码中是将项目文件分析后写入数据库的，所以每个文件的tokens需要再次转载)
		
		//mydoz.clearDBTables();  手工处理不要全删
		
		ldaOperator.SaveDB();
		mydoz.exitMYSQLConn();
	}
	

	public static void testInsertWords() {
		DozPreprocessing  mydoz=new DozPreprocessing();
		
		mydoz.preMYSQLParameter();
		LDAOperator ldaOperator=new LDAOperator();
		ldaOperator.getInstance();
		ldaOperator.setDB(mydoz.getDb());
		List<Word> words=new ArrayList<Word>();
		   Word w=new Word();
		   w.setId(99999);
		   w.setName("99999");
		   words.add(w);
		   Word w1=new Word();
		   w1.setId(99998);
		   w1.setName("99998");
		   words.add(w1);
		   Word w2=new Word();
		   w2.setId(99997);
		   w2.setName(null);
		   words.add(w2);
		
		LDADAL.insertWords(mydoz.getDb(), words);
		
		mydoz.exitMYSQLConn();
	}
	
public void testcommandline() {
	// TODO Auto-generated method stub
	String [] args = null;
	args[0]="1";
	args[1]="2";
	
	 try {
         Options JDUL = new Options();

         JDUL.addOption("h"   ,false, "Print help for JDUL");
         JDUL.addOption("END" ,true,  "select the Big or Little Endian");
         JDUL.addOption("SSM" ,true,  "select MSSM or ASSM");

         BasicParser parser = new BasicParser();
       CommandLine cl = parser.parse(JDUL, args);

         if( cl.hasOption('h') ) {
             HelpFormatter f = new HelpFormatter();
            f.printHelp("OptionsTip", JDUL);
         }
         else{
             System.out.println(cl.getOptionValue("END"));
             System.out.println(cl.getOptionValue("SSM"));
         }
     }
     catch(ParseException e) {
         e.printStackTrace();
     }
}

	public static String pureTokens(String content) {
//	   content="<p>Besides Stack Overflow of course, here are mine.</p>&#xA;&#xA;<ul>&#xA;<li>Many have already mentioned <a href= http://www.hanselminutes.com/hanselminutes%5Fmp3Direct.xml  rel= nofollow >Hanselminutes</a>.</li>&#xA;<li>Some have already mentioned <a href= http://feeds.feedburner.com/netRocksFullMp3Downloads  rel= nofollow >.NET Rocks!</a> </li>&#xA;<li>Not quite as many have mentioned <a href= http://feeds.feedburner.com/RunasRadio  rel= nofollow >RunAs Radio</a>.</li>&#xA;</ul>&#xA;&#xA;<p>I can't believe the size of some of these lists. With podcasts, I like to keep the list short and the quality high. As such, I tend to skip the aggregates like ITConversations et. al.</p>&#xA;";
		  content=pureTokens(content);
		String[] myList=content.split("[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]");
		for (String str : myList) {
			System.out.println(str);
		}
		List<String> list=new ArrayList<String>();
		for (int i = 0; i < myList.length; i++) {
			if(!myList[i].isEmpty())
			list.add(myList[i]);
		}
		content=CommUtil.tokenListToString(list);
	
		return content;
	}

}
