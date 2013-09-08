package cn.ihuhai.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.platform.VerticleFactory;
import org.vertx.java.platform.impl.ModuleClassLoader;

public class JavaVerticleFactoryTest {

	String files = "file:/home/huhai/git/log-watcher/target/classes/, file:/home/huhai/develop/maven_repository/com/fasterxml/jackson/core/jackson-annotations/2.2.2/jackson-annotations-2.2.2.jar, file:/home/huhai/develop/maven_repository/com/fasterxml/jackson/core/jackson-core/2.2.2/jackson-core-2.2.2.jar, file:/home/huhai/develop/maven_repository/com/fasterxml/jackson/core/jackson-databind/2.2.2/jackson-databind-2.2.2.jar, file:/home/huhai/develop/maven_repository/com/hazelcast/hazelcast/2.6/hazelcast-2.6.jar, file:/home/huhai/develop/maven_repository/io/netty/netty-all/4.0.2.Final/netty-all-4.0.2.Final.jar, file:/home/huhai/develop/maven_repository/org/vertx/vertx-core/2.0.0-final/vertx-core-2.0.0-final.jar, file:/home/huhai/develop/maven_repository/org/vertx/vertx-platform/2.0.0-final/vertx-platform-2.0.0-final.jar, file:/home/huhai/develop/maven_repository/org/slf4j/slf4j-api/1.6.2/slf4j-api-1.6.2.jar, file:/home/huhai/develop/maven_repository/log4j/log4j/1.2.16/log4j-1.2.16.jar, file:/home/huhai/develop/maven_repository/junit/junit/4.11/junit-4.11.jar, file:/home/huhai/develop/maven_repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar, file:/home/huhai/develop/maven_repository/net/contentobjects/jnotify/0.94/jnotify-0.94.jar, file:/home/huhai/develop/maven_repository/javassist/javassist/3.11.0.GA/javassist-3.11.0.GA-sources.jar";
	private ModuleClassLoader mcl;
	private VerticleFactory factory;
	private ClassLoader classLoader;

	// static ClassLoader getCallerClassLoader() {
	// // NOTE use of more generic Reflection.getCallerClass()
	// Class caller = Reflection.getCallerClass(3);
	// // This can be null if the VM is requesting it
	// if (caller == null) {
	// return null;
	// }
	// // Circumvent security check since this is package-private
	// return caller.getClassLoader0();
	// }

	@Test
	public void testCast() throws MalformedURLException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String[] fileUrls = files.split(",");
		URL[] urls = new URL[fileUrls.length];
		urls = new URL[]{};
//		for (int i = 0; i < fileUrls.length; i++) {
//			urls[i] = new URL(fileUrls[i].trim());
//		}
		String factoryName = "org.vertx.java.platform.impl.java.JavaVerticleFactory";
		Class<?> clazz = null;

		// clazz.newInstance().getClass().getClassLoader()
		// clazz.isInstance(VerticleFactory)

		factory = new org.vertx.java.platform.impl.java.JavaVerticleFactory();
		System.out.println("static new instance ");

		System.out.println();

		clazz = Class.forName(factoryName);
		printInterface(clazz);
		factory = (VerticleFactory) clazz.newInstance();
		System.out.println("Default ClassLoader dynamic new instance :");

		System.out.println();

		classLoader = this.getClass().getClassLoader();
		clazz = classLoader.loadClass(factoryName);
		printInterface(clazz);
		factory = (VerticleFactory) clazz.newInstance();
		System.out.println("current ClassLoader dynamic new instance :"
				+ this.getClass().getClassLoader().getClass()
						.getCanonicalName());

		System.out.println();

		classLoader = new URLClassLoader(urls);
		clazz = classLoader.loadClass(factoryName);
		printInterface(clazz);
		factory = (VerticleFactory) clazz.newInstance();
		System.out.println("new URLClassLoader dynamic new instance :"
				+ classLoader.getClass().getCanonicalName());

		System.out.println();

		System.out.println();
		System.out.println(Thread.currentThread().getContextClassLoader());

		// classLoader = new URLClassLoader(urls);
		// mcl = new ModuleClassLoader(classLoader, urls, true);
		// clazz = mcl.loadClass(factoryName);
		// printInterface(clazz);
		// factory = (VerticleFactory) clazz.newInstance();
		// System.out.println("current mcl ClassLoader dynamic new instance ");

		System.out.println();

		ClassLoader platformClassLoader = Thread.currentThread()
				.getContextClassLoader();
		mcl = new ModuleClassLoader(platformClassLoader, urls, false);
		clazz = mcl.loadClass(factoryName);
		Object obj = clazz.newInstance();
		System.out.println("obj:" + obj.getClass().getClassLoader());
		factory = (VerticleFactory)obj;
		printInterface(clazz);
		System.out.println("platformClassLoader dynamic new instance ");

		Assert.assertNotNull(factory);
	}

	private void printInterface(Class clazz) {
		Class[] interfaces = clazz.getInterfaces();
		for (Class face : interfaces) {
			System.out.println(face.getCanonicalName());
		}
	}
}
