package cn.ihuhai.logwatch.listener;

import java.util.Set;

import net.contentobjects.jnotify.JNotifyListener;
import cn.ihuhai.logwatch.listener.impl.AbstractJNotifyListener;


public interface IFileNotifyListener extends JNotifyListener{

	void watchFile(String pattern, int eventType, AbstractJNotifyListener listener);
	
	Set<JNotifyListener> getListener(String pattern, int eventType);
	
	long getInitFileSize(String filePath);

	void addInitFileSize(String dir);
}
