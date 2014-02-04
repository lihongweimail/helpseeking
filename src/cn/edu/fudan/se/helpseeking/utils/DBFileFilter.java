/**
 * 
 */
package cn.edu.fudan.se.helpseeking.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Grand  2012-11-28上午11:16:09
 *
 */
public class DBFileFilter extends FileFilter{

	String ext;
	public DBFileFilter(String ext)
	{
		this.ext=ext;
	}

	/* 在accept()方法中,当程序所抓到的是一个目录而不是文件时,我们返回true值,表示将此目录显示出来. */
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String fileName = f.getName();
		int index = fileName.lastIndexOf('.');
		if (index > 0 && index < fileName.length() - 1) {
			// 表示文件名称不为".xxx"现"xxx."之类型
			String extension = fileName.substring(index + 1).toLowerCase();
			// 若所抓到的文件扩展名等于我们所设置要显示的扩展名(即变量ext值),则返回true,表示将此文件显示出来,否则返回
			// true.
			if (extension.equals(ext))
				return true;
		}
		return false;
	}

	// 实现getDescription()方法,返回描述文件的说明字符串!!!
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		if (ext.equals("db"))
			return "SQLite file (*.db)";
		return "";		
		}

}
