package com.mebigfatguy.ds.spi;

public class ComponentDSProvider extends AbstractDSProvider {

	private static final String COMPONENT_NAMESPACE = "http://com.mebigfatguy/ds/component";
	private static final String COMPONENT_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/component.xsd";
	
	public ComponentDSProvider() {
		super(COMPONENT_NAMESPACE, COMPONENT_SCHEMA_RESOURCE);
	}
}
