package cn.ihuhai.logwatch.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.contentobjects.jnotify.JNotifyException;

import org.apache.log4j.Logger;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.core.streams.Pump;
import org.vertx.java.platform.Verticle;

import cn.ihuhai.logwatch.listener.IContentListener;
import cn.ihuhai.logwatch.notify.IContentChangeNotifyService;
import cn.ihuhai.logwatch.notify.impl.ContentChangeNotifyService;
import cn.ihuhai.logwatch.server.route.DefaultRoute;
import cn.ihuhai.logwatch.server.route.StaticFileRoute;

public class MainServer extends Verticle {
	private int port = 5427;
	
	private Logger log = Logger.getLogger(MainServer.class);
	
	@Override
	public void start() {
		try {
			log.info("=============================");
			Integer cfg_port = container.config().getInteger("port");
			if(null != cfg_port){
				port = cfg_port;
			}
			
			HttpServer httpServer = vertx.createHttpServer();
			
			HttpRequestHandler requestHandler = new HttpRequestHandler();
			requestHandler.addRoute(new DefaultRoute(vertx));
			requestHandler.addRoute(new StaticFileRoute(vertx));
			httpServer.requestHandler(requestHandler);
			
			SockJSServer sockServer = vertx.createSockJSServer(httpServer);
			final Set<SockJSSocket> socks = Collections.synchronizedSet(new HashSet<SockJSSocket>());
			
			sockServer.installApp(new JsonObject().putString("prefix", "/log-watcher"), new Handler<SockJSSocket>() {

				@Override
				public void handle(final SockJSSocket sock) {
					sock.dataHandler(new Handler<Buffer>() {

						@Override
						public void handle(Buffer data) {
							log.info("received:" + data);
							
							if("register".equals(data.toString())){
								socks.add(sock);
							}else{
								Pump.createPump(sock, sock).start();
							}
						}
					});
					
				}
			
			});
			
			httpServer.listen(port);
			
			log.info("server startup on port " + port);
			
			String dir = "/home/huhai/script/shell/update_dns/logs";
			IContentChangeNotifyService contentChangeNotifyService = new ContentChangeNotifyService();
			try {
				contentChangeNotifyService.watch(dir, new IContentListener() {
					
					@Override
					public void notice(long bytes, StringBuffer diff) {
						System.out.println(diff);
						
						int count = 0;
						for(SockJSSocket sock : socks){
							sock.write(new Buffer().appendString(diff.toString(), "UTF-8"));
							count++;
						}
						
						log.info("noticed " + count + " clients");
					}
				}, ".*\\.log");
			} catch (JNotifyException e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
