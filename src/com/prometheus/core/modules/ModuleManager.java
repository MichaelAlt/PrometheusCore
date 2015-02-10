package com.prometheus.core.modules;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.prometheus.core.exception.ConfigurationException;
import com.prometheus.core.modules.annotations.ModuleName;
import com.prometheus.core.modules.annotations.ModuleType;
import com.prometheus.core.modules.annotations.ModuleVersion;

/**
 * 
 * @author alt
 *
 */
public class ModuleManager {

	private List<ModuleConfiguration> moduleConfigurations;

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ConfigurationException
	 */
	public ModuleManager() throws IOException, ClassNotFoundException, ConfigurationException {

		moduleConfigurations = new ArrayList<ModuleConfiguration>();

		loadLibraries();
		loadModules();
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void loadLibraries() throws IOException {

		System.out.println("Libraries loaded on startup:");
		File directory = new File(System.getProperty("user.dir") + "/lib/");

		for (File library : directory.listFiles()) {

			if (library.getName().endsWith(".jar")) {
				loadBinary(library);
			}
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ConfigurationException
	 */
	private void loadModules() throws IOException, ClassNotFoundException, ConfigurationException {

		System.out.println("Modules loaded on startup:");
		File directory = new File(System.getProperty("user.dir") + "/modules/");

		for (File module : directory.listFiles()) {

			if (module.getName().endsWith(".jar")) {
				loadBinary(module);
				loadConfiguration(module);
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadBinary(File file) throws IOException {

		System.out.println(" - " + file.getName());

		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;

		try {

			Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { file.toURI().toURL() });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}

	}

	/**
	 * 
	 * @param file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ConfigurationException
	 */
	private void loadConfiguration(File file) throws IOException, ClassNotFoundException, ConfigurationException {

		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> entries = jarFile.entries();

		while (entries.hasMoreElements()) {

			JarEntry entry = entries.nextElement();
			if (entry.getName().endsWith(".class")) {

				String moduleClass = entry.getName().replace("/", ".").replace(".class", "");
				Class<?> configuration = ClassLoader.getSystemClassLoader().loadClass(moduleClass);

				if (configuration.isAnnotationPresent(ModuleName.class)) {

					ModuleType moduleType = (ModuleType) configuration.getAnnotation(ModuleType.class);
					ModuleName moduleName = (ModuleName) configuration.getAnnotation(ModuleName.class);
					ModuleVersion moduleVersion = (ModuleVersion) configuration.getAnnotation(ModuleVersion.class);

					ModuleConfiguration moduleConfiguration = new ModuleConfiguration();
					moduleConfiguration.setModuleType(moduleType.value());
					moduleConfiguration.setModuleName(moduleName.value());
					moduleConfiguration.setModuleVersion(moduleVersion.value());
					moduleConfiguration.setModuleClass(moduleClass);

					for (ModuleConfiguration module : moduleConfigurations) {

						if (module.getModuleName().equals(moduleName.value())) {
							throw new ConfigurationException("Module already loaded: " + moduleName.value());
						}
					}

					System.out.println(" - " + moduleClass + " (" + moduleVersion.value() + ")");
					moduleConfigurations.add(moduleConfiguration);
				}
			}
		}
		jarFile.close();
	}

	/**
	 * 
	 * @return
	 */
	public List<ModuleConfiguration> getModules() {
		return this.moduleConfigurations;
	}

}