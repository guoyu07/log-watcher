package cn.ihuhai.logwatch.listener.impl;

import net.contentobjects.jnotify.JNotifyListener;

public class AbstractJNotifyListener implements JNotifyListener {
	
	public AbstractJNotifyListener(){
		init();
	}
	
	/**
	 * 初始化方法
	 */
	public void init(){}

	@Override
	public void fileRenamed(int wd, String rootPath, String oldName,
			String newName) {
	}

	@Override
	public void fileModified(int wd, String rootPath, String name) {
		
	}

	@Override
	public void fileDeleted(int wd, String rootPath, String name) {
		
	}

	@Override
	public void fileCreated(int wd, String rootPath, String name) {
		
	}

}
