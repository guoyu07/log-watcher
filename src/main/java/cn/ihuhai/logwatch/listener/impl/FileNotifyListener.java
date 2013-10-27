package cn.ihuhai.logwatch.listener.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;
import cn.ihuhai.logwatch.listener.IFileNotifyListener;

public class FileNotifyListener implements IFileNotifyListener {

	public final static int[] EVENT_TYPES = new int[] { JNotify.FILE_ANY,
			JNotify.FILE_CREATED, JNotify.FILE_DELETED, JNotify.FILE_MODIFIED,
			JNotify.FILE_RENAMED };
	
	private Map<Integer, Set<String>> filterMap = new ConcurrentHashMap<Integer, Set<String>>();
	private Map<Integer, Set<JNotifyListener>> listenerMap = new ConcurrentHashMap<Integer, Set<JNotifyListener>>();
	private Map<String, Long> initFileSizeMap = new ConcurrentHashMap<String, Long>();

	private int getHashCode(String pattern, int eventType) {
		return (pattern + eventType).hashCode();
	}

	@Override
	public void watchFile(String pattern, int eventType,
			AbstractJNotifyListener listener) {
		Set<String> filters = null;
		boolean hasFilters = false;
		for (int type : EVENT_TYPES) {
			if (type == (eventType & type)) {
				if (null != filterMap.get(eventType)) {
					hasFilters = true;
				}
				
				if (!hasFilters) {
					filters = new HashSet<String>();
					filterMap.put(type, filters);
				}
				filters.add(pattern);

				Integer hashCode = getHashCode(pattern, type);
				Set<JNotifyListener> listeners = listenerMap.get(hashCode);
				if (null == listeners) {
					listeners = new HashSet<JNotifyListener>();
					listenerMap.put(hashCode, listeners);
				}
				listeners.add(listener);
			}
		}
	}

	@Override
	public Set<JNotifyListener> getListener(String filePath, int eventType) {
		Set<JNotifyListener> listeners = Collections.emptySet();
		for (int type : EVENT_TYPES) {
			if (type == (eventType & type)) {
				String pattern = null;
				Set<String> filters = filterMap.get(type);
				if (null != filters) {
					for (String filter : filters) {
						if (filePath.matches(filter)) {
							pattern = filter;
							break;
						}
					}
					if (null != pattern) {
						listeners = listenerMap.get(getHashCode(pattern, type));
					}
				}
			}
		}

		return listeners;
	}

	@Override
	public final void fileCreated(int wd, String rootPath, String name) {
		Set<JNotifyListener> listeners = getListener(rootPath + File.separator
				+ name, JNotify.FILE_CREATED);
		for (JNotifyListener listener : listeners) {
			listener.fileCreated(wd, rootPath, name);
		}
	}

	@Override
	public final void fileDeleted(int wd, String rootPath, String name) {
		Set<JNotifyListener> listeners = getListener(rootPath + File.separator
				+ name, JNotify.FILE_DELETED);
		for (JNotifyListener listener : listeners) {
			listener.fileDeleted(wd, rootPath, name);
		}
	}

	@Override
	public final void fileModified(int wd, String rootPath, String name) {
		Set<JNotifyListener> listeners = getListener(rootPath + File.separator
				+ name, JNotify.FILE_MODIFIED);
		for (JNotifyListener listener : listeners) {
			listener.fileModified(wd, rootPath, name);
		}
	}

	@Override
	public final void fileRenamed(int wd, String rootPath, String oldName,
			String newName) {
		Set<JNotifyListener> listeners = getListener(rootPath + File.separator
				+ oldName, JNotify.FILE_RENAMED);
		for (JNotifyListener listener : listeners) {
			listener.fileRenamed(wd, rootPath, oldName, newName);
		}
	}

	@Override
	public void addInitFileSize(String dir) {
		for (Set<String> patterns : filterMap.values()) {
			for (final String pattern : patterns) {
				File fileDir = new File(dir);
				if (fileDir.exists()) {
					FilenameFilter filter = new FilenameFilter() {

						@Override
						public boolean accept(File dir, String name) {
							return name.matches(pattern);
						}
					};
					File[] files = fileDir.listFiles(filter);
					for (File file : files) {
						if (null == initFileSizeMap.get(file.getAbsolutePath())) {
							initFileSizeMap.put(file.getAbsolutePath(),
									file.length());
						}
					}
				}
			}
		}
	}

	@Override
	public long getInitFileSize(String filePath) {
		Long fileSize = initFileSizeMap.get(filePath);
		return (fileSize == null ? 0 : fileSize);
	}

}
