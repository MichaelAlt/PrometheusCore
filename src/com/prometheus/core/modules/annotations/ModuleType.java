package com.prometheus.core.modules.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author alt
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleType {

	/**
	 * 
	 * @author alt
	 *
	 */
	public enum ModuleTypes {
		API, WEB
	}

	ModuleTypes value();

}