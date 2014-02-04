package cn.edu.fudan.se.helpseeking.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.fudan.se.helpseeking.Activator;
import cn.edu.fudan.se.helpseeking.bean.SortDataType;
import cn.edu.fudan.se.helpseeking.bean.Word;
import cn.edu.fudan.se.helpseeking.bean.lda.Post;
import cn.edu.fudan.se.helpseeking.bean.lda.Topic;
import cn.edu.fudan.se.helpseeking.bean.lda.TopicTable;
import cn.edu.fudan.se.helpseeking.bean.lda.TopicToPost;
import cn.edu.fudan.se.helpseeking.bean.lda.TopicToWord;
import cn.edu.fudan.se.helpseeking.dal.LDADAL;
import cn.edu.fudan.se.helpseeking.lda.LDA;
import cn.edu.fudan.se.helpseeking.services.LDAService;
import cn.edu.fudan.se.helpseeking.utils.CommUtil;
import cn.edu.fudan.se.helpseeking.utils.DBHelper;
import cn.edu.fudan.se.helpseeking.utils.FileHelper;
import cn.edu.fudan.se.helpseeking.utils.INIHelper;

public class LDAOperator
{
	private  String[] argStr;  //=new String[40] 大小最好是确定的与输入的参数对应。
	private static List<String> argStrList;
	private static LDAOperator instance;
	private static final String sectionName = "LDA";
	private static String tempFileName;
	private  DBHelper db=null;


	String directory = CommUtil.getCurrentProjectPath();
	
	float[][] posts_topic = null;

	private final int PostsNumber = 10;

	private int nposts = 1;

	private int ntopics = 1;

	private int ntopWordscount = -1;

	private int nwords = -1;

	private final String Topic_Keyword = "Topic ";

	float[][] topic_word = null;

	List<Topic> topicList=null;
    //List<String>  PostIdList=new ArrayList();
    List<String> PostIdList = null;
	Map<Integer, String> wordMapHashMap = new HashMap<Integer, String>();

	public  LDAOperator()
	{
        super();
	}

	public static LDAOperator getInstance()
	{
		if (instance == null) instance = new LDAOperator();
		return instance;
	}

	public void analysisFiles()
	{
		analysisOthers();
		
		analysisWordMap();  //得到wordMapHashMap  	//需要读出生成的文件，并写入wordMapHashMap，并写入到word表中  见方法 analysisWordMap()
		//分析topic与post关系文档
		analysisTheta();
		//分析topic以及组成topic的词
		analysisTwords();
		//分析词word与topic的关系文档
		analysisPhi();
	}

	private void analysisOthers()
	{
		String[] mapArray = FileHelper.getContentArray(CommUtil
				.getCurrentProjectPath() + "\\LDAResult\\model-final.others");
		HashMap<String, String> othersHashMap = new HashMap<String, String>();

		for (int i = 0; i < mapArray.length; i++)
		{

			String[] tempString = mapArray[i].split("=");

			tempString[0].toString().toLowerCase().trim();
			tempString[1].toString().toLowerCase().trim();
			othersHashMap.put(tempString[0], tempString[1]);

		}
		ntopics = Integer.parseInt(othersHashMap.get("ntopics"));
		nposts = Integer.parseInt(othersHashMap.get("ndocs"));
		nwords = Integer.parseInt(othersHashMap.get("nwords"));

	}

	private void analysisPhi()
	{
		String[] mapArray = FileHelper.getContentArray(CommUtil
				.getCurrentProjectPath() + "\\LDAResult\\model-final.phi");
		topic_word = new float[ntopics][nwords];
		for (int i = 0; i < ntopics; i++)
		{
			String[] values = mapArray[i].split("[ ]");
			for (int j = 0; j < nwords; j++)
			{
				topic_word[i][j] = Float.parseFloat(values[j]);
			}
		}
	}

	private void analysisTheta()
	{
		String[] mapArray = FileHelper.getContentArray(CommUtil
				.getCurrentProjectPath() + "\\LDAResult\\model-final.theta");
		posts_topic = new float[nposts][ntopics];
		for (int i = 0; i < nposts; i++)
		{
			String[] values = mapArray[i].split("[ ]");
			for (int j = 0; j < ntopics; j++)
			{
				posts_topic[i][j] = Float.parseFloat(values[j]);
			}
		}

	}

	public void analysisTwords()
	{
		topicList = new ArrayList<Topic>();
		String[] mapArray = FileHelper.getContentArray(CommUtil
				.getCurrentProjectPath() + "\\LDAResult\\model-final.twords");

		String[] tempString;
		Topic newTopic = null;
		for (int i = 0; i < mapArray.length; i++)
		{

			if (mapArray[i].contains(Topic_Keyword))
			{
				newTopic = new Topic();
				tempString = mapArray[i].split(" ");
				tempString[1] = tempString[1].replace("th:", " ").trim();
				newTopic.setIndex(Integer.parseInt(tempString[1]));

				topicList.add(newTopic);
			}
			else
			{
				if (mapArray[i].trim().split(" ").length == 1) continue;
				String word = mapArray[i].trim().split(" ")[0];
				if (word.trim().length() > 0 && newTopic != null) newTopic
						.addTopWord(word);
			}
		}

	}

	public List<Topic> getTopicList()
	{
		return topicList;
	}

	private void analysisWordMap()
	{
		String[] mapArray = FileHelper.getContentArray(CommUtil
				.getCurrentProjectPath() + "\\LDAResult\\wordmap.txt");
		for (int i = 1; i < mapArray.length; i++)
		{
			String[] tempString = mapArray[i].split(" ");

			tempString[0] = tempString[0].toString().toLowerCase().trim();
			tempString[1] = tempString[1].toString().toLowerCase().trim();

			//如果单词是空值，放弃它

			if (tempString[1].length() != 0)
			{
			wordMapHashMap.put(Integer.parseInt(tempString[1]), tempString[0]);
			}

//			if (tempString[1].length() == 0)
//			{
//				tempString[1] = tempString[0];
//				tempString[0] = " ";
//			}
//
//			wordMapHashMap.put(Integer.parseInt(tempString[1]), tempString[0]);
		}

	}

	
	public  void doLDAAnalysis(DBHelper inDB)
	{
		
		System.out.println("LDA参数准备");
//		LDAOperator ldaOperator = LDAOperator.getInstance();
//		
//		ldaOperator.setup();
//		ldaOperator.setTokensInfo();
		
			
		setup();
		setTokensInfo();
		argStr=new String[argStrList.size()];
		
		for (int i=0;i<argStrList.size();i++)
		{
			argStr[i]=argStrList.get(i);
			
		}
		
		//提醒在做LDA时为了能够足够的内存使用，迭代时，回写模型的次数多一些。
		System.out.println("调用LDA");
		LDA.grandLDAmain(argStr);
		
		//由于改进需求，直接从数据库中获得post ；写入 .(金水实现的代码中是将项目文件分析后写入数据库的，所以每个文件的tokens需要再次转载)
		System.out.println("postIdList调入中");
		loadPostsID();
		
		System.out.println("分析LDA结果文件");
		analysisFiles();
		
		System.out.println("写入数据库中");
		SaveDB();
		//为了节约内存  采用分析一个数据就写入数据库 (看样子还要分析一下所读取的LDA结果文件之间的依赖，要不然出现空LIST，插入空表)
       //		writeLDAResultToDB();
		
	}

	public void writeLDAResultToDB() {
		
		//为了节约内存  采用分析一个数据就写入数据库 (看样子还要分析一下所读取的LDA结果文件之间的依赖，要不然出现空LIST，插入空表)
		//目前这样读文件和写文件的顺序有问题
		
		// LDA PARAMETER FOR TOPIC MODEL
		analysisOthers();
		
	
		
		//WORD MAP 
		analysisWordMap();
		insertWords();
		
		//TOPIC AND DOCUMENT (POST) DISTURBUTION
		analysisTheta();
		insertTopicToPosts();
	
	
		// TOPIC LIST
		analysisTwords();
		generateTopicList();
		insertTopics();
		
		//WORD AND TOPIC DISTURBUTION
		analysisPhi();
		insertTopicToWords();


	} 
	
	public void setDB(DBHelper inDB) {
		this.db=inDB;
	}
//需要调用
	public void generateTopicList()
	{
		topicList = new ArrayList<Topic>();
		for (int topicID = 0; topicID < ntopics; topicID++)
		{
			List<TopicToWord> topicWords = LDADAL.getTopWordsByTopic(db,topicID,
					ntopWordscount);
			Topic topic = new Topic();
			topic.setIndex(topicID);
			for (int i = 0; i < topicWords.size(); i++)
			{
				topic.getTopWords().add(
						getWordNameByID(topicWords.get(i).getWordID()));//根据word ID 去具体的word

			}
			List<String> posts = getTopicRelatedPosts(topicID);
			topic.setRelatedPosts(posts);
			topicList.add(topic);
		}
	}

	
	//use some keywords to location the related topic
	//用于给定的一组关键词寻找与其相关的可能的topic
	public List<Topic> getRelatedTopic(String queryWords)
	{

		String[] keywords = queryWords.split("[ ]");
		SortDataType<Topic> sortedTopic = new SortDataType<Topic>();
		for (String keyword : keywords)
		{
			for (Topic ldaTopic : topicList)
			{
				if (ldaTopic.getTopWords().contains(keyword)) sortedTopic
						.addContent(ldaTopic);
			}
		}
		sortedTopic.sort();
		return sortedTopic.getContentList();
	}

	public List<String> getTopicRelatedPosts(int topicIndex)
	{
		float[] postArray = new float[nposts];
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < nposts; i++)
		{
			postArray[i] = posts_topic[i][topicIndex];
		}
		try
		{
			for (int i = 0; i < PostsNumber; i++)
			{
				float maxValue = postArray[0];
				int indexOfMaxValue = 0;
				for (int j = 1; j < nposts; j++)
				{
					if (postArray[j] > maxValue)
					{
						maxValue = postArray[j];
						indexOfMaxValue = j;
					}
				}
				//新加入 防止越界 和空值  list 从0开始取值（list.get(0)）和数组是通用的。
				if ((indexOfMaxValue<PostIdList.size())&&(indexOfMaxValue<postArray.length) && (PostIdList.get(indexOfMaxValue)!=null)) {
				result.add(PostIdList.get(indexOfMaxValue));
				postArray[indexOfMaxValue] = 0;
				} 
				
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public List<String> getTopicTopAllPostsAndSort(int topicIndex)
	{
		float[] postsArray = new float[nposts];
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < nposts; i++)
		{
			postsArray[i] = posts_topic[i][topicIndex];
		}
		for (int i = 0; i < nposts; i++)
		{
			float maxValue = postsArray[0];
			int indexOfMaxValue = 0;
			for (int j = 1; j < nposts; j++)
			{
				if (postsArray[j] > maxValue)
				{
					maxValue = postsArray[j];
					indexOfMaxValue = j;
				}
			}
			result.add(PostIdList.get(indexOfMaxValue));
			postsArray[indexOfMaxValue] = 0;
		}
		return result;
	}
	
	public List<String> getTopicTopAllPosts(int topicIndex)
	{
		float[] documentArray = new float[nposts];
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < nposts; i++)
		{
			documentArray[i] = posts_topic[i][topicIndex];
		}
		for (int i = 0; i < nposts; i++)
		{
			float maxValue = documentArray[0];
			int indexOfMaxValue = 0;
			for (int j = 1; j < nposts; j++)
			{
				if (documentArray[j] > maxValue)
				{
					maxValue = documentArray[j];
					indexOfMaxValue = j;
				}
			}
			result.add(PostIdList.get(indexOfMaxValue));
			documentArray[indexOfMaxValue] = 0;
		}
		return result;
	}

		
	private String getWordNameByID(int wordID)
	{
		return wordMapHashMap.get(wordID);
	}

//	private void insertPosts()
//	{
//		List<Post> documents = new ArrayList<Post>();
//		for (int i = 0; i < PostIdList.size(); i++)
//		{
//			Post newDocument = new Post();
//			newDocument.setId(i);
//			newDocument.setName(PostIdList.get(i));
//			documents.add(newDocument);
//		}
//
//		LDADAL.insertDocuments(documents);
//
//	}

	private void insertTopicToPosts()
	{

		List<TopicToPost> topicToPosts = new ArrayList<TopicToPost>();
		for (int i = 0; i < posts_topic.length; i++)
			for (int j = 0; j < posts_topic[i].length; j++)
			{
				TopicToPost newTopicToPost = new TopicToPost();
				newTopicToPost.setPostID(Integer.valueOf(PostIdList.get(i))); //改造，从postidList中找到对应post的ID值
				newTopicToPost.setTopicID(j);
				newTopicToPost.setPostDistributionValue(posts_topic[i][j]);
				topicToPosts.add(newTopicToPost);
			}
		LDADAL.insertTopicToPost(db,topicToPosts);
	}

	private void insertTopicToWords()
	{
		List<TopicToWord> topicToWords = new ArrayList<TopicToWord>();
		for (int i = 0; i < topic_word.length; i++)
			for (int j = 0; j < topic_word[i].length; j++)
			{
				TopicToWord newTopicToWord = new TopicToWord();
				newTopicToWord.setTopicID(i);
				newTopicToWord.setWordID(j);
				newTopicToWord.setWordDistributionValue(topic_word[i][j]);
				topicToWords.add(newTopicToWord);
			}
		LDADAL.inserTopicToWord(db,topicToWords);
	}

	private void insertWords()
	{
		List<Word> words = new ArrayList<Word>();
		for (int i = 0; i < wordMapHashMap.size(); i++)
		{
			Word newWord = new Word();
			if(wordMapHashMap.get(i)!=null)
			{
			newWord.setId(i);
			newWord.setName(wordMapHashMap.get(i));
			words.add(newWord);
			}
		}
		LDADAL.insertWords(db,words);

	}
	
	public void insertTopics() {
		
		
		 LDADAL.insertTopics(db, topicList);
		
	}


	public void loadDataFromDB()
	{
		loadPostsID();
		loadWords();
		loadOther();
		loadTopicToPosts();
		loadTopicToWords();
		generateTopicList();
	}

	public void loadPostsID()
	{
		PostIdList = LDADAL.getPostsIDs(db);
	
	}

	private void loadOther()
	{
		nwords = wordMapHashMap.size();
		nposts = PostIdList.size();
		ntopics = LDADAL.getTopicCount(db);

	}

	private void loadTopicToPosts()
	{
		List<TopicToPost> topicToPosts = LDADAL.getTopicToPosts(db);
		posts_topic = new float[nposts][ntopics];
		for (int i = 0; i < topicToPosts.size(); i++)
		{
			TopicToPost newTopicToPost = topicToPosts.get(i);
			posts_topic[newTopicToPost.getPostID()][newTopicToPost
					.getTopicID()] = newTopicToPost
					.getPostDistributionValue();

		}

	}

	private void loadTopicToWords()
	{
		List<TopicToWord> topicToWords = LDADAL.getTopicToWord(db);
		topic_word = new float[ntopics][nwords];
		for (int i = 0; i < topicToWords.size(); i++)
		{
			TopicToWord newTopicToWord = topicToWords.get(i);
			topic_word[newTopicToWord.getTopicID()][newTopicToWord.getWordID()] = newTopicToWord
					.getWordDistributionValue();
		}

	}

	public void loadWords()
	{
		List<Word> words = LDADAL.getWords(db);
		wordMapHashMap.clear();
		for (int i = 0; i < words.size(); i++)
		{
			wordMapHashMap.put(words.get(i).getId(), words.get(i).getName());
		}

	}

	public void SaveDB()
	{
		
		generateTopicList();
		//insertPosts();
		System.out.println("写入LDA后词汇表WORD");
		insertWords();
		System.out.println("写入LDA后topic与posts关系");
		insertTopicToPosts();
		System.out.println("写入LDA后topic与word关系");
		insertTopicToWords();
		System.out.println("写入LDA后topic表");
		insertTopics();
	
	}



	public void setup()
	{
		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath()
				+ "\\conf.ini");
		String alphaValue = iniHelper.getValue(sectionName, "alpha", "0.5");
		String betaValue = iniHelper.getValue(sectionName, "beta", "0.1");
		String ntopicsValue = iniHelper.getValue(sectionName, "ntopics", "100");
		String nitersValue = iniHelper.getValue(sectionName, "niters", "100");
		String savestepValue = iniHelper.getValue(sectionName, "savestep",
				"100");
		ntopWordscount = Integer.parseInt(iniHelper.getValue(sectionName,
				"twords", "20"));
		
//		argStr = "-est -alpha " + alphaValue + " -beta " + betaValue + " -dir "
//				+ CommUtil.getCurrentProjectPath() + "\\LDAResult"
//				+ " -ntopics " + ntopicsValue + " -niters " + nitersValue
//				+ " -savestep " + savestepValue + " -twords " + ntopWordscount;
		
		//初始化argstrlist  必须初始化
		argStrList=new ArrayList<String>();
		argStrList.add("-est");
		argStrList.add("-alpha");		
		argStrList.add(alphaValue);
		argStrList.add("-beta");
		argStrList.add(betaValue);
		argStrList.add("-dir");
		argStrList.add(CommUtil.getCurrentProjectPath() + "\\LDAResult");
		argStrList.add("-ntopics");
		argStrList.add( ntopicsValue);
		argStrList.add("-niters");
		argStrList.add(nitersValue);
		argStrList.add("-savestep");
		argStrList.add(savestepValue);
		argStrList.add("-twords");
		argStrList.add(String.valueOf(ntopWordscount));
		
	}


	private void setTokensInfo()
	{
		//TODO rename tempFilename   postsTokensFilename
		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath()
				+ "\\conf.ini");
		String tempFileName = iniHelper.getValue("IDENTIFIEREXTRACTOR",
				"postsForLDAFileName", "postsTokens.txt");
//		tempFileName=CommUtil.getCurrentProjectPath()+"\\"+tempFileName;
//			argStr = argStr + " -dfile " + tempFileName;
			
			argStrList.add("-dfile");
			argStrList.add(tempFileName);

	}




}
