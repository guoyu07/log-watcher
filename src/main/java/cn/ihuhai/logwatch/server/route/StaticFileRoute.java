package cn.ihuhai.logwatch.server.route;

import org.apache.log4j.Logger;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class StaticFileRoute extends AbstractRoute {
	private Logger logger = Logger.getLogger(StaticFileRoute.class);
	
	private static final JsonObject DEFAULT_CONFIG = new JsonObject();
	
	private JsonObject config = new JsonObject();
	private Vertx vertx;
	
	static{
		JsonArray statics = new JsonArray();
		statics.addString(".js");
		statics.addString(".html");
		statics.addString(".htm");
		statics.addString(".css");
		statics.addString(".jpg");
		statics.addString(".jpeg");
		statics.addString(".png");
		statics.addString(".gif");
		statics.addString(".mp3");
		DEFAULT_CONFIG.putArray(STATIC_FILE_EXT, statics);
		DEFAULT_CONFIG.putString(WEB_ROOT, DEFAULT_WEB_ROOT);
	}
	
	public StaticFileRoute(Vertx vertx){
		this(vertx, null);
	}
	
	public StaticFileRoute(Vertx vertx, JsonObject config){
		this.vertx = vertx;
		this.config.mergeIn(DEFAULT_CONFIG);
		if(null != config){
			this.config.mergeIn(config);
		}
	}

	@Override
	public boolean route(HttpServerRequest request) {
		for(Object suffix : config.getArray(STATIC_FILE_EXT)){
			if(request.path().endsWith(suffix.toString())){
				Buffer buf = null;
				try {
					buf = vertx.fileSystem().readFileSync(config.getString(WEB_ROOT) + request.path());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return false;
				}
				
				if(null != buf){
					request.response().end(buf);
					return true;
				}
			}
		}
		
		return false;
	}

}
