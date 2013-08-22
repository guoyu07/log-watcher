package cn.ihuhai.logwatch.notify.impl;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import org.junit.Assert;

import cn.ihuhai.logwatch.listener.IFileNotifyListener;
import cn.ihuhai.logwatch.notify.INotifyService;

public class NotifyService implements INotifyService {
	
	@Override
	public int addWatch(String dir, int mask, boolean watchSubtree,
			IFileNotifyListener listener) throws JNotifyException {
		
		Assert.assertNotNull(listener);
		listener.addInitFileSize(dir);
		return JNotify.addWatch(dir, mask, watchSubtree, listener);
	}

	@Override
	public int addWatch(String dir, int mask,
			IFileNotifyListener listener) throws JNotifyException {
		return addWatch(dir, mask, false, listener);
	}

	@Override
	public int addCMWatch(String dir, IFileNotifyListener listener) throws JNotifyException {
		
		return addWatch(dir, JNotify.FILE_CREATED | JNotify.FILE_MODIFIED, listener);
	}

	@Override
	public boolean removeWatch(int watchId) throws JNotifyException {
		
		return JNotify.removeWatch(watchId);
	}

}
