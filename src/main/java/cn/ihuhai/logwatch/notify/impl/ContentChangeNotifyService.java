package cn.ihuhai.logwatch.notify.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import org.apache.log4j.Logger;

import cn.ihuhai.logwatch.listener.IContentListener;
import cn.ihuhai.logwatch.listener.IFileNotifyListener;
import cn.ihuhai.logwatch.listener.impl.AbstractJNotifyListener;
import cn.ihuhai.logwatch.listener.impl.FileNotifyListener;
import cn.ihuhai.logwatch.notify.IContentChangeNotifyService;
import cn.ihuhai.logwatch.notify.INotifyService;

public class ContentChangeNotifyService implements IContentChangeNotifyService {
	
	private Logger logger = Logger.getLogger(ContentChangeNotifyService.class);
	private static String LINE_SEPARATOR = System.getProperty("line.separator");
	private static int LINE_SEPARATOR_LENGTH = LINE_SEPARATOR.toCharArray().length;
	
	private INotifyService notifyService = new NotifyService();
	private final IFileNotifyListener listener = new FileNotifyListener();

	@Override
	public void watch(String dir, final String encoding, final IContentListener contentListener, String... patterns) throws JNotifyException {
		if(patterns.length > 0){
			for(String pattern : patterns){
				listener.watchFile(pattern, JNotify.FILE_CREATED | JNotify.FILE_MODIFIED, new AbstractJNotifyListener() {
					long fileSize = 0;
					boolean first = true;	
					
					private void fileChanged(int wd, String rootPath, String name){
						String filePath = rootPath + File.separator + name;
						
						if(first){
							fileSize = listener.getInitFileSize(filePath);
							first = false;
						}
						File file = new File(filePath);
						long diff = file.length() - fileSize;
						
						if(diff > 0){
							RandomAccessFile randomAccessFile = null;
							try {
								randomAccessFile = new RandomAccessFile(file, "r");
								randomAccessFile.seek(fileSize);
								String line = null;
								StringBuffer buf = new StringBuffer();
								while(null != (line = randomAccessFile.readLine())){
									buf.append(new String(line.getBytes("ISO-8859-1"), encoding)).append(LINE_SEPARATOR);
								}
								int len = buf.length();
								if(buf.length() > LINE_SEPARATOR_LENGTH){
									buf.delete(len - LINE_SEPARATOR_LENGTH, len);
								}
								
								contentListener.notice(diff, buf);
								
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}finally{
								if(null != randomAccessFile){
									try {
										randomAccessFile.close();
									} catch (IOException e) {
										logger.error(e.getMessage(), e);
									}
								}
							}
						}
						
						fileSize = file.length();
					}

					@Override
					public void fileModified(int wd, String rootPath, String name) {
						logger.info("file modified");
						fileChanged(wd, rootPath, name);
					}

					@Override
					public void fileCreated(int wd, String rootPath, String name) {
						logger.info("file created");
						fileChanged(wd, rootPath, name);
					}

				});
			}
			
			notifyService.addCMWatch(dir, listener);
		}
	}

	@Override
	public void watch(String dir, IContentListener contentListener,
			String... patterns) throws JNotifyException {
		watch(dir, "UTF-8", contentListener, patterns);
	}

}
