package cn.edu.fudan.se.helpseeking.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * @描述：公共数据访问交互工具类（注意：在其他机器使用数据库，请修改数据库连接账号(username)及密码(password)，连接地址(url)"）
 * @作者：王虎
 * @版本：1.0
 * @开发时间：2013-4-20上午11:30:10
 */
public class DbUtils {
private Connection conn = null;
private Statement stmt = null;
private ResultSet rs = null;
private String username = "root";
private String password = "mysql";
private String url="jdbc:mysql://localhost:3308/classtest";
/**
 * @描述：修改数据库默认url
 * @作者：王虎
 * @参数：@param url
 * @返回值：void
 */
public void setUrl(String url) {
	this.url = url;
}
/**
 * @描述：修改默认账号username;
 * @作者：王虎
 * @参数：@param username
 * @返回值：void
 */
public void setUsername(String username) {
	this.username = username;
}
/**
 * @描述：修改默认密码password;
 * @作者：王虎
 * @参数：@param password
 * @返回值：void
 */
public void setPassword(String password) {
	this.password = password;
}
/**
 * @描述:用来在类中最先执行数据库的驱动加载
 * @作者：王虎
 */
static {
	try{
		Class.forName("com.mysql.jdbc.Driver");
	}catch(ClassNotFoundException e){
		System.out.println("找不到类，请检查驱动包是否导入");
		e.printStackTrace();
	}	
	}	
/**
 * @描述：建立数据库连接并返回给开发者连接对象
 * @作者：王虎
 * @参数：@return
 * @返回值：Connection
 */
public Connection getConn(){
	try{
		conn = DriverManager.getConnection(url,username,password);
		return conn;
	}catch(Exception e){
		System.out.println("连接不上数据库，请检查连接地址（url）,账号（username）,密码（password）");
		e.printStackTrace();
		return null;
	}
}
/**
 * @描述：执行更新数据库的sql语句
 * @作者：王虎
 * @参数：@param sql
 * @参数：@return
 * @返回值：int  = 影响表中记录行数  更新成功 = >0的整数  更新失败 = 0
 */
public int updata(String sql){
	try{
		stmt = getConn().createStatement();
		return stmt.executeUpdate(sql);
	}catch(SQLException e){
		System.out.println("更新失败，请检查sql语法 及格式");
		e.printStackTrace();
		return -1;
	}
}

/**
 * @描述：执行查询数据库的SQL语句
 * @作者：王虎
 * @参数：@param sql
 * @参数：@return
 * @返回值：ArrayList<String[]>
 */
public ArrayList<String[]> find (String sql){
	ArrayList<String[]>  rsList=null;
	try {
		rsList = new ArrayList<String[]>();
		getConn();
		//执行查询
		ResultSet rs=stmt.executeQuery(sql);
		//结果集结构对象
		ResultSetMetaData rsmd =rs.getMetaData();		
		while(rs.next()){		
			String[] infos=new String[rsmd.getColumnCount()];
			for(int i=0;i<rsmd.getColumnCount();i++){
				infos[i]=rs.getString(rsmd.getColumnName(i+1));
			}		
			rsList.add(infos);
		}
		return rsList;		
	} catch (Exception e) {
		System.out.println("查询失败，请检查sql语法 及格式");
		e.printStackTrace();
		return null;
	}
}
/**
 * @描述：释放连接时内存
 * @作者：王虎
 * @参数：无
 * @返回值：void
 */
public void close(){
	if(rs != null){
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	if(stmt != null){
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	if(conn != null){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
}