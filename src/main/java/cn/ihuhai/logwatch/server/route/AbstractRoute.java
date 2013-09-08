package cn.ihuhai.logwatch.server.route;

import java.io.File;

import org.vertx.java.core.http.HttpServerRequest;

import cn.ihuhai.logwatch.server.IRoute;

public class AbstractRoute implements IRoute {
	
	public static final String DEFAULT_WEB_ROOT = new File("").getAbsolutePath() + "/webapp";

	@Override
	public boolean route(HttpServerRequest request) {
		return false;
	}

}
