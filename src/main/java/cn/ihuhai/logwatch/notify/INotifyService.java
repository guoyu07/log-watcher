package cn.ihuhai.logwatch.notify;

import net.contentobjects.jnotify.JNotifyException;
import cn.ihuhai.logwatch.listener.IFileNotifyListener;

/**
 * 通知服务接口
 * @author XF
 *
 */
public interface INotifyService {

	/**
	 * 添加监听器
	 * @param dir 监听目录路径
	 * @param mask 监听类型掩码
	 * @param watchSubtree 是否监听子目录
	 * @param listener 文件监听器
	 * @return
	 * @throws JNotifyException 
	 */
	public int addWatch(String dir, int mask, boolean watchSubtree, IFileNotifyListener listener) throws JNotifyException;
	
	/**
	 * 添加监听器(不监听子目录)
	 * @param dir 监听目录路径
	 * @param mask 监听类型掩码
	 * @param listener 文件监听器
	 * @return
	 * @throws JNotifyException 
	 */
	public int addWatch(String dir, int mask, IFileNotifyListener listener) throws JNotifyException;
	
	/**
	 * 添加文件创建和修改监听器(不监听子目录)
	 * @param dir 监听目录路径
	 * @param listener 文件监听器
	 * @return
	 * @throws JNotifyException 
	 */
	public int addCMWatch(String dir, IFileNotifyListener listener) throws JNotifyException;
	
	/**
	 * 移除监听器
	 * @param watchId
	 * @return 是否移除成功
	 * @throws JNotifyException 
	 */
	public boolean removeWatch(int watchId) throws JNotifyException;
}
