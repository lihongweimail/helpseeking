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
public class CopyOfActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "HelpSeeking"; //$NON-NLS-1$
	
	private static DBHelper helpseekingDB = new DBHelper();

	// The shared instance
	private static CopyOfActivator plugin;
	
	/**
	 * The constructor
	 */
	public CopyOfActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		helpseekingDB.openPluginConn(getProjectPath());
		super.start(context);
		plugin = this;
		
	}
    public  String getProjectPath() {
    	String packageName = this.getClass().getResource("").getPath();
    	packageName = packageName.replace("/", "\\");
    	System.out.println("包名："+packageName);
    	String projectPath = null;
    	try {
    	    String packageFullName = GetPath.getPathFromClass(this.getClass());
    	    projectPath = packageFullName.substring(0,  packageFullName.indexOf(packageName) );
    	    System.out.println("工程路径："+projectPath);
    	} catch (IOException e1) {
    	    projectPath = null;
    	    e1.printStackTrace();
    	}
    	return projectPath;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		helpseekingDB.closePluginConn();		
		super.stop(context);
		
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CopyOfActivator getDefault() {
		return plugin;
	}

}
