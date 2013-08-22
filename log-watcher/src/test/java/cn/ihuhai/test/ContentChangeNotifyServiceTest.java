package cn.ihuhai.test;

import net.contentobjects.jnotify.JNotifyException;

import org.junit.Test;

import cn.ihuhai.logwatch.listener.IContentListener;
import cn.ihuhai.logwatch.notify.IContentChangeNotifyService;
import cn.ihuhai.logwatch.notify.impl.ContentChangeNotifyService;

public class ContentChangeNotifyServiceTest {
	
	@Test
	public void testWatch() throws JNotifyException, InterruptedException{
		String dir = "/home/huhai/script/shell/update_dns/logs";
		IContentChangeNotifyService contentChangeNotifyService = new ContentChangeNotifyService();
		contentChangeNotifyService.watch(dir, new IContentListener() {
			
			@Override
			public void notice(long bytes, StringBuffer diff) {
				System.out.println(diff);				
			}
		}, ".*\\.log");
		
		Thread.sleep(1000000);
	}
}
