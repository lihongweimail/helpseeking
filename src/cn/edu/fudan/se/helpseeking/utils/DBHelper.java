package cn.edu.fudan.se.helpseeking.utils;


import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import cn.edu.fudan.se.helpseeking.Activator;
import cn.edu.fudan.se.helpseeking.dal.HelpSeekingDAL;


public class DBHelper  extends Object
{


	public final String TEST_TABLE_NAME = "test";

	private  Connection	conn=null;
	private  Statement	stmt=null;
	private  String DBFileURL="";
	private  boolean isOpen=false;

	/**
	 * @return the isOpen
	 */
	public boolean isOpen() {
		return isOpen;
	}



	/**
	 * @param isOpen the isOpen to set
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * @return the dBFileURL
	 */
	public String getDBFileURL() {
		return DBFileURL;
	}

	/**
	 * @param dBFileURL the dBFileURL to set
	 */
	public void setDBFileURL(String dBFileURL) {
		DBFileURL = dBFileURL;
	}

	public  DBHelper() {
		super();
	}

	public void closeConn()
	{
		try
		{
			conn.commit();
			conn.close();		
			setOpen(false);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void closeMYSQLConn()
	{
		try
		{
			conn.commit();
			conn.close();		
			setOpen(false);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void closePluginConn()
	{
		try
		{
			conn.commit();
			conn.close();		
			setOpen(false);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public ResultSet executeQuery(String sql)
	{
		ResultSet rs = null;
		try
		{

			rs = stmt.executeQuery(sql);			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}

		return rs;
	}
	public void executeUpdate(String sql)
	{
		try
		{			
			stmt.executeUpdate(sql);
		}
		catch(Exception exp)
		{			
			exp.printStackTrace( );
		}

	}
	public Connection getConn()
	{
		return conn;
	}


	public void initialDB()
	{		
		try
		{
			stmt.executeUpdate(FileHelper.getContent(CommUtil.getCurrentProjectPath() + "\\createDB.sql"));
		}
		catch (SQLException e)
		{			
			e.printStackTrace();
		}
	}
	public void initialPluginDB(String localPath)
	{		
		try
		{
			stmt.executeUpdate(FileHelper.getContent( localPath+"\\"+"createDB.sql"));
		}
		catch (SQLException e)
		{			
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public Connection openConn()
	{
		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath() + "\\conf.ini");
		String JDBC_DRIVER = iniHelper.getValue("DB", "JDBC_DRIVER", "");
		String DB_URL = iniHelper.getValue("DB", "DB_URL", "");	
		String DB_USER = iniHelper.getValue("DB", "DB_USER", "");
		String DB_PASSWORD = iniHelper.getValue("DB", "DB_PASSWORD", "");
		String DB_FILENAME =  iniHelper.getValue("DB", "DB_FILENAME", "");	
		setDBFileURL(CommUtil.getCurrentProjectPath()+"\\"+DB_FILENAME);
		try
		{
			File file = new File(DB_FILENAME);
			if(file.exists() == false)			
				file.createNewFile();		
			Class<Driver> jdbcDriver = (Class<Driver>) Class.forName(JDBC_DRIVER);
			DriverManager.registerDriver( jdbcDriver.newInstance());
			if(JDBC_DRIVER.contains("mysql"))
			{
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);	
			}
			else
			{
				conn = DriverManager.getConnection(DB_URL + DB_FILENAME);		
			}
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			//重要的数据库初始化工作
			initialDB();
			setOpen(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		
		return conn;
	}

	@SuppressWarnings("unchecked")
	public Connection openPluginConn()
	{
		INIHelper iniHelper = new INIHelper( "conf.ini");
		String JDBC_DRIVER = iniHelper.getValue("DB", "JDBC_DRIVER", "");
		String DB_URL = iniHelper.getValue("DB", "DB_URL", "");	
		String DB_USER = iniHelper.getValue("DB", "DB_USER", "");
		String DB_PASSWORD = iniHelper.getValue("DB", "DB_PASSWORD", "");
		String DB_FILENAME =  iniHelper.getValue("DB", "DB_FILENAME", "");	
		setDBFileURL(DB_FILENAME);
		try
		{
			File file = new File(DB_FILENAME);
			if(file.exists() == false)			
				file.createNewFile();		
			Class<Driver> jdbcDriver = (Class<Driver>) Class.forName(JDBC_DRIVER);
			DriverManager.registerDriver( jdbcDriver.newInstance());
			if(JDBC_DRIVER.contains("mysql"))
			{
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);	
			}
			else
			{
				conn = DriverManager.getConnection(DB_URL + DB_FILENAME);		
			}
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			//重要的数据库初始化工作
			initialDB();
			setOpen(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		
		return conn;
	}


	/**
	 * public Connection openConn(String DBFileName)
	 * 	用于打开配置数据库与当前数据库一致的，指定文件名的数据库文件，目的是用于将这个打开的数据库合并到当前应用使用的数据库中。
	 * 使用时需要实例化一个DBHelper 再使用这个openConn
	 * @param DBFileName  数据库文件名字
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Connection openConn(String DBFileName)
	{
		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath() + "\\conf.ini");
		String JDBC_DRIVER = iniHelper.getValue("DB", "JDBC_DRIVER", "");
		String DB_URL = iniHelper.getValue("DB", "DB_URL", "");	
		String DB_USER = iniHelper.getValue("DB", "DB_USER", "");
		String DB_PASSWORD = iniHelper.getValue("DB", "DB_PASSWORD", "");
		String DB_FILENAME =  DBFileName; 
		setDBFileURL(DB_FILENAME);
		try
		{
			File file = new File(DB_FILENAME);
			if(file.exists() == false)			
				file.createNewFile();		
			Class<Driver> jdbcDriver = (Class<Driver>) Class.forName(JDBC_DRIVER);
			DriverManager.registerDriver( jdbcDriver.newInstance());
			if(JDBC_DRIVER.contains("mysql"))
			{
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);	
			}
			else
			{
				conn = DriverManager.getConnection(DB_URL + DB_FILENAME);		
			}
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			setOpen(true);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		
		return conn;
	}


	@SuppressWarnings("unchecked")

	public Connection openPluginConn(String locationPath)
	{
		INIHelper iniHelper = new INIHelper(locationPath + "\\conf.ini");
		System.out.println(locationPath);
		String JDBC_DRIVER = iniHelper.getValue("DB", "JDBC_DRIVER", "");
		String DB_URL = iniHelper.getValue("DB", "DB_URL", "");	
		String DB_USER = iniHelper.getValue("DB", "DB_USER", "");
		String DB_PASSWORD = iniHelper.getValue("DB", "DB_PASSWORD", "");
		String DB_FILENAME =  iniHelper.getValue("DB", "DB_FILENAME", "");	
		//		DB_FILENAME =  locationPath+"\\"+DB_FILENAME; 
		setDBFileURL( locationPath+"\\"+DB_FILENAME);
		try
		{
			File file = new File(locationPath+"\\"+DB_FILENAME);
			if(file.exists() == false)			
				file.createNewFile();	

			Class<Driver> jdbcDriver = (Class<Driver>) Class.forName(JDBC_DRIVER);
			System.out.println("it is passing!");
			//TODO  Class.forName("org.sqlite.JDBC") why there are error about class not found exception "org.sqlite.JDBC"
			//why always say Class not found "org.sqlite.JDBC" on runtime ??? 2013-11-19
			DriverManager.registerDriver( jdbcDriver.newInstance());
			if(JDBC_DRIVER.contains("mysql"))
			{
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);	
			}
			else
			{
				//				conn = DriverManager.getConnection(DB_URL+ DB_FILENAME);	
				//				it got the filename : D:\lihongwei\developtools\eclipseRCP\helpseeking.db
				conn = DriverManager.getConnection(DB_URL+locationPath+"\\"+ DB_FILENAME);		
				//			why?	java.sql.SQLException: invalid database address: D:\lihongwei\research\research topic\help seeking\newIdeas\tool\HelpSeeking\helpseeking.db
			}
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			//重要的数据库初始化工作
			initialPluginDB(locationPath);

			setOpen(true);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		
		return conn;
	}



	//测试是否可以打开任意目录的数据库
	public static void main(String[] args) throws SQLException {
		//		DBHelper mydDbHelper=new DBHelper();
		//		mydDbHelper.openConn("C:\\Users\\Grand\\Desktop\\VideoCountPro\\pansenvideocount.db");
		//		ResultSet rset=mydDbHelper.executeQuery("select sessionIntentionID,sessionIntention from sessionIntention order by sessionIntentionID  asc");

		DBHelper mydDbHelper=new DBHelper();
		mydDbHelper.openConnMySQL("jdbc:mysql://10.131.252.224:3309/stackoverflow", "root", "root");
		ResultSet rset=null;
		try {
			rset = HelpSeekingDAL.queryPosts(mydDbHelper);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList result = new ArrayList();
		ResultSetMetaData rsmd = rset.getMetaData();
	
		while (rset.next())
		{
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				result.add(rset.getString(i));
			}
			System.out.println(result.toString());
			result.clear();
		}
			

		rset.close();

	}





	public Connection openConnMySQL(String url, String user, String passWord){

		try {
			Class.forName("com.mysql.jdbc.Driver"). newInstance();                         //加载数据库驱动 在官网www.mysql.org下载驱动mysql-connector-java-3.0-ga-bin.jar包
			//			String url=url  "jdbc:mysql://localhost:3306/db_database15";         //指定连接数据库的URL
			//			String user="root";                                          	//指定连接数据库的用户名
			//			String passWord="111";											//指定连接数据库的密码
			conn=DriverManager.getConnection(url,user,passWord);
			if (conn!=null) {
				System.out.println("数据库连接成功");
			}
			conn.setAutoCommit(false);//设置为false时可能批处理提交容易,并不是自动提交，这样不 需要频繁验证。处理回滚事务。
			stmt = conn.createStatement();
			//允许记录集中贯标方便移动
//			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); 
			setOpen(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}


	//	public Connection getConnectionSQL2000(String url, String user, String passWord) {
	//		try {
	//			//一种方法是应用驱动包jtds.jar，从“http://sourceforge.net”上面下载
	//	Class.forName("net.sourceforge.jtds.jdbc.Driver");   //加载数据库驱动
	//		    
	//		    //另一种方法是：
	//		    //如果应用驱动包msbase.jar  mssqlserver.jar  msutil.jar ，从“http://microsoft.com”上面下载
	//		    //只需要替换Class的forName()方法的参数为“com.microsoft.jdbc.sqlserver.SQLServerDriver;”
	//		    //并将DriverManager类的getConnection（）方法指定连接数据库的URL替换为：“jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=db_database15”
	//		    
	//		    
	//		    System.out.println("数据库驱动加载成功！");
	//		    //定义连接数据库的URL
	////		    String url="jdbc:jtds:sqlserver://localhost:1433;DatabaseName=db_database15";   // 数据库名称：db_database15
	////		    String userName="sa";															//连接数据库的用户名， 通常是系统分析员
	////		    String passWord="";																//连接数据库的密码
	//		    conn=DriverManager.getConnection(url,user,passWord);
	//		    if (conn!=null) {
	//				System.out.println("数据库连接成功！");
	//			}
	//		    
	//		    
	//			
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		
	//		return conn;
	//	}
	//	
	//	public Connection getConnectionSQL2005(String url, String user, String passWord)
	//	{
	//		try {
	//			//驱动程序使用sqljdbc.jar 在微软官网下载
	//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	//			System.out.println("数据库驱动加载成功！");
	//			//conn=DriverManager.getConnection("jdbc:sqlserver://localhost\\BROTHER:1433;DatabaseName=db_database15", "sa", "");
	//			conn=DriverManager.getConnection(url, user,passWord);
	//			//BROTHER数据库实例名
	//			if (conn!=null) {
	//				System.out.println("数据库连接成功！");
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		return conn;
	//	}
	//	

}

