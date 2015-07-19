package com.mebigfatguy.ds.spi;

import java.net.URL;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.mebigfatguy.ds.service.DSHandlerProvider;

public abstract class AbstractDSProvider extends DefaultHandler implements DSHandlerProvider {

	private String xsdDSNamespace;
	private String xsdDSResource;
	
	protected AbstractDSProvider(String xsdNamespace, String xsdResource) {
		xsdDSNamespace = xsdNamespace;
		xsdDSResource = xsdResource;
	}
	
	@Override
	public String getXSDNamespace() {
		return xsdDSNamespace;
	}
	
	@Override
	public URL getSchema() {
		return FrameDSProvider.class.getResource(xsdDSResource);
	}

	@Override
	public ContentHandler getDSHandler() {
		return this;
	}
}