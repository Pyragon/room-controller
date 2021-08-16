package com.cryo.utils;

import com.cryo.RoomController;
import com.cryo.entities.Account;
import com.cryo.entities.annotations.*;
import com.cryo.web.AccountUtils;
import com.google.common.reflect.ClassPath;
import de.neuland.pug4j.Pug4J;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

import static com.cryo.web.WebController.render500;
import static spark.Spark.get;
import static spark.Spark.post;

@Slf4j
public class Utilities {

	public static ArrayList<Class<?>> getClassesWithAnnotation(String packageName, Class<? extends Annotation> annotation) throws ClassNotFoundException, IOException {
		ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (ClassPath.ClassInfo info : cp.getTopLevelClassesRecursive(packageName)) {
			if (!Class.forName(info.getName()).isAnnotationPresent(annotation))
				continue;
			classes.add(Class.forName(info.getName()));
		}
		return classes;
	}

	public static void initializeEndpoints() {
		long start = System.currentTimeMillis();
		int get = 0;
		int post = 0;
		int classCount = 0;
		boolean classChecked = false;
		try {
			ArrayList<Class<?>> classes = Utilities.getClassesWithAnnotation("com.cryo", EndpointSubscriber.class);
			for (Class<?> clazz : classes) {
				if (!clazz.isAnnotationPresent(EndpointSubscriber.class)) continue;
				classChecked = false;
				for (Method method : clazz.getMethods()) {
					int count = method.getParameterCount();
					if ((count != 2 && count != 3) || (!method.isAnnotationPresent(Endpoint.class) && !method.isAnnotationPresent(SPAEndpoint.class) && !method.isAnnotationPresent(SPAEndpoints.class)))
						continue;
					if (!Modifier.isStatic(method.getModifiers())) {
						log.error("Expected method to be static in order for endpoint to work! " + method.getName());
						throw new RuntimeException();
					}
					if (method.getReturnType() != String.class) {
						log.error("Expected endpoint to return a String! " + method.getName());
						throw new RuntimeException();
					}
					if (!classChecked) {
						classCount++;
						classChecked = true;
					}
					if (method.isAnnotationPresent(SPAEndpoints.class)) {
						SPAEndpoints endpoint = method.getAnnotation(SPAEndpoints.class);
						assert !endpoint.value().equals("") : "Invalid endpoint: " + method.getName() + " in " + method.getDeclaringClass().getSimpleName();
						for (String endpointString : endpoint.value().split(", ")) {
							get++;
							post++;
							get(endpointString, (req, res) -> {
								Object[] parameters = new Object[count];
								parameters[0] = count == 3 ? endpointString : req;
								parameters[1] = count == 3 ? req : res;
								if (count == 3)
									parameters[2] = res;
								return method.invoke(null, parameters);
							});
							post(endpointString, (req, res) -> {
								Object[] parameters = new Object[count];
								parameters[0] = count == 3 ? endpointString : req;
								parameters[1] = count == 3 ? req : res;
								if (count == 3)
									parameters[2] = res;
								return method.invoke(null, parameters);
							});
						}
						continue;
					}
					if (method.isAnnotationPresent(SPAEndpoint.class)) {
						SPAEndpoint endpoint = method.getAnnotation(SPAEndpoint.class);
						assert !endpoint.value().equals("") : "Invalid endpoint: " + method.getName() + " in " + method.getDeclaringClass().getSimpleName();
						get++;
						post++;
						get(endpoint.value(), (req, res) -> {
							Object[] parameters = new Object[count];
							parameters[0] = count == 3 ? endpoint.value() : req;
							parameters[1] = count == 3 ? req : res;
							if (count == 3)
								parameters[2] = res;
							return method.invoke(null, parameters);
						});
						post(endpoint.value(), (req, res) -> {
							Object[] parameters = new Object[count];
							parameters[0] = count == 3 ? endpoint.value() : req;
							parameters[1] = count == 3 ? req : res;
							if (count == 3)
								parameters[2] = res;
							return method.invoke(null, parameters);
						});
						continue;
					}
					Endpoint endpoint = method.getAnnotation(Endpoint.class);
					if (!endpoint.values()[0].equals("")) {
						int index = 0;
						while (index < endpoint.values().length) {
							String endpointMethod = endpoint.values()[index++];
							String endpointString = endpoint.values()[index++];
							if (endpointMethod.equals("GET")) {
								get(endpointString, (req, res) -> {
									Object[] parameters = new Object[count];
									parameters[0] = count == 3 ? endpointString : req;
									parameters[1] = count == 3 ? req : res;
									if (count == 3)
										parameters[2] = res;
									return method.invoke(null, parameters);
								});
								get++;
							} else {
								post(endpointString, (req, res) -> {
									Object[] parameters = new Object[count];
									parameters[0] = count == 3 ? endpointString : req;
									parameters[1] = count == 3 ? req : res;
									if (count == 3)
										parameters[2] = res;
									return method.invoke(null, parameters);
								});
								post++;
							}
						}
					} else {
						if (endpoint.method().equals("GET")) {
							get(endpoint.endpoint(), (req, res) -> {
								Object[] parameters = new Object[count];
								parameters[0] = count == 3 ? endpoint.endpoint() : req;
								parameters[1] = count == 3 ? req : res;
								if (count == 3)
									parameters[2] = res;
								return method.invoke(null, parameters);
							});
							get++;
						} else {
							post(endpoint.endpoint(), (req, res) -> {
								Object[] parameters = new Object[count];
								parameters[0] = count == 3 ? endpoint.endpoint() : req;
								parameters[1] = count == 3 ? req : res;
								if (count == 3)
									parameters[2] = res;
								return method.invoke(null, parameters);
							});
							post++;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error in initializing endpoints.", e);
		}
		long end = System.currentTimeMillis();
		log.info("Loaded " + get + " GET and " + post + " POST endpoints from " + classCount + " classes in " + (end - start) + "ms.");
	}

	public static void sendStartupHooks() {
		long start = System.currentTimeMillis();
		int startup = 0;
		try {
			ArrayList<Class<?>> classes = getClassesWithAnnotation("com.cryo", ServerStartSubscriber.class);
			for (Class<?> clazz : classes) {
				if (!clazz.isAnnotationPresent(ServerStartSubscriber.class)) continue;
				for (Method method : clazz.getMethods()) {
					if (method.getParameterCount() != 0 || !method.isAnnotationPresent(ServerStart.class)) continue;
					if (!Modifier.isStatic(method.getModifiers())) {
						log.error("Expected startup method to be static! " + method.getName() + " in " + clazz.getSimpleName());
						throw new RuntimeException();
					}
					if (method.getReturnType() != void.class) {
						log.error("Expected startup method to be void! " + method.getName() + ":" + method.getReturnType());
						throw new RuntimeException();
					}
					method.invoke(null);
					startup++;
				}
			}
		} catch (Exception e) {
			log.error("Error in sending startup hooks.", e);
		}
		long end = System.currentTimeMillis();
		log.info("WebStartInitializer", "Executed " + startup + " startup methods in " + (end - start) + "ms.");
	}

	@SuppressWarnings({"rawtypes"})
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				try {
					classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
				} catch (Throwable e) {

				}
			}
		}
		return classes;
	}

	public static String renderPage(String module, HashMap<String, Object> model, Request request, Response response) {
		try {
			Account account = AccountUtils.getAccount(request);
			if(request.requestMethod().equals("GET"))
				return Pug4J.render("./source/modules/index.pug", new HashMap<>());
			if(model == null)
				model = new HashMap<>();
			Properties prop = new Properties();
			prop.put("success", true);
			model.put("loggedIn", account != null);
			if(account != null)
				model.put("user", account);
			if(model.containsKey("404"))
				prop.put("404", true);
			module = "./source/modules/"+module+".pug";
			String html = Pug4J.render(module, model);
			prop.put("html", html);
			return RoomController.getGson().toJson(prop);
		} catch(Exception e) {
			e.printStackTrace();
			render500(response, e.getMessage());
			return "";
		}
	}
}
