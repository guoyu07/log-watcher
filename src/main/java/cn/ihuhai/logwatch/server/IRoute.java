package cn.ihuhai.logwatch.server;

import org.vertx.java.core.http.HttpServerRequest;


public interface IRoute {
	
	public static final String WEB_ROOT = "web_root";
	public static final String STATIC_FILE_EXT = "static_file_ext";
	public static final String INDEX = "index";

	/**
	 * 路由请求
	 * @param request
	 * @return 是否路由成功
	 */
	public boolean route(HttpServerRequest request);
}
