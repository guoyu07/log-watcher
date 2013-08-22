package cn.ihuhai.test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import org.junit.Test;

import cn.ihuhai.logwatch.listener.IFileNotifyListener;
import cn.ihuhai.logwatch.listener.impl.AbstractJNotifyListener;
import cn.ihuhai.logwatch.listener.impl.FileNotifyListener;
import cn.ihuhai.logwatch.notify.INotifyService;
import cn.ihuhai.logwatch.notify.impl.NotifyService;

public class NotifyServiceTest {

	@Test
	public void test() throws JNotifyException, InterruptedException {
		final String dir = "/home/huhai/script/shell/update_dns/logs";
		final IFileNotifyListener listener = new FileNotifyListener();
		AbstractJNotifyListener modifyListener = new AbstractJNotifyListener() {
			long fileSize = 0;
			boolean first = true;
			{
				System.out.println("初始化完毕");
			}
			@Override
			public void init() {
				System.out.println("init完毕");
			}

			@Override
			public void fileModified(int wd, String rootPath, String name) {
				String filePath = rootPath + File.separator + name;
				if(first){
					fileSize = listener.getInitFileSize(filePath);
					first = false;
				}
				File file = new File(filePath);
				long diff = file.length() - fileSize;
				
				if(diff > 0){
					RandomAccessFile randomAccessFile = null;
					System.out.println("增加字节数：" + diff);
					try {
						randomAccessFile = new RandomAccessFile(file, "r");
						randomAccessFile.seek(fileSize);
						String line = null;
						while(null != (line = randomAccessFile.readLine())){
							System.out.println(new String(line.getBytes("ISO-8859-1"), "UTF-8"));
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(null != randomAccessFile){
							try {fileSize = file.length();
								randomAccessFile.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				fileSize = file.length();
			}

		};
		
		listener.watchFile(".*\\.log", JNotify.FILE_MODIFIED, modifyListener);
		
		INotifyService notifyService = new NotifyService();
		notifyService.addCMWatch(dir, listener);
		
		Thread.sleep(1000000);
	}
}
