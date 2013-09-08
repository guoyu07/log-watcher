package cn.ihuhai.logwatch.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

public class HttpRequestHandler implements Handler<HttpServerRequest> {
	
	public static final String CHUNKED = "chunked";
	
	private Logger logger = Logger.getLogger(HttpRequestHandler.class);
	
	private JsonObject config = new JsonObject();
	private static final JsonObject DEFAULT_CONFIG = new JsonObject();
	private List<IRoute> routes = new ArrayList<IRoute>();
	
	static{
		DEFAULT_CONFIG.putBoolean(CHUNKED, false);
	}
	
	public HttpRequestHandler(JsonObject config) {
		super();
		this.config.mergeIn(DEFAULT_CONFIG);
		if(null != config){
			this.config.mergeIn(config);
		}
		
	}
	
	public HttpRequestHandler(){
		this(null);
	}

	public void addRoute(IRoute route){
		if(null  != route){
			routes.add(route);
		}
	}

	@Override
	public void handle(HttpServerRequest request) {
		logger.debug(request.path());
		
		request.response().setChunked(config.getBoolean(CHUNKED));
		
		boolean match = false;
		for(IRoute route : routes){
			match = route.route(request);
			if(match){
				break;
			}
		}
		if(!match){
			request.response().setStatusCode(404).end(new Buffer("找不到您请求的资源", "UTF-8"));
		}
		
//		if("/".equals(request.path())){
//			request.response().end(vertx.fileSystem().readFileSync("/home/huhai/git/log-watcher/src/main/webapp/index.html"));
//		}else if("/lib/socketjs/sockjs-0.3.4.min.js".equals(request.path())){
//			request.response().end(vertx.fileSystem().readFileSync("/home/huhai/git/log-watcher/src/main/webapp/lib/sockjs/sockjs-0.3.4.min.js"));
//		}else{
//			request.response().setStatusCode(404).end(new Buffer("找不到您请求的资源"));
//		}
	}

}
