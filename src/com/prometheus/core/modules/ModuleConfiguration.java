package com.prometheus.core.modules;

import com.prometheus.core.modules.annotations.ModuleType.ModuleTypes;

/**
 * 
 * @author alt
 *
 */
public class ModuleConfiguration {

	private ModuleTypes moduleType;
	private String moduleName;
	private String moduleVersion;
	private String moduleClass;

	public ModuleTypes getModuleType() {
		return moduleType;
	}

	protected void setModuleType(ModuleTypes moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleName() {
		return moduleName;
	}

	protected void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleVersion() {
		return moduleVersion;
	}

	protected void setModuleVersion(String moduleVersion) {
		this.moduleVersion = moduleVersion;
	}

	public String getModuleClass() {
		return moduleClass;
	}

	protected void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}

}