package cn.ihuhai.logwatch.server.route;

import org.apache.log4j.Logger;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

public class DefaultRoute extends AbstractRoute{
	private Logger logger = Logger.getLogger(DefaultRoute.class);
	private static final JsonObject DEFAULT_CONFIG = new JsonObject();
	
	private JsonObject config = new JsonObject();
	private Vertx vertx;
	
	static{
		DEFAULT_CONFIG.putString(INDEX, "index.html");
		DEFAULT_CONFIG.putString(WEB_ROOT, DEFAULT_WEB_ROOT);
	}

	@Override
	public boolean route(HttpServerRequest request) {
		if(request.path().endsWith("/")){
			Buffer buf = null;
			try {
				buf = vertx.fileSystem().readFileSync(config.getString(WEB_ROOT) + request.path() + config.getString(INDEX));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return false;
			}
			
			if(null != buf){
				request.response().end(buf);
				return true;
			}
		}
		return false;
	}

	public DefaultRoute(Vertx vertx){
		this(vertx, null);
	}
	
	public DefaultRoute(Vertx vertx, JsonObject config){
		this.vertx = vertx;
		this.config.mergeIn(DEFAULT_CONFIG);
		if(null != config){
			this.config.mergeIn(config);
		}
	}
}
