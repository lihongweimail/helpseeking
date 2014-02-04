package cn.edu.fudan.se.helpseeking;

import java.io.IOException;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cn.edu.fudan.se.helpseeking.utils.CommUtil;
import cn.edu.fudan.se.helpseeking.utils.DBHelper;
import cn.edu.fudan.se.helpseeking.utils.GetPath;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "HelpSeeking"; //$NON-NLS-1$
	
	public static DBHelper helpseekingDB = new DBHelper();

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	private void preMYSQLParameter() {
		 //加载数据库驱动 在官网www.mysql.org下载驱动mysql-connector-java-3.0-ga-bin.jar包
		String url="jdbc:mysql://10.131.252.224:3309/stackoverflow";         //指定连接数据库的URL
//		String url="jdbc:mysql://localhost:3306/stackoverflow";         //指定本机连接数据库的URL
		String user="root";                                          	//指定连接数据库的用户名
		String passWord="root";											//指定连接数据库的密码
		
		helpseekingDB.openConnMySQL(url, user, passWord);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		preMYSQLParameter();
		super.start(context);
		plugin = this;
		
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		helpseekingDB.closeMYSQLConn();
		super.stop(context);
		
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
