package cn.ihuhai.logwatch.notify;

import net.contentobjects.jnotify.JNotifyException;
import cn.ihuhai.logwatch.listener.IContentListener;

/**
 * 内容变化通知服务
 * @author XF
 *
 */
public interface IContentChangeNotifyService {
	
	/**
	 * 监听文件（默认以UTF-8编码读取文件）
	 * @param dir 文件目录
	 * @param contentListener 内容监听器
	 * @param patterns 文件名模式（正则表达式）
	 * @throws JNotifyException 
	 */
	public void watch(String dir, IContentListener contentListener, String ... patterns) throws JNotifyException;

	/**
	 * 监听文件
	 * @param dir 文件目录
	 * @param encoding 读取文件的编码
	 * @param contentListener 内容监听器
	 * @param patterns 文件名模式（正则表达式）
	 * @throws JNotifyException 
	 */
	public void watch(String dir, String encoding, IContentListener contentListener, String ... patterns) throws JNotifyException;
}
