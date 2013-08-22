package cn.ihuhai.logwatch.listener;

/**
 * 内容变化监听器
 * @author XF
 *
 */
public interface IContentListener {

	/**
	 * 通知内容变化
	 * @param bytes 变化的字节数
	 * @param diff 变化的内容
	 */
	void notice(long bytes, StringBuffer diff);
}
