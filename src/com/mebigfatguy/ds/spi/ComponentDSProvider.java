package com.mebigfatguy.ds.spi;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.mebigfatguy.ds.service.DSHandlerProvider;

public class ComponentDSProvider extends DefaultHandler implements DSHandlerProvider {

	private static final String COMPONENT_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/component.xsd";
	private static final URI COMPONENT_NAMESPACE;
	
	static {
		try {
			COMPONENT_NAMESPACE = new URI("http://com.mebigfatguy/ds/component");
		} catch (URISyntaxException e) {
			throw new Error("Unable to define namespace", e);
		}
	}
	
	@Override
	public URI getXSDNamespace() {
		return COMPONENT_NAMESPACE;
	}
	
	@Override
	public URL getSchema() {
		return FrameDSProvider.class.getResource(COMPONENT_SCHEMA_RESOURCE);
	}

	@Override
	public ContentHandler getDSHandler() {
		return this;
	}
}
