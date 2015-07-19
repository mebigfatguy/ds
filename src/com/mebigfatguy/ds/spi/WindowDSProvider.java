package com.mebigfatguy.ds.spi;

public class WindowDSProvider extends AbstractDSProvider {

	private static final String WINDOW_NAMESPACE = "http://com.mebigfatguy/ds/window";
	private static final String WINDOW_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/window.xsd";
	
	public WindowDSProvider() {
		super(WINDOW_NAMESPACE, WINDOW_SCHEMA_RESOURCE);
	}
}
